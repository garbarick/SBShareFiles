package ru.net.serbis.share.tool;

import ru.net.serbis.share.*;

public class StringTool
{
    private static StringTool instance = new StringTool();

    public static StringTool get()
    {
        return instance;
    }

    public String trimRight(String str, char c)
    {
        int len = str.length() - 1;
        char last = str.charAt(len);
        if (last == c)
        {
            return str.substring(0, len);
        }
        return str;
    }

    public int getInt(String value, int defaultavalue)
    {
        try
        {
            return Integer.valueOf(value);
        }
        catch (Exception e)
        {
            Log.error(this, e);
            return defaultavalue;
        }
    }
}
