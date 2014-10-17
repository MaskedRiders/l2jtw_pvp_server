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
 * 
 * @author mrTJO
 */
public class ExShowUsm extends L2GameServerPacket
{
	final int _usmVideo;
	
	public static int GD1_INTRO = 2;
	
	public static int Q001 = 0x01;
	public static int Q002 = 0x03;
	public static int Q003 = 0x04;
	public static int Q004 = 0x05;
	public static int Q005 = 0x06;
	public static int Q006 = 0x07;
	public static int Q007 = 0x08;
	public static int Q009 = 0x09;
	public static int Q010 = 0x0A;
	
	public static int AWAKE_1 = 0x8B;
	public static int AWAKE_2 = 0x8C;
	public static int AWAKE_3 = 0x8D;
	public static int AWAKE_4 = 0x8E;
	public static int AWAKE_5 = 0x8F;
	public static int AWAKE_6 = 0x90;
	public static int AWAKE_7 = 0x91;
	public static int AWAKE_8 = 0x92;
	public static int ERTHEIA = 0x93;
	public static int INTRO_2 = 0x94;
	
	public ExShowUsm(int usmVideo)
	{
		_usmVideo = usmVideo;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x109); // 603
		writeD(_usmVideo);
	}
}
