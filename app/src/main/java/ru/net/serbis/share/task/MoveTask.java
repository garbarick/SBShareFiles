package ru.net.serbis.share.task;

import android.os.*;
import jcifs.smb.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.tool.*;
import ru.net.serbis.share.data.Error;

public class MoveTask extends AsyncTask<String, Void, Error>
{
    private BrowserCallback callback;
    private Smb smb;

    public MoveTask(BrowserCallback callback, Smb smb)
    {
        this.callback = callback;
        this.smb = smb;
    }

    @Override
    protected Error doInBackground(String... params)
    {
        SmbFile source = null;
        SmbFile target = null;
        try
        {
            String path = params[0];
            source = smb.getFile(path);
            if (!source.isFile() || !source.exists())
            {
                return new Error(smb, Constants.ERROR_FILE_IS_NOT_FOUND, R.string.error_file_is_not_found);
            }
            String selectPath = params[1];
            target = smb.getFile(selectPath + source.getName());
            source.copyTo(target);
            source.delete();
            return null;
        }
        catch (Throwable e)
        {
            Log.error(this, e);
            return new Error(Constants.ERROR_MOVE, e.getMessage());
        }
        finally
        {
            smb.close(source);
            smb.close(target);
        }
    }

    @Override
    protected void onPostExecute(Error error)
    {
        if (error == null)
        {
            callback.onMoveFinish();
        }
        else
        {
            callback.onError(error);
        }
    }
}
