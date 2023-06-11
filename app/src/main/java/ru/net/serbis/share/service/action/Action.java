package ru.net.serbis.share.service.action;

import android.accounts.*;
import android.content.*;
import android.os.*;
import android.text.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.data.*;
import ru.net.serbis.share.task.*;
import ru.net.serbis.share.tool.*;
import ru.net.serbis.share.account.*;
import ru.net.serbis.share.data.Error;

public class Action implements LoginCallback, BrowserCallback
{
	protected Messenger messenger;
	protected String name;
	protected String path;
	protected Context context;
	
	public Action(Context context, Message msg)
	{
		this.messenger = msg.replyTo;
		this.context = context;
		initEmailPath(msg);
	}
	
	protected void initEmailPath(Message msg)
	{
		String namePath = getPath(msg);
		if (!TextUtils.isEmpty(namePath))
		{
			Matcher matcher = Constants.PATH_PATTERN.matcher(namePath);
			if (matcher.matches())
			{
				name = matcher.group(1);
				path = matcher.group(2);
			}
		}
	}

	protected String getPath(Message msg)
	{
		return msg.getData().getString(Constants.PATH);
	}
	
	public void execute()
	{
		if (!NetTool.isNetworkAvailable(context))
		{
			sendError(new Error(context, Constants.ERROR_NETWORK_IS_NOT_AVAILABLE, R.string.error_network_is_not_available));
			return;
		}
		AccountManager manager = AccountManager.get(context);
        String pass = manager.getPassword(new ShareAccount(name));
        new LoginTask(this).execute(name, pass);
		
	}
    
    protected void sendResult(String key, String value)
    {
        Bundle data = new Bundle();
        data.putString(key, value);
        sendResult(data);
    }

    protected void sendError(Error error)
    {
        UITool.toast(context, error);

        Bundle data = new Bundle();
        data.putInt(Constants.ERROR_CODE, error.getCode());
        data.putString(Constants.ERROR, error.getMessage());
        sendResult(data);
    }

    protected void sendResult(Bundle data)
    {
        Message msg = Message.obtain();
        msg.setData(data);
        try
        {
            messenger.send(msg);
        }
        catch (RemoteException e)
        {
            Log.error(this, e);
        }
	}

    @Override
    public void onLogin(Smb smb)
    {
    }

    @Override
    public void onError(Error error)
    {
        sendError(error);
    }

    @Override
    public void onChildren(ShareFile dir, List<ShareFile> children)
    {
    }

    @Override
    public void onDownloadFinish(File file)
    {
    }

    @Override
    public void onDelete()
    {
    }

    @Override
    public void onUploadFinish()
    {
    }

	@Override
	public void progress(int persent)
	{
	}

	@Override
	public void onMoveFinish()
	{
	}

    @Override
    public void onFilesList(File file)
    {
    }
}
