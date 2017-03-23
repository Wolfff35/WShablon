package com.wolff.wshablon.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wolff.wshablon.R;
import com.wolff.wshablon.camera.ActivityCamera;
import com.wolff.wshablon.objects.WItem;
import com.wolff.wshablon.objects.WSeasons;
import com.wolff.wshablon.sqlite.DatabaseHelper;

import java.io.File;


/**
 * Created by wolff on 13.03.2017.
 */

public class Fragment_item extends Fragment {
    private int seekDelta = 50;
    private int seekIncrement = 5;
    final String TAG = "!!!!!!!!!!";
    private WItem mainItem;
    private boolean isNewItem;
    private Menu optionsMenu;
    private ImageView ivPhoto;
    private EditText edName;
    private Spinner spSeason;
    private SeekBar seekMinTemperature;
    private SeekBar seekMaxTemperature;
    private TextView tvMinTemp;
    private TextView tvMaxTemp;

    public static Fragment_item newInstance(WItem item){

        Fragment_item fragment = new Fragment_item();
        Bundle bundle = new Bundle();
        bundle.putSerializable("WItem",item);
         fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        savedInstanceState = this.getArguments();
        if (savedInstanceState != null) {
           mainItem = (WItem)savedInstanceState.getSerializable("WItem");
            if(mainItem!=null){
                isNewItem=false;
            }else {
                isNewItem=true;
                mainItem=  new WItem();
            }
        }
        setHasOptionsMenu(true);
         super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e("FRAGMENT","onActivityResult: requestCode - "+requestCode+"; resultCode - "+resultCode);
            if(data!=null) {
                mainItem.setPictureName(data.getStringExtra("ImagePath"));
                setImage();
                Log.e("PICTURES",""+mainItem.getPictureName()+"; "+data.getStringExtra("ImagePath"));
            }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_item,container, false);
                ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
                edName = (EditText) view.findViewById(R.id.edName);
                spSeason = (Spinner) view.findViewById(R.id.spSeason);
                seekMinTemperature = (SeekBar) view.findViewById(R.id.seekMinTemperature);
                seekMaxTemperature = (SeekBar) view.findViewById(R.id.seekMaxTemperature);
                tvMinTemp = (TextView) view.findViewById(R.id.tvMinTemp);
                tvMaxTemp = (TextView) view.findViewById(R.id.tvMaxTemp);

                ArrayAdapter<WSeasons> spAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, WSeasons.values());
                spSeason.setAdapter(spAdapter);
                // tvMinTemp.setText("0");
                //tvMaxTemp.setText("0");
                seekMinTemperature.incrementProgressBy(seekIncrement);
                seekMaxTemperature.incrementProgressBy(seekIncrement);
                if(mainItem!=null&mainItem.getId()>0) {
                    spSeason.setSelection(spAdapter.getPosition(mainItem.getSeason()));
                    edName.setText(mainItem.getName());
                    seekMinTemperature.setProgress(mainItem.getMinTemperature()+seekDelta);
                    seekMaxTemperature.setProgress(mainItem.getMaxTemperature()+seekDelta);
                    tvMinTemp.setText(String.valueOf(mainItem.getMinTemperature()));
                    tvMaxTemp.setText(String.valueOf(mainItem.getMaxTemperature()));
                    setImage();
                }
                seekMinTemperature.setOnSeekBarChangeListener(minSeekBarChangeListener);
                seekMaxTemperature.setOnSeekBarChangeListener(maxSeekBarChangeListener);

                ivPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cam = new Intent(getContext(), ActivityCamera.class);
                        startActivityForResult(cam,1234);
                    }
                });
               // Log.e("CREATE FRGM","CREATE!!!");
                edName.addTextChangedListener(textChangedListener);
                return view;
    }
    private SeekBar.OnSeekBarChangeListener minSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            progress = progress / seekIncrement;
            progress = progress * seekIncrement;
            tvMinTemp.setText(String.valueOf(progress-seekDelta));
            //Log.e("SEEKBAR",""+seekBar.getId());
            //tvMinTemp.setText(String.valueOf(seekMinTemperature.getProgress()-seekDelta));
            if(seekMinTemperature.getProgress()>seekMaxTemperature.getProgress()){
                seekMaxTemperature.setProgress(seekMinTemperature.getProgress());
            }

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
            progress = progress / seekIncrement;
            progress = progress * seekIncrement;
            tvMaxTemp.setText(String.valueOf(progress-seekDelta));

            //tvMaxTemp.setText(String.valueOf(seekMaxTemperature.getProgress()-seekDelta));
            Log.e("SEEKBAR",""+seekBar.getId()+"; progress = "+progress+"; seekMaxTemperature.getProgress()"+seekMaxTemperature.getProgress());
            if(seekMinTemperature.getProgress()>seekMaxTemperature.getProgress()){
                seekMinTemperature.setProgress(seekMaxTemperature.getProgress());
            }
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
    private TextWatcher textChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setOptionsMenuVisibility();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setImage(){
        String path = mainItem.getPictureName();
        if(path!=null){
            File fill = new File(mainItem.getPictureName());
            if (fill.exists()) {
                //ivPhoto.setImageURI(Uri.parse("file:"+mainItem.getPictureName()));
                ivPhoto.setImageDrawable(Drawable.createFromPath(mainItem.getPictureName()));
            } else {
                ivPhoto.setImageResource(R.drawable.ic_menu_camera);
            }
        }else {
            ivPhoto.setImageResource(R.drawable.ic_menu_camera);
        }
        setOptionsMenuVisibility();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        this.optionsMenu = menu;
        inflater.inflate(R.menu.fragment_item_options_menu,optionsMenu);
        super.onCreateOptionsMenu(optionsMenu, inflater);
        setOptionsMenuVisibility();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete:
                Log.e("MENU FRAGM", "DELETE");
                deleteItemFromBD();
                break;
            case R.id.action_save:
                Log.e("MENU FRAGM", "SAVE");
                saveItemToBD();
                break;
            case R.id.action_undo:
                Log.e("MENU FRAGM", "UNDO");
                break;
            default:
                Log.e("MENU FRAGM", "DEFAULT");
                break;
        }
            return super.onOptionsItemSelected(item);
    }
    //==============================================================================================
    public void saveItemToBD(){
        DatabaseHelper dbh = DatabaseHelper.getInstance(getContext());
        mainItem.setName(edName.getText().toString());
        mainItem.setSeason(WSeasons.valueOf(spSeason.getSelectedItem().toString()));
        mainItem.setMinTemperature(seekMinTemperature.getProgress()-seekDelta);
        mainItem.setMaxTemperature(seekMaxTemperature.getProgress()-seekDelta);

        Log.e("SAVE ITEM","ID = "+mainItem.getId());
        if(mainItem.getId()>0) {
            dbh.item_update(mainItem);
            Log.e("UPDATE",""+mainItem.getId());

        }else {
            dbh.item_add(mainItem);
            Log.e("ADD",""+mainItem.getId());
        }
    }
    public void deleteItemFromBD(){
        if(mainItem.getId()>0){
            DatabaseHelper dbh = DatabaseHelper.getInstance(getContext());
            dbh.item_delete(mainItem);
        }else {
            Log.e("ITEM","NOTHING TO DELETE");
        }
    }
    private void setOptionsMenuVisibility(){
        if(optionsMenu!=null) {
            MenuItem item_save = optionsMenu.findItem(R.id.action_save);
            if ((edName.getText().length() > 0) && ((mainItem != null) & (mainItem.getPictureName()!=null))) {
                item_save.setVisible(true);
            } else {
                item_save.setVisible(false);
            }
        }
    }
}
