package com.deep.deepface;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.FaceDetector.Face;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
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
    private boolean isTaken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_androidface);
        initUI();
    }

    public void initUI() {
        FrameLayout preview = (FrameLayout)findViewById(R.id.camera_preview);
        mCamera = Camera.open(1);
        mCamera.setDisplayOrientation(90);
       AndroidDetectUtils.getInstance().setCameraParam(mCamera);
        mCameraSurPreview = new CameraSurfacePreview(this,mCamera);

        preview.addView(mCameraSurPreview);

        takeBtn = (Button)findViewById(R.id.button_capture);
        takeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isTaken){
                    takePhoto();
                }else {
                    if (mCamera!=null){
                        mCamera.startPreview();
                    }
                    takeBtn.setText(R.string.take_photo);
                    isTaken = false;


                }
            }
        });
    }
    private void takePhoto(){
        mCameraSurPreview.takePicture(new PictureCallback() {
            @Override
            public void onPictureTaken(final byte[] bytes, final Camera camera) {
                isTaken = true;
                takeBtn.setText(R.string.again_take_photo);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AndroidDetectUtils.getInstance().check(bytes, new CheckCallback() {
                            @Override
                            public void onSuccess(int count, Face[] faces) {
                                Log.e("xxxxxx","success "+" count="+count);
                            }

                            @Override
                            public void onFail() {
                                Log.e("xxxxxx","fail ");

                            }
                        });
                    }
                }).start();

                Log.e(Constants.TAG, "Picture is taken and saved.");

            }
        });
    }
}
