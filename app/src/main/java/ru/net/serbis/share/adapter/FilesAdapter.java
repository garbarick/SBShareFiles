package ru.net.serbis.share.adapter;

import android.content.*;
import java.io.*;

public class FilesAdapter extends NodesAdapter<File>
{
    public FilesAdapter(Context context)
    {
        super(context);
	}

    @Override
    protected String getName(File node)
    {
        return node.getName();
    }

    @Override
    protected String getInfo(File node)
    {
        return "";
    }

    @Override
    protected boolean isDir(File node)
    {
        return node.isDirectory();
    }
}
