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
package com.l2jserver.gameserver.model.skills;
import java.util.logging.Level; // l2jtw add
import java.util.logging.Logger; // l2jtw add

/**
 * Abnormal Visual Effect enumerated.
 * @author DrHouse, Zoey76
 */
public enum AbnormalVisualEffect
{
	/* l2jtw start
	NONE(0x0000000, 0),
	DOT_BLEEDING(0x00000001, 0),
	DOT_POISON(0x00000002, 0),
	DOT_FIRE(0x00000004, 0),
	DOT_WATER(0x00000008, 0),
	DOT_WIND(0x00000010, 0),
	DOT_SOIL(0x00000020, 0),
	STUN(0x00000040, 0),
	SLEEP(0x00000080, 0),
	SILENCE(0x00000100, 0),
	ROOT(0x00000200, 0),
	PARALYZE(0x00000400, 0),
	FLESH_STONE(0x00000800, 0),
	DOT_MP(0x00001000, 0),
	BIG_HEAD(0x00002000, 0),
	DOT_FIRE_AREA(0x00004000, 0),
	CHANGE_TEXTURE(0x00008000, 0),
	BIG_BODY(0x00010000, 0),
	FLOATING_ROOT(0x00020000, 0),
	DANCE_ROOT(0x00040000, 0),
	GHOST_STUN(0x00080000, 0),
	STEALTH(0x00100000, 0),
	SEIZURE1(0x00200000, 0),
	SEIZURE2(0x00400000, 0),
	MAGIC_SQUARE(0x00800000, 0),
	FREEZING(0x01000000, 0),
	SHAKE(0x02000000, 0),
	BLIND(0x04000000, 0),
	ULTIMATE_DEFENCE(0x08000000, 0),
	VP_UP(0x10000000, 0),
	REAL_TARGET(0x20000000, 0),
	DEATH_MARK(0x40000000, 0),
	TURN_FLEE(0x80000000, 0),
	VP_KEEP(0x10000000, 0), // TODO: Find.
	// Special
	INVINCIBILITY(0x000001, 1),
	AIR_BATTLE_SLOW(0x000002, 1),
	AIR_BATTLE_ROOT(0x000004, 1),
	CHANGE_WP(0x000008, 1),
	CHANGE_HAIR_G(0x000010, 1),
	CHANGE_HAIR_P(0x000020, 1),
	CHANGE_HAIR_B(0x000040, 1),
	STIGMA_OF_SILEN(0x000100, 1),
	SPEED_DOWN(0x000200, 1),
	FROZEN_PILLAR(0x000400, 1),
	CHANGE_VES_S(0x000800, 1),
	CHANGE_VES_C(0x001000, 1),
	CHANGE_VES_D(0x002000, 1),
	TIME_BOMB(0x004000, 1), // High Five
	MP_SHIELD(0x008000, 1), // High Five
	NAVIT_ADVENT(0x080000, 1), // High Five
	// Event
	// TODO: Fix, currently not working.
	BR_NONE(0x000000, 2),
	BR_AFRO_NORMAL(0x000001, 2),
	BR_AFRO_PINK(0x000002, 2),
	BR_AFRO_GOLD(0x000004, 2),
	BR_POWER_OF_EVA(0x000008, 2), // High Five
	BR_HEADPHONE(0x000010, 2), // High Five
	BR_VESPER1(0x000020, 2),
	BR_VESPER2(0x000040, 2),
	BR_VESPER3(0x000080, 2),
	BR_SOUL_AVATAR(0x000100, 2); // High Five
	 */
	// 603-Start
	NONE(0x0000000, 0, 0),
	DOT_BLEEDING(0x00000001, 0, 1),
	DOT_POISON(0x00000002, 0, 2),
	DOT_FIRE(0x00000004, 0, 3),
	DOT_WATER(0x00000008, 0, 4),
	DOT_WIND(0x00000010, 0, 5),
	DOT_SOIL(0x00000020, 0, 6),
	STUN(0x00000040, 0, 7),
	SLEEP(0x00000080, 0, 8),
	SILENCE(0x00000100, 0, 9),
	ROOT(0x00000200, 0, 10),
	PARALYZE(0x00000400, 0, 11),
	FLESH_STONE(0x00000800, 0, 12),
	DOT_MP(0x00001000, 0, 13),
	BIG_HEAD(0x00002000, 0, 14),
	DOT_FIRE_AREA(0x00004000, 0, 15),
	CHANGE_TEXTURE(0x00008000, 0, 16),
	BIG_BODY(0x00010000, 0, 17),
	FLOATING_ROOT(0x00020000, 0, 18),
	DANCE_ROOT(0x00040000, 0, 19),
	GHOST_STUN(0x00080000, 0, 20),
	STEALTH(0x00100000, 0, 21),
	SEIZURE1(0x00200000, 0, 22),
	SEIZURE2(0x00400000, 0, 23),
	MAGIC_SQUARE(0x00800000, 0, 24),
	FREEZING(0x01000000, 0, 25),
	SHAKE(0x02000000, 0, 26),
	BLIND(0x04000000, 0, 27),
	ULTIMATE_DEFENCE(0x08000000, 0, 28),
	VP_UP(0x10000000, 0, 29),
	REAL_TARGET(0x20000000, 0, 30),
	DEATH_MARK(0x40000000, 0, 31),
	TURN_FLEE(0x80000000, 0, 32),
	VP_KEEP(0x10000000, 0, 29), // TODO: Find.
	// Special
	INVINCIBILITY(0x000001, 1, 33),
	AIR_BATTLE_SLOW(0x000002, 1, 34),
	AIR_BATTLE_ROOT(0x000004, 1, 35),
	CHANGE_WP(0x000008, 1, 36),
	CHANGE_HAIR_G(0x000010, 1, 37),
	CHANGE_HAIR_P(0x000020, 1, 38),
	CHANGE_HAIR_B(0x000040, 1, 39),
	S_UNKNOWN_08(0x000080, 1, 40),
	STIGMA_OF_SILEN(0x000100, 1, 41),
	SPEED_DOWN(0x000200, 1, 42),
	FROZEN_PILLAR(0x000400, 1, 43),
	CHANGE_VES_S(0x000800, 1, 44),
	CHANGE_VES_C(0x001000, 1, 45),
	CHANGE_VES_D(0x002000, 1, 46),
	TIME_BOMB(0x004000, 1, 47), // High Five
	MP_SHIELD(0x008000, 1, 48), // High Five
	S_UNKNOWN_49(0x010000, 1, 49),
	S_UNKNOWN_50(0x020000, 1, 50),
	S_UNKNOWN_51(0x030000, 1, 51),
	NAVIT_ADVENT(0x080000, 1, 52), // High Five
	S_UNKNOWN_53(0x100000, 1, 53),
	S_UNKNOWN_54(0x200000, 1, 54),
	S_UNKNOWN_55(0x400000, 1, 55),
	S_UNKNOWN_56(0x800000, 1, 56),
	S_UNKNOWN_57(0x1000000, 1, 57),
	S_UNKNOWN_58(0x2000000, 1, 58),
	S_UNKNOWN_59(0x4000000, 1, 59),
	S_UNKNOWN_60(0x8000000, 1, 60),
	S_UNKNOWN_61(0x10000000, 1, 61),
	S_UNKNOWN_62(0x20000000, 1, 62),
	S_UNKNOWN_63(0x40000000, 1, 63),
	S_UNKNOWN_64(0x80000000, 1, 64),
	// Event
	// TODO: Fix, currently not working.
	BR_NONE(0x000000, 2, 0),
	BR_AFRO_NORMAL(0x000001, 2, 0),
	BR_AFRO_PINK(0x000002, 2, 0),
	BR_AFRO_GOLD(0x000004, 2, 0),
	BR_POWER_OF_EVA(0x000008, 2, 0), // High Five
	BR_HEADPHONE(0x000010, 2, 0), // High Five
	BR_VESPER1(0x000020, 2, 0),
	BR_VESPER2(0x000040, 2, 0),
	BR_VESPER3(0x000080, 2, 0),
	BR_SOUL_AVATAR(0x000100, 2, 0); // High Five
	// 603-End
	
