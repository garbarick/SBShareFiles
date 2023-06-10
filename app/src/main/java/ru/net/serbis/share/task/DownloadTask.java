package ru.net.serbis.share.task;

import android.os.*;
import java.io.*;
import jcifs.smb.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.tool.*;

public class DownloadTask extends AsyncTask<String, Integer, File> implements Progress
{
    private BrowserCallback callback;
    private Smb smb;
    private String error;

    public DownloadTask(BrowserCallback callback, Smb smb)
    {
        this.callback = callback;
        this.smb = smb;
    }

    @Override
    protected File doInBackground(String... params)
    {
        try
        {
            publishProgress(0);
            String path = params[0];
            String targetDir = params[1];
            SmbFile source = smb.getFile(path);
            File target = new File(targetDir, source.getName());
            download(source, target);
            return target;
        }
        catch(Throwable e)
        {
            Log.error(this, e);
            error = e.getMessage();
            return null;
        }
        finally
        {
            publishProgress(0);
        }
    }

    private void download(SmbFile source, File target) throws Exception
    {
        InputStream in = source.getInputStream();
        OutputStream out = new FileOutputStream(target);
        IOTool.copy(in, out, this, source.getContentLengthLong());
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
    protected void onPostExecute(File file)
    {
        if (error != null)
        {
            callback.onError(Constants.ERROR_DOWNLOAD, error);
            return;
        }
        callback.onDownloadFinish(file);
    }
}
