package ru.net.serbis.share.activity;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.data.*;
import ru.net.serbis.share.tool.*;

public abstract class ListActivity<T> extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener
{
    protected ListView list;
	protected ArrayAdapter<T> adapter;
    protected Params params;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

        list = UITool.findView(this, R.id.list);
        list.setOnItemClickListener(this);

        params = new Params(getIntent());
        if (params.selectMode)
        {
            setResult(RESULT_CANCELED);
            UITool.show(this, R.id.actions);
            initActions();
        }
        else
        {
            registerForContextMenu(list);
        }
    }
    
    private void initActions()
    {
        Button ok = UITool.findView(this, R.id.ok);
        ok.setOnClickListener(this);
        Button cancel = UITool.findView(this, R.id.cancel);
        cancel.setOnClickListener(this);
    }
    
    
    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.cancel:
                finish();
                break;
		}
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    }
    
    protected abstract int getOptionsMenu();
    protected abstract int getContextMenu();
    protected abstract String getContextMenuHeader(T item);

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        int menuId = getOptionsMenu();
        if (menuId == 0)
        {
            return false;
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(menuId, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (onItemMenuSelected(item.getItemId(), null))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onItemMenuSelected(int id, T item)
    {
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        int menuId = getContextMenu();
        if (menuId == 0)
        {
            return;
        } 
        if (view.getId() == R.id.list)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            T item = adapter.getItem(info.position);
            menu.setHeaderTitle(getContextMenuHeader(item));
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(menuId, menu);
            editMenu(menu, item);
        }
    }

    protected void editMenu(ContextMenu menu, T item)
    {
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (onItemMenuSelected(item.getItemId(), adapter.getItem(info.position)))
        {
            return true;
        }
        return super.onContextItemSelected(item);
    }
	
}
