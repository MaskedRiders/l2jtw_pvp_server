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
package custom.events.Race;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import com.l2jserver.Config;
import com.l2jserver.gameserver.Announcements;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Event;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.skills.AbnormalType;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Gnacik
 */
public final class Race extends Event
{
	// Event NPC's list
	private List<L2Npc> _npclist;
	// Npc
	private L2Npc _npc;
	// Player list
	private List<L2PcInstance> _players;
	// Event Task
	ScheduledFuture<?> _eventTask = null;
	// Event state
	private static boolean _isactive = false;
	// Race state
	private static boolean _isRaceStarted = false;
	// 5 min for register
	private static final int _time_register = 5;
	// 5 min for race
	private static final int _time_race = 10;
	// NPC's
	private static final int _start_npc = 900103;
	private static final int _stop_npc = 900104;
	// Skills (Frog by default)
	private static int _skill = 6201;
	// We must keep second NPC spawn for radar
	private static int[] _randspawn = null;
	// Locations
	private static final String[] _locations =
	{
		"異端者的地下墓穴",
		"迪恩城堡前方橋",
		"芙羅蘭村莊入口",
		"芙羅蘭邊境要塞"
	};
	
	// @formatter:off
	private static final int[][] _coords =
	{
		// x, y, z, heading
		{ 39177, 144345, -3650, 0 },
		{ 22294, 155892, -2950, 0 },
		{ 16537, 169937, -3500, 0 },
		{  7644, 150898, -2890, 0 }
	};
	private static final int[][] _rewards =
	{
		{ 6622, 2 }, // 巨人的經典
		{ 9625, 2 }, // 巨人的經典 - 遺忘篇
		{ 9626, 2 }, // 巨人的經典 - 訓練篇
		{ 9627, 2 }, // 巨人的經典 - 熟練篇
		{ 9546, 5 }, // 火之原石
		{ 9547, 5 }, // 水之原石
		{ 9548, 5 }, // 地之原石
		{ 9549, 5 }, // 風之原石
		{ 9550, 5 }, // 暗之原石
		{ 9551, 5 }, // 聖之原石
		{ 9574, 3 }, // 中級生命石-80級
		{ 9575, 2 }, // 高級生命石-80級
		{ 9576, 1 }, // 特級生命石-80級
		{ 20034,1 }  // 萬聖節活力糖果-活動用
	};
	// @formatter:on
	
	private Race()
	{
		super(Race.class.getSimpleName(), "custom/events");
		addStartNpc(_start_npc);
		addFirstTalkId(_start_npc);
		addTalkId(_start_npc);
		addStartNpc(_stop_npc);
		addFirstTalkId(_stop_npc);
		addTalkId(_stop_npc);
	}
	
