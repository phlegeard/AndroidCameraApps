package com.example.legeardp.mycameraapplication1;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.res.Configuration;
import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.util.SparseIntArray;
import android.view.TextureView;
import android.graphics.SurfaceTexture;


import java.io.IOException;

//public class MainActivity extends AppCompatActivity {
public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private Camera mCamera;
    private AutoFitTextureView mTextureView;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getSurfaceTexture());
        } else {
            mTextureView.setSurfaceTextureListener(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mTextureView = new AutoFitTextureView(this);
        mTextureView.setSurfaceTextureListener(this);
        setContentView(mTextureView);
    }


    private void openCamera(SurfaceTexture surface) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
            return;
        }

        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();

        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            // if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                Log.d("Camera info", "Camera found");
                cameraId = i;
                break;
            }
        }
        mCamera = Camera.open(cameraId);
        try {
            mCamera.setPreviewTexture(surface);
            int rotation = getWindowManager().getDefaultDisplay()
                    .getRotation();
            mCamera.setDisplayOrientation(ORIENTATIONS.get(rotation));
            Camera.Parameters params =  mCamera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            Camera.Size size = params.getPreviewSize();

            int orientation = getResources().getConfiguration().orientation;
            int previewWidth = size.width;
            int previewHeight = size.height;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mTextureView.setAspectRatio(
                        previewWidth, previewHeight);
            } else {
                mTextureView.setAspectRatio(
                        previewHeight, previewWidth);
            }

            mCamera.setParameters(params);
            mCamera.startPreview();
        } catch (IOException ioe) {
        }
    }


    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        openCamera(surface);
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }

        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }
}
