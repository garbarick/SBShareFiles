package ru.net.serbis.share.service.action;

import android.content.*;
import android.os.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.task.*;
import ru.net.serbis.share.tool.*;

public class UploadFile extends Action
{
    private String file;
    private String bufferSize;

    public UploadFile(Context context, Message msg)
    {
        super(context, msg);
        this.file = msg.getData().getString(Constants.FILE);
        this.bufferSize = msg.getData().getString(Constants.BUFFER_SIZE);
    }

    @Override
    public void onLogin(Smb smb)
    {
        new UploadTask(context, this, smb).execute(file, path, bufferSize);
    }

    @Override
    public void onUploadFinish()
    {
        sendResult(Constants.RESULT, Constants.SUCCESS);
    }
}
