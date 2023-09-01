package ru.net.serbis.share.activity;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.adapter.*;
import ru.net.serbis.share.data.*;
import ru.net.serbis.share.task.*;
import ru.net.serbis.share.tool.*;
import ru.net.serbis.share.data.Error;

public class Browser extends ListActivity<ShareFile> implements BrowserCallback
{
    private Smb smb;
    private ShareFile dir;
    private String path;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

        smb = new Smb(this, params.account);
		adapter = new ShareFilesAdapter(this);
		list.setAdapter(adapter);
        initList();
	}

	@Override
	protected int getOptionsMenu()
	{
		return R.menu.nodes;
	}

	@Override
	protected int getContextMenu()
	{
		return R.menu.node;
	}

	@Override
	protected String getContextMenuHeader(ShareFile file)
	{
		return file.getName();
	}

    @Override
    protected void editMenu(ContextMenu menu, ShareFile file)
    {
        menu.findItem(R.id.download).setVisible(!file.isDir());
	}

	@Override
	public boolean onItemMenuSelected(int id, ShareFile file)
	{
        switch (id)
        {
            case R.id.logout:
                logout();
                return true;
            
            case R.id.move:
                startMove(file);
                return true;

            case R.id.delete:
                startDelete(file);
                return true;

			case R.id.download:
                startDownload(file);
				return true;

            case R.id.upload:
                startUpload();
				return true; 

            case R.id.get_files_list:
                getFilesList();
                return true;
        }
		return super.onItemMenuSelected(id, file);
    }

    private void initList()
    {
        UITool.disable(this, R.id.list);
        new BrowserTask(this, smb).execute(path);
    }

    @Override
    public void onChildren(ShareFile dir, List<ShareFile> children)
    {
		adapter.setNotifyOnChange(false);
		adapter.clear();

        if (dir != null)
        {
            this.dir = dir;
            path = dir.getPath();
            setTitle(dir.getName());

            adapter.addAll(children);

            adapter.setNotifyOnChange(true);
            adapter.notifyDataSetChanged();
        }
        UITool.enable(this, R.id.list);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		ShareFile file = adapter.getItem(position);
		if (file.isDir())
		{
            path = file.getPath();
			initList();
		}
	}

	@Override
	public void onBackPressed()
	{
        path = dir.getParent();
        if (path == null)
        {
            logout();
            return;
        }
        initList();
	}

	@Override
	public void onError(Error error)
	{
        UITool.toast(this, error);
        UITool.enable(this, R.id.list);
	}

    private void logout()
    {
        logout(RESULT_OK);
    }

	private void logout(int result)
	{
        if (params.selectMode &&
            Constants.ACTION_SELECT_PATH == params.action)
        {
            Intent intent = new Intent(getIntent());
            if (result == RESULT_OK)
            {
                params.selectPath(intent, params.selectPath);
            }
            setResult(result, intent);
            finish();
        }
        else
        {
            Intent intent = new Intent(this, Accounts.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
	}

    private void startDownload(ShareFile file)
    {
        UITool.disable(this, R.id.list);
        new DownloadTask(this, smb).execute(
            file.getPath(),
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(),
            null);
    }

	@Override
	public void onDownloadFinish(File file)
	{
        UITool.toast(this, file.getAbsolutePath());
        UITool.enable(this, R.id.list);
	}

    private void startDelete(final ShareFile file)
    {
        new SureDialog(
            this,
            R.string.action_delete,
            new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    UITool.disable(Browser.this, R.id.list);
                    new DeleteTask(Browser.this, smb).execute(file.getPath());
                }
            });
    }

    @Override
    public void onDelete()
    {
        initList();
        UITool.enable(this, R.id.list);
    }

    private void startMove(ShareFile file)
    {
        Intent intent = new Intent(this, Browser.class);
        params.setActionMove(intent, file.getPath());
        params.setAccount(intent, params.account);
        startActivityForResult(intent, 0);
    }

	@Override
	public void onMoveFinish()
	{
		int top = list.getTop();
		initList();
		list.setTop(top);
        UITool.enable(this, R.id.list);
	}

	@Override
	public void progress(int progress)
	{
		ProgressBar bar = UITool.findView(this, R.id.progress);
		bar.setProgress(progress);
	}

    private void startUpload()
    {
        Intent intent = new Intent(this, LocalBrowser.class);
        params.setActionUpload(intent, dir.getPath());
        startActivityForResult(intent, 0);
    }

    @Override
    public void onUploadFinish()
    {
        initList();
        UITool.enable(this, R.id.list);
    }
    
    private void getFilesList()
    {
        UITool.disable(this, R.id.list);
        new FilesListTask(this, smb).execute(dir.getPath());
    }

    @Override
    public void onFilesList(File file)
    {
        UITool.toast(this, file.getAbsolutePath());
        UITool.enable(this, R.id.list);
    }

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.ok:
				{
					Intent intent = new Intent(getIntent());
                    params.selectPath(intent, path);
					setResult(RESULT_OK, intent);
					finish();
				}
				break;

            case R.id.cancel:
                logout(RESULT_CANCELED);
                break;
		}
		super.onClick(view);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (RESULT_OK == resultCode)
        {
            Params params = new Params(data);
			if (params.selectMode)
			{
                switch(params.action)
                {
                    case Constants.ACTION_MOVE:
                        UITool.disable(this, R.id.list);
                        new MoveTask(this, smb).execute(params.path, params.selectPath);
                        break;

                    case Constants.ACTION_SELECT_PATH:
                        this.params.selectPath = params.selectPath;
                        logout();
                        break;

                    case Constants.ACTION_UPLOAD:
                        UITool.disable(this, R.id.list);
                        new UploadTask(this, smb).execute(params.selectPath, dir.getPath(), null);
                        break;
                }
			}
        }
	}
}
