package ru.net.serbis.share.data;

import android.content.*;
import java.text.*;
import jcifs.smb.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.tool.*;

public class ShareFile
{
    private static final DecimalFormat SIZE_FORMAT = new DecimalFormat("###.##");

    private String root;
    private SmbFile file;
    private String name;
    private boolean dir;
    private long size;
    private String info;

    public ShareFile(String root, SmbFile file) throws Exception
    {
        this.root = root;
        this.file = file;
        name = StringTool.get().trimRight(file.getName(), '/');
        dir = file.isDirectory();
    }

    public ShareFile(String root, SmbFile file, Context context) throws Exception
    {
        this(root, file);
        size = file.getContentLengthLong();
        info = initInfo(context);
    }

    public boolean isDir()
    {
        return dir;
    }

    public long getSize()
    {
        return size;
    }

    public String getInfo()
    {
        return info;
    }

    public String getName()
    {
        return name;
    }

    public String getPath()
    {
        return getPath(file.getPath());
    }

    public String getParent()
    {
        return getPath(file.getParent());
    }

    private String getPath(String path)
    {
        if (Constants.SMB.equals(path))
        {
            return null;
        }
        return path.replace(root, "");
    }

    private String initInfo(Context context)
    {
        if (dir)
        {
            return getInfoFolder(context);
        }
        else
        {
            return getSizeString();
        }
    }

    private String getInfoFolder(Context context)
    {
        Pair<Integer, Integer> nums = getNumChild();
        int numFolders = nums.left;
        int numFiles = nums.right;

        StringBuffer info = new StringBuffer();
        if (numFolders > 0)
        {
            info.append(numFolders)
                .append(" ")
                .append(
                context.getResources().getQuantityString(
                    R.plurals.num_folders, numFolders));
        }
        if (numFolders > 0 && numFiles > 0)
        {
            info.append(", ");
        }
        if (numFiles > 0)
        {
            info.append(numFiles)
                .append(" ")
                .append(
                context.getResources().getQuantityString(
                    R.plurals.num_files, numFiles));
        }

        return info.toString();
	}

    public Pair<Integer, Integer> getNumChild()
    {
        final Pair<Integer, Integer> result = new Pair<Integer, Integer>();
        result.left = 0;
        result.right = 0;
        try
        {
            file.listFiles(new SmbFileFilter()
                {
                    @Override
                    public boolean accept(SmbFile child) throws SmbException
                    {
                        if (child.isDirectory())
                        {
                            result.left ++;
                        }
                        else
                        {
                            result.right ++;
                        }
                        child.close();
                        return false;
                    }
            });
        }
        catch (Throwable e)
        {
        }
        return result;
    }

    private String getSizeString()
    {
        String result = "";

        float KB = 1024;
        float MB = KB * 1024;
        float GB = MB * 1024;
        float TB = GB * 1024;

        if (size < KB)
        {
            result = size + " B";
        }
        else if (size < MB)
        {
            result = SIZE_FORMAT.format(size / KB) + " KB";
        }
        else if (size < GB)
        {
            result = SIZE_FORMAT.format(size / MB) + " MB";
        }
        else if (size < TB)
        {
            result = SIZE_FORMAT.format(size / GB) + " GB";
        }
        else
        {
            result = SIZE_FORMAT.format(size / TB) + " TB";
        }

        return result;
    }
}
