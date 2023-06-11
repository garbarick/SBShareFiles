package ru.net.serbis.share.task;

import ru.net.serbis.share.tool.*;
import ru.net.serbis.share.data.Error;

public interface LoginCallback
{
    void onLogin(Smb smb);
    void onError(Error error);
}
