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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.l2jserver.Config;
import com.l2jserver.gameserver.datatables.CharNameTable;
import com.l2jserver.gameserver.network.serverpackets.ExIsCharNameCreatable;
import com.l2jserver.gameserver.util.Util;

/**
 * Format (ch)S
 * S: Character Name
 *
 * @author OSTIN
 */
public class RequestCharacterNameCreatable extends L2GameClientPacket
{
	private static final String _C__D0_A9_REQUESTCHARACTERNAMECREATABLE = "[C] D0:A9 RequestCharacterNameCreatable";
	String _name;
	
	@Override
	protected void readImpl()
	{
		_name = readS();
	}
	
	@Override
	protected void runImpl()
	{
		if ((_name.length() < 1) || (_name.length() > 16))
		{
			sendPacket(new ExIsCharNameCreatable(ExIsCharNameCreatable.REASON_16_ENG_CHARS));
			return;
		}
		
		if (Config.FORBIDDEN_NAMES.length > 1)
		{
			for(String st : Config.FORBIDDEN_NAMES)
			{
				if(_name.toLowerCase().contains(st.toLowerCase()))
				{
					sendPacket(new ExIsCharNameCreatable(ExIsCharNameCreatable.REASON_INCORRECT_NAME));
					return;
				}
			}
		}
		
		if (!Util.isAlphaNumeric(_name) || !isValidName(_name))
		{
			sendPacket(new ExIsCharNameCreatable(ExIsCharNameCreatable.REASON_INCORRECT_NAME));
			return;
		}
		
		int _code = CharNameTable.getInstance().getIdByName(_name) != -1 ? 2 : -1;
		
		sendPacket(new ExIsCharNameCreatable(_code));
	}
	
	private boolean isValidName(String text)
	{
		boolean result = true;
		String test = text;
		Pattern pattern;
		try
		{
			pattern = Pattern.compile(Config.CNAME_TEMPLATE);
		}
		catch (PatternSyntaxException e) // case of illegal pattern
		{
			pattern = Pattern.compile(".*");
		}
		Matcher regexp = pattern.matcher(test);
		if (!regexp.matches())
		{
			result = false;
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.l2jserver.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__D0_A9_REQUESTCHARACTERNAMECREATABLE;
	}
}
