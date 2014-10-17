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
package com.l2jserver.gameserver.model.base;

import static com.l2jserver.gameserver.model.base.ClassLevel.Awaken; // 603
import static com.l2jserver.gameserver.model.base.ClassLevel.First;
import static com.l2jserver.gameserver.model.base.ClassLevel.Fourth;
import static com.l2jserver.gameserver.model.base.ClassLevel.Second;
import static com.l2jserver.gameserver.model.base.ClassLevel.Third;
import static com.l2jserver.gameserver.model.base.ClassType.Fighter;
import static com.l2jserver.gameserver.model.base.ClassType.Mystic;
import static com.l2jserver.gameserver.model.base.ClassType.Priest;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

import com.l2jserver.Config;
import com.l2jserver.gameserver.enums.Race;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author luisantonioa
 */
public enum PlayerClass
{
	HumanFighter(Race.HUMAN, Fighter, First, 0),
	Warrior(Race.HUMAN, Fighter, Second, 0),
	Gladiator(Race.HUMAN, Fighter, Third, 2),
	Warlord(Race.HUMAN, Fighter, Third, 2),
	HumanKnight(Race.HUMAN, Fighter, Second, 0),
	Paladin(Race.HUMAN, Fighter, Third, 1),
	DarkAvenger(Race.HUMAN, Fighter, Third, 1),
	Rogue(Race.HUMAN, Fighter, Second, 0),
	TreasureHunter(Race.HUMAN, Fighter, Third, 3),
	Hawkeye(Race.HUMAN, Fighter, Third, 4),
	HumanMystic(Race.HUMAN, Mystic, First, 0),
	HumanWizard(Race.HUMAN, Mystic, Second, 0),
	Sorceror(Race.HUMAN, Mystic, Third, 5),
	Necromancer(Race.HUMAN, Mystic, Third, 5),
	Warlock(Race.HUMAN, Mystic, Third, 7),
	Cleric(Race.HUMAN, Priest, Second, 0),
	Bishop(Race.HUMAN, Priest, Third, 8),
	Prophet(Race.HUMAN, Priest, Third, 6),
	
	ElvenFighter(Race.ELF, Fighter, First, 0),
	ElvenKnight(Race.ELF, Fighter, Second, 0),
	TempleKnight(Race.ELF, Fighter, Third, 1),
	Swordsinger(Race.ELF, Fighter, Third, 6),
	ElvenScout(Race.ELF, Fighter, Second, 0),
	Plainswalker(Race.ELF, Fighter, Third, 3),
	SilverRanger(Race.ELF, Fighter, Third, 4),
	ElvenMystic(Race.ELF, Mystic, First, 0),
	ElvenWizard(Race.ELF, Mystic, Second, 0),
	Spellsinger(Race.ELF, Mystic, Third, 5),
	ElementalSummoner(Race.ELF, Mystic, Third, 7),
	ElvenOracle(Race.ELF, Priest, Second, 0),
	ElvenElder(Race.ELF, Priest, Third, 8),
	
	DarkElvenFighter(Race.DARK_ELF, Fighter, First, 0),
	PalusKnight(Race.DARK_ELF, Fighter, Second, 0),
	ShillienKnight(Race.DARK_ELF, Fighter, Third, 1),
	Bladedancer(Race.DARK_ELF, Fighter, Third, 6),
	Assassin(Race.DARK_ELF, Fighter, Second, 0),
	AbyssWalker(Race.DARK_ELF, Fighter, Third, 3),
	PhantomRanger(Race.DARK_ELF, Fighter, Third, 4),
	DarkElvenMystic(Race.DARK_ELF, Mystic, First, 0),
	DarkElvenWizard(Race.DARK_ELF, Mystic, Second, 0),
	Spellhowler(Race.DARK_ELF, Mystic, Third, 5),
	PhantomSummoner(Race.DARK_ELF, Mystic, Third, 7),
	ShillienOracle(Race.DARK_ELF, Priest, Second, 0),
	ShillienElder(Race.DARK_ELF, Priest, Third, 8),
	
