package com.handpay.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.handpay.camera.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;

import uk.co.senab.photoview.PhotoView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_watermark_camera;
    private Button btn_sanner_camera;
    private PhotoView iv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_watermark_camera = (Button) findViewById(R.id.btn_watermark_camera);
        btn_sanner_camera = (Button) findViewById(R.id.btn_sanner_camera);
        iv_result = (PhotoView) findViewById(R.id.iv_result);

        btn_watermark_camera.setOnClickListener(this);
        btn_sanner_camera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_watermark_camera:
                intent.putExtra("CODE", CameraCardActivity.TAKE_PICTURE_BLACK);
                intent.setClass(MainActivity.this, CameraCardActivity.class);
                startActivityForResult(intent, CameraCardActivity.TAKE_PICTURE_BLACK);
                break;
            case R.id.btn_sanner_camera:
                intent.setClass(MainActivity.this, CameraActivity.class);
                String pathStr = FileUtils.root;
                String nameStr = System.currentTimeMillis() + ".jpg";
                String typeStr = "idcardFront";
                intent.putExtra("path", pathStr);
                intent.putExtra("name", nameStr);
                intent.putExtra("type", typeStr);
                startActivityForResult(intent, 100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CameraCardActivity.TAKE_PICTURE_BLACK) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String path = extras.getString(CameraCardActivity.RESULT_PATH);
                showImageByZip(path);
            }
        } else if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String path = extras.getString("path");
                String type = extras.getString("type");
                showImageByZip(path);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /***
     * 根据文件路径显示图片
     *
     * @param path
     */
    private void showImageWithPath(String path) {
        File file = new File(path);
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(inStream);
            iv_result.setImageBitmap(bitmap);
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 压缩后的图片
     *
     * @param path
     */
    public void showImageByZip(String path) {
        byte[] result = FileUtils.getCatBitmap(path, 480, 800, 200);
        final Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0,
                result.length);
        iv_result.setImageBitmap(bitmap);
    }
}
