package com.deep.deepface;

import java.io.File;

import android.content.Intent;
import android.database.Cursor;
import android.media.FaceDetector.Face;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import deep.com.face_deep.AndroidDetectUtils;
import deep.com.face_deep.CheckCallback;

public class MainActivity extends AppCompatActivity {
    private final int OPEN_ALBUM_CODE = 233;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // open camera
        findViewById(R.id.button4).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AndroidFaceActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        // open album
        findViewById(R.id.button5).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // 设定结果返回
                startActivityForResult(i, OPEN_ALBUM_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_ALBUM_CODE) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            // 获取选择照片的数据视图
            Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
            cursor.moveToFirst();
            // 从数据视图中获取已选择图片的路径
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String picturePath = cursor.getString(columnIndex);
            cursor.close();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AndroidDetectUtils.getInstance().check(new File(picturePath), new CheckCallback() {
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


        }
    }
}
