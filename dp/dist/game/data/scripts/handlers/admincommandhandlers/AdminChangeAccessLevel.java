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
package handlers.admincommandhandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.datatables.AdminTable;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.L2AccessLevel;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.datatables.MessageTable;

/**
 * Change access level command handler.
 */
public final class AdminChangeAccessLevel implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_changelvl"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		String[] parts = command.split(" ");
		if (parts.length == 2)
		{
			try
			{
				int lvl = Integer.parseInt(parts[1]);
				if (activeChar.getTarget() instanceof L2PcInstance)
				{
					onlineChange(activeChar, (L2PcInstance) activeChar.getTarget(), lvl);
				}
				else
				{
					activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
				}
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Usage: //changelvl <target_new_level> | <player_name> <new_level>");
			}
		}
		else if (parts.length == 3)
		{
			String name = parts[1];
			int lvl = Integer.parseInt(parts[2]);
			L2PcInstance player = L2World.getInstance().getPlayer(name);
			if (player != null)
			{
				onlineChange(activeChar, player, lvl);
			}
			else
			{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection())
				{
					PreparedStatement statement = con.prepareStatement("UPDATE characters SET accesslevel=? WHERE char_name=?");
					statement.setInt(1, lvl);
					statement.setString(2, name);
					statement.execute();
					int count = statement.getUpdateCount();
					statement.close();
					if (count == 0)
					{
						/* MessageTable.Messages[1473]
						activeChar.sendMessage("Character not found or access level unaltered.");
						 */
						activeChar.sendMessage(1473);
					}
					else
					{
						/* MessageTable
						activeChar.sendMessage("Character's access level is now set to " + lvl);
						 */
						activeChar.sendMessage(MessageTable.Messages[1474].getMessage() + lvl);
					}
				}
				catch (SQLException se)
				{
					activeChar.sendMessage("SQLException while changing character's access level");
					if (Config.DEBUG)
					{
						se.printStackTrace();
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	/**
	 * @param activeChar the active GM
	 * @param player the online target
	 * @param lvl the access level
	 */
	private static void onlineChange(L2PcInstance activeChar, L2PcInstance player, int lvl)
	{
		if (lvl >= 0)
		{
			if (AdminTable.getInstance().hasAccessLevel(lvl))
			{
				final L2AccessLevel acccessLevel = AdminTable.getInstance().getAccessLevel(lvl);
				player.setAccessLevel(lvl);
				/* MessageTable
				player.sendMessage("Your access level has been changed to " + acccessLevel.getName() + " (" + acccessLevel.getLevel() + ").");
				activeChar.sendMessage(player.getName() + "'s access level has been changed to " + acccessLevel.getName() + " (" + acccessLevel.getLevel() + ").");
				 */
				player.sendMessage(MessageTable.Messages[1475].getMessage() + acccessLevel.getName() + " (" + acccessLevel.getLevel() + MessageTable.Messages[1475].getExtra(1));
				activeChar.sendMessage(player.getName() + MessageTable.Messages[1477].getMessage() + acccessLevel.getName() + " (" + acccessLevel.getLevel() + MessageTable.Messages[1477].getExtra(1));
			}
			else
			{
				/* MessageTable
				activeChar.sendMessage("You are trying to set unexisting access level: " + lvl + " please try again with a valid one!");
				 */
				activeChar.sendMessage(MessageTable.Messages[1478].getMessage() + lvl + MessageTable.Messages[1478].getExtra(1));
			}
		}
		else
		{
			player.setAccessLevel(lvl);
			/* MessageTable.Messages[1476]
			player.sendMessage("Your character has been banned. Bye.");
			 */
			player.sendMessage(1476);
			player.logout();
		}
	}
}
