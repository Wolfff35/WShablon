package com.wolff.wshablon.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
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

//import java.net.URI;

/**
 * Created by wolff on 13.03.2017.
 */

public class Fragment_item extends Fragment {

    final String TAG = "!!!!!!!!!!";
    private WItem mainItem;
    private boolean isNewItem;

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
            }
        }
        setHasOptionsMenu(true);
         super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("FRAGMENT","onActivityResult: requestCode - "+requestCode+"; resultCode - "+resultCode);
        //if(requestCode==1234){
        //    if(data!=null){
                mainItem.setPictureName(data.getStringExtra("ImagePath"));
        ivPhoto.setImageDrawable(Drawable.createFromPath(mainItem.getPictureName()));
        //ivPhoto.setImageURI(Uri.parse("file:"+mainItem.getPictureName()));
                Log.e("PICTURES",""+mainItem.getPictureName()+"; "+data.getStringExtra("ImagePath"));

       //     }
       // }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_item,container, false);
                ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
                edName = (EditText) view.findViewById(R.id.edName);
                spSeason = (Spinner) view.findViewById(R.id.spSeason);

                ArrayAdapter<WSeasons> spAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, WSeasons.values());
                spSeason.setAdapter(spAdapter);
                spSeason.setSelection(spAdapter.getPosition(mainItem.getSeason()));
                edName.setText(mainItem.getName());
                //ivPhoto.setImageDrawable(Drawable.createFromPath(mainItem.getPictureName()));
                ivPhoto.setImageURI(Uri.parse("file:"+mainItem.getPictureName()));
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

}
