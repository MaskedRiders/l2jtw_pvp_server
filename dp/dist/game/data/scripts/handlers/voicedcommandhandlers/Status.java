package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * GoTown user command.
 */
public class Status implements IVoicedCommandHandler {

	private static final String[] VOICED_COMMANDS
			= {
				"status"
			};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params) {
		activeChar.sendMessage("hgoehoge");
		final NpcHtmlMessage html = new NpcHtmlMessage();
		html.setFile(activeChar.getHtmlPrefix(), "data/html/status.htm");
		sendHtmlMessage(activeChar, html);
		
		return true;
	}

	@Override
	public String[] getVoicedCommandList() {
		return VOICED_COMMANDS;
	}

	private void sendHtmlMessage(L2PcInstance player, NpcHtmlMessage html) {
		
		// カンマ区切り形式表示
		NumberFormat nfNum = NumberFormat.getNumberInstance();

		// Calendarクラスで表示
		Calendar currCal = Calendar.getInstance();
		currCal.setTimeInMillis(player.getBattleScoreBestDate());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd H:mm:ss");
		
		html.replace("%name%", String.valueOf(player.getName()));
		// 戦闘スコア
		html.replace("%battleScore%", nfNum.format(player.getBattleScore()));
		// 戦闘スコアベスト
		html.replace("%battleScoreBest%", nfNum.format(player.getBattleScoreBest()));
		// 戦闘スコアベスト時刻
		html.replace("%battleScoreBestDate%", sdf.format(currCal.getTime()));
		// 戦闘記録
		html.replace("%battleLog%", String.valueOf(player.getBattleLog()));
		// TvTスコア
		html.replace("%tvTScore%",  nfNum.format(player.getTvTScore()));
		// TvTスコア記録
		html.replace("%tvTScoreLog%", String.valueOf(player.getTvTScoreLog()));
		// 交換用ポイント
		html.replace("%tradingPoint%", nfNum.format(player.getTradingPoint()));
		// PvP死亡時刻
		html.replace("%pvPDeathDate%", sdf.format(player.getPvPDeathDate()));
		// ゾンビ
		html.replace("%pvPZombie%", String.valueOf(player.getPvPZombie()));
		player.sendPacket(html);
	}

}
