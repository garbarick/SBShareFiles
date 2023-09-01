package ru.net.serbis.share.task;

import android.os.*;
import java.io.*;
import jcifs.smb.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.data.*;
import ru.net.serbis.share.tool.*;
import ru.net.serbis.share.data.Error;

public class DownloadTask extends AsyncTask<String, Integer, File> implements Progress
{
    private BrowserCallback callback;
    private Smb smb;
    private Error error;

    public DownloadTask(BrowserCallback callback, Smb smb)
    {
        this.callback = callback;
        this.smb = smb;
    }

    @Override
    protected File doInBackground(String... params)
    {
        SmbFile source = null;
        try
        {
            publishProgress(0);
            String path = params[0];
            String targetDir = params[1];
            int bufferSize = StringTool.get().getInt(params[2], Constants.DEFAULT_BUFFER_SIZE);
            source = smb.getFile(path);

            if (!source.isFile() || !source.exists())
            {
                error = new Error(smb, Constants.ERROR_FILE_IS_NOT_FOUND, R.string.error_file_is_not_found);
                return null;
            }

            File target = new File(targetDir, source.getName());
            download(source, target, bufferSize);
            return target;
        }
        catch(Throwable e)
        {
            Log.error(this, e);
            error = new Error(Constants.ERROR_DOWNLOAD, e.getMessage());
            return null;
        }
        finally
        {
            smb.close(source);
            publishProgress(0);
        }
    }

    private void download(SmbFile source, File target, int bufferSize) throws Exception
    {
        InputStream in = source.getInputStream();
        OutputStream out = new FileOutputStream(target);
        IOTool.copy(in, out, this, bufferSize, source.getContentLengthLong());
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
            callback.onError(error);
            return;
        }
        callback.onDownloadFinish(file);
    }
}
