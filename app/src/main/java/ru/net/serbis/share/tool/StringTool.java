package ru.net.serbis.share.tool;

public class StringTool
{
    public static String trimRight(String str, char c)
    {
        int len = str.length() - 1;
        char last = str.charAt(len);
        if (last == c)
        {
            return str.substring(0, len);
        }
        return str;
    }
}
