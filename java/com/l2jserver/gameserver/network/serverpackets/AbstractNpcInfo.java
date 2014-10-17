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
import com.l2jserver.gameserver.datatables.ClanTable;
import com.l2jserver.gameserver.instancemanager.TownManager;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.PcCondOverride;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.L2Summon;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2NpcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2TrapInstance;
import com.l2jserver.gameserver.model.skills.AbnormalVisualEffect;
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.datatables.MessageTable;

public abstract class AbstractNpcInfo extends L2GameServerPacket
{
	protected int _x, _y, _z, _heading;
	protected int _idTemplate;
	protected boolean _isAttackable, _isSummoned;
	protected int _mAtkSpd, _pAtkSpd;
	protected final int _runSpd, _walkSpd;
	protected final int _swimRunSpd, _swimWalkSpd;
	protected final int _flyRunSpd, _flyWalkSpd;
	protected double _moveMultiplier;
	
	protected int _rhand, _lhand, _chest, _enchantEffect;
	protected double _collisionHeight, _collisionRadius;
	protected String _name = "";
	protected String _title = "";
	
	public AbstractNpcInfo(L2Character cha)
	{
		_isSummoned = cha.isShowSummonAnimation();
		_x = cha.getX();
		_y = cha.getY();
		_z = cha.getZ();
		_heading = cha.getHeading();
		_mAtkSpd = cha.getMAtkSpd();
		_pAtkSpd = cha.getPAtkSpd();
		_moveMultiplier = cha.getMovementSpeedMultiplier();
		_runSpd = (int) Math.round(cha.getRunSpeed() / _moveMultiplier);
		_walkSpd = (int) Math.round(cha.getWalkSpeed() / _moveMultiplier);
		_swimRunSpd = (int) Math.round(cha.getSwimRunSpeed() / _moveMultiplier);
		_swimWalkSpd = (int) Math.round(cha.getSwimWalkSpeed() / _moveMultiplier);
		_flyRunSpd = cha.isFlying() ? _runSpd : 0;
		_flyWalkSpd = cha.isFlying() ? _walkSpd : 0;
	}
	
	/**
	 * Packet for Npcs
	 */
	public static class NpcInfo extends AbstractNpcInfo
	{
		private final L2Npc _npc;
		private int _clanCrest = 0;
		private int _allyCrest = 0;
		private int _allyId = 0;
		private int _clanId = 0;
		private int _displayEffect = 0;
		
		public NpcInfo(L2Npc cha, L2Character attacker)
		{
			super(cha);
			_npc = cha;
			_idTemplate = cha.getTemplate().getDisplayId(); // On every subclass
			_rhand = cha.getRightHandItem(); // On every subclass
			_lhand = cha.getLeftHandItem(); // On every subclass
			_enchantEffect = cha.getEnchantEffect();
			_collisionHeight = cha.getCollisionHeight();// On every subclass
			_collisionRadius = cha.getCollisionRadius();// On every subclass
			_isAttackable = cha.isAutoAttackable(attacker);
			if (cha.getTemplate().isUsingServerSideName())
			{
				_name = cha.getName();// On every subclass
			}
			
			if (_npc.isInvisible())
			{
				_title = "Invisible";
			}
			else if (Config.L2JMOD_CHAMPION_ENABLE && cha.isChampion())
			{
				/* MessageTable
				_title = (Config.L2JMOD_CHAMP_TITLE); // On every subclass
				 */
				_title = MessageTable.Messages[1926].getMessage(); // On every subclass
			}
			else if (cha.getTemplate().isUsingServerSideTitle())
			{
				_title = cha.getTemplate().getTitle(); // On every subclass
			}
			else
			{
				_title = cha.getTitle(); // On every subclass
			}
			
			if (Config.SHOW_NPC_LVL && (_npc instanceof L2MonsterInstance))
			{
				/* MessageTable
				String t = "Lv " + cha.getLevel() + (cha.isAggressive() ? "*" : "");
				 */
				String t = MessageTable.Messages[211].getExtra(1) + cha.getLevel() + (cha.isAggressive() ? MessageTable.Messages[211].getExtra(2) : "");
				if (_title != null)
				{
					t += " " + _title;
				}
				
				_title = t;
			}
			
			// npc crest of owning clan/ally of castle
			if ((cha instanceof L2NpcInstance) && cha.isInsideZone(ZoneId.TOWN) && (Config.SHOW_CREST_WITHOUT_QUEST || cha.getCastle().getShowNpcCrest()) && (cha.getCastle().getOwnerId() != 0))
			{
				int townId = TownManager.getTown(_x, _y, _z).getTownId();
				if ((townId != 33) && (townId != 22))
				{
					L2Clan clan = ClanTable.getInstance().getClan(cha.getCastle().getOwnerId());
					_clanCrest = clan.getCrestId();
					_clanId = clan.getId();
					_allyCrest = clan.getAllyCrestId();
					_allyId = clan.getAllyId();
				}
			}
			
			_displayEffect = cha.getDisplayEffect();
		}
		
