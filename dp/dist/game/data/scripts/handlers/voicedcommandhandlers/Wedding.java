/*
 * Copyright (C) 2004-2014 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.voicedcommandhandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.GameTimeController;
import com.l2jserver.gameserver.SevenSigns;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.enums.PlayerAction;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.instancemanager.CoupleManager;
import com.l2jserver.gameserver.instancemanager.GrandBossManager;
import com.l2jserver.gameserver.instancemanager.SiegeManager;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.L2Event;
import com.l2jserver.gameserver.model.entity.TvTEvent;
import com.l2jserver.gameserver.model.skills.AbnormalVisualEffect;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.network.serverpackets.ActionFailed;
import com.l2jserver.gameserver.network.serverpackets.ConfirmDlg;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillUse;
import com.l2jserver.gameserver.network.serverpackets.SetupGauge;
import com.l2jserver.gameserver.util.Broadcast;
import com.l2jserver.gameserver.datatables.MessageTable;

/**
 * Wedding voiced commands handler.
 * @author evill33t
 */
public class Wedding implements IVoicedCommandHandler
{
	static final Logger _log = Logger.getLogger(Wedding.class.getName());
	private static final String[] _voicedCommands =
	{
		"divorce",
		"engage",
		"gotolove"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
	{
		if (activeChar == null)
		{
			return false;
		}
		if (command.startsWith("engage"))
		{
			return engage(activeChar);
		}
		else if (command.startsWith("divorce"))
		{
			return divorce(activeChar);
		}
		else if (command.startsWith("gotolove"))
		{
			return goToLove(activeChar);
		}
		return false;
	}
	
	public boolean divorce(L2PcInstance activeChar)
	{
		if (activeChar.getPartnerId() == 0)
		{
			return false;
		}
		
		int _partnerId = activeChar.getPartnerId();
		int _coupleId = activeChar.getCoupleId();
		long AdenaAmount = 0;
		
		if (activeChar.isMarried())
		{
			/* MessageTable.Messages[1215]
			activeChar.sendMessage("You are now divorced.");
			 */
			activeChar.sendMessage(1215);
			
			AdenaAmount = (activeChar.getAdena() / 100) * Config.L2JMOD_WEDDING_DIVORCE_COSTS;
			activeChar.getInventory().reduceAdena("Wedding", AdenaAmount, activeChar, null);
			
		}
		else
		{
			/* MessageTable.Messages[1216]
			activeChar.sendMessage("You have broken up as a couple.");
			 */
			activeChar.sendMessage(1216);
		}
		
		final L2PcInstance partner = L2World.getInstance().getPlayer(_partnerId);
		if (partner != null)
		{
			partner.setPartnerId(0);
			if (partner.isMarried())
			{
				/* MessageTable.Messages[1217]
				partner.sendMessage("Your spouse has decided to divorce you.");
				 */
				partner.sendMessage(1217);
			}
			else
			{
				/* MessageTable.Messages[1218]
				partner.sendMessage("Your fiance has decided to break the engagement with you.");
				 */
				partner.sendMessage(1218);
			}
			
			// give adena
			if (AdenaAmount > 0)
			{
				partner.addAdena("WEDDING", AdenaAmount, null, false);
			}
		}
		CoupleManager.getInstance().deleteCouple(_coupleId);
		return true;
	}
	
	public boolean engage(L2PcInstance activeChar)
	{
		if (activeChar.getTarget() == null)
		{
			/* MessageTable.Messages[1219]
			activeChar.sendMessage("You have no one targeted.");
			 */
			activeChar.sendMessage(1219);
			return false;
		}
		else if (!(activeChar.getTarget() instanceof L2PcInstance))
		{
			/* MessageTable.Messages[1220]
			activeChar.sendMessage("You can only ask another player to engage you.");
			 */
			activeChar.sendMessage(1220);
			return false;
		}
		else if (activeChar.getPartnerId() != 0)
		{
			/* MessageTable.Messages[1221]
			activeChar.sendMessage("You are already engaged.");
			 */
			activeChar.sendMessage(1221);
			if (Config.L2JMOD_WEDDING_PUNISH_INFIDELITY)
			{
				activeChar.startAbnormalVisualEffect(true, AbnormalVisualEffect.BIG_HEAD); // give player a Big Head
				// lets recycle the sevensigns debuffs
				int skillId;
				
				int skillLevel = 1;
				
				if (activeChar.getLevel() > 40)
				{
					skillLevel = 2;
				}
				
				if (activeChar.isMageClass())
				{
					skillId = 4362;
				}
				else
				{
					skillId = 4361;
				}
				
				final Skill skill = SkillData.getInstance().getSkill(skillId, skillLevel);
				if (!activeChar.isAffectedBySkill(skillId))
				{
					skill.applyEffects(activeChar, activeChar);
				}
			}
			return false;
		}
		final L2PcInstance ptarget = (L2PcInstance) activeChar.getTarget();
		// check if player target himself
		if (ptarget.getObjectId() == activeChar.getObjectId())
		{
			/* MessageTable.Messages[1222]
			activeChar.sendMessage("Is there something wrong with you, are you trying to go out with youself?");
			 */
			activeChar.sendMessage(1222);
			return false;
		}
		
		if (ptarget.isMarried())
		{
			/* MessageTable.Messages[1223]
			activeChar.sendMessage("Player already married.");
			 */
			activeChar.sendMessage(1223);
			return false;
		}
		
		if (ptarget.isEngageRequest())
		{
			/* MessageTable.Messages[1224]
			activeChar.sendMessage("Player already asked by someone else.");
			 */
			activeChar.sendMessage(1224);
			return false;
		}
		
		if (ptarget.getPartnerId() != 0)
		{
			/* MessageTable.Messages[1225]
			activeChar.sendMessage("Player already engaged with someone else.");
			 */
			activeChar.sendMessage(1225);
			return false;
		}
		
		if ((ptarget.getAppearance().getSex() == activeChar.getAppearance().getSex()) && !Config.L2JMOD_WEDDING_SAMESEX)
		{
			/* MessageTable.Messages[1226]
			activeChar.sendMessage("Gay marriage is not allowed on this server!");
			 */
			activeChar.sendMessage(1226);
			return false;
		}
		
		// check if target has player on friendlist
		boolean FoundOnFriendList = false;
		int objectId;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			final PreparedStatement statement = con.prepareStatement("SELECT friendId FROM character_friends WHERE charId=?");
			statement.setInt(1, ptarget.getObjectId());
			final ResultSet rset = statement.executeQuery();
			while (rset.next())
			{
				objectId = rset.getInt("friendId");
				if (objectId == activeChar.getObjectId())
				{
					FoundOnFriendList = true;
				}
			}
			statement.close();
		}
		catch (Exception e)
		{
			_log.warning("could not read friend data:" + e);
		}
		
		if (!FoundOnFriendList)
		{
			/* MessageTable.Messages[1227]
			activeChar.sendMessage("The player you want to ask is not on your friends list, you must first be on each others friends list before you choose to engage.");
			 */
			activeChar.sendMessage(1227);
			return false;
		}
		
		ptarget.setEngageRequest(true, activeChar.getObjectId());
		ptarget.addAction(PlayerAction.USER_ENGAGE);
		
		/* MessageTable
		final ConfirmDlg dlg = new ConfirmDlg(activeChar.getName() + " is asking to engage you. Do you want to start a new relationship?");
		 */
		final ConfirmDlg dlg = new ConfirmDlg(activeChar.getName() + MessageTable.Messages[1228].getMessage());
		dlg.addTime(15 * 1000);
		ptarget.sendPacket(dlg);
		return true;
	}
	
