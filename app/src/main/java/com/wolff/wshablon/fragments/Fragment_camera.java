package com.wolff.wshablon.fragments;

import android.app.Fragment;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraDevice;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wolff.wshablon.R;
import com.wolff.wshablon.camera.WCameraManager;


/**
 * Created by wolff on 14.03.2017.
 */

public class Fragment_camera extends Fragment {
    protected CameraDevice cameraDevice;
    TextureView textureView;
    Button btn_TakePicture;
    final String TAG = "FRAG_CAM";
     CameraDevice.StateCallback stateCallback;
    final WCameraManager cameraManager = new WCameraManager();
    TextureView.SurfaceTextureListener textureListener;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        Log.e(TAG, "onCreateView");
        View.OnClickListener takePictureListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imagePath = cameraManager.takePicture(getContext(),cameraDevice,textureView);
                Log.e(TAG,"imagePath = "+imagePath);
                //TODO возврат фрагмента после фото
                //указание каталога хранения файлов
                //возможность выбора готовой картинки

            }
        };
        textureView = (TextureView) view.findViewById(R.id.textureView);
        btn_TakePicture = (Button) view.findViewById(R.id.btn_TakePicture);
        btn_TakePicture.setOnClickListener(takePictureListener);
        textureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                //open your camera here
                cameraManager.openCamera(getContext(),stateCallback);
            }
            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                // Transform you image captured size according to the surface width and height
            }
            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }
            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        };
        stateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                Log.e(TAG, "onOpened");
                cameraDevice = camera;
                cameraManager.createCameraPreview(textureView,cameraDevice);

            }

            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {
                cameraDevice.close();

            }

            @Override
            public void onError(@NonNull CameraDevice camera, int error) {
                cameraDevice.close();
                cameraDevice = null;

            }
        };
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        cameraManager.startBackgroundThread();
        if (textureView.isAvailable()) {
            cameraManager.openCamera(getContext(),stateCallback);
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        } }

    @Override
    public void onPause() {
        Log.e(TAG, "onPause");
        //closeCamera();
        cameraManager.stopBackgroundThread();
        super.onPause();
    }


}
