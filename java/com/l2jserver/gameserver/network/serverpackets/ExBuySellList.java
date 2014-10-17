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

import java.util.ArrayList; // 603
import java.util.List; // 603
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;

/**
 * @author ShanSoft
 */
public class ExBuySellList extends AbstractItemPacket
{
	private L2ItemInstance[] _sellList = null;
	private L2ItemInstance[] _refundList = null;
	private final boolean _done;
	private final List<L2ItemInstance> _items = new ArrayList<>(); // 603
	
	public ExBuySellList(L2PcInstance player, boolean done)
	{
		_sellList = player.getInventory().getAvailableItems(false, false, false);
		if (player.hasRefund())
		{
			_refundList = player.getRefund().getItems();
		}
		_done = done;
		// 603 add start
		for (L2ItemInstance item : player.getInventory().getItems())
		{
			if (!item.isQuestItem())
			{
				_items.add(item);
			}
		}
		// 603 add end
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xFE);
		writeH(0xB8); // 603
		writeD(0x01);
		writeD(_items.size()); // 603
		
		if ((_sellList != null))
		{
			writeH(_sellList.length);
			for (L2ItemInstance item : _sellList)
			{
				writeItem(item);
				writeQ(item.getItem().getReferencePrice() / 2);
			}
		}
		else
		{
			writeH(0x00);
		}
		
		if ((_refundList != null) && (_refundList.length > 0))
		{
			writeH(_refundList.length);
			int i = 0;
			for (L2ItemInstance item : _refundList)
			{
				writeItem(item);
				writeD(i++);
				writeQ((item.getItem().getReferencePrice() / 2) * item.getCount());
			}
		}
		else
		{
			writeH(0x00);
		}
		
		writeC(_done ? 0x01 : 0x00);
	}
}