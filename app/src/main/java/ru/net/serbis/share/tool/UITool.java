package ru.net.serbis.share.tool;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.share.*;

public class UITool
{
    public static <T> T findView(View view, int id)
    {
        return (T) view.findViewById(id);
    }

    public static <T> T findView(Activity view, int id)
    {
        return (T) view.findViewById(id);
    }

    public static String getEditText(Activity activity, int id)
    {
        EditText text = findView(activity, id);
        return text.getText().toString();
    }

    public static void hide(Activity activity, int id)
    {
        View view = findView(activity, id);
        view.setVisibility(View.GONE);
    }

    public static void show(Activity activity, int id)
    {
        View view = findView(activity, id);
        view.setVisibility(View.VISIBLE);
    }

    public static void enable(Activity activity, int id)
    {
        View view = findView(activity, id);
        view.setEnabled(true);
    }

    public static void disable(Activity activity, int id)
    {
        View view = findView(activity, id);
        view.setEnabled(false);
    }

    public static void toast(final Context context, final String text)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(
            new Runnable() {
                public void run()
                {
                    Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                }
            });
    }
    
    public static void toast(Context context, int code, String text)
    {
        toast(context, code + ": " + text);
    }
    
    public static void toastNotImpl(Context context)
    {
        toast(context, Constants.ERROR_NOT_IMPLEMENTED, "not implemented");
    }

    public static int getPercent(long max, long cur)
    {
        Double percent = 100.0 / max * cur;
        return percent.intValue();
    }
}
