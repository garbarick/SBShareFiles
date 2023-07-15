package ru.net.serbis.share.service.action;

import android.content.*;
import android.os.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.task.*;
import ru.net.serbis.share.tool.*;

public class UploadFile extends Action
{
    private String file;

    public UploadFile(Context context, Message msg)
    {
        super(context, msg);
        this.file = msg.getData().getString(Constants.FILE);
    }

    @Override
    public void onLogin(Smb smb)
    {
        new UploadTask(this, smb).execute(file, path);
    }

    @Override
    public void onUploadFinish()
    {
        sendResult(Constants.RESULT, Constants.SUCCESS);
    }
}
