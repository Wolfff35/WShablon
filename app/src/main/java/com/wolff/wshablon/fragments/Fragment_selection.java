package com.wolff.wshablon.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.wolff.wshablon.R;
import com.wolff.wshablon.listAdapters.CatalogListAdapter;
import com.wolff.wshablon.objects.WItem;
import com.wolff.wshablon.objects.WSeasons;
import com.wolff.wshablon.sqlite.DatabaseHelper;
import com.wolff.wshablon.yahooWeather.WeatherInfo;

import java.util.ArrayList;


/**
 * Created by wolff on 24.03.2017.
 */

public class Fragment_selection extends Fragment {
    private Fragment_catalog.Fragment_catalogListener catalogListener;
    private CatalogListAdapter catalogListAdapter;
    private ArrayList<WItem> mainCatalogList = new ArrayList<>();
    private WeatherInfo mWeatherInfo;
    private SeekBar seekMinTemp;
    private SeekBar seekMaxTemp;
    private Spinner spSeason;
    private TextView tvWeatherInfo;
    private ListView lvSelection;
    private TextView tvMinTemp;
    private TextView tvMaxTemp;
    private int seekDelta = 50;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mWeatherInfo = (WeatherInfo) args.getSerializable("WeatherInfo");
        //Log.e("CATALOG","WEATHER INFO "+mWeatherInfo.getCurrentTemp());

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selection,container, false);
        seekMinTemp = (SeekBar)view.findViewById(R.id.seekMinTemperature);
        seekMaxTemp = (SeekBar)view.findViewById(R.id.seekMaxTemperature);
        spSeason = (Spinner) view.findViewById(R.id.spSeason);
        lvSelection = (ListView)view.findViewById(R.id.lvSelection);
        tvWeatherInfo = (TextView)view.findViewById(R.id.tvWeatherInfo);
        tvMinTemp = (TextView)view.findViewById(R.id.tvMinTemp);
        tvMaxTemp = (TextView)view.findViewById(R.id.tvMaxTemp);
        if(mWeatherInfo!=null) {
            tvWeatherInfo.setText("" + mWeatherInfo.getCurrentConditionDate() + "\n Температура в Киеве: " + mWeatherInfo.getCurrentTemp() + "°С \n Ветер " + mWeatherInfo.getWindSpeed() + " м/с");
            seekMinTemp.setProgress(mWeatherInfo.getCurrentTemp()+seekDelta-5);
            seekMaxTemp.setProgress(mWeatherInfo.getCurrentTemp()+seekDelta+5);
            tvMinTemp.setText(String.valueOf(mWeatherInfo.getCurrentTemp()-5));
            tvMaxTemp.setText(String.valueOf(mWeatherInfo.getCurrentTemp()+5));
        }else{
            tvWeatherInfo.setText("Нет интернета");
            seekMinTemp.setProgress(seekDelta);
            seekMaxTemp.setProgress(seekDelta);
            tvMinTemp.setText(String.valueOf(0));
            tvMaxTemp.setText(String.valueOf(0));
        }
        ArrayAdapter<WSeasons> spAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, WSeasons.values());
        spSeason.setAdapter(spAdapter);


        seekMinTemp.setOnSeekBarChangeListener(minSeekBarChangeListener);
        seekMaxTemp.setOnSeekBarChangeListener(maxSeekBarChangeListener);
        spSeason.setOnItemSelectedListener(spSeasonOnItemSelectedListener);
        makeSelection();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            catalogListener = (Fragment_catalog.Fragment_catalogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }
//====================================
    private Spinner.OnItemSelectedListener spSeasonOnItemSelectedListener = new Spinner.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        makeSelection();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    };
    private SeekBar.OnSeekBarChangeListener minSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tvMinTemp.setText(String.valueOf(progress-seekDelta));
        if(seekMinTemp.getProgress()>seekMaxTemp.getProgress()){
            seekMaxTemp.setProgress(seekMinTemp.getProgress());
        }
    makeSelection();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
};
    private SeekBar.OnSeekBarChangeListener maxSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            tvMaxTemp.setText(String.valueOf(progress-seekDelta));
          if(seekMinTemp.getProgress()>seekMaxTemp.getProgress()){
                seekMinTemp.setProgress(seekMaxTemp.getProgress());
            }
            makeSelection();

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //Log.e("SEEK START",""+seekMaxTemperature.getProgress());
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //Log.e("SEEK STOP",""+seekMaxTemperature.getProgress());

        }
    };

private void makeSelection(){
  //Log.e("MAKE","SELECTION");
        Context context = getActivity().getApplicationContext();
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        mainCatalogList = dbHelper.items_getSelection_list(seekMinTemp.getProgress()-seekDelta,seekMaxTemp.getProgress()-seekDelta,(WSeasons) spSeason.getSelectedItem());
        catalogListAdapter= new CatalogListAdapter(context,mainCatalogList);
        lvSelection.setAdapter(catalogListAdapter);
        lvSelection.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            catalogListener.onItemInListSelected(mainCatalogList.get(i));
        }
    });

}

}
