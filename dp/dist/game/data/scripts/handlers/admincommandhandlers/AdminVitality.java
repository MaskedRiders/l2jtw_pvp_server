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

import com.l2jserver.Config;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.stat.PcStat;
import com.l2jserver.gameserver.datatables.MessageTable;

/**
 * @author Psychokiller1888
 */
public class AdminVitality implements IAdminCommandHandler
{
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_set_vitality",
		"admin_set_vitality_level",
		"admin_full_vitality",
		"admin_empty_vitality",
		"admin_get_vitality"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (activeChar == null)
		{
			return false;
		}
		
		if (!Config.ENABLE_VITALITY)
		{
			/* MessageTable.Messages[1910]
			activeChar.sendMessage("Vitality is not enabled on the server!");
			 */
			activeChar.sendMessage(1910);
			return false;
		}
		
		int level = 0;
		int vitality = 0;
		
		StringTokenizer st = new StringTokenizer(command, " ");
		String cmd = st.nextToken();
		
		if (activeChar.getTarget() instanceof L2PcInstance)
		{
			L2PcInstance target;
			target = (L2PcInstance) activeChar.getTarget();
			
			if (cmd.equals("admin_set_vitality"))
			{
				try
				{
					vitality = Integer.parseInt(st.nextToken());
				}
				catch (Exception e)
				{
					/* MessageTable.Messages[1911]
					activeChar.sendMessage("Incorrect vitality");
					 */
					activeChar.sendMessage(1911);
				}
				
				target.setVitalityPoints(vitality, true);
				/* MessageTable
				target.sendMessage("Admin set your Vitality points to " + vitality);
				 */
				target.sendMessage(MessageTable.Messages[1912].getMessage() + vitality);
			}
			else if (cmd.equals("admin_set_vitality_level"))
			{
				try
				{
					level = Integer.parseInt(st.nextToken());
				}
				catch (Exception e)
				{
					/* MessageTable.Messages[1913]
					activeChar.sendMessage("Incorrect vitality level (0-4)");
					 */
					activeChar.sendMessage(1913);
				}
				
				if ((level >= 0) && (level <= 4))
				{
					if (level == 0)
					{
						vitality = PcStat.MIN_VITALITY_POINTS;
					}
					else
					{
						/*
						vitality = PcStat.VITALITY_LEVELS[level - 1];
						 */
						vitality = PcStat.VITALITY_LEVELS[level]; // rocknow-God
					}
					target.setVitalityPoints(vitality, true);
					/* MessageTable
					target.sendMessage("Admin set your Vitality level to " + level);
					 */
					target.sendMessage(MessageTable.Messages[1914].getMessage() + level);
				}
				else
				{
					/* MessageTable.Messages[1913]
					activeChar.sendMessage("Incorrect vitality level (0-4)");
					 */
					activeChar.sendMessage(1913);
				}
			}
			else if (cmd.equals("admin_full_vitality"))
			{
				target.setVitalityPoints(PcStat.MAX_VITALITY_POINTS, true);
				/* MessageTable.Messages[1915]
				target.sendMessage("Admin completly recharged your Vitality");
				 */
				target.sendMessage(1915);
			}
			else if (cmd.equals("admin_empty_vitality"))
			{
				target.setVitalityPoints(PcStat.MIN_VITALITY_POINTS, true);
				/* MessageTable.Messages[1916]
				target.sendMessage("Admin completly emptied your Vitality");
				 */
				target.sendMessage(1916);
			}
			else if (cmd.equals("admin_get_vitality"))
			{
				level = target.getVitalityLevel();
				vitality = target.getVitalityPoints();
				
				/* MessageTable
				activeChar.sendMessage("Player vitality level: " + level);
				activeChar.sendMessage("Player vitality points: " + vitality);
				 */
				activeChar.sendMessage(MessageTable.Messages[1917].getMessage() + level);
				activeChar.sendMessage(MessageTable.Messages[1918].getMessage() + vitality);
			}
			return true;
		}
		/* MessageTable.Messages[1919]
		activeChar.sendMessage("Target not found or not a player");
		 */
		activeChar.sendMessage(1919);
		return false;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	public static void main(String[] args)
	{
		new AdminVitality();
	}
}
