package com.wolff.wshablon.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Spinner;

import com.wolff.wshablon.R;
import com.wolff.wshablon.camera.ActivityCamera;
import com.wolff.wshablon.objects.WItem;
import com.wolff.wshablon.objects.WSeasons;
import com.wolff.wshablon.sqlite.DatabaseHelper;

import java.io.File;

//import java.net.URI;

/**
 * Created by wolff on 13.03.2017.
 */

public class Fragment_item extends Fragment {

    final String TAG = "!!!!!!!!!!";
    private WItem mainItem;
    private boolean isNewItem;
    private Menu optionsMenu;
    private ImageView ivPhoto;
    private EditText edName;
    private Spinner spSeason;
    private EditText edMinTemperature;
    private EditText edMaxTemperature;

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
        Log.e("FRAGMENT","onActivityResult: requestCode - "+requestCode+"; resultCode - "+resultCode);
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
                edMinTemperature = (EditText) view.findViewById(R.id.edMinTemperature);
                edMaxTemperature = (EditText) view.findViewById(R.id.edMaxTemperature);

                ArrayAdapter<WSeasons> spAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, WSeasons.values());
                spSeason.setAdapter(spAdapter);
                if(mainItem!=null&mainItem.getId()>0) {
                    spSeason.setSelection(spAdapter.getPosition(mainItem.getSeason()));
                    edName.setText(mainItem.getName());
                    edMinTemperature.setText(String.valueOf(mainItem.getMinTemperature()));
                    edMaxTemperature.setText(String.valueOf(mainItem.getMaxTemperature()));
                    setImage();
                }
                //ivPhoto.setImageURI(Uri.parse("file:"+mainItem.getPictureName()));
                ivPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cam = new Intent(getContext(), ActivityCamera.class);
                        startActivityForResult(cam,1234);
                    }
                });
                Log.e("CREATE FRGM","CREATE!!!");
                return view;
    }

    private void setImage(){
        String path = mainItem.getPictureName();
        if(path!=null){
            File fill = new File(mainItem.getPictureName());
            if (fill.exists()) {
                ivPhoto.setImageDrawable(Drawable.createFromPath(mainItem.getPictureName()));
            } else {
                ivPhoto.setImageResource(R.drawable.ic_menu_camera);
            }
        }else {
            ivPhoto.setImageResource(R.drawable.ic_menu_camera);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        this.optionsMenu = menu;
        inflater.inflate(R.menu.fragment_item_options_menu,optionsMenu);
        super.onCreateOptionsMenu(optionsMenu, inflater);
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
       //mainItem.setPictureName();
        mainItem.setSeason(WSeasons.valueOf(spSeason.getSelectedItem().toString()));
        try {
            mainItem.setMinTemperature(Integer.valueOf(edMinTemperature.getText().toString()));
        }catch (Exception e){
            mainItem.setMinTemperature(0);
        }
        try{
            mainItem.setMaxTemperature(Integer.valueOf(edMaxTemperature.getText().toString()));
        }catch (Exception e){
            mainItem.setMaxTemperature(0);
        }
        Log.e("SAVE ITEM","ID = "+mainItem.getId());
        if(mainItem.getId()>0) {
            dbh.item_update(mainItem);

        }else {
            dbh.item_add(mainItem);
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
}
