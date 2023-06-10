package ru.net.serbis.share.service.action;

import android.content.*;
import android.os.*;
import java.io.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.task.*;
import ru.net.serbis.share.tool.*;

public class GetFile extends Action
{
	public GetFile(Context context, Message msg)
	{
		super(context, msg);
	}

    @Override
    public void onLogin(Smb smb)
    {
        new DownloadTask(this, smb).execute(
            path,
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
    }

	@Override
	public void onDownloadFinish(File file)
	{
		sendResult(Constants.FILE, file.getAbsolutePath());
	}
}
