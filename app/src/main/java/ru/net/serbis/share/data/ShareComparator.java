package ru.net.serbis.share.data;

import java.util.*;

public class ShareComparator implements Comparator<ShareFile>
{
    @Override
    public int compare(ShareFile f1, ShareFile f2)
    {
        int result = Boolean.compare(f2.isDir(), f1.isDir());
        if (result != 0)
        {
            return result;
        }
        return f1.getName().compareTo(f2.getName());
    }
}
