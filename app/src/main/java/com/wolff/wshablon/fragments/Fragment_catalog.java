package com.wolff.wshablon.fragments;


import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.wolff.wshablon.listAdapters.CatalogListAdapter;
import com.wolff.wshablon.objects.WItem;
import com.wolff.wshablon.objects.WSeasons;

import java.util.ArrayList;

/**
 * Created by wolff on 15.03.2017.
 */

public class Fragment_catalog extends ListFragment {
    private Fragment_catalogListener catalogListener;
    CatalogListAdapter catalogListAdapter;
    ArrayList<WItem> mainCatalogList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            catalogListener = (Fragment_catalogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainCatalogList = new ArrayList<>();

        //TEST String name,WSeasons season,int minTemperature,int maxTemperature,String pictureName
        mainCatalogList.add(new WItem("name 1", WSeasons.AUTUMN,10,30,"c:/temp/q.jpg"));
        mainCatalogList.add(new WItem("name 2", WSeasons.SPRING,1,20,"c:/temp/q.jpg"));
        mainCatalogList.add(new WItem("name 3", WSeasons.SUMMER,30,50,"/storage/emulated/0/Pictures/WCatalog/item_1489687170280.jpg"));
        mainCatalogList.add(new WItem("name 4", WSeasons.WINTER,0,6,"/storage/emulated/0/Pictures/WCatalog/item_1489673404231.jpg"));

    }

        @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Context context = getActivity().getApplicationContext();
        catalogListAdapter= new CatalogListAdapter(context,mainCatalogList);
        setListAdapter(catalogListAdapter);
        getListView().setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                catalogListener.onItemInListSelected(mainCatalogList.get(i));
            }
        });
    }

    public interface Fragment_catalogListener{
        void onItemInListSelected(WItem item);

    }

}
