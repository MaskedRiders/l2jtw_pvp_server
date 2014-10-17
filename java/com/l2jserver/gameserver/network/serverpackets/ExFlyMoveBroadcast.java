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

import com.l2jserver.gameserver.model.actor.L2Character;

/**
 * RusDev
 * @author OSTIN
 */
public class ExFlyMoveBroadcast extends L2GameServerPacket
{
	int _objId, _x, _y, _z, _xDest, _yDest, _zDest;
	
	public ExFlyMoveBroadcast(L2Character cha)
	{
		_objId = cha.getObjectId();
		_x = cha.getXdestination();
		_y = cha.getYdestination();
		_z = cha.getZdestination();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xFE);
		writeH(0x108); // 603
		writeD(_objId);
		
		writeD(0x02);
		writeD(0x00);
		
		writeD(_xDest);
		writeD(_yDest);
		writeD(_zDest);
		writeD(0x00);
		
		writeD(_x);
		writeD(_y);
		writeD(_z);
	}
}
