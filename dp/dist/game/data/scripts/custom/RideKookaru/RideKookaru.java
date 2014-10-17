/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package custom.RideKookaru;

import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.network.serverpackets.NpcSay;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * @author rocknow
 */
public class RideKookaru extends Quest
{
	private static final String qn = "RideKookaru";
	private static final int[] NPCs =
	{
		33124
	};
	
	public RideKookaru(int questId, String name, String descr)
	{
		super(questId, name, descr);
		for (int i : NPCs)
		{
			addStartNpc(i);
			addTalkId(i);
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		QuestState st = player.getQuestState(qn);
		if (st == null)
			st = newQuestState(player);
		if (event.equalsIgnoreCase("transform"))
		{
			if (player.isTransformed() || player.isInStance())
			{
			
				npc.broadcastPacket(new NpcSay(npc.getObjectId(), 22, npc.getId(), NpcStringId.YOU_CANT_RIDE_A_KOOKARU_NOW));
			}
			else 
			{
				SkillData.getInstance().getSkill(9204, 1).applyEffects(npc, player);
			}
		}
		st.exitQuest(true);
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new RideKookaru(-1, qn, "custom");
	}
}