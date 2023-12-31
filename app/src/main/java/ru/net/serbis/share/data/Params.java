package ru.net.serbis.share.data;

import android.accounts.*;
import android.content.*;
import ru.net.serbis.share.*;

public class Params
{
    public Account account;
    public boolean selectMode;
    public int action;
    public String path;
    public String selectPath;
    
    public Params(Intent intent)
    {
		init(intent);
    }

	private void init(Intent intent)
	{
		if (intent != null)
		{
			if (intent.hasExtra(Constants.ACCOUNT))
			{
				account = intent.getParcelableExtra(Constants.ACCOUNT);
			}
			if (intent.hasExtra(Constants.SELECT_MODE))
			{
				selectMode = intent.getBooleanExtra(Constants.SELECT_MODE, false);
			}
			if (intent.hasExtra(Constants.ACTION))
			{
				action = intent.getIntExtra(Constants.ACTION, 0);
			}
			if (intent.hasExtra(Constants.PATH))
			{
				path = intent.getStringExtra(Constants.PATH);
			}
			if (intent.hasExtra(Constants.SELECT_PATH))
			{
				selectPath = intent.getStringExtra(Constants.SELECT_PATH);
			}
		}
	}
    
    public void setActionMove(Intent intent, String path)
    {
        intent.putExtra(Constants.SELECT_MODE, true);
        intent.putExtra(Constants.PATH, path);
        intent.putExtra(Constants.ACTION, Constants.ACTION_MOVE);
    }
    
    public void selectPath(Intent intent, String path)
    {
        intent.putExtra(Constants.SELECT_PATH, path);
    }

    public void setActionSelectPath(Intent intent)
    {
        intent.putExtra(Constants.SELECT_MODE, true);
        intent.putExtra(Constants.ACTION, Constants.ACTION_SELECT_PATH);
    }

    public void setAccount(Intent intent, Account account)
    {
        intent.putExtra(Constants.ACCOUNT, account);
    }

    public void setActionSelectPath(Intent intent, Account account)
    {
        setActionSelectPath(intent);
        setAccount(intent, account);
    }

    public void setActionSelectAccountPath(Intent intent)
    {
        intent.putExtra(Constants.SELECT_MODE, true);
        intent.putExtra(Constants.ACTION, Constants.ACTION_SELECT_ACCOUNT_PATH);
    }

    public void setActionUpload(Intent intent, String path)
    {
        intent.putExtra(Constants.SELECT_MODE, true);
        intent.putExtra(Constants.PATH, path);
        intent.putExtra(Constants.ACTION, Constants.ACTION_UPLOAD);
    }
}
