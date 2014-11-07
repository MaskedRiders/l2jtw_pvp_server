package handlers.voicedcommandhandlers;

import static com.l2jserver.Config.PURIFICATION_PVP_ZOMBIE_DELAY;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import static handlers.MasterHandler.GO_TOWN_RESOURCEES;
import java.util.Calendar;
import org.python.modules.math;

/**
 * GoTown user command.
 */
public class GoTown implements IVoicedCommandHandler
{
	private static final String[] COMMAND_IDS =
	{
		"go_town"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
	{
		L2PcInstance target = (L2PcInstance) activeChar.getTarget();
		if(target.isPvPZombie()){
			long pvPDeathDate = Calendar.getInstance().getTimeInMillis() - target.getPvPDeathDate();
			long delay = (long) math.floor((PURIFICATION_PVP_ZOMBIE_DELAY - pvPDeathDate) / 1000);
			target.sendMessage(String.format(GO_TOWN_RESOURCEES.get("PvPZombieForTarget"), delay));
			target.teleToLocation(83368, 147986, -3405);
			activeChar.sendMessage(GO_TOWN_RESOURCEES.get("PostPvPZombieForActor"));
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return COMMAND_IDS;
	}
}
