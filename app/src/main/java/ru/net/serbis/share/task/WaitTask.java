package ru.net.serbis.share.task;

import android.content.*;
import android.os.*;
import jcifs.smb.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.data.*;
import ru.net.serbis.share.tool.*;

import ru.net.serbis.share.data.Error;

public abstract class WaitTask<P, R> extends AsyncTask<P, Integer, R>
{
    protected Context context;
    protected Error error;

    public WaitTask(Context context)
    {
        this.context = context;
    }

    protected R waiting(SmbException e, P ... params)
    {
        int status = e.getNtStatus();
        Log.info(this, "status:" + status);
        Log.error(this, e);
        if (status == Constants.ERROR_TOO_MANY_CONNECTIONS)
        {
            UITool.toastWait(context);
            publishProgress(0);
            for (int i = 1; i <= 100; i++)
            {
                IOTool.sleep(100);
                publishProgress(i);
            }
            publishProgress(0);
            return doInBackground(params);
        }
        error = new Error(status, e.getMessage());
        return null;
    }
}
