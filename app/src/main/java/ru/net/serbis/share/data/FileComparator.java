package ru.net.serbis.share.data;

import java.io.*;
import java.util.*;

public class FileComparator implements Comparator<File>
{
    @Override
    public int compare(File f1, File f2)
    {
        int result = Boolean.compare(f2.isDirectory(), f1.isDirectory());
        if (result != 0)
        {
            return result;
        }
        return f1.getName().compareTo(f2.getName());
    }
}
