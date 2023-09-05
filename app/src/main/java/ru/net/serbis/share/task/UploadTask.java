package ru.net.serbis.share.task;

import android.content.*;
import java.io.*;
import jcifs.smb.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.data.*;
import ru.net.serbis.share.tool.*;

import ru.net.serbis.share.data.Error;

public class UploadTask extends WaitTask<String, Void> implements Progress
{
    private BrowserCallback callback;
    private Smb smb;

    public UploadTask(Context context, BrowserCallback callback, Smb smb)
    {
        super(context);
        this.callback = callback;
        this.smb = smb;
    }

    @Override
    protected Void doInBackground(String... params)
    {
        SmbFile target = null;
        try
        {
            publishProgress(0);
            String path = params[0];
            String targetDir = params[1];
            int bufferSize = StringTool.get().getInt(params[2], Constants.DEFAULT_BUFFER_SIZE);
            File source = new File(path);
            target = smb.getFile(targetDir + source.getName());
            download(source, target, bufferSize);
            return null;
        }
        catch (SmbException e)
        {
            return waiting(e, params);
        }
        catch (Throwable e)
        {
            Log.error(this, e);
            error = new Error(Constants.ERROR_UPLOAD, e.getMessage());
            return null;
        }
        finally
        {
            smb.close(target);
            publishProgress(0);
        }
    }

    private void download(File source, SmbFile target, int bufferSize) throws Exception
    {
        InputStream in = new FileInputStream(source);
        OutputStream out = target.getOutputStream();
        IOTool.copy(in, out, this, bufferSize, source.length());
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
    protected void onPostExecute(Void result)
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
