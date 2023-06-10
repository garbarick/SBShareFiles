package ru.net.serbis.share.task;

import android.os.*;
import java.io.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.tool.*;
import jcifs.smb.*;

public class FilesListTask extends AsyncTask<String, Integer, File>
{
    private BrowserCallback callback;
    private Smb smb;
    private String error;
    private long allFiles;
    private long files;

    public FilesListTask(BrowserCallback callback, Smb smb)
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
            SmbFile dir = smb.getFile(path);
            File outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = File.createTempFile("files_list_", ".txt", outputDir);
            generateFilesList(dir, file);
            return file;
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

    @Override
    protected void onProgressUpdate(Integer[] progress)
    {
        callback.progress(progress[0]);
    }

    @Override
    protected void onPostExecute(File file)
    {
        if (error != null)
        {
            callback.onError(Constants.ERROR_FILES_LIST, error);
            return;
        }
        callback.onFilesList(file);
    }

    private void generateFilesList(SmbFile dir, File file)
    {
        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            collectChild(writer, dir);
            writer.flush();
        }
        catch (Throwable e)
        {
            Log.error(this, e);
        }
        finally
        {
            IOTool.close(writer);
        }
	}

    private void collectChild(BufferedWriter writer, SmbFile file) throws Exception
    {
        SmbFile[] children = file.listFiles();
        allFiles += children.length;
        for (SmbFile child : children)
        {
            if (child.isDirectory())
            {
                collectChild(writer, child);
            }
            else
            {
                writer.append(
                    "//" +
                    Constants.SBSHARE +
                    "/" +
                    smb.getName() +
                    smb.getShareFile(child).getPath());
                writer.newLine();
                
                files ++;
                publishProgress(UITool.getPercent(allFiles, files));
            }
        }
	}
}
