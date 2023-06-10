package ru.net.serbis.share.task;

import android.os.*;
import java.util.*;
import jcifs.smb.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.tool.*;
import ru.net.serbis.share.data.*;

public class BrowserTask extends AsyncTask<String, Integer, List<ShareFile>>
{
    private BrowserCallback callback;
    private Smb smb;
    private ShareFile dir;
    private String error;

    public BrowserTask(BrowserCallback callback, Smb smb)
    {
        this.callback = callback;
        this.smb = smb;
    }
    
    @Override
    protected List<ShareFile> doInBackground(String... params)
    {
        try
        {
            publishProgress(0);
            String path = params[0];
            SmbFile file = path == null ? smb.getRoot() : smb.getFile(path);
            dir = smb.getShareFile(file);
            return getChildren(file);
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
    protected void onPostExecute(List<ShareFile> children)
    {
        if (error != null)
        {
            callback.onError(Constants.ERROR_BROWSE, error);
            return;
        }
        callback.onChildren(dir, children);
    }
    
    public List<ShareFile> getChildren(SmbFile file) throws Exception
    {
        if (file.isDirectory())
        {
            SmbFile[] children = file.listFiles();
            if (children.length > 0)
            {
                List<ShareFile> result = new ArrayList<ShareFile>();
                int iter = 0;
                double step = 100./children.length;
                for (SmbFile child : children)
                {
                    result.add(smb.getShareViewFile(child));
                    iter += step;
                    publishProgress(iter);
                }
                Collections.sort(result, new ShareComparator());
                return result;
            }
        }
        return Collections.emptyList();
    }
}
