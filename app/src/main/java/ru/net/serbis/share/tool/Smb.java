package ru.net.serbis.share.tool;

import android.accounts.*;
import android.content.*;
import java.util.*;
import jcifs.*;
import jcifs.config.*;
import jcifs.context.*;
import jcifs.smb.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.data.*;

public class Smb
{
    private Context context;
    private String host;
    private String user;
    private String pass;

    public Smb(Context context, String name, String pass)
    {
        this.context = context;
        String[] data = name.split("@");
        this.host = data[1];
        this.user = data[0];
        this.pass = pass;
    }

    public Smb(Context context, Account account)
    {
        this(context, account.name, AccountManager.get(context).getPassword(account));
    }

    public Context getContext()
    {
        return context;
    }

    public String getHost()
    {
        return host;
    }

    public String getUser()
    {
        return user;
    }

    public String getPass()
    {
        return pass;
    }

    public String getName()
    {
        return user + "@" + host;
    }

    public ShareFile getShareFile(SmbFile file) throws Exception
    {
        return new ShareFile(getRootPath(), file);
    }

    public ShareFile getShareViewFile(SmbFile file) throws Exception
    {
        return new ShareFile(getRootPath(), file, context);
    }

    public SmbFile getFile(String path) throws Exception
    {
        return new SmbFile(getRootPath() + path, getAuth());
    }

    private String getRootPath()
    {
        return Constants.SMB + host;
    }

    public SmbFile getRoot() throws Exception
    {
        return new SmbFile(getRootPath(), getAuth());
    }

    public void check() throws Exception
    {
        SmbFile dir = getRoot();
        dir.listFiles(new SmbFileFilter()
            {
                @Override
                public boolean accept(SmbFile child)
                {
                    return false;
                }
        });
    }

    public CIFSContext getAuth() throws Exception
    {
        Properties prop  = new Properties();
        prop.setProperty("jcifs.smb.client.enableSMB2", "true");
        prop.setProperty("jcifs.smb.client.dfs.disabled", "true");
        Configuration config = new PropertyConfiguration(prop);
        BaseContext baseCxt = new BaseContext(config);
        return baseCxt.withCredentials(new NtlmPasswordAuthenticator(host, user, pass));
    }
}
