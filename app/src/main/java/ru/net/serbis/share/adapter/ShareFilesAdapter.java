package ru.net.serbis.share.adapter;

import android.content.*;
import ru.net.serbis.share.data.*;

public class ShareFilesAdapter extends NodesAdapter<ShareFile>
{
    public ShareFilesAdapter(Context context)
    {
        super(context);
	}

    @Override
    protected String getName(ShareFile node)
    {
        return node.getName();
    }

    @Override
    protected String getInfo(ShareFile node)
    {
        return node.getInfo();
    }

    @Override
    protected boolean isDir(ShareFile node)
    {
        return node.isDir();
    }
}