	OrcFighter(Race.ORC, Fighter, First, 0),
	OrcRaider(Race.ORC, Fighter, Second, 0),
	Destroyer(Race.ORC, Fighter, Third, 2),
	OrcMonk(Race.ORC, Fighter, Second, 0),
	Tyrant(Race.ORC, Fighter, Third, 2),
	OrcMystic(Race.ORC, Mystic, First, 0),
	OrcShaman(Race.ORC, Mystic, Second, 0),
	Overlord(Race.ORC, Mystic, Third, 6),
	Warcryer(Race.ORC, Mystic, Third, 6),
	
	DwarvenFighter(Race.DWARF, Fighter, First, 0),
	DwarvenScavenger(Race.DWARF, Fighter, Second, 0),
	BountyHunter(Race.DWARF, Fighter, Third, 3),
	DwarvenArtisan(Race.DWARF, Fighter, Second, 0),
	Warsmith(Race.DWARF, Fighter, Third, 2),
	
	dummyEntry1(null, null, null, 0),
	dummyEntry2(null, null, null, 0),
	dummyEntry3(null, null, null, 0),
	dummyEntry4(null, null, null, 0),
	dummyEntry5(null, null, null, 0),
	dummyEntry6(null, null, null, 0),
	dummyEntry7(null, null, null, 0),
	dummyEntry8(null, null, null, 0),
	dummyEntry9(null, null, null, 0),
	dummyEntry10(null, null, null, 0),
	dummyEntry11(null, null, null, 0),
	dummyEntry12(null, null, null, 0),
	dummyEntry13(null, null, null, 0),
	dummyEntry14(null, null, null, 0),
	dummyEntry15(null, null, null, 0),
	dummyEntry16(null, null, null, 0),
	dummyEntry17(null, null, null, 0),
	dummyEntry18(null, null, null, 0),
	dummyEntry19(null, null, null, 0),
	dummyEntry20(null, null, null, 0),
	dummyEntry21(null, null, null, 0),
	dummyEntry22(null, null, null, 0),
	dummyEntry23(null, null, null, 0),
	dummyEntry24(null, null, null, 0),
	dummyEntry25(null, null, null, 0),
	dummyEntry26(null, null, null, 0),
	dummyEntry27(null, null, null, 0),
	dummyEntry28(null, null, null, 0),
	dummyEntry29(null, null, null, 0),
	dummyEntry30(null, null, null, 0),
	/*
	 * (3rd classes)
	 */
	duelist(Race.HUMAN, Fighter, Fourth, 2),
	dreadnought(Race.HUMAN, Fighter, Fourth, 2),
	phoenixKnight(Race.HUMAN, Fighter, Fourth, 1),
	hellKnight(Race.HUMAN, Fighter, Fourth, 1),
	sagittarius(Race.HUMAN, Fighter, Fourth, 4),
	adventurer(Race.HUMAN, Fighter, Fourth, 3),
	archmage(Race.HUMAN, Mystic, Fourth, 5),
	soultaker(Race.HUMAN, Mystic, Fourth, 5),
	arcanaLord(Race.HUMAN, Mystic, Fourth, 7),
	cardinal(Race.HUMAN, Priest, Fourth, 8),
	hierophant(Race.HUMAN, Priest, Fourth, 6),
	
	evaTemplar(Race.ELF, Fighter, Fourth, 1),
	swordMuse(Race.ELF, Fighter, Fourth, 6),
	windRider(Race.ELF, Fighter, Fourth, 3),
	moonlightSentinel(Race.ELF, Fighter, Fourth, 4),
	mysticMuse(Race.ELF, Mystic, Fourth, 5),
	elementalMaster(Race.ELF, Mystic, Fourth, 7),
	evaSaint(Race.ELF, Priest, Fourth, 8),
	
