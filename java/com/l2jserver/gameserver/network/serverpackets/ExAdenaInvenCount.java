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

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;

public class ExAdenaInvenCount extends L2GameServerPacket
{
	private L2PcInstance _activeChar;
	private final List<L2ItemInstance> _items = new ArrayList<>();
	private final long _adenas;
	private final int _counts;
	private long _item_count[] = new long[3]; // 603 : GS-comment-051
	
	public ExAdenaInvenCount(L2PcInstance activeChar)
	{
		_activeChar = activeChar;
		
		for (L2ItemInstance item : _activeChar.getInventory().getItems())
		{
			if (!item.isQuestItem())
			{
				_items.add(item);
			}
		}
		_adenas = _activeChar.getAdena();
		_counts = _items.size();
	}
	
	@Override
	protected final void writeImpl()
	{
		// 603 : GS-comment-051 start
		_item_count[0] = System.currentTimeMillis();
		_item_count[1] = (long) _adenas;
		_item_count[2] = (long) _counts;
		
		if ((System.currentTimeMillis() - _activeChar.getItemCount(0)) > 60000)
		{
			for (int i = 0; i < 3; i++)
			{
				_activeChar.setItemCount(i, _item_count[i]);
			}
		}
		else
		{
			int _needUpdate = 0;
			for (int i = 1; i < 3; i++)
			{
				if (_item_count[i] != _activeChar.getItemCount(i))
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
					_activeChar.setItemCount(i, _item_count[i]);
				}
			}
		}
		// 603 : GS-comment-051 end
		writeC(0xFE);
		writeH(0x13E);
		writeQ(_adenas);
		writeH(_counts);
	}
}
