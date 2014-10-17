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

import javolution.util.FastList;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.CrystallizationItem;
import com.l2jserver.gameserver.enums.Race;
import com.l2jserver.gameserver.enums.PrivateStoreType;
import com.l2jserver.gameserver.model.itemcontainer.PcInventory;
import com.l2jserver.gameserver.model.items.L2Item;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.items.type.CrystalType;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.model.skills.CommonSkill;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ActionFailed;
import com.l2jserver.gameserver.network.serverpackets.ExGetCrystalizingEstimation;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.util.Util;

/**
 * Created by IntelliJ IDEA.
 * User: Keiichi
 * Date: 28.05.2011
 * Time: 14:01:33
 * To change this template use File | Settings | File Templates.
 */
public class RequestCrystallizeEstimate extends L2GameClientPacket
{
	private static final String _C__D0_91_REQUESTCRYSTALIZEESTIMATE = "[C] D0:91 RequestCrystallizeEstimate";
	private FastList<CrystallizationItem> products;
	
	private int _objectId;
	private long _count;
	
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_count = readQ();
	}
	
	@Override
	protected void runImpl()
	{
		if(products == null)
		{
			products = new FastList<CrystallizationItem>();
		}
		
		L2PcInstance activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			_log.fine("RequestCrystallizeEstimate: activeChar was null");
			return;
		}
		
		if (!getClient().getFloodProtectors().getTransaction().tryPerformAction("crystallize"))
		{
			/* MessageTable.Messages[436]
			activeChar.sendMessage("You crystallizing too fast.");
			 */
			activeChar.sendMessage(436);
			return;
		}
		
		if (_count <= 0)
		{
			Util.handleIllegalPlayerAction(activeChar, "[RequestCrystallizeItem] count <= 0! ban! oid: " + _objectId + " owner: " + activeChar.getName(), Config.DEFAULT_PUNISH);
			return;
		}
		
		if (activeChar.getPrivateStoreType() != PrivateStoreType.NONE || activeChar.isInCrystallize())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CANNOT_TRADE_DISCARD_DROP_ITEM_WHILE_IN_SHOPMODE));
			return;
		}
		
		int skillLevel = activeChar.getSkillLevel(CommonSkill.CRYSTALLIZE.getId());
		if (skillLevel <= 0)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CRYSTALLIZE_LEVEL_TOO_LOW));
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			if (activeChar.getRace() != Race.DWARF && activeChar.getClassId().ordinal() != 117 && activeChar.getClassId().ordinal() != 55)
				_log.info("Player " + activeChar.getClient() + " used crystalize with classid: " + activeChar.getClassId().ordinal());
			return;
		}
		
		PcInventory inventory = activeChar.getInventory();
		if (inventory != null)
		{
			L2ItemInstance item = inventory.getItemByObjectId(_objectId);
			if (item == null)
			{
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if (item.isHeroItem())
				return;
			
			if (_count > item.getCount())
				_count = activeChar.getInventory().getItemByObjectId(_objectId).getCount();
		}
		
		L2ItemInstance itemToRemove = activeChar.getInventory().getItemByObjectId(_objectId);
		if (itemToRemove == null
				|| itemToRemove.isShadowItem()
				|| itemToRemove.isTimeLimitedItem())
			return;
		
		if (/*!itemToRemove.getItem().isCrystallizable() || */(itemToRemove.getItem().getCrystalCount() < 0) || (itemToRemove.getItem().getCrystalType() == CrystalType.NONE)) // rocknow-temp fix
		{
			_log.warning(activeChar.getName() + " (" + activeChar.getObjectId() + ") tried to crystallize " + itemToRemove.getItem().getId());
			return;
		}
		
		if (!activeChar.getInventory().canManipulateWithItemId(itemToRemove.getId()))
		{
			/* MessageTable.Messages[437]
			activeChar.sendMessage("Cannot use this item.");
			 */
			activeChar.sendMessage(437);
			return;
		}
		
		// Check if the char can crystallize items and return if false;
		boolean canCrystallize = true;
		
		switch (itemToRemove.getItem().getItemGradeSPlus())
		{
			case C:
			{
				if (skillLevel <= 1)
					canCrystallize = false;
				break;
			}
			case B:
			{
				if (skillLevel <= 2)
					canCrystallize = false;
				break;
			}
			case A:
			{
				if (skillLevel <= 3)
					canCrystallize = false;
				break;
			}
			case S:
			{
				if (skillLevel <= 4)
					canCrystallize = false;
				break;
			}
			case R:
			{
				if (skillLevel <= 5)
					canCrystallize = false;
				break;
			}
			case R95:
			{
				if (skillLevel <= 6)
					canCrystallize = false;
				break;
			}
			case R99:
			{
				if (skillLevel <= 7)
					canCrystallize = false;
				break;
			}
		}
		
		if (!canCrystallize)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CRYSTALLIZE_LEVEL_TOO_LOW));
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// add crystals
		int crystalId = itemToRemove.getItem().getCrystalItemId();
		int crystalAmount = itemToRemove.getCrystalCount();
		
		
		CrystallizationItem item = new CrystallizationItem(crystalId, crystalAmount, 100);
		products.add(item);
		activeChar.sendPacket(new ExGetCrystalizingEstimation(products));
	}
	
	@Override
	public String getType()
	{
		return _C__D0_91_REQUESTCRYSTALIZEESTIMATE;
	}
}
