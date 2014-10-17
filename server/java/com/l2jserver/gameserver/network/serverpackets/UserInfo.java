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

import com.l2jserver.Config;
import com.l2jserver.gameserver.datatables.ExperienceTable;
import com.l2jserver.gameserver.datatables.NpcData;
import com.l2jserver.gameserver.instancemanager.CursedWeaponsManager;
import com.l2jserver.gameserver.instancemanager.TerritoryWarManager;
import com.l2jserver.gameserver.model.Elementals;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.model.skills.AbnormalVisualEffect;
import com.l2jserver.gameserver.model.items.L2Weapon; // 603
import com.l2jserver.gameserver.model.items.type.WeaponType; // 603
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.datatables.MessageTable;

public final class UserInfo extends L2GameServerPacket
{
	private final L2PcInstance _activeChar;
	private int _relation;
	private int _airShipHelm;
	private int data_name_Err = 0; // 603-TEST
	private byte[] data_name; // 603-TEST
	private int data_title_Err = 0; // 603-TEST
	private byte[] data_title; // 603-TEST
	
	private final int _runSpd, _walkSpd;
	private final int _swimRunSpd, _swimWalkSpd;
	private final int _flyRunSpd, _flyWalkSpd;
	private final double _moveMultiplier;
	private long _user_userinfo[] = new long[87]; // 603 : GS-comment-051
	private String _user_username[] = new String[2]; // 603 : GS-comment-051
	private double _user_userspeed[] = new double[4]; // 603 : GS-comment-051
	
	public UserInfo(L2PcInstance cha)
	{
		_activeChar = cha;
		
		int _territoryId = TerritoryWarManager.getInstance().getRegisteredTerritoryId(cha);
		_relation = _activeChar.isClanLeader() ? 0x40 : 0;
		if (_activeChar.getSiegeState() == 1)
		{
			if (_territoryId == 0)
			{
				_relation |= 0x180;
			}
			else
			{
				_relation |= 0x1000;
			}
		}
		if (_activeChar.getSiegeState() == 2)
		{
			_relation |= 0x80;
		}
		// _isDisguised = TerritoryWarManager.getInstance().isDisguised(character.getObjectId());
		if (_activeChar.isInAirShip() && _activeChar.getAirShip().isCaptain(_activeChar))
		{
			_airShipHelm = _activeChar.getAirShip().getHelmItemId();
		}
		else
		{
			_airShipHelm = 0;
		}
		
		_moveMultiplier = cha.getMovementSpeedMultiplier();
		_runSpd = (int) Math.round(cha.getRunSpeed() / _moveMultiplier);
		_walkSpd = (int) Math.round(cha.getWalkSpeed() / _moveMultiplier);
		_swimRunSpd = (int) Math.round(cha.getSwimRunSpeed() / _moveMultiplier);
		_swimWalkSpd = (int) Math.round(cha.getSwimWalkSpeed() / _moveMultiplier);
		_flyRunSpd = cha.isFlying() ? _runSpd : 0;
		_flyWalkSpd = cha.isFlying() ? _walkSpd : 0;
		_activeChar.sendPacket(new ExSubjobInfo(_activeChar)); // 603
	}
	
