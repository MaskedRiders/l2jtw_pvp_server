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

import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance; // 603

public final class PledgeStatusChanged extends L2GameServerPacket
{
	private final L2Clan _clan;
	private final L2PcInstance _activeChar; // 603
	private final int _pledgeType; // 603
	
	public PledgeStatusChanged(L2Clan clan, L2PcInstance activeChar) // 603
	{
		_clan = clan;
		_activeChar = activeChar; // 603
		_pledgeType = activeChar.getPledgeType(); // 603
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xCD);
		writeD(2); // 603
		writeD(_clan.getLeaderId());
		writeD(_clan.getId());
		writeD(_clan.getCrestId());
		writeD(_clan.getAllyId());
		writeD(_clan.getAllyCrestId());
		writeD(_clan.getCrestLargeId()); // 603
		writeD(_pledgeType); // 603
	}
}
