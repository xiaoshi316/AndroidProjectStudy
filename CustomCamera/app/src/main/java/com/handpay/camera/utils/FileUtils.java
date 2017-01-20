package com.handpay.camera.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URI;

import static android.graphics.BitmapFactory.decodeFile;

/**
 * @author sxshi on 2017/1/20.
 * @email:emotiona_xiaoshi@126.com
 * @describe:Describe the function  of the current class
 */

public class FileUtils {
    public static final String root = Environment.getExternalStorageDirectory() + "/zzTong/cardImg/";

    /***
     * 根据文件名保存文件
     *
     * @param data
     * @param context
     * @return
     */
    public static String saveByteFile(byte[] data, Context context) {
        String path = root + System.currentTimeMillis() + ".jpg";
        //1保存图片
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dirs = new File(root);
                if (!dirs.exists()) {
                    dirs.mkdirs();
                } else {
                    File[] files = dirs.listFiles();
                    Log.e("tag", "files.len=" + files.length);
                    for (File file : files) {
                        Log.e("tag", "fileName=" + file.getName());
                        boolean isDelete = file.delete();
                        Log.e("tag", "isDelete=" + isDelete);
                    }
                }
                File file = new File(path);
                if (!file.exists())
                    // 创建文件
                    file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data);
                fos.close();
                //2.往手机系统插入保存的图片
                try {
                    MediaStore.Images.Media.insertImage(context.getContentResolver(),
                            file.getAbsolutePath(), System.currentTimeMillis() + ".jpg", null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // 最后通知图库更新
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return path;
    }

    /**
     * @param data
     * @param bAutoDelFile
     * @return
     */
    public static String saveByteFile(byte[] data, boolean bAutoDelFile) {
        String path = root + System.currentTimeMillis() + ".jpg";
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dirs = new File(root);
                if (!dirs.exists()) {
                    dirs.mkdirs();
                } else {
                    if (bAutoDelFile) {
                        File[] files = dirs.listFiles();
                        for (File file : files) {
                            boolean isDelete = file.delete();
                        }
                    }
                }
                File file = new File(path);
                if (!file.exists())
                    file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data);
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return path;
    }

    /**
     * 压缩图片
     *
     * @param filepath   图片地址
     * @param req_width  期望宽度
     * @param req_height 期望高度
     * @param reqSize    压缩图片大小
     * @return
     */
    public static byte[] getCatBitmap(String filepath, int req_width, int req_height, int reqSize) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        filepath = filepath.startsWith("file:///") || filepath.startsWith("content:") ? new File(URI.create(filepath)).getAbsolutePath() : filepath;
        Log.d("TAG", "-----------" + filepath + "------------");
        options.inJustDecodeBounds = true;
        Bitmap res_bitmap = BitmapFactory.decodeFile(filepath, options);
        options.inSampleSize = calculateInSampleSize(options, req_width, req_height);
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inJustDecodeBounds = false;
        Log.d("TAG", "手机品牌：" + android.os.Build.BRAND);
        if (android.os.Build.BRAND.equals("Sony") || android.os.Build.BRAND.equals("MeiZu")) {
            /***
             * <p>Sony MeiZu手机用BitmapFactory.decodeFile(filepath, options)方式获取Bitmap retun null </p>
             * <p>所以检测当 return null　的时候用下面 decodeByteArray(bitmapBytes,0,bitmapBytes.length,options)</p>
             */
            if (res_bitmap == null) {
                File file = new File(filepath);
                FileInputStream fileInputStream = null;
                byte[] bitmapBytes = new byte[(int) file.length()];
                try {
                    fileInputStream = new FileInputStream(file);
                    fileInputStream.read(bitmapBytes);
                    fileInputStream.close();
                    res_bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            res_bitmap = decodeFile(filepath, options);
        }
        //压缩图片
        int reqQuality = 100;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        res_bitmap.compress(Bitmap.CompressFormat.JPEG, reqQuality, baos);
        try {
            while (baos.toByteArray().length / 1024 > reqSize) {
                baos.reset();
                reqQuality -= 5;
                res_bitmap.compress(Bitmap.CompressFormat.JPEG, reqQuality, baos);
            }
        } catch (Exception e) {
            res_bitmap.compress(Bitmap.CompressFormat.JPEG, 5, baos);
        }
        return baos.toByteArray();
    }

    /**
     * 计算压缩值
     *
     * @param options
     * @param reqWidth  期望宽度
     * @param reqHeight 期望高度
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        // inSampleSize这个值决定图片的压缩率，取接近2的值
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        Log.i("result", "压缩比率：" + inSampleSize);
        return inSampleSize;
    }
}
