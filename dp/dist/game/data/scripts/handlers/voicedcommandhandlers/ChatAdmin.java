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

import java.util.StringTokenizer;

import com.l2jserver.gameserver.datatables.AdminTable;
import com.l2jserver.gameserver.datatables.CharNameTable;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.instancemanager.PunishmentManager;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.punishment.PunishmentAffect;
import com.l2jserver.gameserver.model.punishment.PunishmentTask;
import com.l2jserver.gameserver.model.punishment.PunishmentType;
import com.l2jserver.gameserver.util.Util;
import com.l2jserver.gameserver.datatables.MessageTable;

public class ChatAdmin implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"banchat",
		"unbanchat"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
	{
		if (!AdminTable.getInstance().hasAccess(command, activeChar.getAccessLevel()))
		{
			return false;
		}
		
		if (command.equals(VOICED_COMMANDS[0])) // banchat
		{
			if (params == null)
			{
				activeChar.sendMessage("Usage: .banchat name [minutes]");
				return true;
			}
			StringTokenizer st = new StringTokenizer(params);
			if (st.hasMoreTokens())
			{
				String name = st.nextToken();
				long expirationTime = 0;
				if (st.hasMoreTokens())
				{
					String token = st.nextToken();
					if (Util.isDigit(token))
					{
						expirationTime = System.currentTimeMillis() + (Integer.parseInt(st.nextToken()) * 60 * 1000);
					}
				}
				
				int objId = CharNameTable.getInstance().getIdByName(name);
				if (objId > 0)
				{
					L2PcInstance player = L2World.getInstance().getPlayer(objId);
					if ((player == null) || !player.isOnline())
					{
						/* MessageTable.Messages[1189]
						activeChar.sendMessage("Player not online !");
						 */
						activeChar.sendMessage(1189);
						return false;
					}
					if (player.isChatBanned())
					{
						/* MessageTable.Messages[1190]
						activeChar.sendMessage("Player is already punished !");
						 */
						activeChar.sendMessage(1190);
						return false;
					}
					if (player == activeChar)
					{
						/* MessageTable.Messages[1191]
						activeChar.sendMessage("You can't ban yourself !");
						 */
						activeChar.sendMessage(1191);
						return false;
					}
					if (player.isGM())
					{
						/* MessageTable.Messages[1192]
						activeChar.sendMessage("You can't ban GM !");
						 */
						activeChar.sendMessage(1192);
						return false;
					}
					if (AdminTable.getInstance().hasAccess(command, player.getAccessLevel()))
					{
						/* MessageTable.Messages[1193]
						activeChar.sendMessage("You can't ban moderator !");
						 */
						activeChar.sendMessage(1193);
						return false;
					}
					
					PunishmentManager.getInstance().startPunishment(new PunishmentTask(objId, PunishmentAffect.CHARACTER, PunishmentType.CHAT_BAN, expirationTime, "Chat banned by moderator", activeChar.getName()));
					/* MessageTable
					player.sendMessage("Chat banned by moderator " + activeChar.getName());
					 */
					player.sendMessage(MessageTable.Messages[1194].getMessage() + activeChar.getName());
					
					if (expirationTime > 0)
					{
						/* MessageTable
						activeChar.sendMessage("Player " + player.getName() + " chat banned for " + expirationTime + " minutes.");
						 */
						activeChar.sendMessage(MessageTable.Messages[1195].getExtra(1) + player.getName() + MessageTable.Messages[1195].getExtra(2) + expirationTime + MessageTable.Messages[1195].getExtra(3));
					}
					else
					{
						/* MessageTable
						activeChar.sendMessage("Player " + player.getName() + " chat banned forever.");
						 */
						activeChar.sendMessage(MessageTable.Messages[1195].getExtra(1) + player.getName() + MessageTable.Messages[1195].getExtra(4));
					}
				}
				else
				{
					/* MessageTable.Messages[1196]
					activeChar.sendMessage("Player not found !");
					 */
					activeChar.sendMessage(1196);
					return false;
				}
			}
		}
		else if (command.equals(VOICED_COMMANDS[1])) // unbanchat
		{
			if (params == null)
			{
				activeChar.sendMessage("Usage: .unbanchat name");
				return true;
			}
			StringTokenizer st = new StringTokenizer(params);
			if (st.hasMoreTokens())
			{
				String name = st.nextToken();
				
				int objId = CharNameTable.getInstance().getIdByName(name);
				if (objId > 0)
				{
					L2PcInstance player = L2World.getInstance().getPlayer(objId);
					if ((player == null) || !player.isOnline())
					{
						/* MessageTable.Messages[1189]
						activeChar.sendMessage("Player not online !");
						 */
						activeChar.sendMessage(1189);
						return false;
					}
					if (!player.isChatBanned())
					{
						/* MessageTable.Messages[1197]
						activeChar.sendMessage("Player is not chat banned !");
						 */
						activeChar.sendMessage(1197);
						return false;
					}
					
					PunishmentManager.getInstance().stopPunishment(objId, PunishmentAffect.CHARACTER, PunishmentType.CHAT_BAN);
					
					/* MessageTable
					activeChar.sendMessage("Player " + player.getName() + " chat unbanned.");
					player.sendMessage("Chat unbanned by moderator " + activeChar.getName());
					 */
					activeChar.sendMessage(MessageTable.Messages[1195].getExtra(1) + player.getName() + MessageTable.Messages[1195].getExtra(5));
					player.sendMessage(MessageTable.Messages[1198].getMessage() + activeChar.getName());
				}
				else
				{
					/* MessageTable.Messages[1196]
					activeChar.sendMessage("Player not found !");
					 */
					activeChar.sendMessage(1196);
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}
