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

import com.l2jserver.Config;
import com.l2jserver.gameserver.datatables.SkillReplaceTable;
import com.l2jserver.gameserver.datatables.SkillTreesData;
import com.l2jserver.gameserver.model.L2SkillLearn;
import com.l2jserver.gameserver.model.base.AcquireSkillType;
import com.l2jserver.gameserver.model.holders.ItemHolder;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.model.skills.CommonSkill; // l2jtw add

/**
 * Acquire Skill Info server packet implementation.
 * @author Zoey76
 */
public class ExAcquireSkillInfo extends L2GameServerPacket
{
	private final AcquireSkillType _type;
	private final int _id;
	private final int _level;
	private final int _spCost;
	private final List<Req> _reqs;
	
	/**
	 * Private class containing learning skill requisites.
	 */
	private static class Req
	{
		public int itemId;
		public long count;
		
		/**
		 * @param pItemId the item Id.
		 * @param itemCount the item count.
		 */
		public Req(int pItemId, long itemCount)
		{
			itemId = pItemId;
			count = itemCount;
		}
	}
	
	/**
	 * Constructor for the acquire skill info object.
	 * @param skillType the skill learning type.
	 * @param skillLearn the skill learn.
	 */
	public ExAcquireSkillInfo(AcquireSkillType skillType, L2SkillLearn skillLearn)
	{
		_id = skillLearn.getSkillId();
		_level = skillLearn.getSkillLevel();
		_spCost = skillLearn.getLevelUpSp();
		_type = skillType;
		_reqs = new ArrayList<>();
		if ((skillType != AcquireSkillType.PLEDGE) || Config.LIFE_CRYSTAL_NEEDED)
		{
			for (ItemHolder item : skillLearn.getRequiredItems())
			{
				if (!Config.DIVINE_SP_BOOK_NEEDED && (_id == CommonSkill.DIVINE_INSPIRATION.getId()))
				{
					continue;
				}
				_reqs.add(new Req(item.getId(), item.getCount()));
			}
		}
	}
	
	/**
	 * Special constructor for Alternate Skill Learning system.<br>
	 * Sets a custom amount of SP.
	 * @param skillType the skill learning type.
	 * @param skillLearn the skill learn.
	 * @param sp the custom SP amount.
	 */
	public ExAcquireSkillInfo(AcquireSkillType skillType, L2SkillLearn skillLearn, int sp)
	{
		_id = skillLearn.getSkillId();
		_level = skillLearn.getSkillLevel();
		_spCost = sp;
		_type = skillType;
		_reqs = new ArrayList<>();
		for (ItemHolder item : skillLearn.getRequiredItems())
		{
			_reqs.add(new Req(item.getId(), item.getCount()));
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		L2SkillLearn lskill = SkillTreesData.getInstance().getSkillLearn(_type, _id, _level, getClient().getActiveChar());
		writeC(0xfe);
		writeH(0xfc); // 603
		writeD(_id);
		writeD(_level);
		writeQ(_spCost); // 603
		writeH(lskill == null ? _level + 1 : lskill.getGetLevel()); //c4
		writeH(0x00); // 603
		
		writeD(_reqs.size());
		for (Req temp : _reqs)
		{
			writeD(temp.itemId);
			writeQ(temp.count);
		}
		List<Integer> rskill = SkillReplaceTable.getInstance().getReplaceSkills(_id);
		writeD(rskill.size());
		for(int id : rskill)
		{
			writeD(id);
			writeD(1);//skill level
		}
	}
}