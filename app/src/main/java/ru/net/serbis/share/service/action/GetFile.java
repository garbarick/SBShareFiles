package ru.net.serbis.share.service.action;

import android.content.*;
import android.os.*;
import java.io.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.task.*;
import ru.net.serbis.share.tool.*;

public class GetFile extends Action
{
    private String bufferSize;

	public GetFile(Context context, Message msg)
	{
		super(context, msg);
        this.bufferSize = msg.getData().getString(Constants.BUFFER_SIZE);
	}

    @Override
    public void onLogin(Smb smb)
    {
        new DownloadTask(context, this, smb).execute(
            path,
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(),
            bufferSize);
    }

	@Override
	public void onDownloadFinish(File file)
	{
		sendResult(Constants.FILE, file.getAbsolutePath());
	}
}
