package ru.net.serbis.share.tool;

import android.content.*;
import android.net.*;

public class NetTool
{
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null;
	}
}