	shillienTemplar(Race.DARK_ELF, Fighter, Fourth, 1),
	spectralDancer(Race.DARK_ELF, Fighter, Fourth, 6),
	ghostHunter(Race.DARK_ELF, Fighter, Fourth, 3),
	ghostSentinel(Race.DARK_ELF, Fighter, Fourth, 4),
	stormScreamer(Race.DARK_ELF, Mystic, Fourth, 5),
	spectralMaster(Race.DARK_ELF, Mystic, Fourth, 7),
	shillienSaint(Race.DARK_ELF, Priest, Fourth, 8),
	
	titan(Race.ORC, Fighter, Fourth, 2),
	grandKhavatari(Race.ORC, Fighter, Fourth, 2),
	dominator(Race.ORC, Mystic, Fourth, 6),
	doomcryer(Race.ORC, Mystic, Fourth, 6),
	
	fortuneSeeker(Race.DWARF, Fighter, Fourth, 3),
	maestro(Race.DWARF, Fighter, Fourth, 2),
	
	dummyEntry31(null, null, null, 0),
	dummyEntry32(null, null, null, 0),
	dummyEntry33(null, null, null, 0),
	dummyEntry34(null, null, null, 0),
	
	maleSoldier(Race.KAMAEL, Fighter, First, 0),
	femaleSoldier(Race.KAMAEL, Fighter, First, 0),
	trooper(Race.KAMAEL, Fighter, Second, 0),
	warder(Race.KAMAEL, Fighter, Second, 0),
	berserker(Race.KAMAEL, Fighter, Third, 2),
	maleSoulbreaker(Race.KAMAEL, Fighter, Third, 5),
	femaleSoulbreaker(Race.KAMAEL, Fighter, Third, 5),
	arbalester(Race.KAMAEL, Fighter, Third, 4),
	doombringer(Race.KAMAEL, Fighter, Fourth, 2),
	maleSoulhound(Race.KAMAEL, Fighter, Fourth, 5),
	femaleSoulhound(Race.KAMAEL, Fighter, Fourth, 5),
	trickster(Race.KAMAEL, Fighter, Fourth, 4),
	inspector(Race.KAMAEL, Fighter, Third, 6),
	judicator(Race.KAMAEL, Fighter, Fourth, 6),
	
	dummyEntry35(null, null, null, 0),
	dummyEntry36(null, null, null, 0),
	
	// Awakening
	sigelKnight(Race.HUMAN, Fighter, Awaken, 1),
	tyrrWarrior(Race.HUMAN, Fighter, Awaken, 2),
	othellRogue(Race.HUMAN, Fighter, Awaken, 3),
	yulArcher(Race.HUMAN, Fighter, Awaken, 4),
	feohWizard(Race.HUMAN, Mystic, Awaken, 5),
	issEnchanter(Race.KAMAEL, Fighter, Awaken, 6),
	wynnSummoner(Race.HUMAN, Mystic, Awaken, 7),
	aeoreHealer(Race.HUMAN, Priest, Awaken, 8),
	
	dummyEntry37(null, null, null, 0),
	
