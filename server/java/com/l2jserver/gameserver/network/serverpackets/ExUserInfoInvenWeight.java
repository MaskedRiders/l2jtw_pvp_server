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

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class ExUserInfoInvenWeight extends L2GameServerPacket
{
	private L2PcInstance _activeChar;
	private final int _objId;
	private final int _nowload;
	private final int _maxload;
	private long _item_weight[] = new long[3]; // 603 : GS-comment-051
	
	public ExUserInfoInvenWeight(L2PcInstance activeChar)
	{
		_activeChar = activeChar;
		_objId = _activeChar.getObjectId();
		_nowload = _activeChar.getCurrentLoad();
		_maxload = _activeChar.getMaxLoad();
	}
	
	@Override
	protected final void writeImpl()
	{
		// 603 : GS-comment-051 start
		_item_weight[0] = System.currentTimeMillis();
		_item_weight[1] = (long) _nowload;
		_item_weight[2] = (long) _maxload;
		
		if ((System.currentTimeMillis() - _activeChar.getItemWeight(0)) > 60000)
		{
			for (int i = 0; i < 3; i++)
			{
				_activeChar.setItemWeight(i, _item_weight[i]);
			}
		}
		else
		{
			int _needUpdate = 0;
			for (int i = 1; i < 3; i++)
			{
				if (_item_weight[i] != _activeChar.getItemWeight(i))
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
				for (int i = 0; i < 3; i++)
				{
					_activeChar.setItemWeight(i, _item_weight[i]);
				}
			}
		}
		// 603 : GS-comment-051 end
		writeC(0xFE);
		writeH(0x166);
		writeD(_objId);
		writeD(_nowload);
		writeD(_maxload);
	}
}
