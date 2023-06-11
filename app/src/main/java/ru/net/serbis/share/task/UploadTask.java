package ru.net.serbis.share.task;

import android.os.*;
import java.io.*;
import jcifs.smb.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.data.*;
import ru.net.serbis.share.tool.*;
import ru.net.serbis.share.data.Error;

public class UploadTask extends AsyncTask<String, Integer, Error> implements Progress
{
    private BrowserCallback callback;
    private Smb smb;

    public UploadTask(BrowserCallback callback, Smb smb)
    {
        this.callback = callback;
        this.smb = smb;
    }

    @Override
    protected Error doInBackground(String... params)
    {
        try
        {
            publishProgress(0);
            String path = params[0];
            String targetDir = params[1];
            File source = new File(path);
            SmbFile target = smb.getFile(targetDir + source.getName());
            download(source, target);
            return null;
        }
        catch(Throwable e)
        {
            Log.error(this, e);
            return new Error(Constants.ERROR_UPLOAD, e.getMessage());
        }
        finally
        {
            publishProgress(0);
        }
    }

    private void download(File source, SmbFile target) throws Exception
    {
        InputStream in = new FileInputStream(source);
        OutputStream out = target.getOutputStream();
        IOTool.copy(in, out, this, source.length());
    }

    @Override
    protected void onProgressUpdate(Integer[] progress)
    {
        callback.progress(progress[0]);
    }
    
    @Override
    public void progress(int progress)
    {
        publishProgress(progress);
    }

    @Override
    protected void onPostExecute(Error error)
    {
        if (error == null)
        {
            callback.onUploadFinish();
        }
        else
        {
            callback.onError(error);
        }
    }
}