	SigelPhoenixKnight(Race.HUMAN, Fighter, Awaken, 1),
	SigelHellKnight(Race.HUMAN, Fighter, Awaken, 1),
	SigelEvaTemplar(Race.ELF, Fighter, Awaken, 1),
	SigelShillienTemplar(Race.DARK_ELF, Fighter, Awaken, 1),
	TyrrDuelist(Race.HUMAN, Fighter, Awaken, 2),
	TyrrDreadnought(Race.HUMAN, Fighter, Awaken, 2),
	TyrrTitan(Race.ORC, Fighter, Awaken, 2),
	TyrrGrandKhavatari(Race.ORC, Fighter, Awaken, 2),
	TyrrMaestro(Race.DWARF, Fighter, Awaken, 2),
	TyrrDoombringer(Race.KAMAEL, Fighter, Awaken, 2),
	OthellAdventurer(Race.HUMAN, Fighter, Awaken, 3),
	OthellWindRider(Race.ELF, Fighter, Awaken, 3),
	OthellGhostHunter(Race.DARK_ELF, Fighter, Awaken, 3),
	OthellFortuneSeeker(Race.DWARF, Fighter, Awaken, 3),
	YulSagittarius(Race.HUMAN, Fighter, Awaken, 4),
	YulMoonlightSentinel(Race.ELF, Fighter, Awaken, 4),
	YulGhostSentinel(Race.DARK_ELF, Fighter, Awaken, 4),
	YulTrickster(Race.KAMAEL, Fighter, Awaken, 4),
	FeohArchmage(Race.HUMAN, Mystic, Awaken, 5),
	FeohSoultaker(Race.HUMAN, Mystic, Awaken, 5),
	FeohMysticMuse(Race.ELF, Mystic, Awaken, 5),
	FeohStormScreamer(Race.DARK_ELF, Mystic, Awaken, 5),
	FeohSoulhound(Race.KAMAEL, Mystic, Awaken, 5),
	IssHierophant(Race.HUMAN, Fighter, Awaken, 6),
	IssSwordMuse(Race.ELF, Fighter, Awaken, 6),
	IssSpectralDancer(Race.DARK_ELF, Fighter, Awaken, 6),
	IssDominator(Race.ORC, Fighter, Awaken, 6),
	IssDoomcryer(Race.ORC, Fighter, Awaken, 6),
	WynnArcanaLord(Race.HUMAN, Mystic, Awaken, 7),
	WynnElementalMaster(Race.ELF, Mystic, Awaken, 7),
	WynnSpectralMaster(Race.DARK_ELF, Mystic, Awaken, 7),
	AeoreCardinal(Race.HUMAN, Priest, Awaken, 8),
	AeoreEvaSaint(Race.ELF, Priest, Awaken, 8),
	AeoreShillienSaint(Race.DARK_ELF, Priest, Awaken, 8),
	ErtheiaFighter(Race.ERTHEIA, Fighter, First, 0),
	ErtheiaWizard(Race.ERTHEIA, Mystic, First, 0),
	Marauder(Race.ERTHEIA, Fighter, Third, 0),
	CloudBreaker(Race.ERTHEIA, Mystic, Third, 0),
	Ripper(Race.ERTHEIA, Fighter, Fourth, 0),
	Stratomancer(Race.ERTHEIA, Mystic, Fourth, 0),
	Eviscerator(Race.ERTHEIA, Fighter, Awaken, 0),
	SayhaSeer(Race.ERTHEIA, Mystic, Awaken, 0);
	
	private Race _race;
	private ClassLevel _level;
	private ClassType _type;
	private int _classtype2; // 603
	
	private static final Set<PlayerClass> mainSubclassSet;
	/* 603
	private static final Set<PlayerClass> neverSubclassed = EnumSet.of(Overlord, Warsmith);
	 */
	private static final Set<PlayerClass> neverSubclassed = EnumSet.of(Overlord, Warsmith, Marauder, CloudBreaker);
	
