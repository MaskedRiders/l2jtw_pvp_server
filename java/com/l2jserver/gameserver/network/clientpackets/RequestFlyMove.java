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

import com.l2jserver.gameserver.instancemanager.JumpManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
//import com.l2jserver.gameserver.network.serverpackets.ExNotifyFlyMoveStart;

/**
 * Format: (ch)d
 * 
 * @author mrTJO
 */
public final class RequestFlyMove extends L2GameClientPacket
{
	private static final String _C__D0_94_REQUESTFLYMOVE = "[C] D0:94 RequestFlyMove";
	//private static Logger _log = Logger.getLogger(RequestFlyMove.class.getName());
	int _nextPoint;
	//int _mode;
	
	@Override
	protected void readImpl()
	{
		_nextPoint = readD();
		//_mode = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
			return;
		
		JumpManager.getInstance().NextJump(activeChar, _nextPoint);
		//if (_mode == 0)
			//activeChar.sendPacket(new ExNotifyFlyMoveStart());
	}
	
	/* (non-Javadoc)
	 * @see com.l2jserver.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__D0_94_REQUESTFLYMOVE;
	}
}