	@Override
	protected final void writeImpl()
	{
		String user_name = _activeChar.getAppearance().getVisibleName();
		int len_user_name = _activeChar.getAppearance().getVisibleName().length();
		if (len_user_name > 0)
		{
			try
			{
				data_name = user_name.getBytes("UTF-16LE");
			}
			catch (Exception e)
			{
				data_name_Err = 2;
			}
		}
		
		String title = _activeChar.getTitle();
		if (_activeChar.isInvisible())
		{
			title = MessageTable.Messages[228].getMessage();
		}
		if (_activeChar.getPoly().isMorphed())
		{
			final L2NpcTemplate polyObj = NpcData.getInstance().getTemplate(_activeChar.getPoly().getPolyId());
			if (polyObj != null)
			{
				title += " (" + _activeChar.getPoly().getPolyId() + " " + polyObj.getName() + ")";
			}
		}
		
		int len_user_title = title.length();
		if (len_user_title > 0)
		{
			try
			{
				data_title = title.getBytes("UTF-16LE");
			}
			catch (Exception e)
			{
				data_title_Err = 2;
			}
		}
		
		// 603 : GS-comment-051 start
		_user_username[0] = _activeChar.getAppearance().getVisibleName();
		_user_username[1] = title;
		_user_userinfo[0] = System.currentTimeMillis();
		_user_userinfo[1] = (long) _relation;
		_user_userinfo[2] = (long) (_activeChar.isGM() ? 1 : 0);
		_user_userinfo[3] = (long) _activeChar.getRace().ordinal();
		_user_userinfo[4] = (long) (_activeChar.getAppearance().getSex() ? 1 : 0);
		_user_userinfo[5] = (long) _activeChar.getBaseClass();
		_user_userinfo[6] = (long) _activeChar.getClassId().getId();
		_user_userinfo[7] = (long) _activeChar.getLevel();
		_user_userinfo[8] = (long) _activeChar.getSTR();
		_user_userinfo[9] = (long) _activeChar.getDEX();
		_user_userinfo[10] = (long) _activeChar.getCON();
		_user_userinfo[11] = (long) _activeChar.getINT();
		_user_userinfo[12] = (long) _activeChar.getWIT();
		_user_userinfo[13] = (long) _activeChar.getMEN();
		_user_userinfo[14] = (long) _activeChar.getMaxHp();
		_user_userinfo[15] = (long) _activeChar.getMaxMp();
		_user_userinfo[16] = (long) _activeChar.getMaxCp();
		_user_userinfo[17] = (long) Math.round(_activeChar.getCurrentHp());
		_user_userinfo[18] = (long) Math.round(_activeChar.getCurrentMp());
		_user_userinfo[19] = (long) _activeChar.getCurrentCp();
		_user_userinfo[20] = (long) _activeChar.getSp();
		_user_userinfo[21] = (long) _activeChar.getExp();
		_user_userinfo[22] = (long) (_activeChar.isMounted() || (_airShipHelm != 0) ? 0 : _activeChar.getEnchantEffect());
		_user_userinfo[23] = (long) _activeChar.getAppearance().getHairStyle();
		_user_userinfo[24] = (long) _activeChar.getAppearance().getHairColor();
		_user_userinfo[25] = (long) _activeChar.getAppearance().getFace();
		_user_userinfo[26] = (long) _activeChar.getPrivateStoreType().getId();
		_user_userinfo[27] = (long) (_activeChar.hasDwarvenCraft() || _activeChar.isAwaken()? 1 : 0);
		if (_activeChar.getActiveWeaponItem() != null)
		{
			L2Weapon weaponItem = _activeChar.getActiveWeaponItem();
			if (weaponItem.getItemType() == WeaponType.POLE)
				_user_userinfo[28] = 80;
			else if (weaponItem.getItemType() == WeaponType.BOW)
				_user_userinfo[28] = 500;
			else if (weaponItem.getItemType() == WeaponType.CROSSBOW)
				_user_userinfo[28] = 400;
			else
				_user_userinfo[28] = 40;
		}
		else
			_user_userinfo[28] = 0;
		_user_userinfo[29] = (long) _activeChar.getPAtk(null);
		_user_userinfo[30] = (long) _activeChar.getPAtkSpd();
		_user_userinfo[31] = (long) _activeChar.getPDef(null);
		_user_userinfo[32] = (long) _activeChar.getEvasionRate(null);
		_user_userinfo[33] = (long) _activeChar.getAccuracy();
		_user_userinfo[34] = (long) _activeChar.getCriticalHit(null, null);
		_user_userinfo[35] = (long) _activeChar.getMAtk(null, null);
		_user_userinfo[36] = (long) _activeChar.getMAtkSpd();
		_user_userinfo[37] = (long) _activeChar.getMDef(null, null);
		_user_userinfo[38] = (long) _activeChar.getMCriticalHit(null, null);
		_user_userinfo[39] = (long) _activeChar.getDefenseElementValue(Elementals.FIRE);
		_user_userinfo[40] = (long) _activeChar.getDefenseElementValue(Elementals.WATER);
		_user_userinfo[41] = (long) _activeChar.getDefenseElementValue(Elementals.WIND);
		_user_userinfo[42] = (long) _activeChar.getDefenseElementValue(Elementals.EARTH);
		_user_userinfo[43] = (long) _activeChar.getDefenseElementValue(Elementals.HOLY);
		_user_userinfo[44] = (long) _activeChar.getDefenseElementValue(Elementals.DARK);
		_user_userinfo[45] = (long) _activeChar.getX();
		_user_userinfo[46] = (long) _activeChar.getY();
		_user_userinfo[47] = (long) _activeChar.getZ();
		_user_userinfo[48] = (long) (_activeChar.getVehicle() != null ? _activeChar.getVehicle().getObjectId() : 0);
		_user_userinfo[49] = (long) _runSpd;
		_user_userinfo[50] = (long) _walkSpd;
		_user_userinfo[51] = (long) _swimRunSpd;
		_user_userinfo[52] = (long) _swimWalkSpd;
		_user_userinfo[53] = (long) _flyRunSpd;
		_user_userinfo[54] = (long) _flyWalkSpd;
		_user_userinfo[55] = (long) _flyRunSpd;
		_user_userinfo[56] = (long) _flyWalkSpd;
		_user_userspeed[0] = _moveMultiplier;
		_user_userspeed[1] = _activeChar.getAttackSpeedMultiplier();
		_user_userspeed[2] = _activeChar.getCollisionRadius();
		_user_userspeed[3] = _activeChar.getCollisionHeight();
		_user_userinfo[57] = (long) _activeChar.getAttackElement();
		byte _attackAttribute = _activeChar.getAttackElement();
		_user_userinfo[58] = (long) _activeChar.getAttackElementValue(_attackAttribute);
		_user_userinfo[59] = (long) _activeChar.getPledgeType();
		_user_userinfo[60] = (long) _activeChar.getClanId();
		_user_userinfo[61] = (long) _activeChar.getClanCrestLargeId();
		_user_userinfo[62] = (long) _activeChar.getClanCrestId();
		_user_userinfo[63] = (long) _activeChar.getClanPrivileges().getBitmask();
		_user_userinfo[64] = (long) (_activeChar.isClanLeader() ? 1 : 0);
		_user_userinfo[65] = (long) _activeChar.getAllyId();
		_user_userinfo[66] = (long) _activeChar.getAllyCrestId();
		_user_userinfo[67] = (long) (_activeChar.isInPartyMatchRoom() ? 1 : 0);
		_user_userinfo[68] = (long) _activeChar.getPvpFlag();
		_user_userinfo[69] = (long) _activeChar.getKarma();
		_user_userinfo[70] = (long) (_activeChar.isNoble() ? 1 : 0); 
		_user_userinfo[71] = (long) (_activeChar.isHero() || (_activeChar.isGM() && Config.GM_HERO_AURA) ? 1 : 0);
		_user_userinfo[72] = (long) _activeChar.getPledgeClass();
		_user_userinfo[73] = (long) _activeChar.getPkKills();
		_user_userinfo[74] = (long) _activeChar.getPvpKills();
		_user_userinfo[75] = (long) _activeChar.getRecomLeft();
		_user_userinfo[76] = (long) _activeChar.getRecomHave();
		_user_userinfo[77] = (long) _activeChar.getVitalityPoints();
		_user_userinfo[78] = (long) _activeChar.getFame();
		_user_userinfo[79] = (long) _activeChar.getInventory().getTalismanSlots();
		_user_userinfo[80] = (long) _activeChar.getTeam().getId();
		_user_userinfo[81] = (long) (_activeChar.isInsideZone(ZoneId.WATER) ? 1 : _activeChar.isFlyingMounted() ? 2 : 0);
		_user_userinfo[82] = (long) (_activeChar.isRunning() ? 0x01 : 0x00);
		_user_userinfo[83] = (long) _activeChar.getAppearance().getNameColor();
		_user_userinfo[84] = (long) _activeChar.getAppearance().getTitleColor();
		_user_userinfo[85] = (long) _activeChar.getInventoryLimit();
		_user_userinfo[86] = (long) (_activeChar.isCursedWeaponEquipped() ? CursedWeaponsManager.getInstance().getLevel(_activeChar.getCursedWeaponEquippedId()) : 0);
		
		if ((System.currentTimeMillis() - _activeChar.getUserEquipSlot(0)) > 60000)
		{
			for (int i = 0; i < 87; i++)
			{
				_activeChar.setUserUserInfo(i, _user_userinfo[i]);
			}
			for (int n = 0; n < 2; n++)
			{
				_activeChar.setUserUserName(n, _user_username[n]);
			}
			for (int d = 0; d < 4; d++)
			{
				_activeChar.setUserUserSpeed(d, _user_userspeed[d]);
			}
		}
		else
		{
			int _needUpdate = 0;
			for (int i = 1; i < 87; i++)
			{
				if (_user_userinfo[i] != _activeChar.getUserUserInfo(i))
				{
					_needUpdate = 1;
				}
			}
			for (int n = 0; n < 2; n++)
			{
				if (_user_username[n] != _activeChar.getUserUserName(n))
				{
					_needUpdate = 1;
				}
			}
			for (int d = 0; d < 4; d++)
			{
				if (_user_userspeed[d] != _activeChar.getUserUserSpeed(d))
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
				for (int i = 0; i < 87; i++)
				{
					_activeChar.setUserUserInfo(i, _user_userinfo[i]);
				}
				for (int n = 0; n < 2; n++)
				{
					_activeChar.setUserUserName(n, _user_username[n]);
				}
				for (int d = 0; d < 4; d++)
				{
					_activeChar.setUserUserSpeed(d, _user_userspeed[d]);
				}
			}
		}
		// 603 : GS-comment-051 end
		writeC(0x32);
		writeD(_activeChar.getObjectId());
		writeD(377 + len_user_name*2 + data_name_Err + len_user_title*2 + data_title_Err);
		writeH(23);
		writeH(65535);
		writeC(254);
		// 0x40 leader rights
		// siege flags: attacker - 0x180 sword over name, defender - 0x80 shield, 0xC0 crown (|leader), 0x1C0 flag (|leader)
		writeH(_relation); // 603
		writeH(0);
		
		/////////////////////////////////////////////////////////////////
		writeH(16 + len_user_name*2 + data_name_Err); // 603-1
		writeH(len_user_name + data_name_Err/2); // 603-1
		if (len_user_name > 0)
		{
			if (data_name_Err == 0)
				writeB(data_name);
			else
				writeS(_activeChar.getAppearance().getVisibleName());
		}
		writeC(_activeChar.isGM() ? 1 : 0);
		writeC(_activeChar.getRace().ordinal());
		writeC(_activeChar.getAppearance().getSex() ? 1 : 0);
		writeD(_activeChar.getBaseClass());
		writeD(_activeChar.getClassId().getId());
		writeC(_activeChar.getLevel());
		
		/////////////////////////////////////////////////////////////////
		writeH(18); // 603-2
		writeH(_activeChar.getSTR());
		writeH(_activeChar.getDEX());
		writeH(_activeChar.getCON());
		writeH(_activeChar.getINT());
		writeH(_activeChar.getWIT());
		writeH(_activeChar.getMEN());
		writeH(0); // 603-New-1-LUC
		writeH(0); // 603-New-2-CHA
		
		/////////////////////////////////////////////////////////////////
		writeH(14); // 603-3
		writeD(_activeChar.getMaxHp());
		writeD(_activeChar.getMaxMp());
		writeD(_activeChar.getMaxCp());
		
		/////////////////////////////////////////////////////////////////
		writeH(38); // 603-4
		writeD((int) Math.round(_activeChar.getCurrentHp()));
		writeD((int) Math.round(_activeChar.getCurrentMp()));
		writeD((int) _activeChar.getCurrentCp());
		writeQ(_activeChar.getSp());
		writeQ(_activeChar.getExp());
		writeF((float) (_activeChar.getExp() - ExperienceTable.getInstance().getExpForLevel(_activeChar.getLevel())) / (ExperienceTable.getInstance().getExpForLevel(_activeChar.getLevel() + 1) - ExperienceTable.getInstance().getExpForLevel(_activeChar.getLevel()))); // High Five exp %
		
		/////////////////////////////////////////////////////////////////
		writeH(4); // 603-5
		writeC(_activeChar.isMounted() || (_airShipHelm != 0) ? 0 : _activeChar.getEnchantEffect());
		writeC(0);
		
		/////////////////////////////////////////////////////////////////
		writeH(15); // 603-6
		writeD(_activeChar.getAppearance().getHairStyle());
		writeD(_activeChar.getAppearance().getHairColor());
		writeD(_activeChar.getAppearance().getFace());
		writeC(1);
		
		/////////////////////////////////////////////////////////////////
		writeH(6); // 603-7
		writeC(0);
		writeC(_activeChar.getPrivateStoreType().getId());
		writeC(_activeChar.hasDwarvenCraft() || _activeChar.isAwaken()? 1 : 0);
		writeC(0); // 603
		
		/////////////////////////////////////////////////////////////////
		writeH(56); // 603-8
		if (_activeChar.getActiveWeaponItem() != null)
		{
			L2Weapon weaponItem = _activeChar.getActiveWeaponItem();
			if (weaponItem.getItemType() == WeaponType.POLE)
				writeH(80);
			else if (weaponItem.getItemType() == WeaponType.BOW)
				writeH(500);
			else if (weaponItem.getItemType() == WeaponType.CROSSBOW)
				writeH(400);
			else
				writeH(40);
		}
		else
			writeH(0);
		writeD(_activeChar.getPAtk(null));
		writeD(_activeChar.getPAtkSpd());
		writeD(_activeChar.getPDef(null));
		writeD(_activeChar.getEvasionRate(null));
		writeD(_activeChar.getAccuracy());
		writeD(_activeChar.getCriticalHit(null, null));
		writeD(_activeChar.getMAtk(null, null));
		writeD(_activeChar.getMAtkSpd());
		writeD(_activeChar.getPAtkSpd());
		writeD(_activeChar.getEvasionRate(null)); //Magic
		writeD(_activeChar.getMDef(null, null));
		writeD(_activeChar.getAccuracy()); //Magic
		writeD(_activeChar.getMCriticalHit(null, null)); //Magic
		
		/////////////////////////////////////////////////////////////////
		writeH(14); // 603-9
		writeH(_activeChar.getDefenseElementValue(Elementals.FIRE));
		writeH(_activeChar.getDefenseElementValue(Elementals.WATER));
		writeH(_activeChar.getDefenseElementValue(Elementals.WIND));
		writeH(_activeChar.getDefenseElementValue(Elementals.EARTH));
		writeH(_activeChar.getDefenseElementValue(Elementals.HOLY));
		writeH(_activeChar.getDefenseElementValue(Elementals.DARK));
		
		/////////////////////////////////////////////////////////////////
		writeH(18); // 603-10
		writeD(_activeChar.getX());
		writeD(_activeChar.getY());
		writeD(_activeChar.getZ());
		writeD(_activeChar.getVehicle() != null ? _activeChar.getVehicle().getObjectId() : 0);
		
		/////////////////////////////////////////////////////////////////
		writeH(18); // 603-11
		writeH(_runSpd);
		writeH(_walkSpd);
		writeH(_swimRunSpd);
		writeH(_swimWalkSpd);
		writeH(_flyRunSpd);
		writeH(_flyWalkSpd);
		writeH(_flyRunSpd);
		writeH(_flyWalkSpd);
		
		/////////////////////////////////////////////////////////////////
		writeH(18); // 603-12
		writeF(_moveMultiplier);
		writeF(_activeChar.getAttackSpeedMultiplier());
		
		/////////////////////////////////////////////////////////////////
		writeH(18); // 603-13
		writeF(_activeChar.getCollisionRadius());
		writeF(_activeChar.getCollisionHeight());
		
		/////////////////////////////////////////////////////////////////
		writeH(5); // 603-14
		byte attackAttribute = _activeChar.getAttackElement();
		writeC(attackAttribute);
		writeH(_activeChar.getAttackElementValue(attackAttribute));
		
		/////////////////////////////////////////////////////////////////
		writeH(32 + len_user_title*2 + data_title_Err); // 603-15
		writeH(len_user_title + data_title_Err/2);
		if (len_user_title > 0)
		{
			if (data_title_Err == 0)
				writeB(data_title);
			else
				writeS(title);
		}
		writeH(_activeChar.getPledgeType());
		writeD(_activeChar.getClanId());
		writeD(_activeChar.getClanCrestLargeId());
		writeD(_activeChar.getClanCrestId());
		writeC(_activeChar.getClanPrivileges().getBitmask());
		writeH(0);
		writeC(0);
		writeC(_activeChar.isClanLeader() ? 1 : 0);
		writeD(_activeChar.getAllyId());
		writeD(_activeChar.getAllyCrestId());
		writeC(_activeChar.isInPartyMatchRoom() ? 1 : 0);
		
		/////////////////////////////////////////////////////////////////
		writeH(22); // 603-16
		writeC(_activeChar.getPvpFlag());
		int Karma = 0 - _activeChar.getKarma();
		writeD(Karma);
		writeC(_activeChar.isNoble() ? 1 : 0); 
		writeC(_activeChar.isHero() || (_activeChar.isGM() && Config.GM_HERO_AURA) ? 1 : 0);
		writeC(_activeChar.getPledgeClass());
		writeD(_activeChar.getPkKills());
		writeD(_activeChar.getPvpKills());
		writeH(_activeChar.getRecomLeft());
		writeH(_activeChar.getRecomHave());
		
		/////////////////////////////////////////////////////////////////
		writeH(15); // 603-17
		writeD(_activeChar.getVitalityPoints());
		writeC(0);
		writeD(_activeChar.getFame());
		writeD(0);
		
		/////////////////////////////////////////////////////////////////
		writeH(9); // 603-18
		writeC(_activeChar.getInventory().getTalismanSlots());
		writeC(0);
		writeC(_activeChar.getTeam().getId());
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
		
		/////////////////////////////////////////////////////////////////
		writeH(4); // 603-19
		writeC(_activeChar.isInsideZone(ZoneId.WATER) ? 1 : _activeChar.isFlyingMounted() ? 2 : 0);
		writeC(_activeChar.isRunning() ? 0x01 : 0x00);
		
		/////////////////////////////////////////////////////////////////
		writeH(10); // 603-20
		writeD(_activeChar.getAppearance().getNameColor());
		writeD(_activeChar.getAppearance().getTitleColor());
		
		/////////////////////////////////////////////////////////////////
		writeH(9); // 603-21
		writeD(0);
		writeH(_activeChar.getInventoryLimit());
		writeC(_activeChar.isCursedWeaponEquipped() ? CursedWeaponsManager.getInstance().getLevel(_activeChar.getCursedWeaponEquippedId()) : 0);
		
		/////////////////////////////////////////////////////////////////
		writeH(9); // 603-22
		writeD(1);
		writeH(0);
		writeC(0);
		
		/////////////////////////////////////////////////////////////////
		// in old UserInfo, but not in new UserInfo
		//writeD(_activeChar.getMountNpcId() > 0 ? _activeChar.getMountNpcId() + 1000000 : 0);
		//writeC(_activeChar.getMountType().ordinal()); // mount type// 1-on Strider, 2-on Wyvern, 3-on Great Wolf, 0-no mount
		//writeD(_activeChar.isMinimapAllowed() ? 1 : 0); // Minimap on Hellbound
		//writeC(_activeChar.getInventory().canEquipCloak() ? 1 : 0); // 603-not need
	}
	
	@Override
	public void runImpl() // 603
	{
		getClient().sendPacket(new ExUserInfoEquipSlot(getClient().getActiveChar()));
		getClient().sendPacket(new ExUserInfoCubic(getClient().getActiveChar()));
		getClient().sendPacket(new ExUserInfoAbnormalVisualEffect(getClient().getActiveChar()));
	}
}
