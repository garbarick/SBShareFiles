package ru.net.serbis.share.service;

import android.app.*;
import android.content.*;
import android.os.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.service.action.*;

public class FilesService extends Service
{
    private class IncomingHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case Constants.ACTION_GET_FILES_LIST:
                    new GetFilesList(context, msg).execute();
                    break;
				case Constants.ACTION_GET_FILE:
                    new GetFile(context, msg).execute();
                    break;
				case Constants.ACTION_REMOVE_FILE:
                    new RemoveFile(context, msg).execute();
                    break;
				case Constants.ACTION_PING:
                    new Ping(context, msg).execute();
                    break;
                case Constants.ACTION_UPLOAD:
                    new UploadFile(context, msg).execute();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private Messenger messenger;
    private Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();
        messenger = new Messenger(new IncomingHandler());
        context = getApplicationContext();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return messenger.getBinder();
    }
}
