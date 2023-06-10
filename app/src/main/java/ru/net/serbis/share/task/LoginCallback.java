package ru.net.serbis.share.task;

import ru.net.serbis.share.tool.*;

public interface LoginCallback
{
    void onLogin(Smb smb);
    void onError(int errorCode, String error);
}
