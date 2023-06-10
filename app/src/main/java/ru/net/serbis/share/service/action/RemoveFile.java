package ru.net.serbis.share.service.action;

import android.content.*;
import android.os.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.task.*;
import ru.net.serbis.share.tool.*;

public class RemoveFile extends Action
{
	public RemoveFile(Context context, Message msg)
	{
		super(context, msg);
	}

    @Override
    public void onLogin(Smb smb)
    {
        new DeleteTask(this, smb).execute(path);
    }

    @Override
    public void onDelete()
    {
        sendResult(Constants.RESULT, Constants.SUCCESS);
    }
}
