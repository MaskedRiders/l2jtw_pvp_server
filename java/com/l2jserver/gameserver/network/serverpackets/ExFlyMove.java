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

import com.l2jserver.gameserver.instancemanager.JumpManager.JumpNode;
import com.l2jserver.gameserver.instancemanager.JumpManager.JumpWay;

/**
 * Created by IntelliJ IDEA. User: Keiichi Date: 27.05.2011 Time: 12:06:19 To change this template use File | Settings | File Templates.
 */
public class ExFlyMove extends L2GameServerPacket
{
	final int _objId;
	final int _trackId;
	final JumpWay _jw;
	
	public ExFlyMove(int objid, int trackId, JumpWay jw)
	{
		_objId = objid;
		_trackId = trackId;
		_jw = jw;
	}
	
	@Override
	protected void writeImpl()
	{
		if (_jw == null)
		{
			return;
		}
		writeC(0xfe);
		writeH(0xe8); // 603
		writeD(_objId);
		
		if (_jw.size() == 1)
		{
			writeD(2);
			
		}
		else
		{
			writeD(0);
		}
		writeD(0);
		writeD(_trackId);
		
		writeD(_jw.size());
		for (JumpNode n : _jw)
		{
			writeD(n.getNext());
			writeD(0);
			
			writeD(n.getX());
			writeD(n.getY());
			writeD(n.getZ());
			
		}
	}
}
