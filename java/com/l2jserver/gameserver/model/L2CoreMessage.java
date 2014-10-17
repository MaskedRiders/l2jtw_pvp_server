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
package com.l2jserver.gameserver.model;


import java.util.Vector;

import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * @author ShanSoft<br>
 */
public class L2CoreMessage
{

	//private static final Logger _log = Logger.getLogger(L2CoreMessage.class.getName());
	
	int _mid;
	String _language,_message,_extra;
	Vector<String> value = new Vector<>();
	Vector<String> extravalue = new Vector<>();
	
	public L2CoreMessage(int mid,String language, String message, String extra)
	{
		
		_mid = mid;
		_language = language;
		_message = message;
		_extra = extra;
		
	}
	
	public L2CoreMessage(L2CoreMessage msg)
	{
		_mid = msg._mid;
		_language = msg._language;
		_message = msg._message;
		_extra = msg._extra;
	}
	
	public int getMessageId()
	{
		return _mid;
	}
	
	public String getLanguage()
	{
		return _language;
	}
	
	public String getMessage()
	{
		return _message;
	}
	
	public String getExtra()
	{
		return _extra;
	}
	
	public String getExtra(int num)
	{
		String[] text = _extra.split(";");
		return text[num-1];
	}
	
	public void addString(String text)
	{
		value.add(text);
	}
	
	public void addSkillName(int id, int level)
	{
		String text = SkillData.getInstance().getSkill(id, level).getName();
		value.add(text);
	}
	
	public void addSkillName(int id)
	{
		String text = SkillData.getInstance().getSkill(id, 1).getName();
		value.add(text);
	}
	
	public void addSkillName(Skill skill)
	{
		String text = skill.getName();
		value.add(text);
	}
	
	public void addItemName(int id)
	{
		String text = ItemTable.getInstance().getTemplate(id).getName();
		value.add(text);
	}	
	
	public void addItemName(L2ItemInstance item)
	{
		String text = item.getName();
		value.add(text);
	}
	
	public void addExtra(int num)
	{
		String[] text = _extra.split(";");
		extravalue.add(text[num-1]);
	}
	
	public void addNumber(double num)
	{
		String text = "" + num;
		value.add(text);
	}
	
	public void addNumber(long num)
	{
		String text = "" + num;
		value.add(text);
	}
	
	public void addNumber(int num)
	{
		String text = "" + num;
		value.add(text);
	}
	
	public String renderMsg()
	{
		int i=0;

		for (String text : extravalue)
		{
			i++;
			_message = _message.replace("$E" + i, text);
		}
		
		i = 0;
		
		for (String text : value)
		{
			i++;
			_message = _message.replace("$" + i, text);
		}
		
		return _message;
	}
	
	public void sendMessage(L2PcInstance player)
	{
		/*
		int i=0;

		for (String text : extravalue)
		{
			i++;
			_message = _message.replace(MessageTable.extrafiller[i], text);
		}
		
		i = 0;
		
		for (String text : value)
		{
			_message = _message.replace(MessageTable.filler[i], text);
			i++;
		}
		 */
		
		SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1);
		sm.addString(renderMsg());
		player.sendPacket(sm);
		
	}
	
	
}