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

import com.l2jserver.gameserver.model.PartyMatchRoomList; // l2jtw add
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance; // l2jtw add

/**
 * @author KenM
 */
public class ExAskJoinPartyRoom extends L2GameServerPacket
{
	private final String _charName;
	private final String _roomName; // l2jtw add
	
	public ExAskJoinPartyRoom(L2PcInstance player) // l2jtw add
	{
		_charName = player.getName(); // l2jtw add
		_roomName = PartyMatchRoomList.getInstance().getPlayerRoom(player).getTitle(); // l2jtw add
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x35);
		writeS(_charName);
		writeS(_roomName); // l2jtw add
	}
}
