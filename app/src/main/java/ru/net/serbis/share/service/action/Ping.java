package ru.net.serbis.share.service.action;

import android.content.*;
import android.os.*;
import ru.net.serbis.share.*;

public class Ping extends Action
{
	public Ping(Context context, Message msg)
	{
		super(context, msg);
	}

	@Override
	public void execute()
	{
		sendResult(Constants.RESULT, Constants.SUCCESS);
	}
}
