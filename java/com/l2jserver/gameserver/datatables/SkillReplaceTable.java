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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javolution.util.FastMap;

import com.l2jserver.gameserver.engines.DocumentParser;

/**
 * @author a
 *
 */
public class SkillReplaceTable extends DocumentParser
{
	private final Map<Integer, List<Integer>> _skillReplaceData = new FastMap<>();

	public SkillReplaceTable()
	{
		load();
	}

	public static SkillReplaceTable getInstance()
	{
		return SingletonHolder._instance;
	}

	private static class SingletonHolder
	{
		protected static final SkillReplaceTable _instance = new SkillReplaceTable();
	}

	/* (non-Javadoc)
	 * @see com.l2jserver.gameserver.engines.DocumentParser#load()
	 */
	@Override
	public void load()
	{
		// TODO Auto-generated method stub
		_skillReplaceData.clear();
		parseDatapackFile("data/stats/skillReplace.xml");
	}

	/* (non-Javadoc)
	 * @see com.l2jserver.gameserver.engines.DocumentParser#parseDocument()
	 */
	@Override
	protected void parseDocument()
	{
		// TODO Auto-generated method stub
		Node list = getCurrentDocument().getFirstChild();
		for (Node node = list.getFirstChild(); node != null; node = node.getNextSibling()){
			List<Integer> l = new java.util.ArrayList<>();
			for (Node rnode = node.getFirstChild(); rnode != null; rnode = rnode.getNextSibling()){
				 NamedNodeMap a = rnode.getAttributes();
				 if (a == null) continue;
				l.add(parseInteger(a, "id"));
			}
			 NamedNodeMap a = node.getAttributes();
			 if (a == null) continue;
			_skillReplaceData.put(parseInteger(a, "id"), l);
		}
	}
	
	public List<Integer> getReplaceSkills(int skillid){
		List<Integer> l = _skillReplaceData.get(skillid);
		return (l == null ? new java.util.ArrayList<Integer>(): l);
	}
	
}
