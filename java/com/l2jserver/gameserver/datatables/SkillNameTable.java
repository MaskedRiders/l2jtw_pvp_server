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
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.skills.Skill;

/**
 * 
 */
public class SkillNameTable
{
	private static final Logger _log = Logger.getLogger(SkillNameTable.class.getName());
	private static SkillNameTable _instance;
	private FastList<Nametable> tables = new FastList<>();
	
	private class Nametable
	{
		public int id;
		public int level;
		public String name;
	}
	
	public static SkillNameTable getInstance()
	{
		if (_instance == null)
			_instance = new SkillNameTable();
		
		return _instance;
	}
	
	private SkillNameTable()
	{
		reload();
	}
	
	@SuppressWarnings("synthetic-access")
	private void reload()
	{
		int count = 0;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT " + L2DatabaseFactory.getInstance().safetyString(new String[] {"skill_id","level","name"}) + " FROM zzz_skill_name");
			ResultSet skill_name = statement.executeQuery())
		{
			Nametable data;
			while(skill_name.next())
			{
				data = new Nametable();
				data.id = skill_name.getInt("skill_id");
				data.level = skill_name.getInt("level");
				data.name = skill_name.getString("name");
				
				if (tables == null)
					new FastList<Nametable>();
				
				tables.add(data);
				count++;
			}
		}
		catch (Exception e)
		{
			_log.warning("Skill Name Table: FAILED");
			_log.warning("" + e);
		}
		
		Skill result = null;
		for(Nametable n : tables)
		{
			result = SkillData.getInstance()._skills.get(SkillData.getSkillHashCode(n.id, n.level));
			if(result != null)
				SkillData.getInstance()._skills.get(SkillData.getSkillHashCode(n.id, n.level)).setName(n.name);
		}
		_log.info("Skill Name Table: " + count + " Name.");
	}
}