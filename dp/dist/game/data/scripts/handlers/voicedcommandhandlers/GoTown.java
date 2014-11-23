package handlers.voicedcommandhandlers;

import static com.l2jserver.Config.PURIFICATION_PVP_ZOMBIE_DELAY;
import com.l2jserver.gameserver.datatables.MessageTable;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import java.util.Calendar;
import org.python.modules.math;

/**
 * GoTown user command.
 */
public class GoTown implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"go_town"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
	{
		L2PcInstance target = (L2PcInstance) activeChar.getTarget();
		if(target == null) return false;
		if(target.isPvPZombie()){
			long pvPDeathDate = Calendar.getInstance().getTimeInMillis() - target.getPvPDeathDate();
			long delay = (long) math.floor((PURIFICATION_PVP_ZOMBIE_DELAY - pvPDeathDate) / 1000);
			// あなたはまだゾンビなので、ギランに戻されます。あと;秒後に参戦してください。
			target.sendMessage(String.format(MessageTable.Messages[4000].getExtra(1) + delay + MessageTable.Messages[4000].getExtra(2)));
			target.teleToLocation(83368, 147986, -3405);
			// ゾンビをふっ飛ばしました。
			activeChar.sendMessage(MessageTable.Messages[4001].getMessage()); 
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}
