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

import java.util.Map;

import com.l2jserver.gameserver.instancemanager.InstanceManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.instancezone.InstanceWorld;
import com.l2jserver.gameserver.network.serverpackets.ExInzoneWaitingInfo;

/**
 * @author a
 */
public final class RequestInzoneWaitingTime extends L2GameClientPacket
{
	private static final String _C__D0_BA_REQUESTINZONEWAITINGTIME = "[C] D0:BA RequestInzoneWaitingTime";
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jserver.gameserver.network.clientpackets.L2GameClientPacket#readImpl()
	 */
	@Override
	protected void readImpl()
	{
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jserver.gameserver.network.clientpackets.L2GameClientPacket#runImpl()
	 */
	@Override
	protected void runImpl()
	{
		// TODO Auto-generated method stub
		L2PcInstance activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		final InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(activeChar);
		ExInzoneWaitingInfo eiwi = new ExInzoneWaitingInfo((world != null) && (world.getTemplateId() >= 0) ? world.getTemplateId() : 0);
		
		Map<Integer, Long> instanceTimes = InstanceManager.getInstance().getAllInstanceTimes(activeChar.getObjectId());
		if (instanceTimes != null)
		{
			for (int instanceId : instanceTimes.keySet())
			{
				int remainingTime = (int) ((instanceTimes.get(instanceId) - System.currentTimeMillis()) / 1000);
				if (remainingTime > 60)
				{
					eiwi.add(instanceId, remainingTime);
				}
				else
				{
					InstanceManager.getInstance().deleteInstanceTime(activeChar.getObjectId(), instanceId);
				}
			}
		}
		activeChar.sendPacket(eiwi);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jserver.gameserver.network.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		// TODO Auto-generated method stub
		return _C__D0_BA_REQUESTINZONEWAITINGTIME;
	}
	
}
