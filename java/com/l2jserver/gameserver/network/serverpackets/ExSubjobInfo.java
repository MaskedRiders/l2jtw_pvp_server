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

import com.l2jserver.gameserver.model.base.ClassLevel;
import com.l2jserver.gameserver.model.base.SubClass;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class ExSubjobInfo extends L2GameServerPacket
{
	private final L2PcInstance _player;
	private long _sub_info[] = new long[19]; // 603 : GS-comment-051
	
	public ExSubjobInfo(L2PcInstance _cha)
	{
		_player = _cha;
	}
	
	@Override
	protected final void writeImpl()
	{
		// 603 : GS-comment-051 start
		_sub_info[0] = System.currentTimeMillis();
		_sub_info[1] = (long) _player.getClassId().getId();
		_sub_info[2] = (long) _player.getRace().ordinal();
		_sub_info[3] = (long) _player.getSubClasses().size();
		_sub_info[4] = (long) _player.getClassIndex();
		_sub_info[5] = (long) _player.getBaseClass();
		_sub_info[6] = (long) _player.getStat().getBaseLevel();
		int j = 7;
		for (SubClass sc : _player.getSubClasses().values())
		{
			_sub_info[j] = (long) sc.getClassIndex();
			j++;
			_sub_info[j] = (long) sc.getClassId();
			j++;
			_sub_info[j] = (long) sc.getLevel();
			j++;
			_sub_info[j] = (long) (sc.getClassDefinition().isOfLevel(ClassLevel.Awaken) ? 1 : 2);
			j++;
		}
		
		if ((System.currentTimeMillis() - _player.getSubjobInfo(0)) > 60000)
		{
			for (int i = 0; i < 19; i++)
			{
				_player.setSubjobInfo(i, _sub_info[i]);
			}
		}
		else
		{
			int _needUpdate = 0;
			for (int i = 1; i < 19; i++)
			{
				if (_sub_info[i] != _player.getSubjobInfo(i))
				{
					_needUpdate = 1;
				}
			}
			if (_needUpdate == 0)
			{
				return;
			}
			else
			{
				for (int i = 0; i < 19; i++)
				{
					_player.setSubjobInfo(i, _sub_info[i]);
				}
			}
		}
		// 603 : GS-comment-051 end
		writeC(0xFE);
		writeH(0xEA);
		writeC(0x00);
		writeD(_player.getClassId().getId());
		writeD(_player.getRace().ordinal());
		writeD(_player.getSubClasses().size() + 1);
		
		writeD(_player.getClassIndex());
		writeD(_player.getBaseClass());
		writeD(_player.getStat().getBaseLevel());
		writeC(0x00); // 0 main, 1 dual, 2 sub
		
		for (SubClass sc : _player.getSubClasses().values())
		{
			writeD(sc.getClassIndex());
			writeD(sc.getClassId());
			writeD(sc.getLevel());
			writeC(sc.getClassDefinition().isOfLevel(ClassLevel.Awaken) ? 1 : 2); // 0 main, 1 dual, 2 sub
		}
	}
}