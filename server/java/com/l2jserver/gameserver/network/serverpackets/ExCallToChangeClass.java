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

public class ExCallToChangeClass extends L2GameServerPacket
{
	private int _classId;
	
	public ExCallToChangeClass(int classId)
	{
		classId = _classId;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0xfe); // 603
		writeD(_classId);
		writeD(0); // 0 icon, window-the request to change the Saba 1, 2 and TD-0
		writeD(1);
	}
}
