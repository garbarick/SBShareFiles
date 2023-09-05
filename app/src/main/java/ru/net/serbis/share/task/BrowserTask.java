package ru.net.serbis.share.task;

import android.content.*;
import java.util.*;
import jcifs.smb.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.data.*;
import ru.net.serbis.share.tool.*;

import ru.net.serbis.share.data.Error;

public class BrowserTask extends WaitTask<String, List<ShareFile>>
{
    private BrowserCallback callback;
    private Smb smb;
    private ShareFile dir;

    public BrowserTask(Context context, BrowserCallback callback, Smb smb)
    {
        super(context);
        this.callback = callback;
        this.smb = smb;
    }
    
    @Override
    protected List<ShareFile> doInBackground(String... params)
    {
        SmbFile file = null;
        try
        {
            publishProgress(0);
            String path = params[0];
            file = path == null ? smb.getRoot() : smb.getFile(path);
            dir = smb.getShareFile(file);
            return getChildren(file);
        }
        catch (SmbException e)
        {
            return waiting(e, params);
        }
        catch (Throwable e)
        {
            Log.error(this, e);
            error = new Error(Constants.ERROR_BROWSE, e.getMessage());
            return null;
        }
        finally
        {
            smb.close(file);
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
        if (error == null)
        {
            callback.onChildren(dir, children);
        }
        else
        {
            callback.onError(error);
        }
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
                    child.close();
                }
                Collections.sort(result, new ShareComparator());
                return result;
            }
        }
        return Collections.emptyList();
    }
}
