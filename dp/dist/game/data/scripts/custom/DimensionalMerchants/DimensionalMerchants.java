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
package custom.DimensionalMerchants;

import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;

public class DimensionalMerchants extends Quest
{
	private static final String qn = "DimensionalMerchants";
	private static final int DIMENSIONAL_MERCHANT = 32478;

	public DimensionalMerchants(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(DIMENSIONAL_MERCHANT);
		addTalkId(DIMENSIONAL_MERCHANT);
		addFirstTalkId(DIMENSIONAL_MERCHANT);
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		htmltext = event;
		QuestState st = player.getQuestState(getName());

		if (event.equalsIgnoreCase("13017") || event.equalsIgnoreCase("13018") || event.equalsIgnoreCase("13019") || event.equalsIgnoreCase("13020"))
		{
			// Player can either have an event coupon or a normal coupon, so first check for the normal one
			long normalItem = st.getQuestItemsCount(13273);
			long eventItem = st.getQuestItemsCount(13383);
			if (normalItem >= 1)
			{
				st.takeItems(13273, 1);
				st.giveItems(Integer.valueOf(event), 1);
				st.exitQuest(true);
				htmltext = "32478-23.htm";
				return htmltext;
			}
			else if (eventItem >= 1)
			{
				event = (event) + 286;
				st.takeItems(13383, 1);
				st.giveItems(Integer.valueOf(event), 1);
				st.exitQuest(true);
				htmltext = "32478-23.htm";
				return htmltext;
			}
			else
				htmltext = "32478-21.htm";
		}
		else if (event.equalsIgnoreCase("13548") || event.equalsIgnoreCase("13549") || event.equalsIgnoreCase("13550") || event.equalsIgnoreCase("13551"))
		{
			if (st.getQuestItemsCount(14065) >= 1)
			{
				st.takeItems(14065, 1);
				st.giveItems(Integer.valueOf(event), 1);
				st.exitQuest(true);
				htmltext = "32478-23.htm";
				return htmltext;
			}
			htmltext = "32478-21.htm";
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("20915") || event.equalsIgnoreCase("20916") || event.equalsIgnoreCase("20917") || event.equalsIgnoreCase("20918") || event.equalsIgnoreCase("20919") || event.equalsIgnoreCase("20920"))
		{
			if (st.getQuestItemsCount(20914) >= 1)
			{
				st.takeItems(20914, 1);
				st.giveItems(Integer.valueOf(event), 1);
				st.exitQuest(true);
				htmltext = "32478-23.htm";
				return htmltext;
			}
			htmltext = "32478-22.htm";
			st.exitQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		QuestState st = player.getQuestState(getName());

		if (st == null)
		{
			Quest q = QuestManager.getInstance().getQuest(getName());
			st = q.newQuestState(player);
		}

			htmltext = "32478.htm";

		return htmltext;
	}

	public static void main(String[] args)
	{
		new DimensionalMerchants(-1, qn, "custom");
	}

}