	/* 603 start
	private static final Set<PlayerClass> subclasseSet1 = EnumSet.of(DarkAvenger, Paladin, TempleKnight, ShillienKnight);
	private static final Set<PlayerClass> subclasseSet2 = EnumSet.of(TreasureHunter, AbyssWalker, Plainswalker);
	private static final Set<PlayerClass> subclasseSet3 = EnumSet.of(Hawkeye, SilverRanger, PhantomRanger);
	private static final Set<PlayerClass> subclasseSet4 = EnumSet.of(Warlock, ElementalSummoner, PhantomSummoner);
	private static final Set<PlayerClass> subclasseSet5 = EnumSet.of(Sorceror, Spellsinger, Spellhowler);
	 */
	private static final Set<PlayerClass> subclasseSet1 = EnumSet.of(Paladin, DarkAvenger, TempleKnight, ShillienKnight, phoenixKnight, hellKnight, evaTemplar, shillienTemplar);
	private static final Set<PlayerClass> subclasseSet2 = EnumSet.of(Gladiator, Warlord, Warsmith, Destroyer, Tyrant, berserker, doombringer, duelist, dreadnought, titan, grandKhavatari, maestro);
	private static final Set<PlayerClass> subclasseSet3 = EnumSet.of(TreasureHunter, Plainswalker, AbyssWalker, BountyHunter, adventurer, windRider, ghostHunter, fortuneSeeker);
	private static final Set<PlayerClass> subclasseSet4 = EnumSet.of(Hawkeye, SilverRanger, PhantomRanger, arbalester, trickster, sagittarius, moonlightSentinel, ghostSentinel);
	private static final Set<PlayerClass> subclasseSet5 = EnumSet.of(Sorceror, Necromancer, Spellsinger, Spellhowler, maleSoulbreaker, femaleSoulbreaker, maleSoulhound, femaleSoulhound, archmage, soultaker, mysticMuse, stormScreamer);
	private static final Set<PlayerClass> subclasseSet6 = EnumSet.of(Prophet, Swordsinger, Bladedancer, Overlord, Warcryer, inspector, judicator, hierophant, swordMuse, spectralDancer, dominator, doomcryer);
	private static final Set<PlayerClass> subclasseSet7 = EnumSet.of(Warlock, ElementalSummoner, PhantomSummoner, arcanaLord, elementalMaster, spectralMaster);
	private static final Set<PlayerClass> subclasseSet8 = EnumSet.of(Bishop, ElvenElder, ShillienElder, cardinal, evaSaint, shillienSaint);
	// 603 end
	
	private static final EnumMap<PlayerClass, Set<PlayerClass>> subclassSetMap = new EnumMap<>(PlayerClass.class);
	
