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
package custom.TalkingIsland;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;

/**
 * @author rocknow
 */
public class TalkingIsland extends Quest
{
	private static final String qn = "TalkingIsland";
	private static final int[] NPCs =
	{
		32972, 33180
	};
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(qn);
		if (st == null)
			st = newQuestState(player);
		int npcId = npc.getId();
		if (npcId == 32972)
		{
			if (player.getClassId().level() == 0 && player.getLevel() < 21)
				player.sendPacket(new ExShowScreenMessage(1,-1,2,0,0,0,1,false,5000,false, null,NpcStringId.BEGIN_TUTORIAL_QUESTS, null));
			npc.showChatWindow(player);
		}
		return null;
		
	}
	
	public TalkingIsland(int questId, String name, String descr)
	{
		super(questId, name, descr);
		for (int i : NPCs)
		{
			addStartNpc(i);
			addTalkId(i);
		}
		addFirstTalkId(32972);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(qn);
		if (st == null)
			st = newQuestState(player);
		if (event.equalsIgnoreCase("Museum"))
		{
			player.teleToLocation(-114711,243911,-7968);
		}
		else if (event.equalsIgnoreCase("Remains"))
		{
			player.teleToLocation(-109300,237498,-2944);
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		new TalkingIsland(-1, qn, "custom");
	}
}