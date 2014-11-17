/*
 * Copyright (C) 2004-2014 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.model.entity;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;

import com.l2jserver.Config;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.datatables.DoorTable;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.datatables.NpcData;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.instancemanager.AntiFeedManager;
import com.l2jserver.gameserver.instancemanager.InstantWorldManager;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.L2Summon;
import com.l2jserver.gameserver.model.actor.instance.L2DoorInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PetInstance;
import com.l2jserver.gameserver.model.actor.instance.L2ServitorInstance;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.model.events.EventDispatcher;
import com.l2jserver.gameserver.model.events.impl.events.OnTvTEventFinish;
import com.l2jserver.gameserver.model.events.impl.events.OnTvTEventKill;
import com.l2jserver.gameserver.model.events.impl.events.OnTvTEventRegistrationStart;
import com.l2jserver.gameserver.model.events.impl.events.OnTvTEventStart;
import com.l2jserver.gameserver.model.itemcontainer.PcInventory;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillUse;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.StatusUpdate;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.util.Rnd;
import com.l2jserver.util.StringUtil;
import com.l2jserver.gameserver.datatables.MessageTable;
import com.l2jserver.gameserver.model.events.impl.events.OnTvTEventMeeting;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author FBIagent
 */
public class TvTEvent
{	

	enum EventState
	{
		INACTIVE, // 非アクティブ
		PARTICIPATING, // 参加
		STARTING, // 開始プロセス中
		MEETING, // ミーティング
		STARTED, // 開始プロセス終了
		REWARDING, // 報酬付与中
		INACTIVATING, // 非アクティブにするプロセス中
	}

	protected static final Logger _log = Logger.getLogger(TvTEvent.class.getName());
	/** html path **/
	private static final String htmlPath = "data/scripts/custom/events/TvT/TvTManager/";
	/** The teams of the TvTEvent<br> */
	private static TvTEventTeam[] _teams = new TvTEventTeam[2];
	/** The state of the TvTEvent<br> */
	private static EventState _state = EventState.INACTIVE;
	/** The spawn of the participation npc<br> */
	private static L2Spawn _npcSpawn = null;
	/** the npc instance of the participation npc<br> */
	private static L2Npc _lastNpcSpawn = null;
	/** Instance id<br> */
	private static int _TvTEventInstantWorldId = 0;
	
	private static TvTConfigStringParser.TvTPattern _pattern = TvTConfigStringParser._patterns.get(TvTConfigStringParser._currentId);
	
	/**
	 * No instance of this class!<br>
	 */
	private TvTEvent()
	{
	}
	
	/**
	 * Teams initializing<br>
	 */
	public static void init()
	{
		AntiFeedManager.getInstance().registerEvent(AntiFeedManager.TVT_ID);
		_teams[0] = new TvTEventTeam(_pattern.TvTEventTeam1Name, _pattern.TvTEventTeam1Coordinates);
		_teams[1] = new TvTEventTeam(_pattern.TvTEventTeam2Name, _pattern.TvTEventTeam2Coordinates);
	}
	
	
	/**
	 * setTvTPattern<br>
	 * 1. XML読み込み<br>
	 * 2. 実行したいTvTPatternIdを選択<br>
	 * 3. チームを再定義<br>
	 * <br>
	 * @return boolean: true if success, otherwise false<br>
	 */
	public static boolean setTvTPattern() {
		File xml = new File(Config.DATAPACK_ROOT, "/data/tvtPatterns.xml");
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			if (!xml.exists())
			{
				_log.severe("[setTvTPattern] Missing tvtPattern.xml.");
				return false;
			}
			factory.setValidating(false); // バリデーション無視
			factory.setIgnoringComments(true); // コメント無視
			TvTConfigStringParser.parseXMLNodes(factory.newDocumentBuilder().parse(xml));
		}
		catch (IOException e)
		{
			_log.log(Level.WARNING, "Instance: can not find " + xml.getAbsolutePath() + " ! " + e.getMessage(), e);
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Instance: error while loading " + xml.getAbsolutePath() + " ! " + e.getMessage(), e);
		}

		_teams[0] = new TvTEventTeam(_pattern.TvTEventTeam1Name, _pattern.TvTEventTeam1Coordinates);
		_teams[1] = new TvTEventTeam(_pattern.TvTEventTeam2Name, _pattern.TvTEventTeam2Coordinates);

