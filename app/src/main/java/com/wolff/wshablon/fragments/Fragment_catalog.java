package com.wolff.wshablon.fragments;


import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.wolff.wshablon.listAdapters.CatalogListAdapter;
import com.wolff.wshablon.objects.WItem;
import com.wolff.wshablon.objects.WSeasons;
import com.wolff.wshablon.sqlite.DatabaseHelper;
import com.wolff.wshablon.yahooWeather.WeatherInfo;

import java.util.ArrayList;

/**
 * Created by wolff on 15.03.2017.
 */

public class Fragment_catalog extends ListFragment {
    private Fragment_catalogListener catalogListener;
    private CatalogListAdapter catalogListAdapter;
    private ArrayList<WItem> mainCatalogList;
    private WeatherInfo mWeatherInfo;
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
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(getContext());
        mainCatalogList = dbHelper.items_getAll_list();
        Log.e("===LIST","size = "+mainCatalogList.size());

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