	@Override
	public boolean eventStart(L2PcInstance eventMaker)
	{
		// Don't start event if its active
		if (_isactive)
		{
			return false;
		}
		// Check Custom Table - we use custom NPC's
		if (!Config.CUSTOM_NPC_DATA)
		{
			_log.info(getName() + ": Event can't be started, because custom npc table is disabled!");
			eventMaker.sendMessage("Event " + getName() + " can't be started because custom NPC table is disabled!");
			return false;
		}
		// Initialize list
		_npclist = new ArrayList<>();
		_players = new CopyOnWriteArrayList<>();
		// Set Event active
		_isactive = true;
		// Spawn Manager
		_npc = recordSpawn(_start_npc, 18429, 145861, -3090, 0, false, 0);
		
		// Announce event start
		Announcements.getInstance().announceToAll("* 賽跑活動開始! *");
		Announcements.getInstance().announceToAll("活動相關內容與報名，請找迪恩城鎮內的活動管理員。活動即將開始，你還有 " + _time_register + " 分鐘能參與報名...");
		
		// Schedule Event end
		_eventTask = ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
			@Override
			public void run()
			{
				StartRace();
			}
		}, _time_register * 60 * 1000);
		
		return true;
		
	}
	
	protected void StartRace()
	{
		// Abort race if no players signup
		if (_players.isEmpty())
		{
			Announcements.getInstance().announceToAll("無人參賽，賽跑活動取消!");
			eventStop();
			return;
		}
		// Set state
		_isRaceStarted = true;
		// Announce
		Announcements.getInstance().announceToAll("開跑！");
		// Get random Finish
		int location = getRandom(0, _locations.length - 1);
		_randspawn = _coords[location];
		// And spawn NPC
		recordSpawn(_stop_npc, _randspawn[0], _randspawn[1], _randspawn[2], _randspawn[3], false, 0);
		// Transform players and send message
		for (L2PcInstance player : _players)
		{
			if ((player != null) && player.isOnline())
			{
				if (player.isInsideRadius(_npc, 500, false, false))
				{
					sendMessage(player, "比賽開始！用你最快的速度跑到終點NPC那邊！他的位置在 " + _locations[location]+" 附近");
					transformPlayer(player);
					player.getRadar().addMarker(_randspawn[0], _randspawn[1], _randspawn[2]);
				}
				else
				{
					sendMessage(player, "我不是有叫你待在我身邊嗎？ 你離我太遠了, 你被踢出競賽了");
					_players.remove(player);
				}
			}
		}
		// Schedule timeup for Race
		_eventTask = ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
			@Override
			public void run()
			{
				timeUp();
			}
		}, _time_race * 60 * 1000);
	}
	
	@Override
	public boolean eventStop()
	{
		// Don't stop inactive event
		if (!_isactive)
		{
			return false;
		}
		
		// Set inactive
		_isactive = false;
		_isRaceStarted = false;
		
		// Cancel task if any
		if (_eventTask != null)
		{
			_eventTask.cancel(true);
			_eventTask = null;
		}
		// Untransform players
		// Teleport to event start point
		for (L2PcInstance player : _players)
		{
			if ((player != null) && player.isOnline())
			{
				player.untransform();
				player.teleToLocation(_npc.getX(), _npc.getY(), _npc.getZ(), true);
			}
		}
		// Despawn NPCs
		for (L2Npc _npc : _npclist)
		{
			if (_npc != null)
			{
				_npc.deleteMe();
			}
		}
		_npclist.clear();
		_players.clear();
		// Announce event end
		Announcements.getInstance().announceToAll("* 賽跑活動結束 *");
		
		return true;
	}
	
	@Override
	public boolean eventBypass(L2PcInstance activeChar, String bypass)
	{
		if (bypass.startsWith("skill"))
		{
			if (_isRaceStarted)
			{
				activeChar.sendMessage("賽跑活動進行中，現在你無法變更變身技能");
			}
			else
			{
				int _number = Integer.valueOf(bypass.substring(5));
				Skill _sk = SkillData.getInstance().getSkill(_number, 1);
				if (_sk != null)
				{
					_skill = _number;
					activeChar.sendMessage("變身技能設定成:");
					activeChar.sendMessage(_sk.getName());
				}
				else
				{
					activeChar.sendMessage("變更變身技能時發生錯誤");
				}
			}
			
		}
		else if (bypass.startsWith("tele"))
		{
			if ((Integer.valueOf(bypass.substring(4)) > 0) && (_randspawn != null))
			{
				activeChar.teleToLocation(_randspawn[0], _randspawn[1], _randspawn[2]);
			}
			else
			{
				activeChar.teleToLocation(18429, 145861, -3090);
			}
		}
		showMenu(activeChar);
		return true;
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return null;
		}
		
		if (event.equalsIgnoreCase("transform"))
		{
			transformPlayer(player);
			return null;
		}
		else if (event.equalsIgnoreCase("untransform"))
		{
			player.untransform();
			return null;
		}
		else if (event.equalsIgnoreCase("showfinish"))
		{
			player.getRadar().addMarker(_randspawn[0], _randspawn[1], _randspawn[2]);
			return null;
		}
		else if (event.equalsIgnoreCase("signup"))
		{
			if (_players.contains(player))
			{
				return "900103-onlist.htm";
			}
			_players.add(player);
			return "900103-signup.htm";
		}
		else if (event.equalsIgnoreCase("quit"))
		{
			player.untransform();
			if (_players.contains(player))
			{
				_players.remove(player);
			}
			return "900103-quit.htm";
		}
		else if (event.equalsIgnoreCase("finish"))
		{
			if (player.isAffectedBySkill(_skill))
			{
				winRace(player);
				return "900104-winner.htm";
			}
			return "900104-notrans.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			st = newQuestState(player);
		}
		if (npc.getId() == _start_npc)
		{
			if (_isRaceStarted)
			{
				return _start_npc + "-started-" + isRacing(player) + ".htm";
			}
			return _start_npc + "-" + isRacing(player) + ".htm";
		}
		else if ((npc.getId() == _stop_npc) && _isRaceStarted)
		{
			return _stop_npc + "-" + isRacing(player) + ".htm";
		}
		return npc.getId() + ".htm";
	}
	
	private int isRacing(L2PcInstance player)
	{
		return _players.contains(player) ? 1 : 0;
	}
	
	private L2Npc recordSpawn(int npcId, int x, int y, int z, int heading, boolean randomOffSet, long despawnDelay)
	{
		final L2Npc npc = addSpawn(npcId, x, y, z, heading, randomOffSet, despawnDelay);
		if (npc != null)
		{
			_npclist.add(npc);
		}
		return npc;
	}
	
	private void transformPlayer(L2PcInstance player)
	{
		if (player.isTransformed() || player.isInStance())
		{
			player.untransform();
		}
		if (player.isSitting())
		{
			player.standUp();
		}
		
		player.getEffectList().stopSkillEffects(true, AbnormalType.SPEED_UP);
		player.stopSkillEffects(true, 268); // Song of Wind
		player.stopSkillEffects(true, 298); // Rabbit Spirit Totem
		SkillData.getInstance().getSkill(_skill, 1).applyEffects(player, player);
	}
	
	private void sendMessage(L2PcInstance player, String text)
	{
		player.sendPacket(new CreatureSay(_npc.getObjectId(), 20, _npc.getName(), text));
	}
	
	private void showMenu(L2PcInstance activeChar)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage();
		String content = getHtm(activeChar.getHtmlPrefix(), "admin_menu.htm");
		html.setHtml(content);
		activeChar.sendPacket(html);
	}
	
	protected void timeUp()
	{
		Announcements.getInstance().announceToAll("時間到, 沒人贏得勝利!");
		eventStop();
	}
	
	private void winRace(L2PcInstance player)
	{
		int[] _reward = _rewards[getRandom(_rewards.length - 1)];
		player.addItem("eventModRace", _reward[0], _reward[1], _npc, true);
		Announcements.getInstance().announceToAll(player.getName() + " 獲勝了!");
		eventStop();
	}
	
	public static void main(String[] args)
	{
		new Race();
	}
}