	static
	{
		Set<PlayerClass> subclasses = getSet(null, Third);
		subclasses.removeAll(neverSubclassed);
		
		mainSubclassSet = subclasses;
		
		/* 603 start
		subclassSetMap.put(DarkAvenger, subclasseSet1);
		subclassSetMap.put(Paladin, subclasseSet1);
		subclassSetMap.put(TempleKnight, subclasseSet1);
		subclassSetMap.put(ShillienKnight, subclasseSet1);
		
		subclassSetMap.put(TreasureHunter, subclasseSet2);
		subclassSetMap.put(AbyssWalker, subclasseSet2);
		subclassSetMap.put(Plainswalker, subclasseSet2);
		
		subclassSetMap.put(Hawkeye, subclasseSet3);
		subclassSetMap.put(SilverRanger, subclasseSet3);
		subclassSetMap.put(PhantomRanger, subclasseSet3);
		
		subclassSetMap.put(Warlock, subclasseSet4);
		subclassSetMap.put(ElementalSummoner, subclasseSet4);
		subclassSetMap.put(PhantomSummoner, subclasseSet4);
		
		subclassSetMap.put(Sorceror, subclasseSet5);
		subclassSetMap.put(Spellsinger, subclasseSet5);
		subclassSetMap.put(Spellhowler, subclasseSet5);
		 */
		subclassSetMap.put(Paladin, subclasseSet1);
		subclassSetMap.put(DarkAvenger, subclasseSet1);
		subclassSetMap.put(TempleKnight, subclasseSet1);
		subclassSetMap.put(ShillienKnight, subclasseSet1);
		subclassSetMap.put(phoenixKnight, subclasseSet1);
		subclassSetMap.put(hellKnight, subclasseSet1);
		subclassSetMap.put(evaTemplar, subclasseSet1);
		subclassSetMap.put(shillienTemplar, subclasseSet1);
		
		subclassSetMap.put(Gladiator, subclasseSet2);
		subclassSetMap.put(Warlord, subclasseSet2);
		subclassSetMap.put(Warsmith, subclasseSet2);
		subclassSetMap.put(Destroyer, subclasseSet2);
		subclassSetMap.put(Tyrant, subclasseSet2);
		subclassSetMap.put(berserker, subclasseSet2);
		subclassSetMap.put(duelist, subclasseSet2);
		subclassSetMap.put(dreadnought, subclasseSet2);
		subclassSetMap.put(titan, subclasseSet2);
		subclassSetMap.put(grandKhavatari, subclasseSet2);
		subclassSetMap.put(maestro, subclasseSet2);
		
		subclassSetMap.put(TreasureHunter, subclasseSet3);
		subclassSetMap.put(Plainswalker, subclasseSet3);
		subclassSetMap.put(AbyssWalker, subclasseSet3);
		subclassSetMap.put(BountyHunter, subclasseSet3);
		subclassSetMap.put(adventurer, subclasseSet3);
		subclassSetMap.put(windRider, subclasseSet3);
		subclassSetMap.put(ghostHunter, subclasseSet3);
		subclassSetMap.put(fortuneSeeker, subclasseSet3);
		
		subclassSetMap.put(Hawkeye, subclasseSet4);
		subclassSetMap.put(SilverRanger, subclasseSet4);
		subclassSetMap.put(PhantomRanger, subclasseSet4);
		subclassSetMap.put(arbalester, subclasseSet4);
		subclassSetMap.put(sagittarius, subclasseSet4);
		subclassSetMap.put(moonlightSentinel, subclasseSet4);
		subclassSetMap.put(ghostSentinel, subclasseSet4);
		
		subclassSetMap.put(Sorceror, subclasseSet5);
		subclassSetMap.put(Necromancer, subclasseSet5);
		subclassSetMap.put(Spellsinger, subclasseSet5);
		subclassSetMap.put(Spellhowler, subclasseSet5);
		subclassSetMap.put(maleSoulbreaker, subclasseSet5);
		subclassSetMap.put(femaleSoulbreaker, subclasseSet5);
		subclassSetMap.put(archmage, subclasseSet5);
		subclassSetMap.put(soultaker, subclasseSet5);
		subclassSetMap.put(mysticMuse, subclasseSet5);
		subclassSetMap.put(stormScreamer, subclasseSet5);
		
		subclassSetMap.put(Prophet, subclasseSet6);
		subclassSetMap.put(Swordsinger, subclasseSet6);
		subclassSetMap.put(Bladedancer, subclasseSet6);
		subclassSetMap.put(Overlord, subclasseSet6);
		subclassSetMap.put(Warcryer, subclasseSet6);
		subclassSetMap.put(inspector, subclasseSet6);
		subclassSetMap.put(hierophant, subclasseSet6);
		subclassSetMap.put(swordMuse, subclasseSet6);
		subclassSetMap.put(spectralDancer, subclasseSet6);
		subclassSetMap.put(dominator, subclasseSet6);
		subclassSetMap.put(doomcryer, subclasseSet6);
		
		subclassSetMap.put(Warlock, subclasseSet7);
		subclassSetMap.put(ElementalSummoner, subclasseSet7);
		subclassSetMap.put(PhantomSummoner, subclasseSet7);
		subclassSetMap.put(arcanaLord, subclasseSet7);
		subclassSetMap.put(elementalMaster, subclasseSet7);
		subclassSetMap.put(spectralMaster, subclasseSet7);
		
		subclassSetMap.put(Bishop, subclasseSet8);
		subclassSetMap.put(ElvenElder, subclasseSet8);
		subclassSetMap.put(ShillienElder, subclasseSet8);
		subclassSetMap.put(cardinal, subclasseSet8);
		subclassSetMap.put(evaSaint, subclasseSet8);
		subclassSetMap.put(shillienSaint, subclasseSet8);
		// 603 end
	}
	
	/* 603
	PlayerClass(Race race, ClassType pType, ClassLevel pLevel)
	 */
	PlayerClass(Race race, ClassType pType, ClassLevel pLevel, int ClassType2)
	{
		_race = race;
		_level = pLevel;
		_type = pType;
		_classtype2 = ClassType2; // 603
	}
	
