package ru.net.serbis.share.task;

import android.content.*;
import jcifs.smb.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.data.*;
import ru.net.serbis.share.tool.*;

import ru.net.serbis.share.data.Error;

public class LoginTask extends WaitTask<String, Void>
{
    private LoginCallback callback;
    private Smb smb;

    public LoginTask(Context context, LoginCallback callback)
    {
        super(context);
        this.callback = callback;
    }
    
    @Override
    protected Void doInBackground(String... params)
    {
        try
        {
            smb = new Smb(context, params[0], params[1]);
            smb.check();
            return null;
        }
        catch (SmbException e)
        {
            return waiting(e, params);
        }
        catch (Throwable e)
        {
            Log.error(this, e);
            error = new Error(Constants.ERROR_LOGIN, e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(Void result)
    {
        if (error == null)
        {
            callback.onLogin(smb);
        }
        else
        {
            callback.onError(error);
        }
    }
}
