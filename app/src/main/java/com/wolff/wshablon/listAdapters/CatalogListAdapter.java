package com.wolff.wshablon.listAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wolff.wshablon.R;
import com.wolff.wshablon.objects.WItem;

import java.io.File;
import java.util.ArrayList;

import static com.wolff.wshablon.R.id.ivPhoto;

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
        ImageView ivPhoto;
        if(view==null){
            view = lInflater.inflate(R.layout.adapter_catalog_item,parent,false);
        }
        tvItemName = (TextView)view.findViewById(R.id.tvItemName);
        ivPhoto = (ImageView)view.findViewById(R.id.ivPhoto);
        WItem currentItem = getItem(position);
        String name;
        if(currentItem.getSeason().name().equalsIgnoreCase("НЕТ")){
            name = currentItem.getName()+"("+currentItem.getMinTemperature()+":"+currentItem.getMaxTemperature()+")";
        }else {
            name = currentItem.getName()+", ("+currentItem.getMinTemperature()+":"+currentItem.getMaxTemperature()+"°C), "+currentItem.getSeason().name();
        }
        tvItemName.setText(name);
        //!!!
        //ivPhoto.setImageDrawable(Drawable.createFromPath(currentItem.getPictureName()));
        setImage(currentItem,ivPhoto);
        return view;
    }

    private void setImage(WItem currentItem,ImageView ivPhoto){
        String path = currentItem.getPictureName();
         if(path!=null){
            File fill = new File(currentItem.getPictureName());
            if (fill.exists()) {
                String selectedImagePath = currentItem.getPictureName();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;

                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                int height = bitmap.getHeight(), width = bitmap.getWidth();

                if (height > 200 && width > 200) {
                    Bitmap imgbitmap = BitmapFactory.decodeFile(selectedImagePath, options);
                    ivPhoto.setImageBitmap(imgbitmap);

                    System.out.println("Need to resize");
                }
            } else {
                ivPhoto.setImageResource(R.drawable.ic_menu_camera);
            }
        }else {
            ivPhoto.setImageResource(R.drawable.ic_menu_camera);
        }




        //============================
    }

}
