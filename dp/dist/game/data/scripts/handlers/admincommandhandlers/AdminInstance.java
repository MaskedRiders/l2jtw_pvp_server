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

import java.util.StringTokenizer;

import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.instancemanager.InstanceManager;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.L2Summon;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.Instance;
import com.l2jserver.gameserver.datatables.MessageTable;

/**
 * @author evill33t, GodKratos
 */
public class AdminInstance implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_setinstance",
		"admin_ghoston",
		"admin_ghostoff",
		"admin_createinstance",
		"admin_destroyinstance",
		"admin_listinstances"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		
		// create new instance
		if (command.startsWith("admin_createinstance"))
		{
			String[] parts = command.split(" ");
			if (parts.length != 3)
			{
				activeChar.sendMessage("Example: //createinstance <id> <templatefile> - ids => 300000 are reserved for dynamic instances");
			}
			else
			{
				try
				{
					final int id = Integer.parseInt(parts[1]);
					if ((id < 300000) && InstanceManager.getInstance().createInstanceFromTemplate(id, parts[2]))
					{
						/* MessageTable.Messages[1691]
						activeChar.sendMessage("Instance created.");
						 */
						activeChar.sendMessage(1691);
					}
					else
					{
						/* MessageTable.Messages[1692]
						activeChar.sendMessage("Failed to create instance.");
						 */
						activeChar.sendMessage(1692);
					}
					return true;
				}
				catch (Exception e)
				{
					activeChar.sendMessage("Failed loading: " + parts[1] + " " + parts[2]);
					return false;
				}
			}
		}
		else if (command.startsWith("admin_listinstances"))
		{
			for (Instance temp : InstanceManager.getInstance().getInstances().values())
			{
				/* MessageTable
				activeChar.sendMessage("Id: " + temp.getId() + " Name: " + temp.getName());
				 */
				activeChar.sendMessage(MessageTable.Messages[1693].getExtra(1) + temp.getId() + MessageTable.Messages[1693].getExtra(2) + temp.getName());
			}
		}
		else if (command.startsWith("admin_setinstance"))
		{
			try
			{
				int val = Integer.parseInt(st.nextToken());
				if (InstanceManager.getInstance().getInstance(val) == null)
				{
					/* MessageTable
					activeChar.sendMessage("Instance " + val + " doesnt exist.");
					 */
					activeChar.sendMessage(MessageTable.Messages[1694].getExtra(1) + val + MessageTable.Messages[1694].getExtra(2));
					return false;
				}
				
				L2Object target = activeChar.getTarget();
				if ((target == null) || (target instanceof L2Summon)) // Don't separate summons from masters
				{
					/* MessageTable.Messages[1695]
					activeChar.sendMessage("Incorrect target.");
					 */
					activeChar.sendMessage(1695);
					return false;
				}
				target.setInstanceId(val);
				if (target instanceof L2PcInstance)
				{
					L2PcInstance player = (L2PcInstance) target;
					/* MessageTable
					player.sendMessage("Admin set your instance to:" + val);
					 */
					player.sendMessage(MessageTable.Messages[1696].getMessage() + val);
					player.teleToLocation(player.getLocation());
				}
				/* MessageTable
				activeChar.sendMessage("Moved " + target.getName() + " to instance " + target.getInstanceId() + ".");
				 */
				activeChar.sendMessage(MessageTable.Messages[1698].getExtra(1) + target.getName() + MessageTable.Messages[1698].getExtra(2) + target.getInstanceId() + MessageTable.Messages[1698].getExtra(3));
				return true;
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Use //setinstance id");
			}
		}
		else if (command.startsWith("admin_destroyinstance"))
		{
			try
			{
				int val = Integer.parseInt(st.nextToken());
				InstanceManager.getInstance().destroyInstance(val);
				/* MessageTable.Messages[1699]
				activeChar.sendMessage("Instance destroyed");
				 */
				activeChar.sendMessage(1699);
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Use //destroyinstance id");
			}
		}
		
		// set ghost mode on aka not appearing on any knownlist
		// you will be invis to all players but you also dont get update packets ;)
		// you will see snapshots (knownlist echoes?) if you port
		// so kinda useless atm
		// TODO: enable broadcast packets for ghosts
		else if (command.startsWith("admin_ghoston"))
		{
			activeChar.getAppearance().setGhostMode(true);
			/* MessageTable.Messages[1700]
			activeChar.sendMessage("Ghost mode enabled");
			 */
			activeChar.sendMessage(1700);
			activeChar.broadcastUserInfo();
			activeChar.decayMe();
			activeChar.spawnMe();
		}
		// ghost mode off
		else if (command.startsWith("admin_ghostoff"))
		{
			activeChar.getAppearance().setGhostMode(false);
			/* MessageTable.Messages[1701]
			activeChar.sendMessage("Ghost mode disabled");
			 */
			activeChar.sendMessage(1701);
			activeChar.broadcastUserInfo();
			activeChar.decayMe();
			activeChar.spawnMe();
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}