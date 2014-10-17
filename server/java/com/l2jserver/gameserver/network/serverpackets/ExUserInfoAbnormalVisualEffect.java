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

public class ExUserInfoAbnormalVisualEffect extends L2GameServerPacket
{
	private L2PcInstance _activeChar;
	private long _user_abnormal[] = new long[51]; // 603 : GS-comment-051
	
	public ExUserInfoAbnormalVisualEffect(L2PcInstance character)
	{
		_activeChar = character;
	}
	
	@Override
	protected final void writeImpl()
	{
		// 603 : GS-comment-051 start
		java.util.List<Integer> _el = _activeChar.getEffectIdList();
		if (_activeChar.isInvisible() && !_el.contains(21))
		{
			_el.add(21);
		}
		
		_user_abnormal[0] = System.currentTimeMillis();
		_user_abnormal[1] = (long) _activeChar.getTransformationDisplayId();
		_user_abnormal[2] = (long) _el.size();
		
		int _cyc = 3;
		int j = _cyc;
		if (_user_abnormal[2] < 49)
		{
			for (int abnormalId : _el)
			{
				_user_abnormal[j] = (long) abnormalId;
				j++;
			}
		}
		
		int old_abnormal_size = (int) _activeChar.getUserAbnormal(2);
		int new_abnormal_size = _el.size();
		int k = 0;
		
		if (new_abnormal_size > old_abnormal_size)
		{
			k = j;
		}
		else
		{
			k = old_abnormal_size + _cyc;
		}
		
		if (_user_abnormal[2] >= 49)
		{
			// not do anything
		}
		else if ((System.currentTimeMillis() - _activeChar.getUserAbnormal(0)) > 60000)
		{
			for (int i = 0; i < k; i++)
			{
				_activeChar.setUserAbnormal(i, _user_abnormal[i]);
			}
		}
		else
		{
			int _needUpdate = 0;
			for (int i = 1; i < k; i++)
			{
				if (_user_abnormal[i] != _activeChar.getUserAbnormal(i))
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
					_activeChar.setUserAbnormal(i, _user_abnormal[i]);
				}
			}
		}
		// 603 : GS-comment-051 end
		writeC(0xFE);
		writeH(0x158);
		writeD(_activeChar.getObjectId());
		writeD(_activeChar.getTransformationDisplayId());
		java.util.List<Integer> el = _activeChar.getEffectIdList();
		if (_activeChar.isInvisible() && !el.contains(21))
		{
			el.add(21);
		}
		writeD(el.size());
		for (int i : el)
		{
			writeH(i);
		}
	}
}
