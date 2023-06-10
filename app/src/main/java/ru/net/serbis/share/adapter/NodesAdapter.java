package ru.net.serbis.share.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.share.*;
import ru.net.serbis.share.tool.*;

public abstract class NodesAdapter<T> extends ArrayAdapter<T>
{
    private static int layoutId = R.layout.node;

    private class Holder
    {
        private ImageView thumbnail;
        private TextView fileName;
        private TextView fileSize;
	}
    
    public NodesAdapter(Context context)
    {
        super(context, layoutId);
	}
    
    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        Holder holder;
        if (view == null)
        {
            view = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
            holder = new Holder();
            holder.thumbnail = UITool.findView(view, R.id.thumbnail);
            holder.fileName = UITool.findView(view, R.id.filename);
            holder.fileSize = UITool.findView(view, R.id.filesize);

            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }

        T node = getItem(position);
        holder.fileName.setText(getName(node));
        holder.fileSize.setText(getInfo(node));
        if (isDir(node))
        {
            holder.thumbnail.setImageResource(R.drawable.folder);
        }
        else
        {
            holder.thumbnail.setImageResource(R.drawable.file);
        }
        return view;
    }
    
    protected abstract String getName(T node);
    protected abstract String getInfo(T node);
    protected abstract boolean isDir(T node);
}
