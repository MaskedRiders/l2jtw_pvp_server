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

import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;

/**
 * @author KenM
 */
public final class ExRpItemLink extends L2GameServerPacket
{
	private final L2ItemInstance _item;
	private int check_Augmentation; // 603
	private int check_ElementType; // 603
	private int check_EnchantOption; // 603
	
	public ExRpItemLink(L2ItemInstance item)
	{
		_item = item;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x6D); // 603
		check_Augmentation = 0;
		check_ElementType = 0;
		check_EnchantOption = 0;
		if (_item.isAugmented())
		{
			check_Augmentation = 1;
		}
		if (_item.getAttackElementPower() > 0)
		{
			check_ElementType = 2;
		}
		else
		{
			for (byte i = 0; i < 6; i++)
			{
				if (_item.getElementDefAttr(i) > 0)
				{
					check_ElementType = 2;
				}
			}
		}
		for (int op : _item.getEnchantOptions())
		{
			if (op > 0)
			{
				check_EnchantOption = 4;
			}
		}
		writeC(check_Augmentation + check_ElementType + check_EnchantOption + 0);
		writeD(_item.getObjectId());
		writeD(_item.getDisplayId());
		writeC(_item.getLocationSlot()); // 603
		writeQ(_item.getCount());
		writeC(_item.getItem().getType2()); // 603
		writeC(_item.getCustomType1()); // 603
		writeH(_item.isEquipped() ? 0x01 : 0x00);
		writeQ(_item.getItem().getBodyPart()); // 603
		writeC(_item.getEnchantLevel()); // 603
		writeC(_item.getCustomType2()); // 603
		/* 603
		if (_item.isAugmented())
		{
			writeD(_item.getAugmentation().getAugmentationId());
		}
		else
		{
			writeD(0x00);
		}
		 */
		writeD(_item.getMana());
		writeD(_item.isTimeLimitedItem() ? (int) (_item.getRemainingTime() / 1000) : -9999);
		writeC(0x01); // 603
		if (_item.isAugmented()) // 603
		{
			writeD(_item.getAugmentation().getAugmentationId());
		}
		if (check_ElementType > 0) // 603
		{
			writeH(_item.getAttackElementType());
			writeH(_item.getAttackElementPower());
			for (byte i = 0; i < 6; i++)
			{
				writeH(_item.getElementDefAttr(i));
			}
		}
		// Enchant Effects
		if (check_EnchantOption > 0) // 603
		{
			for (int op : _item.getEnchantOptions())
			{
				writeH(op);
			}
		}
		//603 writeD(0x00); // 603-Appearance
	}
}
