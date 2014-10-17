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
package com.l2jserver.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import com.l2jserver.gameserver.model.base.AcquireSkillType;

/**
 * Acquire Skill List server packet implementation.
 */
public final class AcquireSkillList extends L2GameServerPacket
{
	private final List<Skill> _skills;
	private final AcquireSkillType _skillType;
	
	/**
	 * Private class containing learning skill information.
	 */
	private static class Skill
	{
		public int id;
		public int nextLevel;
		public int maxLevel;
		public int spCost;
		public int requirements;
		
		public Skill(int pId, int pNextLevel, int pMaxLevel, int pSpCost, int pRequirements)
		{
			id = pId;
			nextLevel = pNextLevel;
			maxLevel = pMaxLevel;
			spCost = pSpCost;
			requirements = pRequirements;
		}
	}
	
	public AcquireSkillList(AcquireSkillType type)
	{
		_skillType = type;
		_skills = new ArrayList<>();
	}
	
	public void addSkill(int id, int nextLevel, int maxLevel, int spCost, int requirements)
	{
		_skills.add(new Skill(id, nextLevel, maxLevel, spCost, requirements));
	}
	
	@Override
	protected void writeImpl()
	{
		if (_skills.isEmpty())
		{
			return;
		}
		
		writeC(0xFE); // 603
		writeH(0xFA); // 603
		writeH(_skillType.ordinal()); // 603
		writeH(_skills.size()); // 603
		
		for (Skill temp : _skills)
		{
			writeD(temp.id);
			writeH(temp.nextLevel); // 603
			writeH(temp.maxLevel); // 603
			writeC(temp.requirements); // 603
			writeQ(temp.spCost); // 603
			writeC(1); // 603
			if (_skillType == AcquireSkillType.SUBPLEDGE)
			{
				writeH(0); // TODO: ?
			}
		}
	}
}