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

import java.util.Collection;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.buylist.L2BuyList;
import com.l2jserver.gameserver.model.buylist.Product;

public final class BuyList extends L2GameServerPacket
{
	private final int _listId;
	private final Collection<Product> _list;
	private final long _money;
	private double _taxRate = 0;
	
	public BuyList(L2BuyList list, long currentMoney, double taxRate)
	{
		_listId = list.getListId();
		_list = list.getProducts();
		_money = currentMoney;
		_taxRate = taxRate;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xFE);
		writeH(0xB8); // 603
		writeD(0x00);
		writeQ(_money); // current money
		writeD(_listId);
		writeD(0x00); // 603 : GS-comment-024
		
		writeH(_list.size());
		
		for (Product product : _list)
		{
			if ((product.getCount() > 0) || !product.hasLimitedStock())
			{
				writeC(0); // 603
				writeD(product.getItemId());
				writeD(product.getItemId());
				writeC(0); // 603
				writeQ(product.getCount() < 0 ? 0 : product.getCount());
				writeC(product.getItem().getType2()); // 603
				writeC(product.getItem().getType1()); // 603 // Custom Type 1
				writeH(0x00); // isEquipped
				writeQ(product.getItem().getBodyPart()); // 603 // Body Part
				writeC(0x00); // 603 // Enchant
				writeC(0x00); // 603 // Custom Type
				//603 writeD(0x00); // Augment
				writeD(-1); // Mana
				writeD(-9999); // Time
				writeC(0x01); // 603
				/* 603
				writeD(0x00); // Augment // 603
				writeH(0x00); // Element Type
				writeH(0x00); // Element Power
				for (byte i = 0; i < 6; i++)
				{
					writeH(0x00);
				}
				// Enchant Effects
				writeH(0x00);
				writeH(0x00);
				writeH(0x00);
				writeD(0x00); // 603-Appearance
				 */
				
				if ((product.getItemId() >= 3960) && (product.getItemId() <= 4026))
				{
					writeQ((long) (product.getPrice() * Config.RATE_SIEGE_GUARDS_PRICE * (1 + _taxRate)));
				}
				else
				{
					writeQ((long) (product.getPrice() * (1 + _taxRate)));
				}
			}
		}
	}
}
