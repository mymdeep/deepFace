package com.deep.deepface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.FaceDetector.Face;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import deep.com.face_deep.AndroidDetectUtils;
import deep.com.face_deep.CameraSurfacePreview;
import deep.com.face_deep.CheckCallback;
import deep.com.face_deep.Constants;

/**
 * Created by wangfei on 2018/3/8.
 */

public class AndroidFaceActivity extends Activity{
    Button takeBtn;
    private CameraSurfacePreview mCameraSurPreview = null;
    private Camera mCamera;
    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_androidface);
        initUI();
    }
    private Camera.Size getBestPictureSize(List<Camera.Size> list){
        Camera.Size maxSize = null;
        if (list!=null&& list.size()>0){
            maxSize = list.get(0);
            for (Camera.Size s:list){
                if (s.width>maxSize.width){
                    maxSize = s;
                }
            }
        }
        return maxSize;
    }
    public void initUI() {
        FrameLayout preview = (FrameLayout)findViewById(R.id.camera_preview);
        mCamera = Camera.open(1);
        mCamera.setDisplayOrientation(90);
        Camera.Parameters parameters=mCamera.getParameters();
        Camera.Size maxSize = getBestPictureSize(parameters.getSupportedPictureSizes());

      if (maxSize!=null){
          parameters.setPictureSize(maxSize.width,maxSize.height);
          parameters.setPreviewSize(maxSize.width,maxSize.height);
      }

        try {
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            //非常罕见的情况
            //个别机型在SupportPreviewSizes里汇报了支持某种预览尺寸，但实际是不支持的，设置进去就会抛出RuntimeException.
            Log.e(Constants.TAG,"个别机型尺寸不支持");
            try {
                //遇到上面所说的情况，只能设置一个最小的预览尺寸
                parameters.setPreviewSize(1920, 1080);
                parameters.setPictureSize(1920, 1080);
                mCamera.setParameters(parameters);
            } catch (Exception e1) {
                Log.e(Constants.TAG,"1920 报错");
            }
        }
        mCameraSurPreview = new CameraSurfacePreview(this,mCamera);
        preview.addView(mCameraSurPreview);

        takeBtn = (Button)findViewById(R.id.button_capture);
        takeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //File outputImage = new File(Environment.getExternalStorageDirectory(),
                //    "tempImage" + ".jpg");
                //try {
                //    if (outputImage.exists()) {
                //        outputImage.delete();
                //    }
                //    outputImage.createNewFile();
                //} catch (IOException e) {
                //    e.printStackTrace();
                //}
                //imageUri = Uri.fromFile(outputImage);
                //Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                //startActivityForResult(intent, TAKE_PHOTO);
                mCameraSurPreview.takePicture(new PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {
                        FileOutputStream outStream = null;
                        Log.e(Constants.TAG,"data = "+bytes.length);



                        try {
                            String name = System.currentTimeMillis()+"a";

                            File outputImage = new File(Environment.getExternalStorageDirectory(),
                                name + ".jpg");
                            Log.e(Constants.TAG,"outputImage="+outputImage);
                            try {
                                if (outputImage.exists()) {
                                    outputImage.delete();
                                }
                                outputImage.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            outStream = new FileOutputStream(outputImage);
                            outStream.write(bytes);
                            outStream.close();
                             final Bitmap bitmap = BitmapFactory.decodeFile(outputImage.getPath()).copy(Bitmap.Config.RGB_565, true);
                            //new Thread(new Runnable() {
                            //    @Override
                            //    public void run() {
                                    AndroidDetectUtils.getInstance().check(bitmap, new CheckCallback() {
                                        @Override
                                        public void onSuccess(int count, Face[] faces) {
                                            Toast.makeText(AndroidFaceActivity.this,"success count="+count+" face="+faces[0].eyesDistance(),Toast.LENGTH_LONG ).show();
                                        }

                                        @Override
                                        public void onFail() {
                                            Toast.makeText(AndroidFaceActivity.this,"fail",Toast.LENGTH_LONG ).show();

                                        }
                                    });
                            //    }
                            //}).start();

                            Log.e(Constants.TAG, "Picture is taken and saved.");
                        }catch (Exception e) {
                            Log.e(Constants.TAG,"Error starting camera preview:"+e.getMessage());
                        }
                    }
                });
            }
        });
    }

}
