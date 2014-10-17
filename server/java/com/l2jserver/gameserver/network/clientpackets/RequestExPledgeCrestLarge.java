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

import com.l2jserver.gameserver.network.serverpackets.ExPledgeCrestLarge;
import com.l2jserver.gameserver.model.L2Clan; // 603
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance; // 603

/**
 * Fomat : chd c: (id) 0xD0 h: (subid) 0x10 d: the crest id This is a trigger
 * @author -Wooden-
 */
public final class RequestExPledgeCrestLarge extends L2GameClientPacket
{
	private static final String _C__D0_10_REQUESTEXPLEDGECRESTLARGE = "[C] D0:10 RequestExPledgeCrestLarge";
	
	private int _crestId;
	private int _unk; // 603
	
	@Override
	protected void readImpl()
	{
		_crestId = readD();
		_unk = readD(); // 603
	}
	
	@Override
	protected void runImpl()
	{
		// 603-Start
		L2PcInstance _activeChar = getClient().getActiveChar();
		if (_activeChar == null)
			return;
		
		L2Clan clan = _activeChar.getClan();
		if (clan == null)
			return;
		// 603-End
		/* 603
		sendPacket(new ExPledgeCrestLarge(_crestId));
		 */
		sendPacket(new ExPledgeCrestLarge(clan, _crestId));
	}
	
	@Override
	public String getType()
	{
		return _C__D0_10_REQUESTEXPLEDGECRESTLARGE;
	}
	
	@Override
	protected boolean triggersOnActionRequest()
	{
		return false;
	}
}