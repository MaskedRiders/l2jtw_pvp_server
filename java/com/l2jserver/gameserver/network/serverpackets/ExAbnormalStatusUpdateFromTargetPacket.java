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

import javolution.util.FastList;

import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.skills.BuffInfo;

public class ExAbnormalStatusUpdateFromTargetPacket extends L2GameServerPacket
{
	private final int _objectId;
	private List<BuffInfo> _effects = new ArrayList<>();
	
	public ExAbnormalStatusUpdateFromTargetPacket(int ObjectId)
	{
		_objectId = ObjectId;
		if (_objectId == 0)
		{
			return;
		}
		
		L2Character target = null;
		target = (L2Character) L2World.getInstance().findObject(_objectId);
		if (target == null)
		{
			return;
		}
		
		_effects = target.getEffectList().getEffects();
	}
	
	@Override
	protected final void writeImpl()
	{
		if (_objectId == 0)
		{
			return;
		}
		
		writeC(0xfe);
		writeH(0xe6); // 603
		writeD(_objectId);
		writeH(_effects.size());
		for (BuffInfo info : _effects)
		{
			if ((info != null) && info.isInUse())
			{
				writeD(info.getSkill().getDisplayId());
				writeH(info.getSkill().getDisplayLevel());
				writeH(0); // 603-index?
				writeH(info.getTime()); // 603
				writeD(0); // GS-comment-037
			}
		}
	}
}
