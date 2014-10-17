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
import com.l2jserver.gameserver.datatables.NpcData;
import com.l2jserver.gameserver.instancemanager.CursedWeaponsManager;
import com.l2jserver.gameserver.model.PcCondOverride;
import com.l2jserver.gameserver.model.actor.L2Decoy;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.model.skills.AbnormalVisualEffect;
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.datatables.MessageTable;

public class CharInfo extends L2GameServerPacket
{
	private final L2PcInstance _activeChar;
	private int _objId;
	private int _x, _y, _z, _heading;
	private final int _mAtkSpd, _pAtkSpd;
	
	private final int _runSpd, _walkSpd;
	private final int _swimRunSpd;
	private final int _swimWalkSpd;
	private final int _flyRunSpd;
	private final int _flyWalkSpd;
	private final double _moveMultiplier;
	private final float _attackSpeedMultiplier;
	
	private int _vehicleId = 0;
	
	private static final int[] PAPERDOLL_ORDER = new int[]
	{
		Inventory.PAPERDOLL_UNDER,
		Inventory.PAPERDOLL_HEAD,
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_LHAND,
		Inventory.PAPERDOLL_GLOVES,
		Inventory.PAPERDOLL_CHEST,
		Inventory.PAPERDOLL_LEGS,
		Inventory.PAPERDOLL_FEET,
		Inventory.PAPERDOLL_CLOAK,
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_HAIR,
		Inventory.PAPERDOLL_HAIR2,
		//603 Inventory.PAPERDOLL_RBRACELET,
		//603 Inventory.PAPERDOLL_LBRACELET,
		//603 Inventory.PAPERDOLL_DECO1,
		//603 Inventory.PAPERDOLL_DECO2,
		//603 Inventory.PAPERDOLL_DECO3,
		//603 Inventory.PAPERDOLL_DECO4,
		//603 Inventory.PAPERDOLL_DECO5,
		//603 Inventory.PAPERDOLL_DECO6,
		//603 Inventory.PAPERDOLL_BELT
	};
	
	public CharInfo(L2PcInstance cha)
	{
		_activeChar = cha;
		_objId = cha.getObjectId();
		if ((_activeChar.getVehicle() != null) && (_activeChar.getInVehiclePosition() != null))
		{
			_x = _activeChar.getInVehiclePosition().getX();
			_y = _activeChar.getInVehiclePosition().getY();
			_z = _activeChar.getInVehiclePosition().getZ();
			_vehicleId = _activeChar.getVehicle().getObjectId();
		}
		else
		{
			_x = _activeChar.getX();
			_y = _activeChar.getY();
			_z = _activeChar.getZ();
		}
		_heading = _activeChar.getHeading();
		_mAtkSpd = _activeChar.getMAtkSpd();
		_pAtkSpd = _activeChar.getPAtkSpd();
		_attackSpeedMultiplier = _activeChar.getAttackSpeedMultiplier();
		_invisible = cha.isInvisible();
		
		_moveMultiplier = cha.getMovementSpeedMultiplier();
		_runSpd = (int) Math.round(cha.getRunSpeed() / _moveMultiplier);
		_walkSpd = (int) Math.round(cha.getWalkSpeed() / _moveMultiplier);
		_swimRunSpd = (int) Math.round(cha.getSwimRunSpeed() / _moveMultiplier);
		_swimWalkSpd = (int) Math.round(cha.getSwimWalkSpeed() / _moveMultiplier);
		_flyRunSpd = cha.isFlying() ? _runSpd : 0;
		_flyWalkSpd = cha.isFlying() ? _walkSpd : 0;
	}
	
	public CharInfo(L2Decoy decoy)
	{
		this(decoy.getActingPlayer()); // init
		_objId = decoy.getObjectId();
		_x = decoy.getX();
		_y = decoy.getY();
		_z = decoy.getZ();
		_heading = decoy.getHeading();
	}
	
