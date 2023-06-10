package ru.net.serbis.share.task;

import android.os.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.tool.*;

public class LoginTask extends AsyncTask<String, Void, String>
{
    private LoginCallback callback;

    public LoginTask(LoginCallback callback)
    {
        this.callback = callback;
    }
    
    @Override
    protected String doInBackground(String... params)
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
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String error)
    {
        if (error != null)
        {
            callback.onError(Constants.ERROR_LOGIN, error);
        }
    }
}
