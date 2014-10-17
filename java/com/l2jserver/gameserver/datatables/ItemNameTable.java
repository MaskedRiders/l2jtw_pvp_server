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
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.model.items.L2Item;

/**
 * 
 */
public class ItemNameTable
{
	private static final Logger _log = Logger.getLogger(ItemNameTable.class.getName());
	private static ItemNameTable _instance;
	private FastList<Nametable> tables;// = new FastList<Nametable>(); // l2jtw
	
	private class Nametable
	{
		public int id;
		public String name;
	}
	
	public static ItemNameTable getInstance()
	{
		if (_instance == null)
			_instance = new ItemNameTable();
		
		return _instance;
	}
	
	private ItemNameTable()
	{
		reload();
	}
	
	@SuppressWarnings("synthetic-access")
	public void reload()// l2jtw
	{
		tables = new FastList<>(); // l2jtw
		int count = 0;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT " + L2DatabaseFactory.getInstance().safetyString(new String[] {"item_id","name"}) + " FROM zzz_item_name");
			ResultSet item_name = statement.executeQuery())
		{
			Nametable data;
			while(item_name.next())
			{
				data = new Nametable();
				data.id = item_name.getInt("item_id");
				data.name = item_name.getString("name");
				
				//if (tables == null)// l2jtw
					//new FastList<Nametable>(); // l2jtw
				
				tables.add(data);
				count++;
			}
		}
		catch (Exception e)
		{
			_log.warning("Item Name Table: FAILED");
			_log.warning("" + e);
		}
		
		L2Item result = null;
		for(Nametable n : tables)
		{
			result = ItemTable.getInstance().getTemplate(n.id);
			if(result != null)
				ItemTable.getInstance().getTemplate(n.id).setName(n.name);
		}
		_log.info("Item Name Table: " + count + " Name.");
		tables = null; // l2jtw
	}
}