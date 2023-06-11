package ru.net.serbis.share.task;

import java.io.*;
import java.util.*;
import ru.net.serbis.share.data.*;
import ru.net.serbis.share.data.Error;

public interface BrowserCallback
{
    void onError(Error error);
    void onChildren(ShareFile dir, List<ShareFile> children);
    void progress(int progress);
    void onDownloadFinish(File file);
    void onDelete();
    void onMoveFinish();
    void onUploadFinish();
    void onFilesList(File file);
}
