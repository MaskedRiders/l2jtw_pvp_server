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
package com.l2jserver.gameserver.model.conditions;

import com.l2jserver.gameserver.instancemanager.CastleManager;
import com.l2jserver.gameserver.instancemanager.FortManager;
import com.l2jserver.gameserver.instancemanager.TerritoryWarManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.Castle;
import com.l2jserver.gameserver.model.entity.Fort;
import com.l2jserver.gameserver.model.stats.Env;
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.network.SystemMessageId;

/**
 * Player Can Create Outpost condition implementation.
 * @author Adry_85
 */
public class ConditionPlayerCanCreateOutpost extends Condition
{
	private final boolean _val;
	
	public ConditionPlayerCanCreateOutpost(boolean val)
	{
		_val = val;
	}
	
	@Override
	public boolean testImpl(Env env)
	{
		boolean canCreateOutpost = true;
		if ((env.getPlayer() == null) || env.getPlayer().isAlikeDead() || env.getPlayer().isCursedWeaponEquipped() || (env.getPlayer().getClan() == null))
		{
			canCreateOutpost = false;
		}
		
		final Castle castle = CastleManager.getInstance().getCastle(env.getPlayer());
		final Fort fort = FortManager.getInstance().getFort(env.getPlayer());
		
		if ((castle == null) && (fort == null))
		{
			canCreateOutpost = false;
		}
		
		L2PcInstance player = env.getPlayer().getActingPlayer();
		if (((fort != null) && (fort.getResidenceId() == 0)) || ((castle != null) && (castle.getResidenceId() == 0)))
		{
			/* MessageTable.Messages[143]
			player.sendMessage("You must be on fort or castle ground to construct an outpost or flag.");
			 */
			player.sendMessage(143);
			canCreateOutpost = false;
		}
		else if (((fort != null) && !fort.getZone().isActive()) || ((castle != null) && !castle.getZone().isActive()))
		{
			/* MessageTable.Messages[144]
			player.sendMessage("You can only construct an outpost or flag on siege field.");
			 */
			player.sendMessage(144);
			canCreateOutpost = false;
		}
		else if (!player.isClanLeader())
		{
			/* MessageTable.Messages[145]
			player.sendMessage("You must be a clan leader to construct an outpost or flag.");
			 */
			player.sendMessage(145);
			canCreateOutpost = false;
		}
		else if (TerritoryWarManager.getInstance().getHQForClan(player.getClan()) != null)
		{
			player.sendPacket(SystemMessageId.NOT_ANOTHER_HEADQUARTERS);
			canCreateOutpost = false;
		}
		else if (TerritoryWarManager.getInstance().getFlagForClan(player.getClan()) != null)
		{
			player.sendPacket(SystemMessageId.A_FLAG_IS_ALREADY_BEING_DISPLAYED_ANOTHER_FLAG_CANNOT_BE_DISPLAYED);
			canCreateOutpost = false;
		}
		else if (!player.isInsideZone(ZoneId.HQ))
		{
			player.sendPacket(SystemMessageId.NOT_SET_UP_BASE_HERE);
			canCreateOutpost = false;
		}
		return (_val == canCreateOutpost);
	}
}