package deep.com.face_deep;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by wangfei on 2018/3/8.
 */

public class CameraSurfacePreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;
    public CameraSurfacePreview(Context context,Camera camera) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.mCamera = camera;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {//open camera in preview mode

            mCamera.setPreviewDisplay(surfaceHolder);
        } catch(IOException e) {
            L.e( "surfaceCreated:"+e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        try {
            mCamera.startPreview();
        }catch (Exception e) {
            L.e("surfaceChanged:" + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
        L.e( "surfaceDestroyed() is called");
    }
    public void takePicture(PictureCallback imageCallback) {
        mCamera.takePicture(null,null, imageCallback);

    }

}
