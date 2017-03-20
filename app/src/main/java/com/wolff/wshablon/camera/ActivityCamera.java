package com.wolff.wshablon.camera;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wolff.wshablon.R;

import java.util.Timer;
import java.util.concurrent.Delayed;

/**
 * Created by wolff on 20.03.2017.
 */

public class ActivityCamera extends AppCompatActivity {

        protected CameraDevice cameraDevice;
        TextureView textureView;
        Button btn_TakePicture;
        final String TAG = "A_CAM";
        CameraDevice.StateCallback stateCallback;
        final WCameraManager cameraManager = new WCameraManager();
        TextureView.SurfaceTextureListener textureListener;
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_camera);
            View.OnClickListener takePictureListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imagePath = cameraManager.takePicture(getApplicationContext(),cameraDevice,textureView);
                    Intent intent = new Intent();
                    intent.putExtra("ImagePath",imagePath);
                    setResult(RESULT_OK,intent);
                    Log.e(TAG,"imagePath = "+imagePath);

                    finish();
                }
            };
            textureView = (TextureView) findViewById(R.id.textureView);
            btn_TakePicture = (Button) findViewById(R.id.btn_TakePicture);
            btn_TakePicture.setOnClickListener(takePictureListener);
            textureListener = new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    //open your camera here
                    cameraManager.openCamera(getApplicationContext(),stateCallback);
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
                    finish();

                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    cameraDevice.close();
                    cameraDevice = null;

                }
            };
        }


        @Override
        public void onResume() {
            super.onResume();
            Log.e(TAG, "onResume");
            cameraManager.startBackgroundThread();
            if (textureView.isAvailable()) {
                cameraManager.openCamera(getApplicationContext(),stateCallback);
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

