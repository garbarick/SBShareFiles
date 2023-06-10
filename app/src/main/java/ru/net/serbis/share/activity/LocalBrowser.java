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

public class LocalBrowser extends ListActivity<File>
{
    private File dir;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        adapter = new FilesAdapter(this);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setAdapter(adapter);
        initList();
	}

    @Override
    protected int getOptionsMenu()
    {
        return 0;
    }

    @Override
    protected int getContextMenu()
    {
        return 0;
    }

    @Override
    protected String getContextMenuHeader(File item)
    {
        return null;
    }

    private void initList()
    {
        list.setItemChecked(-1, true);
        adapter.setNotifyOnChange(false);
        adapter.clear();

        setTitle(dir.getName());
        File[] files = dir.listFiles();
        if (files != null)
        {
            List<File> children = new ArrayList<File>(Arrays.asList(files));
            Collections.sort(children, new FileComparator());
            adapter.addAll(children);
        }

        adapter.setNotifyOnChange(true);
        adapter.notifyDataSetChanged();
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        File file = adapter.getItem(position);
        if (file.isDirectory())
        {
            dir = file;
            initList();
        }
        else
        {
            list.setItemChecked(position, true);
        }
	}

    @Override
    public void onBackPressed()
    {
        dir = dir.getParentFile();
        if (dir == null)
        {
            super.onBackPressed();
            return;
        }
        initList();
	}

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ok:
                {
                    int position = list.getCheckedItemPosition();
                    if (position == -1)
                    {
                        return;
                    }
                    File file = (File)list.getItemAtPosition(position);
                    Intent intent = new Intent(getIntent());
                    params.selectPath(intent, file.getAbsolutePath());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
        super.onClick(view);
	}
}
