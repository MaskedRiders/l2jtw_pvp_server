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

public class ExUserInfoCubic extends L2GameServerPacket
{
	private L2PcInstance _activeChar;
	private long _user_cubic[] = new long[19]; // 603 : GS-comment-051
	
	public ExUserInfoCubic(L2PcInstance character)
	{
		_activeChar = character;
	}
	
	@Override
	protected final void writeImpl()
	{
		// 603 : GS-comment-051 start
		_user_cubic[0] = System.currentTimeMillis();
		_user_cubic[1] = (long) _activeChar.getAgathionId();
		_user_cubic[2] = (long) _activeChar.getCubics().size();
		
		int _cyc = 3;
		int j = _cyc;
		if (_user_cubic[2] < 17)
		{
			for (int cubicId : _activeChar.getCubics().keySet())
			{
				_user_cubic[j] = (long) cubicId;
				j++;
			}
		}
		
		int old_cubic_size = (int) _activeChar.getUserCubic(2);
		int new_cubic_size = _activeChar.getCubics().size();
		int k = 0;
		
		if (new_cubic_size > old_cubic_size)
		{
			k = j;
		}
		else
		{
			k = old_cubic_size + _cyc;
		}
		
		if (_user_cubic[2] >= 17)
		{
			// not do anything
		}
		else if ((System.currentTimeMillis() - _activeChar.getUserCubic(0)) > 60000)
		{
			for (int i = 0; i < k; i++)
			{
				_activeChar.setUserCubic(i, _user_cubic[i]);
			}
		}
		else
		{
			int _needUpdate = 0;
			for (int i = 1; i < k; i++)
			{
				if (_user_cubic[i] != _activeChar.getUserCubic(i))
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
				for (int i = 0; i < k; i++)
				{
					_activeChar.setUserCubic(i, _user_cubic[i]);
				}
			}
		}
		// 603 : GS-comment-051 end
		writeC(0xFE);
		writeH(0x157);
		writeD(_activeChar.getObjectId());
		writeH(_activeChar.getCubics().size());
		for (int cubicId : _activeChar.getCubics().keySet())
		{
			writeH(cubicId);
		}
		writeD(_activeChar.getAgathionId());
	}
}
