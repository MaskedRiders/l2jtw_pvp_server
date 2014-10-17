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

import com.l2jserver.gameserver.model.ItemInfo;
import com.l2jserver.gameserver.model.TradeItem;
import com.l2jserver.gameserver.model.itemcontainer.PcInventory;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;

/**
 * @author UnAfraid
 */
public abstract class AbstractItemPacket extends L2GameServerPacket
{
	private int check_Augmentation; // 603
	private int check_ElementType; // 603
	private int check_EnchantOption; // 603
	
	protected void writeItem(TradeItem item)
	{
		writeItem(new ItemInfo(item));
	}
	
	protected void writeItem(L2ItemInstance item)
	{
		writeItem(new ItemInfo(item));
	}
	
	protected void writeItem(ItemInfo item)
	{
		check_Augmentation = 0;
		check_ElementType = 0;
		check_EnchantOption = 0;
		if (item.getAugmentationBonus() > 0)
		{
			check_Augmentation = 1;
		}
		if (item.getAttackElementPower() > 0)
		{
			check_ElementType = 2;
		}
		else
		{
			for (byte i = 0; i < 6; i++)
			{
				if (item.getElementDefAttr(i) > 0)
				{
					check_ElementType = 2;
				}
			}
		}
		for (int op : item.getEnchantOptions())
		{
			if (op > 0)
			{
				check_EnchantOption = 4;
			}
		}
		writeC(check_Augmentation + check_ElementType + check_EnchantOption + 0);
		// GS-comment-023
		writeD(item.getObjectId()); // ObjectId
		writeD(item.getItem().getDisplayId()); // ItemId
		writeC(item.getLocation()); // 603 // T1
		writeQ(item.getCount()); // Quantity
		writeC(item.getItem().getType2()); // 603 // Item Type 2 : 00-weapon, 01-shield/armor, 02-ring/earring/necklace, 03-questitem, 04-adena, 05-item
		writeC(item.getCustomType1()); // 603 // Filler (always 0)
		writeH(item.getEquipped()); // Equipped : 00-No, 01-yes
		writeQ(item.getItem().getBodyPart()); // 603 // Slot : 0006-lr.ear, 0008-neck, 0030-lr.finger, 0040-head, 0100-l.hand, 0200-gloves, 0400-chest, 0800-pants, 1000-feet, 4000-r.hand, 8000-r.hand
		writeC(item.getEnchant()); // 603 // Enchant level (pet level shown in control item)
		writeC(item.getCustomType2()); // 603 // Pet name exists or not shown in control item
		//603 writeD(item.getAugmentationBonus());
		writeD(item.getMana());
		writeD(item.getTime());
		writeC(0x01); // 603
		if (check_Augmentation > 0) // 603
		{
			writeD(item.getAugmentationBonus());
		}
		writeItemElementalAndEnchant(item);
		//603 writeD(0x00); // 603-Appearance
	}
	
	protected void writeItemElementalAndEnchant(ItemInfo item)
	{
		if (check_ElementType > 0) // 603
		{
			writeH(item.getAttackElementType());
			writeH(item.getAttackElementPower());
			for (byte i = 0; i < 6; i++)
			{
				writeH(item.getElementDefAttr(i));
			}
		}
		// Enchant Effects
		if (check_EnchantOption > 0) // 603
		{
			for (int op : item.getEnchantOptions())
			{
				writeH(op);
			}
		}
	}
	
	protected void writeInventoryBlock(PcInventory inventory)
	{
		if (inventory.hasInventoryBlock())
		{
			writeH(inventory.getBlockItems().length);
			writeC(inventory.getBlockMode());
			for (int i : inventory.getBlockItems())
			{
				writeD(i);
			}
		}
		else
		{
			writeH(0x00);
		}
	}
}
