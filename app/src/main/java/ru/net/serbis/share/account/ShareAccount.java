package ru.net.serbis.share.account;

import android.accounts.*;
import android.os.*;
import ru.net.serbis.share.*;

public class ShareAccount extends Account
{
    public ShareAccount(Parcel parcel)
    {
        super(parcel);
    }
    
    public ShareAccount(String name)
    {
        super(name, Constants.TYPE);
	}
}
