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

/**
 * @author UnAfraid
 */
public class Hit
{
	private static final int HITFLAG_USESS = 0x08; // 603
	private static final int HITFLAG_CRIT = 0x04; // 603
	private static final int HITFLAG_SHLD = 0x02; // 603
	private static final int HITFLAG_MISS = 0x01; // 603
	
	private final int _targetId;
	private final int _damage;
	private int _flags = 0;
	private int _ssGrade = -1; // 603
	
	public Hit(L2Object target, int damage, boolean miss, boolean crit, byte shld, boolean soulshot, int ssGrade)
	{
		_targetId = target.getObjectId();
		_damage = damage;
		_ssGrade = ssGrade; // l2jtw add
		
		if (soulshot)
		{
			/* l2jtw add
			_flags |= HITFLAG_USESS | ssGrade;
			 */
			_flags |= HITFLAG_USESS;
		}
		
		if (crit)
		{
			_flags |= HITFLAG_CRIT;
		}
		
		if (shld > 0)
		{
			_flags |= HITFLAG_SHLD;
		}
		
		if (miss)
		{
			/* 603 start
			_flags |= HITFLAG_MISS;
			 */
			_flags = HITFLAG_MISS;
			_ssGrade = -1;
			// 603 end
		}
	}
	
	public int getTargetId()
	{
		return _targetId;
	}
	
	public int getDamage()
	{
		return _damage;
	}
	
	public int getFlags()
	{
		return _flags;
	}
	// l2jtw add start
	public int getSSGrade()
	{
		return _ssGrade;
	}
	// l2jtw add end
}
