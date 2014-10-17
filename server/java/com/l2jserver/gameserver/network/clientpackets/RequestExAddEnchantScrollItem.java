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
package com.l2jserver.gameserver.network.clientpackets;

import com.l2jserver.gameserver.datatables.EnchantItemData;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.enchant.EnchantScroll;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ExPutEnchantScrollItemResult;

/**
 * @author KenM
 */
public class RequestExAddEnchantScrollItem extends L2GameClientPacket
{
	private static final String _C__D0_E3_REQUESTEXADDENCHANTSCROLLITEM = "[C] D0:E3 RequestExAddEnchantScrollItem";
	
	private int _scroll;
	private int _item;
	
	@Override
	protected void readImpl()
	{
		_scroll = readD();
		_item = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		final L2ItemInstance item = activeChar.getInventory().getItemByObjectId(_item);
		final L2ItemInstance scroll = activeChar.getInventory().getItemByObjectId(_scroll);
		final L2ItemInstance support = activeChar.getInventory().getItemByObjectId(activeChar.getActiveEnchantSupportItemId());
		
		if ((item == null) || (scroll == null))
		{
			// message may be custom
			activeChar.sendPacket(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITION);
			activeChar.setActiveEnchantSupportItemId(L2PcInstance.ID_NONE);
			return;
		}
		
		final EnchantScroll scrollTemplate = EnchantItemData.getInstance().getEnchantScroll(scroll);
		
		if ((scrollTemplate == null) || !scrollTemplate.isValid(item, null))
		{
			activeChar.sendPacket(SystemMessageId.DOES_NOT_FIT_SCROLL_CONDITIONS);
			activeChar.setActiveEnchantSupportItemId(L2PcInstance.ID_NONE);
			activeChar.sendPacket(new ExPutEnchantScrollItemResult(0));
			return;
		}
		activeChar.setIsEnchanting(true);
		activeChar.setActiveEnchantTimestamp(System.currentTimeMillis());
		activeChar.setActiveEnchantItemId(scroll.getObjectId());
		activeChar.sendPacket(new ExPutEnchantScrollItemResult(1));
	}
	
	@Override
	public String getType()
	{
		return _C__D0_E3_REQUESTEXADDENCHANTSCROLLITEM;
	}
}
