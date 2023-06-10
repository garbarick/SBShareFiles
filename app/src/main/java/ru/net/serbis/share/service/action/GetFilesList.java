package ru.net.serbis.share.service.action;

import android.content.*;
import android.os.*;
import java.io.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.task.*;
import ru.net.serbis.share.tool.*;

public class GetFilesList extends Action
{
	public GetFilesList(Context context, Message msg)
	{
		super(context, msg);
	}

    @Override
    public void onLogin(Smb smb)
    {
        new FilesListTask(this, smb).execute(path);
	}

    @Override
    public void onFilesList(File file)
    {
        sendResult(Constants.FILES_LIST, file.getAbsolutePath());
    }
}
