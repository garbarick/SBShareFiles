package ru.net.serbis.share.activity;

import android.accounts.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.account.*;
import ru.net.serbis.share.task.*;
import ru.net.serbis.share.tool.*;
import ru.net.serbis.share.data.*;
import ru.net.serbis.share.data.Error;

public class Login extends AccountAuthenticatorActivity implements LoginCallback
{
    private boolean create;
    private Params params;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        params = new Params(getIntent());
        if (params.selectMode)
        {
            setResult(RESULT_CANCELED);
        }
        if (!NetTool.isNetworkAvailable(this))
        {
            onError(new Error(this, Constants.ERROR_NETWORK_IS_NOT_AVAILABLE, R.string.error_network_is_not_available));
		}
        if (params.account != null)
        {
            AccountManager manager = AccountManager.get(this);
            login(params.account.name, manager.getPassword(params.account));
        }
        else
        {
            create = true;
            UITool.show(this, R.id.login_form);
            initLogon();
        }
    }

    private void initLogon()
    {
        Button login = UITool.findView(this, R.id.login);
        login.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    login();
                }
            }
        );
    }

    private void login()
    {
        View view = this.getCurrentFocus();
        if (view != null)
        {  
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        String host = UITool.getEditText(this, R.id.login_host);
        String user = UITool.getEditText(this, R.id.login_user);
        String pass = UITool.getEditText(this, R.id.login_password);
        login(user + "@" + host, pass);
    }

    private void login(String name, String pass)
    {
        UITool.hide(this, R.id.login_form);
        new LoginTask(this).execute(name, pass);
    }

    @Override
    public void onLogin(Smb smb)
    {
        if (create)
        {
            createAccount(smb);
        }
        else
        {
            brows();
        }
    }

    private void createAccount(Smb smb)
    {
        AccountManager manager = AccountManager.get(this);
        Bundle result = new Bundle();

        ShareAccount account = new ShareAccount(smb.getName());
        if (manager.addAccountExplicitly(account, smb.getPass(), new Bundle()))
        {
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        }
        else
        {
            result.putString(AccountManager.KEY_ERROR_MESSAGE, getString(R.string.error_account_already_exists));
        }

        setAccountAuthenticatorResult(result);
        setResult(RESULT_OK);
		finish();
    }

    @Override
    public void onError(Error error)
    {
        if (error != null)
        {
            UITool.toast(this, error);
        }
        if (create)
        {
            UITool.show(this, R.id.login_form);
        }
        else if (params.selectMode)
        {
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

    @Override
    public void onBackPressed()
    {
        if (params.selectMode)
        {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Params params = new Params(data);
        if (params.selectMode)
        {
            if (RESULT_OK == resultCode)
            {
                Intent intent = new Intent(getIntent());
                params.selectPath(intent, params.selectPath);
                setResult(RESULT_OK, intent);
            }
            finish();
        }
    }

    public void brows()
    {
        if (params.selectMode)
        {
            Intent intent = new Intent(this, Browser.class);
            params.setActionSelectPath(intent);
            params.setAccount(intent, params.account);
            startActivityForResult(intent, 0);
        }
        else
        {
            Intent intent = new Intent(this, Browser.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            params.setAccount(intent, params.account);
            startActivity(intent);
            finish();
        }
	}
}
