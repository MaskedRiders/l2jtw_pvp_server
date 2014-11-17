package com.l2jserver.gameserver.model.events.impl.events;

import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.impl.IBaseEvent;

/**
 * @author MaskedRiderW
 */
public class OnTvTEventMeeting implements IBaseEvent
{
	@Override
	public EventType getType()
	{
		return EventType.ON_TVT_EVENT_MEETING;
	}
}
