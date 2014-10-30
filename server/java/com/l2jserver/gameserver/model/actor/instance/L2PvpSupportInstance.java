package com.l2jserver.gameserver.model.actor.instance;

import com.l2jserver.gameserver.enums.InstanceType;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.network.serverpackets.SocialAction;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * L2PvpSupportInstance
 * @author MaskedRiderW
 */
public class L2PvpSupportInstance extends L2NpcInstance
{
	public L2PvpSupportInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
		setInstanceType(InstanceType.L2PvpSupportInstance);
	}
	private static final Logger _log = Logger.getLogger(L2PvpSupportInstance.class.getName());
	
	@Override
	public String getHtmlPath(int npcId, int val)
	{
		String pom = "";
		
		if (val == 0)
		{
			pom = "" + npcId;
		}
		else
		{
			pom = npcId + "-" + val;
		}
		
		return "data/html/pvp_support/" + pom + ".htm";
	}
	
    public boolean convertTradingPointToBattleScore(L2PcInstance player)
    {
    	int Magnification = 1;
    	if(player.getBattleScore()<10000) Magnification = 2;
    	else if(player.getBattleScore()<20000) Magnification = 3;
    	else if(player.getBattleScore()<50000) Magnification = 5;
    	else if(player.getBattleScore()<100000) Magnification = 10;
    	else Magnification = 15;
		long tradingPoint = (long)Math.floor(player.getBattleScore()/500) * Magnification;
		if(tradingPoint == 0){
    		player.sendMessage("バトルスコアが足りません");
			return false;
		}
    	player.addTradingPoint(tradingPoint);
		broadcastPacket(new SocialAction(player.getObjectId(),SocialAction.LEVEL_UP));
    	player.setBattleScore(player.getBattleScore()%500);
		return true;
    }
	
    public boolean convertItemToTradingPoint(L2PcInstance player,int convertItemid,int GetItemCount,int feeTradingPoint){
    	if(player.getTradingPoint()-feeTradingPoint < 0){
    		player.sendMessage("トレードポイントが足りません");
    		return false;
    	}
    	player.addItem("TpChange", convertItemid, GetItemCount, player.getTarget(), true);
    	player.subtractTradingPoint(feeTradingPoint);
  	   return true;
	}

	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		StringTokenizer st = new StringTokenizer(command, " ");
		//  <a action="bypass -h npc_%objectId%_convert_tradingPoint_to_battleScore">バトルスコア＞＞トレードポイント</a><br>
		if (command.startsWith("convert_tradingPoint_to_battleScore")){
			convertTradingPointToBattleScore(player);
			return;
		}
		// <a action="bypass -h npc_%objectId%_convert_item_to_tradingPoint 7860 1 40000">アペラ頭（トレードポイント４００００必要）</a><br>
	   else if (command.startsWith("convert_item_to_tradingPoint")){
			String[] strAry = command.split(" ");
			convertItemToTradingPoint(player, Integer.valueOf(strAry[1]), Integer.valueOf(strAry[2]),Integer.valueOf(strAry[3]));
			return;
		}

//		player.sendMessage("onBypassFeedback スルー");
		super.onBypassFeedback(player, command);
	}	
}