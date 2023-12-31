package ru.net.serbis.share.tool;

import java.io.*;

public class IOTool
{
    public static void close(Closeable o)
    {
        try
        {
            if (o == null)
            {
                return;
            }
            o.close();
        }
        catch (Exception e)
        {}
    }

    public static void copy(InputStream is, OutputStream os, Progress progress, int bufferSize, long size) throws Exception
    {
        try
        {
            byte[] buf = new byte[bufferSize];
            long bytes = 0;
            int len;
            while ((len = is.read(buf)) > 0)
            {
                os.write(buf, 0, len);
                bytes += len;
                progress.progress(UITool.getPercent(size, bytes));
            }
        }
        finally
        {
            close(is);
            close(os);
        }
    }

    public static void sleep(long m)
    {
        try
        {
            Thread.currentThread().sleep(m);
        }
        catch (InterruptedException e)
        {
        }
    }
}