	public boolean goToLove(L2PcInstance activeChar)
	{
		if (!activeChar.isMarried())
		{
			/* MessageTable.Messages[1229]
			activeChar.sendMessage("You're not married.");
			 */
			activeChar.sendMessage(1229);
			return false;
		}
		
		if (activeChar.getPartnerId() == 0)
		{
			/* MessageTable.Messages[1230]
			activeChar.sendMessage("Couldn't find your fiance in the Database - Inform a Gamemaster.");
			 */
			activeChar.sendMessage(1230);
			_log.severe("Married but couldn't find parter for " + activeChar.getName());
			return false;
		}
		
		if (GrandBossManager.getInstance().getZone(activeChar) != null)
		{
			/* MessageTable.Messages[1231]
			activeChar.sendMessage("You are inside a Boss Zone.");
			 */
			activeChar.sendMessage(1231);
			return false;
		}
		
		if (activeChar.isCombatFlagEquipped())
		{
			/* MessageTable.Messages[1232]
			activeChar.sendMessage("While you are holding a Combat Flag or Territory Ward you can't go to your love!");
			 */
			activeChar.sendMessage(1232);
			return false;
		}
		
		if (activeChar.isCursedWeaponEquipped())
		{
			/* MessageTable.Messages[1257]
			activeChar.sendMessage("While you are holding a Cursed Weapon you can't go to your love!");
			 */
			activeChar.sendMessage(1257);
			return false;
		}
		
		if (GrandBossManager.getInstance().getZone(activeChar) != null)
		{
			/* MessageTable.Messages[1231]
			activeChar.sendMessage("You are inside a Boss Zone.");
			 */
			activeChar.sendMessage(1231);
			return false;
		}
		
		if (activeChar.isJailed())
		{
			/* MessageTable.Messages[1248]
			activeChar.sendMessage("You are in Jail!");
			 */
			activeChar.sendMessage(1248);
			return false;
		}
		
		if (activeChar.isInOlympiadMode())
		{
			/* MessageTable.Messages[1249]
			activeChar.sendMessage("You are in the Olympiad now.");
			 */
			activeChar.sendMessage(1249);
			return false;
		}
		
		if (L2Event.isParticipant(activeChar))
		{
			/* MessageTable.Messages[1250]
			activeChar.sendMessage("You are in an event.");
			 */
			activeChar.sendMessage(1250);
			return false;
		}
		
		if (activeChar.isInDuel())
		{
			/* MessageTable.Messages[1251]
			activeChar.sendMessage("You are in a duel!");
			 */
			activeChar.sendMessage(1251);
			return false;
		}
		
		if (activeChar.inObserverMode())
		{
			/* MessageTable.Messages[1252]
			activeChar.sendMessage("You are in the observation.");
			 */
			activeChar.sendMessage(1252);
			return false;
		}
		
		if ((SiegeManager.getInstance().getSiege(activeChar) != null) && SiegeManager.getInstance().getSiege(activeChar).isInProgress())
		{
			/* MessageTable.Messages[1253]
			activeChar.sendMessage("You are in a siege, you cannot go to your partner.");
			 */
			activeChar.sendMessage(1253);
			return false;
		}
		
		if (activeChar.isFestivalParticipant())
		{
			/* MessageTable.Messages[1254]
			activeChar.sendMessage("You are in a festival.");
			 */
			activeChar.sendMessage(1254);
			return false;
		}
		
		if (activeChar.isInParty() && activeChar.getParty().isInDimensionalRift())
		{
			/* MessageTable.Messages[1255]
			activeChar.sendMessage("You are in the dimensional rift.");
			 */
			activeChar.sendMessage(1255);
			return false;
		}
		
		// Thanks nbd
		if (!TvTEvent.onEscapeUse(activeChar.getObjectId()))
		{
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		if (activeChar.isInsideZone(ZoneId.NO_SUMMON_FRIEND))
		{
			/* MessageTable.Messages[1256]
			activeChar.sendMessage("You are in area which blocks summoning.");
			 */
			activeChar.sendMessage(1256);
			return false;
		}
		
		final L2PcInstance partner = L2World.getInstance().getPlayer(activeChar.getPartnerId());
		if ((partner == null) || !partner.isOnline())
		{
			/* MessageTable.Messages[1233]
			activeChar.sendMessage("Your partner is not online.");
			 */
			activeChar.sendMessage(1233);
			return false;
		}
		
		if (activeChar.getInstantWorldId() != partner.getInstantWorldId())
		{
			/* MessageTable.Messages[1234]
			activeChar.sendMessage("Your partner is in another World!");
			 */
			activeChar.sendMessage(1234);
			return false;
		}
		
		if (partner.isJailed())
		{
			/* MessageTable.Messages[1235]
			activeChar.sendMessage("Your partner is in Jail.");
			 */
			activeChar.sendMessage(1235);
			return false;
		}
		
		if (partner.isCursedWeaponEquipped())
		{
			/* MessageTable.Messages[1258]
			activeChar.sendMessage("Your partner is holding a Cursed Weapon and you can't go to your love!");
			 */
			activeChar.sendMessage(1258);
			return false;
		}
		
		if (GrandBossManager.getInstance().getZone(partner) != null)
		{
			/* MessageTable.Messages[1236]
			activeChar.sendMessage("Your partner is inside a Boss Zone.");
			 */
			activeChar.sendMessage(1236);
			return false;
		}
		
		if (partner.isInOlympiadMode())
		{
			/* MessageTable.Messages[1237]
			activeChar.sendMessage("Your partner is in the Olympiad now.");
			 */
			activeChar.sendMessage(1237);
			return false;
		}
		
		if (L2Event.isParticipant(partner))
		{
			/* MessageTable.Messages[1238]
			activeChar.sendMessage("Your partner is in an event.");
			 */
			activeChar.sendMessage(1238);
			return false;
		}
		
		if (partner.isInDuel())
		{
			/* MessageTable.Messages[1239]
			activeChar.sendMessage("Your partner is in a duel.");
			 */
			activeChar.sendMessage(1239);
			return false;
		}
		
		if (partner.isFestivalParticipant())
		{
			/* MessageTable.Messages[1240]
			activeChar.sendMessage("Your partner is in a festival.");
			 */
			activeChar.sendMessage(1240);
			return false;
		}
		
		if (partner.isInParty() && partner.getParty().isInDimensionalRift())
		{
			/* MessageTable.Messages[1241]
			activeChar.sendMessage("Your partner is in dimensional rift.");
			 */
			activeChar.sendMessage(1241);
			return false;
		}
		
		if (partner.inObserverMode())
		{
			/* MessageTable.Messages[1242]
			activeChar.sendMessage("Your partner is in the observation.");
			 */
			activeChar.sendMessage(1242);
			return false;
		}
		
		if ((SiegeManager.getInstance().getSiege(partner) != null) && SiegeManager.getInstance().getSiege(partner).isInProgress())
		{
			/* MessageTable.Messages[1243]
			activeChar.sendMessage("Your partner is in a siege, you cannot go to your partner.");
			 */
			activeChar.sendMessage(1243);
			return false;
		}
		
		if (partner.isIn7sDungeon() && !activeChar.isIn7sDungeon())
		{
			final int playerCabal = SevenSigns.getInstance().getPlayerCabal(activeChar.getObjectId());
			final boolean isSealValidationPeriod = SevenSigns.getInstance().isSealValidationPeriod();
			final int compWinner = SevenSigns.getInstance().getCabalHighestScore();
			
			if (isSealValidationPeriod)
			{
				if (playerCabal != compWinner)
				{
					/* MessageTable.Messages[1244]
					activeChar.sendMessage("Your Partner is in a Seven Signs Dungeon and you are not in the winner Cabal!");
					 */
					activeChar.sendMessage(1244);
					return false;
				}
			}
			else
			{
				if (playerCabal == SevenSigns.CABAL_NULL)
				{
					/* MessageTable.Messages[1245]
					activeChar.sendMessage("Your Partner is in a Seven Signs Dungeon and you are not registered!");
					 */
					activeChar.sendMessage(1245);
					return false;
				}
			}
		}
		
		if (!TvTEvent.onEscapeUse(partner.getObjectId()))
		{
			/* MessageTable.Messages[1238]
			activeChar.sendMessage("Your partner is in an event.");
			 */
			activeChar.sendMessage(1238);
			return false;
		}
		
		if (partner.isInsideZone(ZoneId.NO_SUMMON_FRIEND))
		{
			/* MessageTable.Messages[1247]
			activeChar.sendMessage("Your partner is in area which blocks summoning.");
			 */
			activeChar.sendMessage(1247);
			return false;
		}
		
		final int teleportTimer = Config.L2JMOD_WEDDING_TELEPORT_DURATION * 1000;
		/* MessageTable
		activeChar.sendMessage("After " + (teleportTimer / 60000) + " min. you will be teleported to your partner.");
		 */
		activeChar.sendMessage(MessageTable.Messages[1259].getExtra(1) + (teleportTimer / 60000) + MessageTable.Messages[1259].getExtra(2));
		activeChar.getInventory().reduceAdena("Wedding", Config.L2JMOD_WEDDING_TELEPORT_PRICE, activeChar, null);
		
		activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
		// SoE Animation section
		activeChar.setTarget(activeChar);
		activeChar.disableAllSkills();
		
		final MagicSkillUse msk = new MagicSkillUse(activeChar, 1050, 1, teleportTimer, 0);
		Broadcast.toSelfAndKnownPlayersInRadius(activeChar, msk, 900);
		final SetupGauge sg = new SetupGauge(0, teleportTimer);
		activeChar.sendPacket(sg);
		// End SoE Animation section
		
		final EscapeFinalizer ef = new EscapeFinalizer(activeChar, partner.getLocation(), partner.isIn7sDungeon());
		// continue execution later
		activeChar.setSkillCast(ThreadPoolManager.getInstance().scheduleGeneral(ef, teleportTimer));
		activeChar.forceIsCasting(GameTimeController.getInstance().getGameTicks() + (teleportTimer / GameTimeController.MILLIS_IN_TICK));
		
		return true;
	}
	
	static class EscapeFinalizer implements Runnable
	{
		private final L2PcInstance _activeChar;
		private final Location _partnerLoc;
		private final boolean _to7sDungeon;
		
		EscapeFinalizer(L2PcInstance activeChar, Location loc, boolean to7sDungeon)
		{
			_activeChar = activeChar;
			_partnerLoc = loc;
			_to7sDungeon = to7sDungeon;
		}
		
		@Override
		public void run()
		{
			if (_activeChar.isDead())
			{
				return;
			}
			
			if ((SiegeManager.getInstance().getSiege(_partnerLoc) != null) && SiegeManager.getInstance().getSiege(_partnerLoc).isInProgress())
			{
				/* MessageTable.Messages[1260]
				_activeChar.sendMessage("Your partner is in siege, you can't go to your partner.");
				 */
				_activeChar.sendMessage(1260);
				return;
			}
			
			_activeChar.setIsIn7sDungeon(_to7sDungeon);
			_activeChar.enableAllSkills();
			_activeChar.setIsCastingNow(false);
			
			try
			{
				_activeChar.teleToLocation(_partnerLoc);
			}
			catch (Exception e)
			{
				_log.log(Level.SEVERE, "", e);
			}
		}
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}