		@Override
		protected void writeImpl()
		{
			writeC(0x0C);
			writeD(_npc.getObjectId());
			writeC(0x00);
			writeC(0x25);
			writeC(0x00);
			writeC(0xED);
			if (_rhand > 0 || _chest > 0 || _lhand > 0)
				writeC(0xFE);
			else
				writeC(0xBE);
			writeC(0x4E);
			writeC(0xA2);
			writeC(0x0C);
			int len_npc_title = 0; // 603
			if (_title != null)
				len_npc_title = _title.length(); // 603
			writeC(7 + len_npc_title*2); // 603
			writeC(_isAttackable ? 1 : 0);
			writeH(0);
			writeH(0);
			writeS(_title);
			if (_rhand > 0 || _chest > 0 || _lhand > 0)
				writeH(68);
			else
				writeH(56);
			writeD(_idTemplate + 1000000); // npctype id
			writeD(_x);
			writeD(_y);
			writeD(_z);
			writeD(_heading);
			writeD(_mAtkSpd);
			writeD(_pAtkSpd);
			putFloat((float)_moveMultiplier);
			putFloat(_npc.getAttackSpeedMultiplier());
			if (_rhand > 0 || _chest > 0 || _lhand > 0)
			{
				writeD(_rhand);
				writeD(_chest);
				writeD(_lhand);
			}
			writeC(1);
			writeC(_npc.isRunning() ? 1 : 0);
			writeC(_npc.isInsideZone(ZoneId.WATER) ? 1 : _npc.isFlying() ? 2 : 0); // C2
			writeD(_npc.isFlying() ? 1 : 0); // C6
			writeC(0);
			writeC(0);
			writeH(0);
			writeD((int)_npc.getCurrentHp());
			writeD(_npc.getMaxHp());
			
			writeC(
			(_npc.isInCombat() ? 1 : 0) + 
			(_npc.isAlikeDead() ? 2 : 0) + 
			(_npc.isTargetable() ? 4 : 0) + 
			(_npc.isShowName() ? 8 : 0));
			
			java.util.List<Integer> el = _npc.getEffectIdList();
			if (_npc.isInvisible() && !el.contains(21))
				el.add(21);
			writeH(el.size());
			for(int i : el)
			{
			   writeH(i);
			}
		}
	}
	
	public static class TrapInfo extends AbstractNpcInfo
	{
		private final L2TrapInstance _trap;
		
		public TrapInfo(L2TrapInstance cha, L2Character attacker)
		{
			super(cha);
			
			_trap = cha;
			_idTemplate = cha.getTemplate().getDisplayId();
			_isAttackable = cha.isAutoAttackable(attacker);
			_rhand = 0;
			_lhand = 0;
			_collisionHeight = _trap.getTemplate().getfCollisionHeight();
			_collisionRadius = _trap.getTemplate().getfCollisionRadius();
			if (cha.getTemplate().isUsingServerSideName())
			{
				_name = cha.getName();
			}
			_title = cha.getOwner() != null ? cha.getOwner().getName() : "";
		}
		
		@Override
		protected void writeImpl()
		{
			writeC(0x0C);
			writeD(_trap.getObjectId());
			writeC(0x00);
			writeC(0x25);
			writeC(0x00);
			writeC(0xED);
			writeC(0xBE);
			writeC(0x4E);
			writeC(0xA2);
			writeC(0x0C);
			int len_trap_title = 0; // 603
			if (_title != null)
				len_trap_title = _title.length(); // 603
			writeC(7 + len_trap_title*2); // 603
			writeC(_isAttackable ? 1 : 0);
			writeH(0);
			writeH(0);
			writeS(_title);
			writeH(56);
			writeD(_idTemplate + 1000000); // npctype id
			writeD(_x);
			writeD(_y);
			writeD(_z);
			writeD(_heading);
			writeD(_mAtkSpd);
			writeD(_pAtkSpd);
			putFloat((float)_moveMultiplier);
			putFloat(_trap.getAttackSpeedMultiplier());
			writeC(1);
			writeC(1);
			writeC(0);
			writeD(0);
			writeC(0);
			writeC(0);
			writeH(0);
			writeD((int)_trap.getCurrentHp());
			writeD(_trap.getMaxHp());
			writeC(
			(_trap.isInCombat() ? 1 : 0) + 
			(_trap.isAlikeDead() ? 2 : 0) + 
			(_trap.isTargetable() ? 4 : 0) + 
			8);
			java.util.List<Integer> el = _trap.getEffectIdList();
			if (_trap.isInvisible() && !el.contains(21))
				el.add(21);
			writeH(el.size());
			for(int i : el)
			{
			   writeH(i);
			}
		}
	}
	
