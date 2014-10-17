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
import com.l2jserver.gameserver.model.itemcontainer.Inventory;

public class ExUserInfoEquipSlot extends L2GameServerPacket
{
	private L2PcInstance _activeChar;
	private int _airShipHelm;
	private long _user_equipslot[] = new long[78]; // 603 : GS-comment-051
	
	public ExUserInfoEquipSlot(L2PcInstance character)
	{
		_activeChar = character;
		if (_activeChar.isInAirShip() && _activeChar.getAirShip().isCaptain(_activeChar))
			_airShipHelm = _activeChar.getAirShip().getHelmItemId();
		else
			_airShipHelm = 0;
	}
	
	@Override
	protected final void writeImpl()
	{
		// 603 : GS-comment-051 start
		_user_equipslot[0] = System.currentTimeMillis();
		_user_equipslot[1] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_UNDER);
		_user_equipslot[2] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_UNDER);
		_user_equipslot[3] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_UNDER);
		_user_equipslot[4] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_REAR);
		_user_equipslot[5] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_REAR);
		_user_equipslot[6] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_REAR);
		_user_equipslot[7] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEAR);
		_user_equipslot[8] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_LEAR);
		_user_equipslot[9] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_LEAR);
		_user_equipslot[10] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_NECK);
		_user_equipslot[11] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_NECK);
		_user_equipslot[12] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_NECK);
		_user_equipslot[13] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RFINGER);
		_user_equipslot[14] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_RFINGER);
		_user_equipslot[15] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RFINGER);
		_user_equipslot[16] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LFINGER);
		_user_equipslot[17] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_LFINGER);
		_user_equipslot[18] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_LFINGER);
		_user_equipslot[19] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HEAD);
		_user_equipslot[20] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_HEAD);
		_user_equipslot[21] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_HEAD);
		if (_airShipHelm == 0)
		{
			_user_equipslot[22] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RHAND);
			_user_equipslot[23] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_RHAND);
			_user_equipslot[24] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND);
			_user_equipslot[25] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LHAND);
			_user_equipslot[26] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_LHAND);
			_user_equipslot[27] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_LHAND);
		}
		else
		{
			_user_equipslot[22] = (long) _airShipHelm;
			_user_equipslot[23] = 0;
			_user_equipslot[24] = 0;
			_user_equipslot[25] = 0;
			_user_equipslot[26] = 0;
			_user_equipslot[27] = 0;
		}
		_user_equipslot[28] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_GLOVES);
		_user_equipslot[29] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_GLOVES);
		_user_equipslot[30] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_GLOVES);
		_user_equipslot[31] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_CHEST);
		_user_equipslot[32] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_CHEST);
		_user_equipslot[33] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_CHEST);
		_user_equipslot[34] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEGS);
		_user_equipslot[35] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_LEGS);
		_user_equipslot[36] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_LEGS);
		_user_equipslot[37] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_FEET);
		_user_equipslot[38] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_FEET);
		_user_equipslot[39] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_FEET);
		_user_equipslot[40] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_CLOAK);
		_user_equipslot[41] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_CLOAK);
		_user_equipslot[42] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_CLOAK);
		_user_equipslot[43] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RHAND);
		_user_equipslot[44] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_RHAND);
		_user_equipslot[45] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND);
		_user_equipslot[46] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HAIR);
		_user_equipslot[47] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_HAIR);
		_user_equipslot[48] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_HAIR);
		_user_equipslot[49] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HAIR2);
		_user_equipslot[50] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_HAIR2);
		_user_equipslot[51] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_HAIR2);
		_user_equipslot[52] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RBRACELET);
		_user_equipslot[53] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_RBRACELET);
		_user_equipslot[54] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RBRACELET);
		_user_equipslot[55] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LBRACELET);
		_user_equipslot[56] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_LBRACELET);
		_user_equipslot[57] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_LBRACELET);
		_user_equipslot[58] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_DECO1);
		_user_equipslot[59] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_DECO1);
		_user_equipslot[60] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_DECO1);
		_user_equipslot[61] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_DECO2);
		_user_equipslot[62] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_DECO2);
		_user_equipslot[63] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_DECO2);
		_user_equipslot[64] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_DECO3);
		_user_equipslot[65] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_DECO3);
		_user_equipslot[66] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_DECO3);
		_user_equipslot[67] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_DECO4);
		_user_equipslot[68] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_DECO4);
		_user_equipslot[69] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_DECO4);
		_user_equipslot[70] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_DECO5);
		_user_equipslot[71] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_DECO5);
		_user_equipslot[72] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_DECO5);
		_user_equipslot[73] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_DECO6);
		_user_equipslot[74] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_DECO6);
		_user_equipslot[75] = (long) _activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_DECO6);
		_user_equipslot[76] = (long) _activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_BELT);
		_user_equipslot[77] = (long) _activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_BELT);
		
		if ((System.currentTimeMillis() - _activeChar.getUserEquipSlot(0)) > 60000)
		{
			for (int i = 0; i < 78; i++)
			{
				_activeChar.setUserEquipSlot(i, _user_equipslot[i]);
			}
		}
		else
		{
			int _needUpdate = 0;
			for (int i = 1; i < 78; i++)
			{
				if (_user_equipslot[i] != _activeChar.getUserEquipSlot(i))
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
				for (int i = 0; i < 78; i++)
				{
					_activeChar.setUserEquipSlot(i, _user_equipslot[i]);
				}
			}
		}
		// 603 : GS-comment-051 end
		writeC(0xFE);
		writeH(0x156);
		writeD(_activeChar.getObjectId());
		writeH(33);
		writeD(-1);
		writeC(255);
		///////////////////////////////////////////////////////////////
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_UNDER));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_UNDER));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_UNDER));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_REAR));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_REAR));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_REAR));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEAR));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_LEAR));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_LEAR));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_NECK));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_NECK));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_NECK));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RFINGER));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_RFINGER));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RFINGER));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LFINGER));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_LFINGER));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_LFINGER));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HEAD));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_HEAD));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_HEAD));
		writeD(0);
		if (_airShipHelm == 0)
		{
			writeH(18);
			writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
			writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_RHAND));
			writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND));
			writeD(0);
			writeH(18);
			writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LHAND));
			writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_LHAND));
			writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_LHAND));
			writeD(0);
		}
		else
		{
			writeH(18);
			writeD(_airShipHelm);
			writeD(0);
			writeD(0);
			writeD(0);
			writeH(18);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
		}
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_GLOVES));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_GLOVES));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_GLOVES));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_CHEST));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_CHEST));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_CHEST));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEGS));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_LEGS));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_LEGS));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_FEET));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_FEET));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_FEET));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_CLOAK));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_CLOAK));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_CLOAK));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_RHAND));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HAIR));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_HAIR));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_HAIR));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HAIR2));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_HAIR2));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_HAIR2));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RBRACELET));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_RBRACELET));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RBRACELET));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LBRACELET));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_LBRACELET));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_LBRACELET));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_DECO1));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_DECO1));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_DECO1));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_DECO2));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_DECO2));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_DECO2));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_DECO3));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_DECO3));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_DECO3));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_DECO4));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_DECO4));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_DECO4));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_DECO5));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_DECO5));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_DECO5));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_DECO6));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_DECO6));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_DECO6));
		writeD(0);
		writeH(18);
		writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_BELT));
		writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_BELT));
		writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_BELT));
		writeD(0);
		///////////////////////////////////////////////////////////////
		writeH(18); // 603-1
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeH(18); // 603-2
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeH(18); // 603-3
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeH(18); // 603-4
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeH(18); // 603-5
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeH(18); // 603-6
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeH(18); // 603-7
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
	}
}
