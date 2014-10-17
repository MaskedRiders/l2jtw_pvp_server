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

public class ExFriendDetailInfo extends L2GameServerPacket
{
	public ExFriendDetailInfo()
	{
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xfe);
		writeH(0xec); // 603 : GS-comment-039.1
		//writeD(???)
		//writeS : GS-comment-039.2
		//writeD : GS-comment-039.3
		//writeD : GS-comment-039.4
		//writeH : GS-comment-039.5
		//writeH : GS-comment-039.6
		//writeD : GS-comment-039.7
		//writeD : GS-comment-039.8
		//writeS : GS-comment-039.9
		//writeD(0-???)
		//writeD(0-???)
		//writeS : GS-comment-039.10
		//writeC : GS-comment-039.11
		//writeC : GS-comment-039.12
		//writeD : GS-comment-039.13
		//writeS : GS-comment-039.14
	}
}
