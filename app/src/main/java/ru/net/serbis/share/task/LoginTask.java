package ru.net.serbis.share.task;

import android.os.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.data.*;
import ru.net.serbis.share.tool.*;
import ru.net.serbis.share.data.Error;

public class LoginTask extends AsyncTask<String, Void, Error>
{
    private LoginCallback callback;

    public LoginTask(LoginCallback callback)
    {
        this.callback = callback;
    }
    
    @Override
    protected Error doInBackground(String... params)
    {
        try
        {
            Smb smb = new Smb(null, params[0], params[1]);
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
