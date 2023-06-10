package ru.net.serbis.share.service;

import android.app.*;
import android.content.*;
import android.os.*;

public class AccountsService extends Service
{
	private Authenticator authenticator;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		authenticator = new Authenticator(getApplicationContext());
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return authenticator.getIBinder();
	}
}
