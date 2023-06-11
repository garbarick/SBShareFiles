package ru.net.serbis.share.data;

import android.content.*;
import ru.net.serbis.share.tool.*;

public class Error
{
    private int code;
    private String message;

    public Error(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public Error(Context context, int code, int message)
    {
        this(code, context.getResources().getString(message));
    }

    public Error(Smb smb, int code, int message)
    {
        this(smb.getContext(), code, message);
    }

    public int getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }
}