	/**
	 * Packet for summons.
	 */
	public static class SummonInfo extends AbstractNpcInfo
	{
		private final L2Summon _summon;
		private final int _form;
		private final int _val;
		
		public SummonInfo(L2Summon cha, L2Character attacker, int val)
		{
			super(cha);
			_summon = cha;
			_val = val;
			_form = cha.getFormId();
			
			_isAttackable = cha.isAutoAttackable(attacker);
			_rhand = cha.getWeapon();
			_lhand = 0;
			_chest = cha.getArmor();
			_enchantEffect = cha.getTemplate().getWeaponEnchant();
			_name = cha.getName();
			_title = (cha.getOwner() != null) && cha.getOwner().isOnline() ? cha.getOwner().getName() : "";
			_idTemplate = cha.getTemplate().getDisplayId();
			_collisionHeight = cha.getTemplate().getfCollisionHeight();
			_collisionRadius = cha.getTemplate().getfCollisionRadius();
			_invisible = cha.isInvisible();
		}
		
		@Override
		protected void writeImpl()
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
			if (_summon.getSummonType() == 2)
			{
				writeC(0xFE);
				writeH(0x15E);
			}
			else if (_summon.getSummonType() == 1)
			{
				writeC(0x8B);
			}
			else
			{
				_log.warning("unknow pet/summer");
				return;
			}
			writeD(_summon.getObjectId());
			writeC(_isSummoned ? 2 : _val); // 603-TEST //  0=teleported  1=default   2=summoned
			writeC(0x25);
			writeC(0/* 603 _isAttackable ? 1 : 0 */);
			
			if (_summon.getSummonType() == 2)
			{
				writeC(0xFD);
				writeC(0xBF);
				writeC(0x5F);
				writeC(0xF3);
				writeC(0xEC);
			}
			else if (_summon.getSummonType() == 1)
			{
				writeC(0xED);
				writeC(0xBF);
				writeC(0x4F);
				writeC(0x02);
				writeC(0x6C);
			}
			int len_npc_title = 0; // 603
			if (_title != null)
				len_npc_title = _title.length(); // 603
			writeC(7 + len_npc_title*2); // 603
			writeC(0x00);
			writeC(0x00);
			writeC(0x00);
			writeC(0x00);
			writeC(0x00);
			writeS(_title);
			int len_npc_name = 0; // 603
			if (_name != null)
				len_npc_name = _name.length(); // 603
			if (_summon.getSummonType() == 2)
				writeH(88 + len_npc_name*2);
			else if (_summon.getSummonType() == 1)
				writeH(58);
			writeD(_idTemplate + 1000000); // npctype id
			writeD(_x);
			writeD(_y);
			writeD(_z);
			writeD(_heading);
			writeD(_mAtkSpd);
			writeD(_pAtkSpd);
			putFloat((float)_moveMultiplier);
			putFloat(_summon.getAttackSpeedMultiplier());
			writeC(1);
			writeC(_summon.isRunning() ? 1 : 0);
			
			if (_summon.getSummonType() == 2)
				writeD(0x00);
			writeH(0x00);
			writeH(_form);
			writeD(0x00);
			writeD(0x00);
			writeH(0x00);
			
			if (_summon.getSummonType() == 2)
			{
				writeD((int)_summon.getCurrentHp());
				writeD((int)_summon.getCurrentMp());
				writeD(_summon.getMaxHp());
				writeD(_summon.getMaxMp());
				writeS(_name);
				writeD(-1);
				writeD(-1);
			}
			
			writeC(_summon.getPvpFlag());
			int Karma = 0 - _summon.getKarma();
			writeD(Karma);
			writeC(
			(_summon.isInCombat() ? 1 : 0) + 
			(_summon.isAlikeDead() ? 2 : 0) + 
			(_summon.isTargetable() ? 4 : 0) + 
			8 );
			java.util.List<Integer> el = _summon.getEffectIdList();
			if (gmSeeInvis && !el.contains(21))
				el.add(21);
			writeH(el.size());
			for(int i : el)
			{
			   writeH(i);
			}
		}
	}
}
