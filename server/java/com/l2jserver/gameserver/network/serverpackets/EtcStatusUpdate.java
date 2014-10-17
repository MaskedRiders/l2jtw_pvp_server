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

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.zone.ZoneId;

/**
 * @author Luca Baldi
 */
public class EtcStatusUpdate extends L2GameServerPacket
{
	private final L2PcInstance _activeChar;
	private long _etc_status[] = new long[8]; // 603 : GS-comment-051
	
	public EtcStatusUpdate(L2PcInstance activeChar)
	{
		_activeChar = activeChar;
	}
	
	@Override
	protected void writeImpl()
	{
		// 603 : GS-comment-051 start
		_etc_status[0] = System.currentTimeMillis();
		_etc_status[1] = (long) _activeChar.getCharges();
		_etc_status[2] = (long) _activeChar.getWeightPenalty();
		_etc_status[3] = (long) _activeChar.getExpertiseWeaponPenalty();
		_etc_status[4] = (long) _activeChar.getExpertiseArmorPenalty();
		_etc_status[5] = (long) _activeChar.getDeathPenaltyBuffLevel();
		_etc_status[6] = (long) _activeChar.getChargedSouls();
		_etc_status[7] = (long) (
		((_activeChar.getMessageRefusal() || _activeChar.isChatBanned() || _activeChar.isSilenceMode()) ? 1 : 0) + 
		(_activeChar.isInsideZone(ZoneId.DANGER_AREA) ? 2 : 0) + 
		(_activeChar.hasCharmOfCourage() ? 4 : 0));
		
		if ((System.currentTimeMillis() - _activeChar.getEtcStatus(0)) > 60000)
		{
			for (int i = 0; i < 8; i++)
			{
				_activeChar.setEtcStatus(i, _etc_status[i]);
			}
		}
		else
		{
			int _needUpdate = 0;
			for (int i = 1; i < 8; i++)
			{
				if (_etc_status[i] != _activeChar.getEtcStatus(i))
				{
					_needUpdate = 1;
				}
			}
			if (_needUpdate == 0)
			{
				return;
			}
			else
			{
				for (int i = 0; i < 8; i++)
				{
					_activeChar.setEtcStatus(i, _etc_status[i]);
				}
			}
		}
		// 603 : GS-comment-051 end
		writeC(0xf9); // several icons to a separate line (0 = disabled)
		writeC(_activeChar.getCharges()); // 603 // 1-7 increase force, lvl
		writeD(_activeChar.getWeightPenalty()); // 1-4 weight penalty, lvl (1=50%, 2=66.6%, 3=80%, 4=100%)
		//603 writeD((_activeChar.getMessageRefusal() || _activeChar.isChatBanned() || _activeChar.isSilenceMode()) ? 1 : 0); // 1 = block all chat
		//603 writeD(_activeChar.isInsideZone(ZoneId.DANGER_AREA) ? 1 : 0); // 1 = danger area
		writeC(_activeChar.getExpertiseWeaponPenalty()); // 603 // Weapon Grade Penalty [1-4]
		writeC(_activeChar.getExpertiseArmorPenalty()); // 603 // Armor Grade Penalty [1-4]
		//603 writeD(_activeChar.hasCharmOfCourage() ? 1 : 0); // 1 = charm of courage (allows resurrection on the same spot upon death on the siege battlefield)
		writeC(_activeChar.getDeathPenaltyBuffLevel()); // 603 // 1-15 death penalty, lvl (combat ability decreased due to death)
		writeC(_activeChar.getChargedSouls()); // 603
		writeC(
		((_activeChar.getMessageRefusal() || _activeChar.isChatBanned() || _activeChar.isSilenceMode()) ? 1 : 0) + 
		(_activeChar.isInsideZone(ZoneId.DANGER_AREA) ? 2 : 0) + 
		(_activeChar.hasCharmOfCourage() ? 4 : 0)); // 603
	}
}
