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

import java.util.List; // l2jtw add
import javolution.util.FastList; // l2jtw add
import com.l2jserver.gameserver.model.Elementals;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance; // l2jtw add
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;

/**
 * @author Kerberos
 */
public class ExChooseInventoryAttributeItem extends L2GameServerPacket
{
	private final int _itemId;
	private final byte _atribute;
	private final int _level;
	private final List<L2ItemInstance> _items; // l2jtw add
	
	/* l2jtw
	public ExChooseInventoryAttributeItem(L2ItemInstance item)
	 */
	public ExChooseInventoryAttributeItem(L2PcInstance activeChar, L2ItemInstance item)
	{
		_itemId = item.getDisplayId();
		_atribute = Elementals.getItemElement(_itemId);
		if (_atribute == Elementals.NONE)
		{
			throw new IllegalArgumentException("Undefined Atribute item: " + item);
		}
		_level = Elementals.getMaxElementLevel(_itemId);
		// l2jtw add start
		_items = new FastList<>();
		for(L2ItemInstance i: activeChar.getInventory().getItems())
		{
			if(i.isElementable())
			{
				_items.add(i);
			}
		}
		// l2jtw add end
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x63); // 603
		writeD(_itemId);
		writeQ(0); // 603-???
		// Structure for now
		// Must be 0x01 for stone/crystal attribute type
		writeD(_atribute == Elementals.FIRE ? 1 : 0); // Fire
		writeD(_atribute == Elementals.WATER ? 1 : 0); // Water
		writeD(_atribute == Elementals.WIND ? 1 : 0); // Wind
		writeD(_atribute == Elementals.EARTH ? 1 : 0); // Earth
		writeD(_atribute == Elementals.HOLY ? 1 : 0); // Holy
		writeD(_atribute == Elementals.DARK ? 1 : 0); // Unholy
		writeD(_level); // Item max attribute level
		// l2jtw add start
		writeD(_items.size());
		for(L2ItemInstance i : _items)
		{
			writeD(i.getObjectId());
		}
		// l2jtw add end
	}
}
