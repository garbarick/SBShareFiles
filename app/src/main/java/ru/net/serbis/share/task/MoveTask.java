package ru.net.serbis.share.task;

import android.os.*;
import jcifs.smb.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.tool.*;
import jcifs.*;

public class MoveTask extends AsyncTask<String, Void, String>
{
    private BrowserCallback callback;
    private Smb smb;

    public MoveTask(BrowserCallback callback, Smb smb)
    {
        this.callback = callback;
        this.smb = smb;
    }

    @Override
    protected String doInBackground(String... params)
    {
        try
        {
            String path = params[0];
            SmbFile source = smb.getFile(path);
            String selectPath = params[1];
            SmbFile target = smb.getFile(selectPath + source.getName());
            source.copyTo(target);
            source.delete();
            return null;
        }
        catch (Throwable e)
        {
            Log.error(this, e);
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String error)
    {
        if (error == null)
        {
            callback.onMoveFinish();
        }
        else
        {
            callback.onError(Constants.ERROR_MOVE, error);
        }
    }
}