	private static final Logger _log = Logger.getLogger(AbnormalVisualEffect.class.getName()); // l2jtw add
	/** Int mask. */
	private final int _mask;
	/** Type: 0 Normal, 1 Special, 2 Event. */
	private final int _type;
	private final int _id; // l2jtw add
	
	/* l2jtw add
	private AbnormalVisualEffect(int mask, int type)
	 */
	private AbnormalVisualEffect(int mask, int type, int id)
	{
		_mask = mask;
		_type = type;
		_id = id; // l2jtw add
	}
	
	/**
	 * Gets the int bitmask for the abnormal visual effect.
	 * @return the int bitmask
	 */
	public final int getMask()
	{
		return _mask;
	}
	
	/**
	 * Verify if it's a special abnormal visual effect.
	 * @return {@code true} it's a special abnormal visual effect, {@code false} otherwise
	 */
	public final boolean isSpecial()
	{
		return _type == 1;
	}
	
	/**
	 * Verify if it's an event abnormal visual effect.
	 * @return {@code true} it's an event abnormal visual effect, {@code false} otherwise
	 */
	public final boolean isEvent()
	{
		return _type == 2;
	}
	// l2jtw add start
	public final int getId()
	{
		return _id;
	}
	
	public static int getAbnormalId(int mask)
	{
		if (mask >= 0)
		{
			for (AbnormalVisualEffect eff : AbnormalVisualEffect.values())
			{
				if (eff.getMask() == mask && eff.getId() <= 32)
				{
					return eff.getId();
				}
			}
			_log.log(Level.WARNING, AbnormalVisualEffect.class.getSimpleName() + ": Abnormal effect not found for mask: " + mask + "!");
		}
		return 0;
	}
	
	public static int getSpecialId(int mask)
	{
		if (mask >= 0)
		{
			for (AbnormalVisualEffect eff : AbnormalVisualEffect.values())
			{
				if (eff.getMask() == mask && eff.getId() > 32)
				{
					return eff.getId();
				}
			}
			_log.log(Level.WARNING, AbnormalVisualEffect.class.getSimpleName() + ": Abnormal effect not found for mask: " + mask + "!");
		}
		return 0;
	}
	// l2jtw add end
}