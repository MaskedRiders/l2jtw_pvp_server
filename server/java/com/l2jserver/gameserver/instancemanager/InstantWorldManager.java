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
package com.l2jserver.gameserver.instancemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javolution.util.FastMap;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.engines.DocumentParser;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.InstantWorld;
import com.l2jserver.gameserver.model.instantzone.InstantZone;

/**
 * @author evill33t, GodKratos
 */
public final class InstantWorldManager extends DocumentParser
{
	private static final Map<Integer, InstantWorld> _instantWorldList = new FastMap<>();
	private final Map<Integer, InstantZone> _instantZones = new FastMap<>();
	private int _dynamicId = 300000;
	// InstanceId Names
	private static final Map<Integer, String> _instantWorldIdNames = new HashMap<>();
	private final Map<Integer, Map<Integer, Long>> _playerInstantWorldTimes = new FastMap<>();
	// SQL Queries
	private static final String ADD_INSTANCE_TIME = "INSERT INTO character_instance_time (charId,instanceId,time) values (?,?,?) ON DUPLICATE KEY UPDATE time=?";
	private static final String RESTORE_INSTANCE_TIMES = "SELECT instanceId,time FROM character_instance_time WHERE charId=?";
	private static final String DELETE_INSTANCE_TIME = "DELETE FROM character_instance_time WHERE charId=? AND instanceId=?";
	
	protected InstantWorldManager()
	{
		// Creates the multiverse.
		_instantWorldList.put(-1, new InstantWorld(-1, "multiverse"));
		_log.info(getClass().getSimpleName() + ": Multiverse Instance created.");
		// Creates the universe.
		_instantWorldList.put(0, new InstantWorld(0, "universe"));
		_log.info(getClass().getSimpleName() + ": Universe Instance created.");
		load();
	}
	
	@Override
	public void load()
	{
		_instantWorldIdNames.clear();
		parseDatapackFile("data/instancenames.xml");
		_log.info(getClass().getSimpleName() + ": Loaded " + _instantWorldIdNames.size() + " instance names.");
	}
	
	/**
	 * @param playerObjId
	 * @param id
	 * @return
	 */
	public long getPlayerInstantWorldTime(int playerObjId, int id)
	{
		if (!_playerInstantWorldTimes.containsKey(playerObjId))
		{
			restorePlayerInstantWorldTimes(playerObjId);
		}
		if (_playerInstantWorldTimes.get(playerObjId).containsKey(id))
		{
			return _playerInstantWorldTimes.get(playerObjId).get(id);
		}
		return -1;
	}
	
	/**
	 * @param playerObjId
	 * @return
	 */
	public Map<Integer, Long> getAllPlayerInstantWorldTimes(int playerObjId)
	{
		if (!_playerInstantWorldTimes.containsKey(playerObjId))
		{
			restorePlayerInstantWorldTimes(playerObjId);
		}
		return _playerInstantWorldTimes.get(playerObjId);
	}
	
