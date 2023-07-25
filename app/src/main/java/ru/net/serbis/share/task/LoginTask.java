package ru.net.serbis.share.task;

import android.content.*;
import android.os.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.data.*;
import ru.net.serbis.share.tool.*;

import ru.net.serbis.share.data.Error;

public class LoginTask extends AsyncTask<String, Void, Error>
{
    private LoginCallback callback;
    private Context context;

    public LoginTask(LoginCallback callback, Context context)
    {
        this.callback = callback;
        this.context = context;
    }
    
    @Override
    protected Error doInBackground(String... params)
    {
        try
        {
            Smb smb = new Smb(context, params[0], params[1]);
            smb.check();
            callback.onLogin(smb);
            return null;
        }
        catch (Throwable e)
        {
            Log.error(this, e);
            return new Error(Constants.ERROR_LOGIN, e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(Error error)
    {
        if (error != null)
        {
            callback.onError(error);
        }
    }
}