	@Override
	protected final void writeImpl()
	{
		boolean gmSeeInvis = false;
		
		if (_invisible)
		{
			final L2PcInstance activeChar = getClient().getActiveChar();
			if ((activeChar != null) && activeChar.canOverrideCond(PcCondOverride.SEE_ALL_PLAYERS))
			{
				gmSeeInvis = true;
			}
		}
		
		final L2NpcTemplate template = _activeChar.getPoly().isMorphed() ? NpcData.getInstance().getTemplate(_activeChar.getPoly().getPolyId()) : null;
		if (template != null)
		{
			writeC(0x0C);
			writeD(_objId);
			writeC(0x00);
			writeC(0x25);
			writeC(0x00);
			writeC(0xED);
			if (template.getRHandId() > 0 || template.getChestId() > 0 || template.getLHandId() > 0)
				writeC(0xFE);
			else
				writeC(0xBE);
			writeC(0x4E);
			writeC(0xA2);
			writeC(0x0C);
			int len_poly_title = 0;
			if (_activeChar.getAppearance().getVisibleTitle() != null)
				len_poly_title = _activeChar.getAppearance().getVisibleTitle().length();
			writeC(7 + len_poly_title*2);
			writeC(_activeChar.getKarma() < 0 ? 1 : 0);
			writeH(0);
			writeH(0);
			writeS(_activeChar.getAppearance().getVisibleTitle());
			if (template.getRHandId() > 0 || template.getChestId() > 0 || template.getLHandId() > 0)
				writeH(68);
			else
				writeH(56);
			writeD(template.getId() + 1000000); // npctype id
			writeD(_x);
			writeD(_y);
			writeD(_z);
			writeD(_heading);
			writeD(_mAtkSpd);
			writeD(_pAtkSpd);
			putFloat((float)_moveMultiplier);
			putFloat(_attackSpeedMultiplier);
			if (template.getRHandId() > 0 || template.getChestId() > 0 || template.getLHandId() > 0)
			{
				writeD(template.getRHandId()); // right hand weapon
				writeD(template.getChestId()); // chest
				writeD(template.getLHandId()); // left hand weapon
			}
			writeC(1);
			writeC(_activeChar.isRunning() ? 1 : 0);
			writeC(_activeChar.isInsideZone(ZoneId.WATER) ? 1 : _activeChar.isFlying() ? 2 : 0); // C2
			writeD(_activeChar.isFlying() ? 1 : 0);
			writeC(0);
			writeC(0);
			writeH(0);
			writeD((int)_activeChar.getCurrentHp());
			writeD(_activeChar.getMaxHp());
			
			writeC(
			(_activeChar.isInCombat() ? 1 : 0) + 
			(_activeChar.isAlikeDead() ? 2 : 0) +
			(template.isTargetable() ? 4 : 0) + 
			(template.isShowName() ? 8 : 0));
			
			java.util.List<Integer> el = _activeChar.getEffectIdList();
			writeH(el.size());
			for(int i : el)
			{
				writeH(i);
			}
		}
		else
		{
			writeC(0x31);
			writeD(_x);
			writeD(_y);
			writeD(_z);
			writeD(_vehicleId);
			writeD(_objId);
			writeS(_activeChar.getAppearance().getVisibleName());
			writeH(_activeChar.getRace().ordinal()); // 603
			writeC(_activeChar.getAppearance().getSex() ? 1 : 0); // 603
			writeD(_activeChar.getBaseClass());
			
			for (int slot : getPaperdollOrder())
			{
				writeD(_activeChar.getInventory().getPaperdollItemDisplayId(slot));
			}
			
			//603 for (int slot : getPaperdollOrder())
			//603 {
			//603 	writeD(_activeChar.getInventory().getPaperdollAugmentationId(slot));
			//603 }
			writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND));
			writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_LHAND));
			writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND));
			
			writeC(_activeChar.getInventory().getTalismanSlots());
			//603 writeD(_activeChar.getInventory().canEquipCloak() ? 1 : 0);
			writeD(0); // 603
			writeD(0); // 603
			writeD(0); // 603
			writeD(0); // 603
			writeD(0); // 603
			writeD(0); // 603
			writeD(0); // 603
			writeD(0); // 603
			writeD(0); // 603
			
			writeC(_activeChar.getPvpFlag()); // 603
			int Karma = 0 - _activeChar.getKarma(); // 603-Test
			writeD(Karma); // 603-Test
			
			writeD(_mAtkSpd);
			writeD(_pAtkSpd);
			
			//603 writeD(0x00); // ?
			
			writeH(_runSpd); // 603
			writeH(_walkSpd); // 603
			writeH(_swimRunSpd); // 603
			writeH(_swimWalkSpd); // 603
			writeH(_flyRunSpd); // 603
			writeH(_flyWalkSpd); // 603
			writeH(_flyRunSpd); // 603
			writeH(_flyWalkSpd); // 603
			writeF(_moveMultiplier);
			writeF(_activeChar.getAttackSpeedMultiplier());
			
			writeF(_activeChar.getCollisionRadius());
			writeF(_activeChar.getCollisionHeight());
			
			writeD(_activeChar.getAppearance().getHairStyle());
			writeD(_activeChar.getAppearance().getHairColor());
			writeD(_activeChar.getAppearance().getFace());
			
			/* MessageTable
			writeS(gmSeeInvis ? "Invisible" : _activeChar.getAppearance().getVisibleTitle());
			 */
			writeS(gmSeeInvis ? MessageTable.Messages[214].getMessage() : _activeChar.getAppearance().getVisibleTitle());
			
			if (!_activeChar.isCursedWeaponEquipped())
			{
				writeD(_activeChar.getClanId());
				writeD(_activeChar.getClanCrestId());
				writeD(_activeChar.getAllyId());
				writeD(_activeChar.getAllyCrestId());
			}
			else
			{
				writeD(0x00);
				writeD(0x00);
				writeD(0x00);
				writeD(0x00);
			}
			
			writeC(_activeChar.isSitting() ? 0 : 1); // standing = 1 sitting = 0
			writeC(_activeChar.isRunning() ? 1 : 0); // running = 1 walking = 0
			writeC(_activeChar.isInCombat() ? 1 : 0);
			
			writeC(!_activeChar.isInOlympiadMode() && _activeChar.isAlikeDead() ? 1 : 0);
			
			writeC(!gmSeeInvis && _invisible ? 1 : 0); // invisible = 1 visible =0
			
			writeC(_activeChar.getMountType().ordinal()); // 1-on Strider, 2-on Wyvern, 3-on Great Wolf, 0-no mount
			writeC(_activeChar.getPrivateStoreType().getId());
			
			writeH(_activeChar.getCubics().size());
			for (int cubicId : _activeChar.getCubics().keySet())
			{
				writeH(cubicId);
			}
			
			writeC(_activeChar.isInPartyMatchRoom() ? 1 : 0);
			
			//603 writeD(gmSeeInvis ? (_activeChar.getAbnormalVisualEffects() | AbnormalVisualEffect.STEALTH.getMask()) : _activeChar.getAbnormalVisualEffects());
			
			writeC(_activeChar.isInsideZone(ZoneId.WATER) ? 1 : _activeChar.isFlyingMounted() ? 2 : 0);
			
			writeH(_activeChar.getRecomHave()); // Blue value for name (0 = white, 255 = pure blue)
			writeD(_activeChar.getMountNpcId() > 0 ? _activeChar.getMountNpcId() + 1000000 : 0); // 603
			writeD(_activeChar.getClassId().getId());
			writeD(0x00); // ?
			writeC(_activeChar.isMounted() ? 0 : _activeChar.getEnchantEffect());
			
			writeC(_activeChar.getTeam().getId());
			
			writeD(_activeChar.getClanCrestLargeId());
			writeC(_activeChar.isNoble() ? 1 : 0); // Symbol on char menu ctrl+I
			writeC(_activeChar.isHero() || (_activeChar.isGM() && Config.GM_HERO_AURA) ? 1 : 0); // Hero Aura
			
			writeC(_activeChar.isFishing() ? 1 : 0); // 0x01: Fishing Mode (Cant be undone by setting back to 0)
			writeD(_activeChar.getFishx());
			writeD(_activeChar.getFishy());
			writeD(_activeChar.getFishz());
			
			writeD(_activeChar.getAppearance().getNameColor());
			
			writeD(_heading);
			
			writeC(_activeChar.getPledgeClass()); // 603
			writeH(_activeChar.getPledgeType()); // 603
			
			writeD(_activeChar.getAppearance().getTitleColor());
			
			writeC(_activeChar.isCursedWeaponEquipped() ? CursedWeaponsManager.getInstance().getLevel(_activeChar.getCursedWeaponEquippedId()) : 0); // 603
			
			writeD(_activeChar.getClanId() > 0 ? _activeChar.getClan().getReputationScore() : 0);
			
			// T1
			writeD(_activeChar.getTransformationDisplayId());
			writeD(_activeChar.getAgathionId());
			
			// T2
			writeC(0x01); // 603
			
			// T2.3
			//603 writeD(_activeChar.getAbnormalVisualEffectSpecial());
			writeD((int) _activeChar.getCurrentCp()); // 603
			writeD(_activeChar.getMaxHp()); // 603
			writeD((int) _activeChar.getCurrentHp()); // 603
			writeD(_activeChar.getMaxMp()); // 603
			writeD((int) _activeChar.getCurrentMp()); // 603
			writeC(0); // 603
			java.util.List<Integer> el = _activeChar.getEffectIdList();
			if (gmSeeInvis && !el.contains(21))
				el.add(21);
			writeD(el.size());
			for(int i : el)
			{
			   writeH(i); // 603
			}
			writeC(0); // 603
			writeC(1); // 603
			writeC(0); // 603 : GS-comment-034
		}
	}
	
	@Override
	protected int[] getPaperdollOrder()
	{
		return PAPERDOLL_ORDER;
	}
}
