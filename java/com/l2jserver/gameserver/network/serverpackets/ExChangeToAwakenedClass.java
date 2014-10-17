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

/**
 * Created by IntelliJ IDEA.
 * User: Keiichi
 * Date: 07.06.2011
 * Time: 0:28:22
 * To change this template use File | Settings | File Templates.
 */
public class ExChangeToAwakenedClass extends L2GameServerPacket
{
	private final int _newId;
	
	public ExChangeToAwakenedClass(int newId)
	{
		_newId = newId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xfe);
		writeH(0xff); // 603
		if (_newId == 144) // GS-comment-038
			writeD(171);
		else
			writeD(_newId);
		//writeD(0x00); // 603
	}
}
