package ru.net.serbis.share.activity;

import android.accounts.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.adapter.*;
import ru.net.serbis.share.data.*;
import ru.net.serbis.share.service.*;
import ru.net.serbis.share.tool.*;

public class Accounts extends ListActivity<Account> implements OnAccountsUpdateListener
{
    protected AccountManager manager;
    private FilesConnection connection = new FilesConnection();
    private String lastSelectedPath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        manager = AccountManager.get(this);
        adapter = new AccountsAdapter(this);
		list.setAdapter(adapter);
        if (params.selectMode)
        {
            UITool.hide(this, R.id.ok);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        manager.addOnAccountsUpdatedListener(this, null, true);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        manager.removeOnAccountsUpdatedListener(this);
	}

    @Override
    protected void onStart()
    {
        super.onStart();
        Intent intent = new Intent(this, FilesService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if (connection.isBound())
        {
            unbindService(connection);
        }
    }

    @Override
    public void onAccountsUpdated(Account[] accounts)
    {
        adapter.setNotifyOnChange(false);
        adapter.clear();
        for(Account account : accounts)
        {
            if (Constants.TYPE.equals(account.type))
            {
                adapter.add(account);
            }
        }
        adapter.setNotifyOnChange(true);
		adapter.notifyDataSetChanged();
    }

    @Override
    protected int getOptionsMenu()
    {
        return R.menu.accounts;
    }

    @Override
    protected int getContextMenu()
    {
        return R.menu.account;
    }

    @Override
    protected String getContextMenuHeader(Account account)
    {
        return account.name;
    }

    @Override
    public boolean onItemMenuSelected(int id, final Account account)
    {
        switch (id)
        {
            case R.id.add:
                addNewAccount();
            	return true;

            case R.id.delete:
                deleteAccount(account);
                return true;

            case R.id.select_path:
                selectPath();
                return true;

            case R.id.get_files_list:
                getFilesList();
                return true;

            case R.id.get_file:
                getFile();
                return true;

            case R.id.remove_file:
                removeFile();
                return true;

            case R.id.ping:
                ping();
                return true;
        }
        return super.onItemMenuSelected(id, account);
    }
 
    private void addNewAccount()
    {
        manager.addAccount(
            Constants.TYPE,
            Constants.TOKEN, 
            null,
            null,
            this,
            new AccountManagerCallback<Bundle>()
            {
                @Override
                public void run(AccountManagerFuture<Bundle> future)
                {
                    try
                    {
                        future.getResult();
                    }
                    catch (Throwable e)
                    {
                        Log.error(this, e);
                    }
                }
            },
            null
        );
	}
    
    private void deleteAccount(final Account account)
    {
        new SureDialog(
            this,
            R.string.mess_delete_account,
            new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int width)
                {
                    manager.removeAccountExplicitly(account);
                }
            });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Account account = adapter.getItem(position);
        if (params.selectMode)
        {
            Intent intent = new Intent(this, Login.class);
            params.setActionSelectPath(intent, account);
            startActivityForResult(intent, 0);
        }
        else
        {
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            params.setAccount(intent, account);
            startActivity(intent);
            finish();
        }
    }

    private void selectPath()
    {
        Intent intent = new Intent(this, Accounts.class);
        params.setActionSelectAccountPath(intent);
        startActivityForResult(intent, 0);
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
                    case Constants.ACTION_SELECT_PATH:
                        Intent intent = new Intent(getIntent());
                        String path = "//" + Constants.SBSHARE + "/" + params.account.name + params.selectPath;
                        params.selectPath(intent, path);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;

                    case Constants.ACTION_SELECT_ACCOUNT_PATH:
                        lastSelectedPath = params.selectPath;
                        UITool.toast(this, params.selectPath);
                        break;
                }
            }
        }
    }

    private void runServiceAction(final int action, boolean input, final String requestKey, String requestValue, final String responseKey)
    {
        if (connection.isBound())
        {
            if (input)
            {
                new InputDialog(
                    this,
                    R.string.mess_link,
                    requestValue,
                    new InputDialog.OnOk()
                    {
                        public void run(String text)
                        {
                            sendServiceAction(action, requestKey, text, responseKey);
                        }
                    });
            }
            else
            {
                sendServiceAction(action, requestKey, requestValue, responseKey);
            }
        }
        else
        {
            UITool.toast(this, "no connection to files service");    
        }
    }

    private void sendServiceAction(int action, String requestKey, String requestValue, final String responseKey)
    {
        Message msg = Message.obtain(null, action, 0, 0);
        Bundle data = new Bundle();
        if (!TextUtils.isEmpty(requestKey))
        {
            data.putString(requestKey, requestValue);
        }
        msg.setData(data);
        msg.replyTo = new Messenger(
            new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    UITool.toast(Accounts.this, msg.getData().getString(responseKey));
                }
            }
        );
        try
        {
            connection.getService().send(msg);
        }
        catch (RemoteException e)
        {
            Log.error(this, e);
        }
	}

    private void getFilesList()
    {
        runServiceAction(
            Constants.ACTION_GET_FILES_LIST,
            true,
            Constants.PATH,
            lastSelectedPath,
            Constants.FILES_LIST);
    }

    private void getFile()
    {
        runServiceAction(
            Constants.ACTION_GET_FILE,
            true,
            Constants.PATH,
            null,
            Constants.FILE);
    }

    private void removeFile()
    {
        runServiceAction(
            Constants.ACTION_REMOVE_FILE,
            true,
            Constants.PATH,
            null,
            Constants.RESULT);
    }

    private void ping()
    {
        runServiceAction(
            Constants.ACTION_PING,
            false,
            null,
            null,
            Constants.RESULT);
    }
}
