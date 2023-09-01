package ru.net.serbis.share;

import java.util.regex.*;

public interface Constants
{
    String TYPE = Constants.class.getPackage().getName();
    String TOKEN = TYPE + ".TOKEN";
    String TOKEN_TYPE = TYPE + ".TOKEN_TYPE";
    String ACCOUNT = TYPE + ".ACCOUNT";
    String SELECT_MODE = TYPE + ".SELECT_MODE";
    String SELECT_PATH = TYPE + ".SELECT_PATH";
    String PATH = TYPE + ".PATH";
    String ACTION = TYPE + ".ACTION";
    String FILES_LIST = TYPE + ".FILES_LIST";
    String FILE = TYPE + ".FILE";
    String ERROR = TYPE + ".ERROR";
    String ERROR_CODE = TYPE + ".ERROR_CODE";
    String RESULT = TYPE + ".RESULT";
    String PROGRESS = TYPE + ".PROGRESS";
    String BUFFER_SIZE = TYPE + ".BUFFER_SIZE";
    int DEFAULT_BUFFER_SIZE = 10240;

    String SBSHARE = "sbshare";
    String SUCCESS = "SUCCESS";

    int ACTION_MOVE = 100;
    int ACTION_SELECT_PATH = 101;
    int ACTION_SELECT_ACCOUNT_PATH = 102;
    int ACTION_GET_FILES_LIST = 103;
    int ACTION_GET_FILE = 104;
    int ACTION_REMOVE_FILE = 105;
    int ACTION_PING = 106;
    int ACTION_UPLOAD = 107;

    Pattern PATH_PATTERN = Pattern.compile("^\\/\\/" + SBSHARE + "\\/(.*?)(\\/.*)$");

    String SMB = "smb://";

    int ERROR_NETWORK_IS_NOT_AVAILABLE = 400;
	int ERROR_FILE_IS_NOT_FOUND = 401;
    int ERROR_UPLOAD = 402;
    int ERROR_MOVE = 403;
    int ERROR_BROWSE = 404;
    int ERROR_DOWNLOAD = 405;
    int ERROR_DELETE = 406;
    int ERROR_LOGIN = 407;
    int ERROR_NOT_IMPLEMENTED = 408;
    int ERROR_FILES_LIST = 409;
}
