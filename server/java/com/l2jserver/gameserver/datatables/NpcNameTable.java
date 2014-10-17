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
package com.l2jserver.gameserver.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

import javolution.util.FastList;

import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.datatables.NpcData;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;

/**
 * 
 */
public class NpcNameTable
{
	private static final Logger _log = Logger.getLogger(NpcNameTable.class.getName());
	private static NpcNameTable _instance;
	private FastList<Nametable> tables;// = new FastList<Nametable>(); // l2jtw
	
	private class Nametable
	{
		public int id;
		public String name;
	}
	
	public static NpcNameTable getInstance()
	{
		if (_instance == null)
			_instance = new NpcNameTable();
		
		return _instance;
	}
	
	private NpcNameTable()
	{
		reload();
	}
	
	@SuppressWarnings("synthetic-access")
	public void reload()// l2jtw
	{
		tables = new FastList<>(); // l2jtw
		int count = 0;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT " + L2DatabaseFactory.getInstance().safetyString(new String[] {"npc_id","name"}) + " FROM zzz_npc_name");
			ResultSet npc_name = statement.executeQuery())
		{
			Nametable data;
			while(npc_name.next())
			{
				data = new Nametable();
				data.id = npc_name.getInt("npc_id");
				data.name = npc_name.getString("name");
				
				//if (tables == null)// l2jtw
					//new FastList<Nametable>(); // l2jtw
				
				tables.add(data);
				count++;
			}
		}
		catch (Exception e)
		{
			_log.warning("Npc Name Table: FAILED");
			_log.warning("" + e);
		}
		
		L2NpcTemplate result = null;
		for(Nametable n : tables)
		{
			result = NpcData.getInstance().getTemplate(n.id);
			if(result != null)
				NpcData.getInstance().getTemplate(n.id).setName(n.name);
		}
		_log.info("Npc Name Table: " + count + " Name.");
		tables = null; // l2jtw
	}
}