		return true;
	}
	
	/**
	 * Starts the participation of the TvTEvent<br>
	 * 1. Get L2NpcTemplate by Config.TVT_EVENT_PARTICIPATION_NPC_ID<br>
	 * 2. Try to spawn a new npc of it<br>
	 * <br>
	 * @return boolean: true if success, otherwise false<br>
	 */
	public static boolean startParticipation()
	{	
		L2NpcTemplate tmpl = NpcData.getInstance().getTemplate(_pattern.TvTEventParticipationNpcId);
		
		if (tmpl == null)
		{
			_log.warning("TvTEventEngine[TvTEvent.startParticipation()]: L2NpcTemplate is a NullPointer -> Invalid npc id in configs?");
			return false;
		}
		
		try
		{
			_npcSpawn = new L2Spawn(tmpl);

			_npcSpawn.setX(_pattern.TvTEventParticipationNpcCoordinates[0]);
			_npcSpawn.setY(_pattern.TvTEventParticipationNpcCoordinates[1]);
			_npcSpawn.setZ(_pattern.TvTEventParticipationNpcCoordinates[2]);
			_npcSpawn.setAmount(1);
			_npcSpawn.setHeading(_pattern.TvTEventParticipationNpcCoordinates[3]);
			_npcSpawn.setRespawnDelay(1);
			// later no need to delete spawn from db, we don't store it (false)
			SpawnTable.getInstance().addNewSpawn(_npcSpawn, false);
			_npcSpawn.init();
			_lastNpcSpawn = _npcSpawn.getLastSpawn();
			_lastNpcSpawn.setCurrentHp(_lastNpcSpawn.getMaxHp());
			_lastNpcSpawn.setTitle("TvT Event Participation");
			_lastNpcSpawn.isAggressive();
			_lastNpcSpawn.decayMe();
			_lastNpcSpawn.spawnMe(_npcSpawn.getLastSpawn().getX(), _npcSpawn.getLastSpawn().getY(), _npcSpawn.getLastSpawn().getZ());
			_lastNpcSpawn.broadcastPacket(new MagicSkillUse(_lastNpcSpawn, _lastNpcSpawn, 1034, 1, 1, 1));
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "TvTEventEngine[TvTEvent.startParticipation()]: exception: " + e.getMessage(), e);
			return false;
		}
		
		setState(EventState.PARTICIPATING);
		EventDispatcher.getInstance().notifyEventAsync(new OnTvTEventRegistrationStart());
		return true;
	}
	
	private static int highestLevelPcInstanceOf(Map<Integer, L2PcInstance> players)
	{
		int maxLevel = Integer.MIN_VALUE, maxLevelId = -1;
		for (L2PcInstance player : players.values())
		{
			if (player.getLevel() >= maxLevel)
			{
				maxLevel = player.getLevel();
				maxLevelId = player.getObjectId();
			}
		}
		return maxLevelId;
	}
	
	/**
	 * Starts the TvTEvent pre fight<br>
	 * 1. stateをEventState.STARTINGにする<br>
	 * 2. チームのバランスを調整する<br>
	 * 3. 参加者が十分いない時はfalseを返し<br>
	 * 4. 参加費を徴収<br>
	 * 5. インスタントダンジョン（ID）をコンフィグに応じて生成<br>
	 * 6. ドアをコンフィグに応じて閉じる<br>
	 * 7. stateをEventState.STARTEDにする<br>
	 * 8. プレイヤーをテレポーターにセット。<br>
	 * <br>
	 * @return boolean: true if success, otherwise false<br>
	 */
	public static boolean startPreFight()
	{
		// Set state to STARTING
		setState(EventState.STARTING);
		
		// チームバランスをとるここから
		Map<Integer, L2PcInstance> allParticipants = new FastMap<>();
		for (TvTEventTeam team : _teams) {
			allParticipants.putAll(team.getParticipatedPlayers());
			team.cleanMe();
		}
		
		L2PcInstance player;
		Iterator<L2PcInstance> players;
		if (needParticipationFee())
		{
			players = allParticipants.values().iterator();
			while (players.hasNext())
			{
				player = players.next();
				if (!hasParticipationFee(player))
				{
					players.remove();
				}
			}
		}
		
		int balance[] ={0, 0};
		int priority = 0;
		int highestLevelPlayerId;
		
		L2PcInstance highestLevelPlayer;
		// TODO: allParticipants should be sorted by level instead of using highestLevelPcInstanceOf for every fetch
		while (!allParticipants.isEmpty())
		{
			// Priority team gets one player
			highestLevelPlayerId = highestLevelPcInstanceOf(allParticipants);
			highestLevelPlayer = allParticipants.get(highestLevelPlayerId);
			allParticipants.remove(highestLevelPlayerId);
			_teams[priority].addPlayer(highestLevelPlayer);
			balance[priority] += highestLevelPlayer.getLevel();
			// Exiting if no more players
			if (allParticipants.isEmpty())
			{
				break;
			}
			// The other team gets one player
			// TODO: Code not dry
			priority = 1 - priority;
			highestLevelPlayerId = highestLevelPcInstanceOf(allParticipants);
			highestLevelPlayer = allParticipants.get(highestLevelPlayerId);
			allParticipants.remove(highestLevelPlayerId);
			_teams[priority].addPlayer(highestLevelPlayer);
			balance[priority] += highestLevelPlayer.getLevel();
			// Recalculating priority
			priority = balance[0] > balance[1] ? 1 : 0;
		}
		// チームバランスをとるここまで
		
		// 参加条件のチェック
		if ((_teams[0].getParticipatedPlayerCount() < Config.TVT_EVENT_MIN_PLAYERS_IN_TEAMS) || (_teams[1].getParticipatedPlayerCount() < Config.TVT_EVENT_MIN_PLAYERS_IN_TEAMS))
		{
			// 参加条件を満たしていない
			// Set state INACTIVE
			setState(EventState.INACTIVE);
			// Cleanup of teams
			for (TvTEventTeam team : _teams) team.cleanMe();
			// Unspawn the event NPC
			unSpawnNpc();
			AntiFeedManager.getInstance().clear(AntiFeedManager.TVT_ID);
			return false;
		}

		if (needParticipationFee())
		{
			// 参加費を徴収
			for (TvTEventTeam team : _teams)
			{
				players = team.getParticipatedPlayers().values().iterator();
				while (players.hasNext())
				{
					player = players.next();
					if (!payParticipationFee(player))
					{
						players.remove();
					}
				}
			}
		}
		
		// インスタントダンジョン（ID）を生成
		if (Config.TVT_EVENT_IN_INSTANCE)
		{
			try
			{
				_TvTEventInstantWorldId = InstantWorldManager.getInstance().createInstantWorld(_pattern.TvTEventInstanceFile);
				InstantWorldManager.getInstance().getInstantWorld(_TvTEventInstantWorldId).setAllowSummon(false);
				InstantWorldManager.getInstance().getInstantWorld(_TvTEventInstantWorldId).setPvPInstance(true);
				InstantWorldManager.getInstance().getInstantWorld(_TvTEventInstantWorldId).setEmptyDestroyTime((Config.TVT_EVENT_START_LEAVE_TELEPORT_DELAY * 1000) + 60000L);
			}
			catch (Exception e)
			{
				_TvTEventInstantWorldId = 0;
				_log.log(Level.WARNING, "TvTEventEngine[TvTEvent.createDynamicInstance]: exception: " + e.getMessage(), e);
			}
		}
		
		// ドアを閉じる
		// Opens all doors specified in configs for tvt
		openDoors(_pattern.TvTDoorsToOpen);
		// Closes all doors specified in configs for tvt
		closeDoors(_pattern.TvTDoorsToClose);
		// Set state MEETING
		setState(EventState.MEETING);

		// Iterate over all teams
		for (TvTEventTeam team : _teams)
		{
			// Iterate over all participated player instances in this team
			for (L2PcInstance playerInstance : team.getParticipatedPlayers().values())
			{
				if (playerInstance != null)
				{
					// Disable player revival.
					playerInstance.setCanRevive(false);
					// Teleporter implements Runnable and starts itself
					new TvTEventTeleporter(playerInstance, team.getCoordinates(), false, false);
				}
			}
		}

		// Notify to scripts.
		EventDispatcher.getInstance().notifyEventAsync(new OnTvTEventMeeting());
		return true;
	}
	
	public static void startFight()
	{
		// Set state STARTED
		setState(EventState.STARTED);
		// Notify to scripts.
		EventDispatcher.getInstance().notifyEventAsync(new OnTvTEventStart());
	}
	
	/*
	 * 結果メッセージ
	 */
	public static String getResultMessage()
	{
		if (_teams[0].getPoints() == _teams[1].getPoints())
		{
			// Check if one of the teams have no more players left
			if ((_teams[0].getParticipatedPlayerCount() == 0) || (_teams[1].getParticipatedPlayerCount() == 0))
			{
				// set state to rewarding
				setState(EventState.REWARDING);
				// TvT Event: Event has ended. No team won due to inactivity!
				// TVTイベント：イベントは終了しました。両チーム何もしませんでした！
				return MessageTable.Messages[457].getMessage();
			}
			
			// Both teams have equals points
			// TvT Event: Event has ended, both teams have tied.
			// TVTイベント：引き分けです
			sysMsgToAllParticipants(MessageTable.Messages[458].getMessage());
			// TvT Event: Event has ended with both teams tying.
			// TVTイベント：両チームが同点で終了しました。
			return MessageTable.Messages[459].getMessage();
		}
		TvTEventTeam team = _teams[_teams[0].getPoints() > _teams[1].getPoints() ? 0 : 1];
		// TvT Event: Event finish. Team ; won with ; kills.
		// TVTイベント：終了。チーム;が勝った;Kills
		return MessageTable.Messages[460].getExtra(1) + team.getName() + MessageTable.Messages[460].getExtra(2) + team.getPoints() + MessageTable.Messages[460].getExtra(3);
	}
	
	/*
	 * 報酬計算
	 */
	public static void calculateRewards()
	{
		// Set state REWARDING so nobody can point anymore
		setState(EventState.REWARDING);
		
		// Get team which has more points
		if (_teams[0].getPoints() > _teams[1].getPoints()){
			giveReward(_teams[0]);
		}
		else if(_teams[0].getPoints() < _teams[1].getPoints()){
			giveReward(_teams[1]);
		}
		else{
			if (Config.TVT_REWARD_TEAM_TIE)
			{
				giveReward(_teams[0]);
				giveReward(_teams[1]);
			}
		}
		// Notify to scripts.
		EventDispatcher.getInstance().notifyEventAsync(new OnTvTEventFinish());
	}
	
	/*
	 * 報酬付与
	 */
	private static void giveReward(TvTEventTeam team){
		// Iterate over all participated player instances of the winning team
		for (L2PcInstance player : team.getParticipatedPlayers().values())
		{
			// Check for nullpointer
			if (player == null)
			{
				continue;
			}
			
			SystemMessage systemMessage = null;
			
			// Iterate over all tvt event rewards
			for (int[] reward : _pattern.TvTEventReward)
			{
				PcInventory inv = player.getInventory();
				
				// Check for stackable item, non stackabe items need to be added one by one
				if (ItemTable.getInstance().createDummyItem(reward[0]).isStackable())
				{
					inv.addItem("TvT Event", reward[0], reward[1], player, player);
					
					if (reward[1] > 1)
					{
						systemMessage = SystemMessage.getSystemMessage(SystemMessageId.EARNED_S2_S1_S);
						systemMessage.addItemName(reward[0]);
						systemMessage.addLong(reward[1]);
					}
					else
					{
						systemMessage = SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1);
						systemMessage.addItemName(reward[0]);
					}
					
					player.sendPacket(systemMessage);
				}
				else
				{
					for (int i = 0; i < reward[1]; ++i)
					{
						inv.addItem("TvT Event", reward[0], 1, player, player);
						systemMessage = SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1);
						systemMessage.addItemName(reward[0]);
						player.sendPacket(systemMessage);
					}
				}
			}
			
			StatusUpdate statusUpdate = new StatusUpdate(player);
			final NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage();
			
			statusUpdate.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
			npcHtmlMessage.setHtml(HtmCache.getInstance().getHtm(player.getHtmlPrefix(), htmlPath + "Reward.html"));
			player.sendPacket(statusUpdate);
			player.sendPacket(npcHtmlMessage);
		}
	}
	
	/**
	 * Stops the TvTEvent fight<br>
	 * 1. Set state EventState.INACTIVATING<br>
	 * 2. Remove tvt npc from world<br>
	 * 3. Open doors specified in configs<br>
	 * 4. Teleport all participants back to participation npc location<br>
	 * 5. Teams cleaning<br>
	 * 6. Set state EventState.INACTIVE<br>
	 */
	public static void stopFight()
	{
		// Set state INACTIVATING
		setState(EventState.INACTIVATING);
		// Unspawn event npc
		unSpawnNpc();
		// Opens all doors specified in configs for tvt
		openDoors(_pattern.TvTDoorsToClose);
		// Closes all doors specified in Configs for tvt
		closeDoors(_pattern.TvTDoorsToOpen);
		
		// Iterate over all teams
		for (TvTEventTeam team : _teams)
		{
			for (L2PcInstance playerInstance : team.getParticipatedPlayers().values())
			{
				// Check for nullpointer
				if (playerInstance != null)
				{
					// Enable player revival.
					playerInstance.setCanRevive(true);
					// Teleport back.
					new TvTEventTeleporter(playerInstance, _pattern.TvTEventParticipationNpcCoordinates, false, false);
				}
			}
		}
		
		// Cleanup of teams
		_teams[0].cleanMe();
		_teams[1].cleanMe();
		// Set state INACTIVE
		setState(EventState.INACTIVE);
		AntiFeedManager.getInstance().clear(AntiFeedManager.TVT_ID);
	}
	
	/**
	 * Adds a player to a TvTEvent team<br>
	 * 1. Calculate the id of the team in which the player should be added<br>
	 * 2. Add the player to the calculated team<br>
	 * <br>
	 * @param playerInstance as L2PcInstance<br>
	 * @return boolean: true if success, otherwise false<br>
	 */
	public static synchronized boolean addParticipant(L2PcInstance playerInstance)
	{
		// Check for nullpoitner
		if (playerInstance == null)
		{
			return false;
		}
		
		byte teamId = 0;
		
		// Check to which team the player should be added
		if (_teams[0].getParticipatedPlayerCount() == _teams[1].getParticipatedPlayerCount())
		{
			teamId = (byte) (Rnd.get(2));
		}
		else
		{
			teamId = (byte) (_teams[0].getParticipatedPlayerCount() > _teams[1].getParticipatedPlayerCount() ? 1 : 0);
		}
		playerInstance.addEventListener(new TvTEventListener(playerInstance));
		return _teams[teamId].addPlayer(playerInstance);
	}
	
	/**
	 * Removes a TvTEvent player from it's team<br>
	 * 1. Get team id of the player<br>
	 * 2. Remove player from it's team<br>
	 * <br>
	 * @param playerObjectId
	 * @return boolean: true if success, otherwise false
	 */
	public static boolean removeParticipant(int playerObjectId)
	{
		// Get the teamId of the player
		byte teamId = getParticipantTeamId(playerObjectId);
		
		// Check if the player is participant
		if (teamId != -1)
		{
			// Remove the player from team
			_teams[teamId].removePlayer(playerObjectId);
			
			final L2PcInstance player = L2World.getInstance().getPlayer(playerObjectId);
			if (player != null)
			{
				player.removeEventListener(TvTEventListener.class);
			}
			return true;
		}
		
		return false;
	}
	
	public static boolean needParticipationFee()
	{
		return (_pattern.TvTEventParticipationFee[0] != 0) && (_pattern.TvTEventParticipationFee[1] != 0);
	}
	
	public static boolean hasParticipationFee(L2PcInstance playerInstance)
	{
		return playerInstance.getInventory().getInventoryItemCount(_pattern.TvTEventParticipationFee[0], -1) >= _pattern.TvTEventParticipationFee[1];
	}
	
	public static boolean payParticipationFee(L2PcInstance playerInstance)
	{
		return playerInstance.destroyItemByItemId("TvT Participation Fee", _pattern.TvTEventParticipationFee[0], _pattern.TvTEventParticipationFee[1], _lastNpcSpawn, true);
	}
	
	public static String getParticipationFee()
	{
		int itemId = _pattern.TvTEventParticipationFee[0];
		int itemNum = _pattern.TvTEventParticipationFee[1];
		
		if ((itemId == 0) || (itemNum == 0))
		{
			return "-";
		}
		
		return StringUtil.concat(String.valueOf(itemNum), " ", ItemTable.getInstance().getTemplate(itemId).getName());
	}
	
	/**
	 * Send a SystemMessage to all participated players<br>
	 * 1. Send the message to all players of team number one<br>
	 * 2. Send the message to all players of team number two<br>
	 * <br>
	 * @param message as String<br>
	 */
	public static void sysMsgToAllParticipants(String message)
	{
		for (L2PcInstance playerInstance : _teams[0].getParticipatedPlayers().values())
		{
			if (playerInstance != null)
			{
				playerInstance.sendMessage(message);
			}
		}
		
		for (L2PcInstance playerInstance : _teams[1].getParticipatedPlayers().values())
		{
			if (playerInstance != null)
			{
				playerInstance.sendMessage(message);
			}
		}
	}
	
	private static L2DoorInstance getDoor(int doorId)
	{
		L2DoorInstance door = null;
		if (_TvTEventInstantWorldId <= 0)
		{
			door = DoorTable.getInstance().getDoor(doorId);
		}
		else
		{
			final InstantWorld inst = InstantWorldManager.getInstance().getInstantWorld(_TvTEventInstantWorldId);
			if (inst != null)
			{
				door = inst.getDoor(doorId);
			}
		}
		return door;
	}
	
	/**
	 * Close doors specified in configs
	 * @param doors
	 */
	private static void closeDoors(List<Integer> doors)
	{
		for (int doorId : doors)
		{
			final L2DoorInstance doorInstance = getDoor(doorId);
			if (doorInstance != null)
			{
				doorInstance.closeMe();
			}
		}
	}
	
	/**
	 * Open doors specified in configs
	 * @param doors
	 */
	private static void openDoors(List<Integer> doors)
	{
		for (int doorId : doors)
		{
			final L2DoorInstance doorInstance = getDoor(doorId);
			if (doorInstance != null)
			{
				doorInstance.openMe();
			}
		}
	}
	
	/**
	 * UnSpawns the TvTEvent npc
	 */
	private static void unSpawnNpc()
	{
		// Delete the npc
		_lastNpcSpawn.deleteMe();
		SpawnTable.getInstance().deleteSpawn(_lastNpcSpawn.getSpawn(), false);
		// Stop respawning of the npc
		_npcSpawn.stopRespawn();
		_npcSpawn = null;
		_lastNpcSpawn = null;
	}
	
	/**
	 * Called when a player logs in<br>
	 * <br>
	 * @param playerInstance as L2PcInstance<br>
	 */
	public static void onLogin(L2PcInstance playerInstance)
	{
		if ((playerInstance == null) || (!isStarting() && !isMeeting() && !isStarted()))
		{
			return;
		}
		
		byte teamId = getParticipantTeamId(playerInstance.getObjectId());
		
		if (teamId == -1)
		{
			return;
		}
		
		_teams[teamId].addPlayer(playerInstance);
		new TvTEventTeleporter(playerInstance, _teams[teamId].getCoordinates(), true, false);
	}
	
	/**
	 * Called when a player logs out<br>
	 * <br>
	 * @param playerInstance as L2PcInstance<br>
	 */
	public static void onLogout(L2PcInstance playerInstance)
	{
		if ((playerInstance != null) && (isStarting() || isMeeting() || isStarted() || isParticipating()))
		{
			if (removeParticipant(playerInstance.getObjectId()))
			{
				
				playerInstance.setXYZInvisible((_pattern.TvTEventParticipationNpcCoordinates[0] + Rnd.get(101)) - 50, (_pattern.TvTEventParticipationNpcCoordinates[1] + Rnd.get(101)) - 50, _pattern.TvTEventParticipationNpcCoordinates[2]);
			}
		}
	}
	
	/**
	 * Called on every onAction in L2PcIstance<br>
	 * <br>
	 * @param playerInstance
	 * @param targetedPlayerObjectId
	 * @return boolean: true if player is allowed to target, otherwise false
	 */
	public static boolean onAction(L2PcInstance playerInstance, int targetedPlayerObjectId)
	{
		if ((playerInstance == null) || !(isStarted() || isMeeting()))
		{
			return true;
		}
		
		if (playerInstance.isGM())
		{
			return true;
		}
		
		byte playerTeamId = getParticipantTeamId(playerInstance.getObjectId());
		byte targetedPlayerTeamId = getParticipantTeamId(targetedPlayerObjectId);
		
		if (((playerTeamId != -1) && (targetedPlayerTeamId == -1)) || ((playerTeamId == -1) && (targetedPlayerTeamId != -1)))
		{
			return false;
		}
		
		if ((playerTeamId != -1) && (targetedPlayerTeamId != -1) && (playerTeamId == targetedPlayerTeamId) && (playerInstance.getObjectId() != targetedPlayerObjectId) && !Config.TVT_EVENT_TARGET_TEAM_MEMBERS_ALLOWED)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Called on every scroll use<br>
	 * <br>
	 * @param playerObjectId
	 * @return boolean: true if player is allowed to use scroll, otherwise false
	 */
	public static boolean onScrollUse(int playerObjectId)
	{
		if (!(isStarted() || isMeeting()))
		{
			return true;
		}
		
		if (isPlayerParticipant(playerObjectId) && !Config.TVT_EVENT_SCROLL_ALLOWED)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Called on every potion use
	 * @param playerObjectId
	 * @return boolean: true if player is allowed to use potions, otherwise false
	 */
	public static boolean onPotionUse(int playerObjectId)
	{
		if (!(isStarted() || isMeeting()))
		{
			return true;
		}
		
		if (isPlayerParticipant(playerObjectId) && !Config.TVT_EVENT_POTIONS_ALLOWED)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Called on every escape use(thanks to nbd)
	 * @param playerObjectId
	 * @return boolean: true if player is not in tvt event, otherwise false
	 */
	public static boolean onEscapeUse(int playerObjectId)
	{
		if (!(isStarted() || isMeeting()))
		{
			return true;
		}
		
		if (isPlayerParticipant(playerObjectId))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * TvT用の変則的チームにのみ聞こえるチャットの使用許可
	 * @param playerObjectId
	 * @return boolean: true if player is not in tvt event, otherwise false
	 */
	public static boolean onTeamOnlyChat(int playerObjectId)
	{
		if (isStarted() || isMeeting())
		{
			if (isPlayerParticipant(playerObjectId))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Called on every summon item use
	 * @param playerObjectId
	 * @return boolean: true if player is allowed to summon by item, otherwise false
	 */
	public static boolean onItemSummon(int playerObjectId)
	{
		if (!(isStarted() || isMeeting()))
		{
			return true;
		}
		
		if (isPlayerParticipant(playerObjectId) && !Config.TVT_EVENT_SUMMON_BY_ITEM_ALLOWED)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Is called when a player is killed<br>
	 * <br>
	 * @param killerCharacter as L2Character<br>
	 * @param killedPlayerInstance as L2PcInstance<br>
	 */
	public static void onKill(L2Character killerCharacter, L2PcInstance killedPlayerInstance)
	{
		if ((killedPlayerInstance == null) || !isStarted())
		{
			return;
		}
		
		byte killedTeamId = getParticipantTeamId(killedPlayerInstance.getObjectId());
		
		if (killedTeamId == -1)
		{
			return;
		}
		
		new TvTEventTeleporter(killedPlayerInstance, _teams[killedTeamId].getCoordinates(), false, false);
		
		if (killerCharacter == null)
		{
			return;
		}
		
		L2PcInstance killerPlayerInstance = null;
		
		if ((killerCharacter instanceof L2PetInstance) || (killerCharacter instanceof L2ServitorInstance))
		{
			killerPlayerInstance = ((L2Summon) killerCharacter).getOwner();
			
			if (killerPlayerInstance == null)
			{
				return;
			}
		}
		else if (killerCharacter instanceof L2PcInstance)
		{
			killerPlayerInstance = (L2PcInstance) killerCharacter;
		}
		else
		{
			return;
		}
		
		byte killerTeamId = getParticipantTeamId(killerPlayerInstance.getObjectId());
		
		if ((killerTeamId != -1) && (killedTeamId != -1) && (killerTeamId != killedTeamId))
		{
			TvTEventTeam killerTeam = _teams[killerTeamId];
			
			killerTeam.increasePoints();
			
			/* MessageTable
			CreatureSay cs = new CreatureSay(killerPlayerInstance.getObjectId(), Say2.TELL, killerPlayerInstance.getName(), "I have killed " + killedPlayerInstance.getName() + "!");
			 */
			CreatureSay cs = new CreatureSay(killerPlayerInstance.getObjectId(), Say2.TELL, killerPlayerInstance.getName(), MessageTable.Messages[461].getExtra(1) + killedPlayerInstance.getName() + MessageTable.Messages[461].getExtra(2));
			
			for (L2PcInstance playerInstance : _teams[killerTeamId].getParticipatedPlayers().values())
			{
				if (playerInstance != null)
				{
					playerInstance.sendPacket(cs);
				}
			}
			
			// Notify to scripts.
			EventDispatcher.getInstance().notifyEventAsync(new OnTvTEventKill(killerPlayerInstance, killedPlayerInstance, killerTeam));
		}
	}
	
	/**
	 * Called on Appearing packet received (player finished teleporting)
	 * @param playerInstance
	 */
	public static void onTeleported(L2PcInstance playerInstance)
	{
		if (!isMeeting()||!isStarted() || (playerInstance == null) || !isPlayerParticipant(playerInstance.getObjectId()))
		{
			return;
		}
		
		if (playerInstance.isMageClass())
		{
			if ((Config.TVT_EVENT_MAGE_BUFFS != null) && !Config.TVT_EVENT_MAGE_BUFFS.isEmpty())
			{
				for (Entry<Integer, Integer> e : Config.TVT_EVENT_MAGE_BUFFS.entrySet())
				{
					Skill skill = SkillData.getInstance().getSkill(e.getKey(), e.getValue());
					if (skill != null)
					{
						skill.applyEffects(playerInstance, playerInstance);
					}
				}
			}
		}
		else
		{
			if ((Config.TVT_EVENT_FIGHTER_BUFFS != null) && !Config.TVT_EVENT_FIGHTER_BUFFS.isEmpty())
			{
				for (Entry<Integer, Integer> e : Config.TVT_EVENT_FIGHTER_BUFFS.entrySet())
				{
					Skill skill = SkillData.getInstance().getSkill(e.getKey(), e.getValue());
					if (skill != null)
					{
						skill.applyEffects(playerInstance, playerInstance);
					}
				}
			}
		}
	}
	
	/**
	 * @param source
	 * @param target
	 * @param skill
	 * @return true if player valid for skill
	 */
	public static final boolean checkForTvTSkill(L2PcInstance source, L2PcInstance target, Skill skill)
	{
		if (!isStarted())
		{
			return true;
		}
		// TvT is started
		final int sourcePlayerId = source.getObjectId();
		final int targetPlayerId = target.getObjectId();
		final boolean isSourceParticipant = isPlayerParticipant(sourcePlayerId);
		final boolean isTargetParticipant = isPlayerParticipant(targetPlayerId);
		
		// both players not participating
		if (!isSourceParticipant && !isTargetParticipant)
		{
			return true;
		}
		// one player not participating
		if (!(isSourceParticipant && isTargetParticipant))
		{
			return false;
		}
		// players in the different teams ?
		if (getParticipantTeamId(sourcePlayerId) != getParticipantTeamId(targetPlayerId))
		{
			if (!skill.isBad())
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Sets the TvTEvent state<br>
	 * <br>
	 * @param state as EventState<br>
	 */
	private static void setState(EventState state)
	{
		synchronized (_state)
		{
			_state = state;
		}
	}
	
	/**
	 * Is TvTEvent inactive?<br>
	 * <br>
	 * @return boolean: true if event is inactive(waiting for next event cycle), otherwise false<br>
	 */
	public static boolean isInactive()
	{
		boolean isInactive;
		
		synchronized (_state)
		{
			isInactive = _state == EventState.INACTIVE;
		}
		
		return isInactive;
	}
	
	/**
	 * Is TvTEvent in inactivating?<br>
	 * <br>
	 * @return boolean: true if event is in inactivating progress, otherwise false<br>
	 */
	public static boolean isInactivating()
	{
		boolean isInactivating;
		
		synchronized (_state)
		{
			isInactivating = _state == EventState.INACTIVATING;
		}
		
		return isInactivating;
	}
	
	/**
	 * Is TvTEvent in participation?<br>
	 * <br>
	 * @return boolean: true if event is in participation progress, otherwise false<br>
	 */
	public static boolean isParticipating()
	{
		boolean isParticipating;
		
		synchronized (_state)
		{
			isParticipating = _state == EventState.PARTICIPATING;
		}
		
		return isParticipating;
	}
	
	/**
	 * Is TvTEvent starting?<br>
	 * <br>
	 * @return boolean: true if event is starting up(setting up fighting spot, teleport players etc.), otherwise false<br>
	 */
	public static boolean isStarting()
	{
		boolean isStarting;
		
		synchronized (_state)
		{
			isStarting = _state == EventState.STARTING;
		}
		
		return isStarting;
	}
	
	/**
	 * Is TvTEvent started?<br>
	 * <br>
	 * @return boolean: true if event is started, otherwise false<br>
	 */
	public static boolean isStarted()
	{
		boolean isStarted;
		
		synchronized (_state)
		{
			isStarted = _state == EventState.STARTED;
		}
		
		return isStarted;
	}
	
	/**
	 * Is TvTEvent Meeting?<br>
	 * <br>
	 * @return boolean: true if event is started, otherwise false<br>
	 */
	public static boolean isMeeting()
	{
		boolean isMeeting;
		
		synchronized (_state)
		{
			isMeeting = _state == EventState.MEETING;
		}
		
		return isMeeting;
	}
	
	/**
	 * Is TvTEvent rewarding?<br>
	 * <br>
	 * @return boolean: true if event is currently rewarding, otherwise false<br>
	 */
	public static boolean isRewarding()
	{
		boolean isRewarding;
		
		synchronized (_state)
		{
			isRewarding = _state == EventState.REWARDING;
		}
		
		return isRewarding;
	}
	
	/**
	 * Returns the team id of a player, if player is not participant it returns -1
	 * @param playerObjectId
	 * @return byte: team name of the given playerName, if not in event -1
	 */
	public static byte getParticipantTeamId(int playerObjectId)
	{
		return (byte) (_teams[0].containsPlayer(playerObjectId) ? 0 : (_teams[1].containsPlayer(playerObjectId) ? 1 : -1));
	}
	
	/**
	 * Returns the team of a player, if player is not participant it returns null
	 * @param playerObjectId
	 * @return TvTEventTeam: team of the given playerObjectId, if not in event null
	 */
	public static TvTEventTeam getParticipantTeam(int playerObjectId)
	{
		return (_teams[0].containsPlayer(playerObjectId) ? _teams[0] : (_teams[1].containsPlayer(playerObjectId) ? _teams[1] : null));
	}
	
	/**
	 * Returns the enemy team of a player, if player is not participant it returns null
	 * @param playerObjectId
	 * @return TvTEventTeam: enemy team of the given playerObjectId, if not in event null
	 */
	public static TvTEventTeam getParticipantEnemyTeam(int playerObjectId)
	{
		return (_teams[0].containsPlayer(playerObjectId) ? _teams[1] : (_teams[1].containsPlayer(playerObjectId) ? _teams[0] : null));
	}
	
	/**
	 * Returns the team coordinates in which the player is in, if player is not in a team return null
	 * @param playerObjectId
	 * @return int[]: coordinates of teams, 2 elements, index 0 for team 1 and index 1 for team 2
	 */
	public static int[] getParticipantTeamCoordinates(int playerObjectId)
	{
		return _teams[0].containsPlayer(playerObjectId) ? _teams[0].getCoordinates() : (_teams[1].containsPlayer(playerObjectId) ? _teams[1].getCoordinates() : null);
	}
	
	/**
	 * Is given player participant of the event?
	 * @param playerObjectId
	 * @return boolean: true if player is participant, ohterwise false
	 */
	public static boolean isPlayerParticipant(int playerObjectId)
	{
		if (!isParticipating() && !isStarting() && !isMeeting() & !isStarted())
		{
			return false;
		}
		
		return _teams[0].containsPlayer(playerObjectId) || _teams[1].containsPlayer(playerObjectId);
	}
	
	/**
	 * Returns participated player count<br>
	 * <br>
	 * @return int: amount of players registered in the event<br>
	 */
	public static int getParticipatedPlayersCount()
	{
		if (!isParticipating() && !isStarting() && !isMeeting() && !isStarted())
		{
			return 0;
		}
		
		return _teams[0].getParticipatedPlayerCount() + _teams[1].getParticipatedPlayerCount();
	}
	
	/**
	 * Returns teams names<br>
	 * <br>
	 * @return String[]: names of teams, 2 elements, index 0 for team 1 and index 1 for team 2<br>
	 */
	public static String[] getTeamNames()
	{
		return new String[]
		{
			_teams[0].getName(),
			_teams[1].getName()
		};
	}
	
	/**
	 * Returns player count of both teams<br>
	 * <br>
	 * @return int[]: player count of teams, 2 elements, index 0 for team 1 and index 1 for team 2<br>
	 */
	public static int[] getTeamsPlayerCounts()
	{
		return new int[]
		{
			_teams[0].getParticipatedPlayerCount(),
			_teams[1].getParticipatedPlayerCount()
		};
	}
	
	/**
	 * Returns points count of both teams
	 * @return int[]: points of teams, 2 elements, index 0 for team 1 and index 1 for team 2<br>
	 */
	public static int[] getTeamsPoints()
	{
		return new int[]
		{
			_teams[0].getPoints(),
			_teams[1].getPoints()
		};
	}
	
	public static int getTvTEventInstantWorldId()
	{
		return _TvTEventInstantWorldId;
	}

	/**
	 * 所属するチームにCreatureSayを流す
	 * @param activeChar
	 * @param cs
	 */
	public static void doTeamOnlyChat(L2PcInstance activeChar, CreatureSay cs) {
		if (activeChar == null) return;
		int teamId = getParticipantTeamId(activeChar.getObjectId());
		if (teamId == -1) return;
		for (L2PcInstance player : _teams[teamId].getParticipatedPlayers().values())
		{
			// Check for nullpointer
			if (player == null)
			{
				continue;
			}
			player.sendPacket(cs);
		}
	}

}
