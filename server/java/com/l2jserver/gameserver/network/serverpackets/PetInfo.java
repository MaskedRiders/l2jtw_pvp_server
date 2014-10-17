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

import com.l2jserver.gameserver.model.actor.L2Summon;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance; // 603
import com.l2jserver.gameserver.model.actor.instance.L2PetInstance;
import com.l2jserver.gameserver.model.actor.instance.L2ServitorInstance;
import com.l2jserver.gameserver.model.PcCondOverride; // 603
import com.l2jserver.gameserver.model.zone.ZoneId;

public class PetInfo extends L2GameServerPacket
{
	private final L2Summon _summon;
	private final int _x, _y, _z, _heading;
	private final boolean _isSummoned;
	private final int _val;
	private final int _mAtkSpd, _pAtkSpd;
	private final int _runSpd, _walkSpd;
	private final int _swimRunSpd, _swimWalkSpd;
	private final int _flyRunSpd, _flyWalkSpd;
	private final double _moveMultiplier;
	private final int _maxHp, _maxMp;
	private int _maxFed, _curFed;
	
	public PetInfo(L2Summon summon, int val)
	{
		_summon = summon;
		_isSummoned = summon.isShowSummonAnimation();
		_x = summon.getX();
		_y = summon.getY();
		_z = summon.getZ();
		_heading = summon.getHeading();
		_mAtkSpd = summon.getMAtkSpd();
		_pAtkSpd = summon.getPAtkSpd();
		_moveMultiplier = summon.getMovementSpeedMultiplier();
		_runSpd = (int) Math.round(summon.getRunSpeed() / _moveMultiplier);
		_walkSpd = (int) Math.round(summon.getWalkSpeed() / _moveMultiplier);
		_swimRunSpd = (int) Math.round(summon.getSwimRunSpeed() / _moveMultiplier);
		_swimWalkSpd = (int) Math.round(summon.getSwimWalkSpeed() / _moveMultiplier);
		_flyRunSpd = summon.isFlying() ? _runSpd : 0;
		_flyWalkSpd = summon.isFlying() ? _walkSpd : 0;
		_maxHp = summon.getMaxHp();
		_maxMp = summon.getMaxMp();
		_val = val;
		if (summon.isPet())
		{
			final L2PetInstance pet = (L2PetInstance) _summon;
			_curFed = pet.getCurrentFed(); // how fed it is
			_maxFed = pet.getMaxFed(); // max fed it can be
		}
		else if (summon.isServitor())
		{
			final L2ServitorInstance sum = (L2ServitorInstance) _summon;
			_curFed = sum.getLifeTimeRemaining();
			_maxFed = sum.getLifeTime();
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		// 603-Start
		boolean gmSeeInvis = false;
		if (_invisible)
		{
			final L2PcInstance activeChar = getClient().getActiveChar();
			if ((activeChar != null) && activeChar.canOverrideCond(PcCondOverride.SEE_ALL_PLAYERS))
			{
				gmSeeInvis = true;
			}
		}
		// 603-End
		writeC(0xb2);
		writeC(_summon.getSummonType()); // 603 // Summer=1 Pet=2
		writeD(_summon.getObjectId());
		writeD(_summon.getTemplate().getDisplayId() + 1000000);
		//603 writeD(0); // 1=attackable
		
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeD(_heading);
		//603 writeD(0);
		writeD(_mAtkSpd);
		writeD(_pAtkSpd);
		writeH(_runSpd); // 603
		writeH(_walkSpd); // 603
		writeH(_swimRunSpd); // 603
		writeH(_swimWalkSpd); // 603
		writeH(_flyRunSpd); // 603
		writeH(_flyWalkSpd); // 603
		writeH(_flyRunSpd); // 603
		writeH(_flyWalkSpd); // 603
		writeF(_moveMultiplier);
		writeF(_summon.getAttackSpeedMultiplier()); // attack speed multiplier
		writeF(_summon.getTemplate().getfCollisionRadius());
		writeF(_summon.getTemplate().getfCollisionHeight());
		writeD(_summon.getWeapon()); // right hand weapon
		writeD(_summon.getArmor()); // body armor
		writeD(0x00); // left hand weapon
		//603 writeC(_summon.getOwner() != null ? 1 : 0); // when pet is dead and player exit game, pet doesn't show master name
		//603 writeC(_summon.isRunning() ? 1 : 0); // running=1 (it is always 1, walking mode is calculated from multiplier)
		//603 writeC(_summon.isInCombat() ? 1 : 0); // attacking 1=true
		//603 writeC(_summon.isAlikeDead() ? 1 : 0); // dead 1=true
		writeC(_isSummoned ? 2 : _val); // 0=teleported 1=default 2=summoned
		writeD(-1); // High Five NPCString ID
		if (_summon.isPet())
		{
			writeS(_summon.getName()); // Pet name.
		}
		else
		{
			writeS(_summon.getTemplate().isUsingServerSideName() ? _summon.getName() : ""); // Summon name.
		}
		writeD(-1); // High Five NPCString ID
		writeS(_summon.getTitle()); // owner name
		//603 writeD(1);
		writeC(_summon.getPvpFlag()); // 603 // 0 = white,2= purpleblink, if its greater then karma = purple
		int Karma = 0 - _summon.getKarma(); // 603-Test
		writeD(Karma); // karma // 603-Test
		writeD(_curFed); // how fed it is
		writeD(_maxFed); // max fed it can be
		writeD((int) _summon.getCurrentHp());// current hp
		writeD(_maxHp);// max hp
		writeD((int) _summon.getCurrentMp());// current mp
		writeD(_maxMp);// max mp
		writeQ(_summon.getStat().getSp()); // 603 // sp
		writeC(_summon.getLevel()); // 603// lvl
		writeQ(_summon.getStat().getExp());
		
		if (_summon.getExpForThisLevel() > _summon.getStat().getExp())
		{
			writeQ(_summon.getStat().getExp());// 0% absolute value
		}
		else
		{
			writeQ(_summon.getExpForThisLevel());// 0% absolute value
		}
		
		writeQ(_summon.getExpForNextLevel());// 100% absoulte value
		writeD(_summon.isPet() ? _summon.getInventory().getTotalWeight() : 0);// weight
		writeD(_summon.getMaxLoad());// max weight it can carry
		writeD(_summon.getPAtk(null));// patk
		writeD(_summon.getPDef(null));// pdef
		//603 writeD(_summon.getMAtk(null, null));// matk
		//603 writeD(_summon.getMDef(null, null));// mdef
		writeD(_summon.getAccuracy());// accuracy
		writeD(_summon.getEvasionRate(null));// evasion
		writeD(_summon.getCriticalHit(null, null));// critical
		writeD(_summon.getMAtk(null, null)); // Magic-Atk // 603
		writeD(_summon.getMDef(null, null)); // Magic-Def // 603
		writeD(_summon.getAccuracy()); // Magic-Accuracy // 603
		writeD(_summon.getEvasionRate(null)); // Magic-EvasionRate // 603
		writeD(_summon.getCriticalHit(null, null)); // Magic-CriticalHit // 603
		writeD((int) _summon.getMoveSpeed());// speed
		writeD(_summon.getPAtkSpd());// atkspeed
		writeD(_summon.getMAtkSpd());// casting speed
		
		//603 writeD(_summon.getAbnormalVisualEffects());// c2 abnormal visual effect... bleed=1; poison=2; poison & bleed=3; flame=4;
		writeC(0/* 603 _summon.isMountable() ? 1 : 0*/); // 603 // c2 ride button
		
		writeC(_summon.isInsideZone(ZoneId.WATER) ? 1 : _summon.isFlying() ? 2 : 0); // c2
		
		// Following all added in C4.
		//603 writeH(0); // ??
		//603 writeC(_summon.getTeam().getId());
		writeC(_summon.getSoulShotsPerHit()); // 603 // How many soulshots this servitor uses per hit
		writeC(_summon.getSpiritShotsPerHit()); // 603 // How many spiritshots this servitor uses per hit
		writeD(_summon.getFormId());// CT1.5 Pet form and skills
		writeD(0); // 603-displayEffect transform ID
		writeC(0); // 603 // 603 used summon point
		writeC(0); // 603 // 603 max summon point
		java.util.List<Integer> el = _summon.getEffectIdList();
		if (gmSeeInvis && !el.contains(21))
			el.add(21);
		writeH(el.size()); // 603
		for(int i : el)
		{
		   writeH(i); // 603
		}
		writeC(_summon.isMountable() ? 38 : 6); // 603 // c2 ride button
	}
}