	public final Set<PlayerClass> getAvailableSubclasses(L2PcInstance player)
	{
		Set<PlayerClass> subclasses = null;
		
		/* l2jtw add
		if (_level == Third)
		 */
		if (_level == Third || _level == Awaken)
		{
			if (player.getRace() != Race.KAMAEL)
			{
				subclasses = EnumSet.copyOf(mainSubclassSet);
				
				subclasses.remove(this);
				
				/* 603 start
				switch (player.getRace())
				{
					case ELF:
						subclasses.removeAll(getSet(Race.DARK_ELF, Third));
						break;
					case DARK_ELF:
						subclasses.removeAll(getSet(Race.ELF, Third));
						break;
				}
				 */
				
				subclasses.removeAll(getSet(Race.KAMAEL, Third));
				
				Set<PlayerClass> unavailableClasses = subclassSetMap.get(this);
				
				if (unavailableClasses != null)
				{
					subclasses.removeAll(unavailableClasses);
				}
				// 603 start
				if (player.getSubClasses().size() > 0)
				{
					for (SubClass subClass : player.getSubClasses().values())
					{
						int classtype2 = 0;
						classtype2 = subClass.getClassDefinition().ClassType2();
						switch (classtype2)
						{
							case 1:
								if (subclasseSet1 != null)
									subclasses.removeAll(subclasseSet1);
								break;
							case 2:
								if (subclasseSet2 != null)
									subclasses.removeAll(subclasseSet2);
								break;
							case 3:
								if (subclasseSet3 != null)
									subclasses.removeAll(subclasseSet3);
								break;
							case 4:
								if (subclasseSet4 != null)
									subclasses.removeAll(subclasseSet4);
								break;
							case 5:
								if (subclasseSet5 != null)
									subclasses.removeAll(subclasseSet5);
								break;
							case 6:
								if (subclasseSet6 != null)
									subclasses.removeAll(subclasseSet6);
								break;
							case 7:
								if (subclasseSet7 != null)
									subclasses.removeAll(subclasseSet7);
								break;
							case 8:
								if (subclasseSet8 != null)
									subclasses.removeAll(subclasseSet8);
								break;
							default:
								break;
						}
					}
				}
				// 603 end
				
			}
			else
			{
				/* 603
				subclasses = getSet(Race.KAMAEL, Third);
				 */
				subclasses = EnumSet.copyOf(mainSubclassSet); // 603
				subclasses.remove(this);
				// Check sex, male subclasses female and vice versa
				// If server owner set MaxSubclass > 3 some kamael's cannot take 4 sub
				// So, in that situation we must skip sex check
				if (Config.MAX_SUBCLASS <= 3)
				{
					if (player.getAppearance().getSex())
					{
						subclasses.removeAll(EnumSet.of(femaleSoulbreaker));
					}
					else
					{
						subclasses.removeAll(EnumSet.of(maleSoulbreaker));
					}
				}
				if (!player.getSubClasses().containsKey(2) || (player.getSubClasses().get(2).getLevel() < 75))
				{
					subclasses.removeAll(EnumSet.of(inspector));
				}
			}
		}
		return subclasses;
	}
	
	public static final EnumSet<PlayerClass> getSet(Race race, ClassLevel level)
	{
		EnumSet<PlayerClass> allOf = EnumSet.noneOf(PlayerClass.class);
		
		for (PlayerClass playerClass : EnumSet.allOf(PlayerClass.class))
		{
			if ((race == null) || playerClass.isOfRace(race))
			{
				if ((level == null) || playerClass.isOfLevel(level))
				{
					allOf.add(playerClass);
				}
			}
		}
		return allOf;
	}
	
	public final boolean isOfRace(Race pRace)
	{
		return _race == pRace;
	}
	
	public final boolean isOfType(ClassType pType)
	{
		return _type == pType;
	}
	
	public final boolean isOfLevel(ClassLevel pLevel)
	{
		return _level == pLevel;
	}
	// 603 Start
	public final int ClassType2()
	{
		return _classtype2;
	}
	// 603 End
	
	public final ClassLevel getLevel()
	{
		return _level;
	}
}
