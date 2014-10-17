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

import com.l2jserver.gameserver.datatables.CrestTable;
import com.l2jserver.gameserver.model.L2Crest;
import com.l2jserver.gameserver.model.L2Clan; // 603

/**
 * @author -Wooden-
 */
public class ExPledgeCrestLarge extends L2GameServerPacket
{
	private final L2Clan _clan; // 603
	private final int _crestId;
	private final byte[] _data;
	
	public ExPledgeCrestLarge(L2Clan clan, int crestId) // 603
	{
		_clan = clan; // 603
		_crestId = crestId;
		final L2Crest crest = CrestTable.getInstance().getCrest(crestId);
		_data = crest != null ? crest.getData() : null;
	}
	
	public ExPledgeCrestLarge(L2Clan clan, int crestId, byte[] data) // 603
	{
		_clan = clan; // 603
		_crestId = crestId;
		_data = data;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x1B);
		/* 603-Start
		writeD(0x00);
		writeD(_crestId);
		if (_data != null)
		{
			writeD(_data.length);
			writeB(_data);
		}
		else
		{
			writeD(0);
		}
		 */
		writeD(2); // 603
		writeD(_clan.getId());
		writeD(_crestId);
		writeD(0); // _data index 0/1/2/3/4
		if (_data != null)
		{
			writeD(65664); // _data.length-Total 14336*4+8320=65664
			writeD(_data.length); // _data.length-Split 14336/14336/14336/14336/8320
			writeB(_data);
		}
		else
		{
			writeD(0); // guess
			writeD(0); // guess
		}
		// 603-End
	}
}