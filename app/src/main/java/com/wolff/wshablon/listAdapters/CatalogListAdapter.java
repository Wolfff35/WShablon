package com.wolff.wshablon.listAdapters;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wolff.wshablon.R;
import com.wolff.wshablon.objects.WItem;

import java.util.ArrayList;

/**
 * Created by wolff on 16.03.2017.
 */

public class CatalogListAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater lInflater;
    private ArrayList<WItem> myCatalog;

    public CatalogListAdapter(Context context,ArrayList<WItem> myCatalog){
        this.context = context;
        this.myCatalog = myCatalog;
        this.lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if(myCatalog!=null) {
            return myCatalog.size();
        }else{
            return 0;
        }
    }

    @Override
    public WItem getItem(int position) {
        return myCatalog.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TextView tvItemName;
        if(view==null){
            view = lInflater.inflate(R.layout.adapter_catalog_item,parent,false);
        }
        tvItemName = (TextView)view.findViewById(R.id.tvItemName);
        WItem currentItem = getItem(position);
        tvItemName.setText(currentItem.getName());
        return view;
    }
}
