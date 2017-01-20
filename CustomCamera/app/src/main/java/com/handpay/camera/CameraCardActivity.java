package com.handpay.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.handpay.camera.utils.FileUtils;

import java.util.List;


public class CameraCardActivity extends Activity implements SurfaceHolder.Callback, SensorEventListener, AutoFocusCallback {
    private Camera mCamera;
    private SurfaceView mPreview;
    private Sensor mAccelerometer;
    private SensorManager mSensorManager;
    private Button mTakePhotoBtn;
    private float motionX = 0;
    private float motionY = 0;
    private float motionZ = 0;
    public static String RESULT_PATH = "PATH";
    public static String REQUEST_CODE = "CODE";
    public static int TAKE_PICTURE_FRONT = 2;
    public static int TAKE_PICTURE_BLACK = 3;
    private boolean isFourced = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera);
        int requestCode = this.getIntent().getIntExtra(REQUEST_CODE, 1);
        FrameLayout mWaterFrameLayout = (FrameLayout) this.findViewById(R.id.id_card_water_fl);
        if (requestCode == TAKE_PICTURE_FRONT) {
            mWaterFrameLayout.setVisibility(View.VISIBLE);
            mWaterFrameLayout.setBackgroundResource(R.mipmap.id_card_water_font);
        } else if (requestCode == TAKE_PICTURE_BLACK) {
            mWaterFrameLayout.setVisibility(View.VISIBLE);
            mWaterFrameLayout.setBackgroundResource(R.mipmap.id_card_water_mark_back);
        } else {
            mWaterFrameLayout.setVisibility(View.INVISIBLE);
        }
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mPreview = (SurfaceView) findViewById(R.id.preview);
        mPreview.getHolder().addCallback(this);
        mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mTakePhotoBtn = (Button) this.findViewById(R.id.take_btn);
        mTakePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFourced)
                    mCamera.takePicture(null, null, jpegCallback);
                else
                    Toast.makeText(CameraCardActivity.this, "聚焦完成后才能拍摄清晰图片", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            Camera.Parameters ps = camera.getParameters();
            if (ps.getPictureFormat() == ImageFormat.JPEG) {
                Log.d("tag", "拍照后:" + data.length);
                String path = FileUtils.saveByteFile(data, CameraCardActivity.this);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(RESULT_PATH, path);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    };

    public void onResume() {
        super.onResume();
        try {
            mCamera = Camera.open();
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } catch (RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(this, "请打开摄像头权限", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    public void onPause() {
        super.onPause();
        try {
            mCamera.release();
            mCamera = null;
            mSensorManager.unregisterListener(this, mAccelerometer);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (Math.abs(event.values[0] - motionX) > 0.2 || Math.abs(event.values[1] - motionY) > 0.2 || Math.abs(event.values[2] - motionZ) > 0.2) {
            try {
                mCamera.autoFocus(this);
            } catch (RuntimeException e) {

            }
        }

        motionX = event.values[0];
        motionY = event.values[1];
        motionZ = event.values[2];
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters params = mCamera.getParameters();
        List<Camera.Size> previewSize = params.getSupportedPreviewSizes();
        List<Camera.Size> pictureSize = params.getSupportedPictureSizes();
        Camera.Size bestDimens = null;
        for (Camera.Size dimens : pictureSize) {
            Log.d("TAG", "拍照支持:width" + dimens.width + "height:" + dimens.height);
            if (dimens.width <= 1024 && dimens.height <= 768) {
                if (bestDimens == null || (dimens.width > bestDimens.width && dimens.height > bestDimens.height)) {
                    bestDimens = dimens;
                }
            }
        }
        Log.d("tag", "拍照支持:bestDimens" + bestDimens.width + "height:" + bestDimens.height);
        params.setPictureSize(bestDimens.width, bestDimens.height);
        params.setPreviewSize(previewSize.get(0).width, previewSize.get(0).height);
        params.setJpegQuality(100);
        mCamera.setParameters(params);
        try {
            mCamera.setPreviewDisplay(mPreview.getHolder());
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void surfaceCreated(SurfaceHolder arg0) {
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
    }

    public void onAutoFocus(boolean success, Camera camera) {
        isFourced = success;
    }
}