	/**
	 * @param playerObjId
	 * @param id
	 * @param time
	 */
	public void getPlayerInstantWorldTime(int playerObjId, int id, long time)
	{
		if (!_playerInstantWorldTimes.containsKey(playerObjId))
		{
			restorePlayerInstantWorldTimes(playerObjId);
		}
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement(ADD_INSTANCE_TIME))
		{
			ps.setInt(1, playerObjId);
			ps.setInt(2, id);
			ps.setLong(3, time);
			ps.setLong(4, time);
			ps.execute();
			_playerInstantWorldTimes.get(playerObjId).put(id, time);
		}
		catch (Exception e)
		{
			_log.warning(getClass().getSimpleName() + ": Could not insert character instance time data: " + e.getMessage());
		}
	}
	
	/**
	 * @param playerObjId
	 * @param id
	 */
	public void deletePlayerInstantWorldTime(int playerObjId, int id)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement(DELETE_INSTANCE_TIME))
		{
			ps.setInt(1, playerObjId);
			ps.setInt(2, id);
			ps.execute();
			_playerInstantWorldTimes.get(playerObjId).remove(id);
		}
		catch (Exception e)
		{
			_log.warning(getClass().getSimpleName() + ": Could not delete character instance time data: " + e.getMessage());
		}
	}
	
	/**
	 * @param playerObjId
	 */
	public void restorePlayerInstantWorldTimes(int playerObjId)
	{
		if (_playerInstantWorldTimes.containsKey(playerObjId))
		{
			return; // already restored
		}
		_playerInstantWorldTimes.put(playerObjId, new FastMap<Integer, Long>());
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement(RESTORE_INSTANCE_TIMES))
		{
			ps.setInt(1, playerObjId);
			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					int id = rs.getInt("instanceId");
					long time = rs.getLong("time");
					if (time < System.currentTimeMillis())
					{
						deletePlayerInstantWorldTime(playerObjId, id);
					}
					else
					{
						_playerInstantWorldTimes.get(playerObjId).put(id, time);
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.warning(getClass().getSimpleName() + ": Could not delete character instance time data: " + e.getMessage());
		}
	}
	
	/**
	 * @param id
	 * @return
	 */
	public String getInstantWorldIdName(int id)
	{
		if (_instantWorldIdNames.containsKey(id))
		{
			return _instantWorldIdNames.get(id);
		}
		return ("UnknownInstance");
	}
	
	@Override
	protected void parseDocument()
	{
		for (Node n = getCurrentDocument().getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("list".equals(n.getNodeName()))
			{
				NamedNodeMap attrs;
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					if ("instance".equals(d.getNodeName()))
					{
						attrs = d.getAttributes();
						_instantWorldIdNames.put(parseInteger(attrs, "id"), attrs.getNamedItem("name").getNodeValue());
					}
				}
			}
		}
	}

	/**
	 * @param world
	 */
	public void addWorld(InstantZone world)
	{
		_instantZones.put(world.getInstanceId(), world);
	}
	
	/**
	 * @param instanceId
	 * @return
	 */
	public InstantZone getWorld(int instanceId)
	{
		return _instantZones.get(instanceId);
	}
	
	/**
	 * Check if the player have a World Instance where it's allowed to enter.
	 * @param player the player to check
	 * @return the instance world
	 */
	public InstantZone getPlayerInstantWorld(L2PcInstance player)
	{
		for (InstantZone temp : _instantZones.values())
		{
			if ((temp != null) && (temp.isAllowed(player.getObjectId())))
			{
				return temp;
			}
		}
		return null;
	}
	
	/**
	 * @param instantWorldid
	 */
	public void destroyInstantWorld(int instantWorldid)
	{
		if (instantWorldid <= 0)
		{
			return;
		}
		final InstantWorld temp = _instantWorldList.get(instantWorldid);
		if (temp != null)
		{
			temp.removeNpcs();
			temp.removePlayers();
			temp.removeDoors();
			temp.cancelTimer();
			_instantWorldList.remove(instantWorldid);
			if (_instantZones.containsKey(instantWorldid))
			{
				_instantZones.remove(instantWorldid);
			}
		}
	}
	
	/**
	 * @param instanceid
	 * @return
	 */
	public InstantWorld getInstantWorld(int instanceid)
	{
		return _instantWorldList.get(instanceid);
	}
	
	/**
	 * @return
	 */
	public Map<Integer, InstantWorld> getInstantWorlds()
	{
		return _instantWorldList;
	}
	
	/**
	 * @param objectId
	 * @return
	 */
	public int getPlayerInstantWorld(int objectId)
	{
		for (InstantWorld temp : _instantWorldList.values())
		{
			if (temp == null)
			{
				continue;
			}
			// check if the player is in any active instance
			if (temp.containsPlayer(objectId))
			{
				return temp.getId();
			}
		}
		// 0 is default instance aka the world
		return 0;
	}
	
	/**
	 * @param id
	 * @return
	 */
	public boolean createInstantWorld(int id)
	{
		if (getInstantWorld(id) != null)
		{
			return false;
		}
		
		final InstantWorld InstantWorld = new InstantWorld(id);
		_instantWorldList.put(id, InstantWorld);
		return true;
	}
	
	/**
	 * @param id
	 * @param template
	 * @return
	 */
	public boolean createInstantWorldFromTemplate(int id, String template)
	{
		if (getInstantWorld(id) != null)
		{
			return false;
		}
		
		final InstantWorld instance = new InstantWorld(id);
		_instantWorldList.put(id, instance);
		instance.loadInstanceTemplate(template);
		return true;
	}
	
	/**
	 * Create a new instance with a dynamic instance id based on a template (or null)
	 * @param template xml file
	 * @return
	 */
	public int createInstantWorld(String template)
	{
		while (getInstantWorld(_dynamicId) != null)
		{
			_dynamicId++;
			if (_dynamicId == Integer.MAX_VALUE)
			{
				_log.warning(getClass().getSimpleName() + ": More then " + (Integer.MAX_VALUE - 300000) + " instances created");
				_dynamicId = 300000;
			}
		}
		final InstantWorld instance = new InstantWorld(_dynamicId);
		_instantWorldList.put(_dynamicId, instance);
		if (template != null)
		{
			instance.loadInstanceTemplate(template);
		}
		return _dynamicId;
	}
	
	/**
	 * Gets the single instance of {@code InstantWorldManager}.
	 * @return single instance of {@code InstantWorldManager}
	 */
	public static final InstantWorldManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final InstantWorldManager _instance = new InstantWorldManager();
	}
}
