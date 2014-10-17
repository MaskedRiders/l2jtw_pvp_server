/*
 * Copyright (C) 2004-2014 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package custom.events.Elpies;

import java.util.concurrent.ScheduledFuture;

import com.l2jserver.Config;
import com.l2jserver.gameserver.Announcements;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2EventMonsterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Event;

public final class Elpies extends Event
{
	// NPC
	private static final int ELPY = 900100;
	// Amount of Elpies to spawn when the event starts
	private static final int ELPY_AMOUNT = 100;
	// Event duration in minutes
	private static final int EVENT_DURATION_MINUTES = 2;
	// @formatter:off
	private static final int[][] DROPLIST_CONSUMABLES =
	{
		// itemId, chance, min amount, max amount
		{  1540, 80, 10, 15 },	// 瞬間體力治癒藥水
		{  1538, 60,  5, 10 },	// 祝福的返回卷軸
		{  3936, 40,  5, 10 },	// 祝福的復活卷軸
		{  6387, 25,  5, 10 },	// 祝福的寵物復活卷軸
		{ 22025, 15,  5, 10 },	// 終極治癒藥水
		{  6622, 10,  1, 1 },	// 巨人的經典
		{ 20034,  5,  1, 1 },	// 萬聖夜活力糖果-活動用
		{ 20004,  1,  1, 1 },	// 活力人蔘
		{ 20004,  0,  1, 1 }	// 活力人蔘

	};
	
	private static final int[][] DROPLIST_CRYSTALS =
	{
		{ 1458, 80, 50, 100 },	// 結晶-D級
		{ 1459, 60, 40,  80 },	// 結晶-C級
		{ 1460, 40, 30,  60 },	// 結晶-B級
		{ 1461, 20, 20,  30 },	// 結晶-A級
		{ 1462,  0, 10,  20 }	// 結晶-S級
	};
	// @formatter:on
	// Non-final variables
	private static boolean EVENT_ACTIVE = false;
	private static int CURRENT_ELPY_COUNT = 0;
	private ScheduledFuture<?> _eventTask = null;
	
	private Elpies()
	{
		super(Elpies.class.getSimpleName(), "custom/events");
		addSpawnId(ELPY);
		addKillId(ELPY);
	}
	
	@Override
	public boolean eventBypass(L2PcInstance activeChar, String bypass)
	{
		return false;
	}
	
	@Override
	public boolean eventStart(L2PcInstance eventMaker)
	{
		if (EVENT_ACTIVE)
		{
			return false;
		}
		
		// Check Custom Table - we use custom NPC's
		if (!Config.CUSTOM_NPC_DATA)
		{
			_log.info(getName() + ": Event can't be started because custom NPC table is disabled!");
			eventMaker.sendMessage("Event " + getName() + " can't be started because custom NPC table is disabled!");
			return false;
		}
		
		EVENT_ACTIVE = true;
		
		EventLocation[] locations = EventLocation.values();
		EventLocation randomLoc = locations[getRandom(locations.length)];
		
		CURRENT_ELPY_COUNT = 0;
		long despawnDelay = EVENT_DURATION_MINUTES * 60000;
		
		for (int i = 0; i < ELPY_AMOUNT; i++)
		{
			addSpawn(ELPY, randomLoc.getRandomX(), randomLoc.getRandomY(), randomLoc.getZ(), 0, true, despawnDelay);
			CURRENT_ELPY_COUNT++;
		}
		
		Announcements.getInstance().announceToAll("*吱吱*");
		Announcements.getInstance().announceToAll("獨角兔 入侵了  " + randomLoc.getName());
		Announcements.getInstance().announceToAll("幫助我們消滅他們!");
		Announcements.getInstance().announceToAll("你還有 " + EVENT_DURATION_MINUTES + " 分鐘...");
		
		_eventTask = ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
			@Override
			public void run()
			{
				Announcements.getInstance().announceToAll("時間到 !");
				eventStop();
			}
		}, despawnDelay);
		return true;
	}
	
	@Override
	public boolean eventStop()
	{
		if (!EVENT_ACTIVE)
		{
			return false;
		}
		
		EVENT_ACTIVE = false;
		
		if (_eventTask != null)
		{
			_eventTask.cancel(true);
			_eventTask = null;
		}
		
		for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(ELPY))
		{
			L2Npc npc = spawn.getLastSpawn();
			if (npc != null)
			{
				npc.deleteMe();
			}
		}
		
		Announcements.getInstance().announceToAll("*吱吱*");
		Announcements.getInstance().announceToAll("獨角兔 的活動結束!");
		return true;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		if (EVENT_ACTIVE)
		{
			dropItem(npc, killer, DROPLIST_CONSUMABLES);
			dropItem(npc, killer, DROPLIST_CRYSTALS);
			CURRENT_ELPY_COUNT--;
			
			if (CURRENT_ELPY_COUNT <= 0)
			{
				Announcements.getInstance().announceToAll("已經沒有 獨角兔 了...");
				eventStop();
			}
		}
		
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		((L2EventMonsterInstance) npc).eventSetDropOnGround(true);
		((L2EventMonsterInstance) npc).eventSetBlockOffensiveSkills(true);
		return super.onSpawn(npc);
	}
	
	private static enum EventLocation
	{
		ADEN("亞丁", 146558, 148341, 26622, 28560, -2200),
		DION("狄恩", 18564, 19200, 144377, 145782, -3081),
		GLUDIN("古魯丁", -84040, -81420, 150257, 151175, -3125),
		HV("獵人村莊", 116094, 117141, 75776, 77072, -2700),
		OREN("歐瑞", 82048, 82940, 53240, 54126, -1490);
		
		private final String _name;
		private final int _minX;
		private final int _maxX;
		private final int _minY;
		private final int _maxY;
		private final int _z;
		
		EventLocation(String name, int minX, int maxX, int minY, int maxY, int z)
		{
			_name = name;
			_minX = minX;
			_maxX = maxX;
			_minY = minY;
			_maxY = maxY;
			_z = z;
		}
		
		public String getName()
		{
			return _name;
		}
		
		public int getRandomX()
		{
			return getRandom(_minX, _maxX);
		}
		
		public int getRandomY()
		{
			return getRandom(_minY, _maxY);
		}
		
		public int getZ()
		{
			return _z;
		}
	}
	
	private static final void dropItem(L2Npc mob, L2PcInstance player, int[][] droplist)
	{
		final int chance = getRandom(100);
		
		for (int[] drop : droplist)
		{
			if (chance >= drop[1])
			{
				mob.dropItem(player, drop[0], getRandom(drop[2], drop[3]));
				break;
			}
		}
	}
	
	public static void main(String[] args)
	{
		new Elpies();
	}
}
