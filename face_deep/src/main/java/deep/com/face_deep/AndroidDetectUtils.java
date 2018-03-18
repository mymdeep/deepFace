package deep.com.face_deep;

import java.io.File;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.FaceDetector;


/**
 * Created by wangfei on 2018/3/8.
 */

public class AndroidDetectUtils {


    private static AndroidDetectUtils instance = null;
    public static synchronized AndroidDetectUtils getInstance() {
        if (instance == null)
            instance = new AndroidDetectUtils();
        return instance;
    }
    private AndroidDetectUtils(){

    }
    public void setCameraParam(Camera camera){
        android.hardware.Camera.Parameters parameters=camera.getParameters();
        android.hardware.Camera.Size maxSize = getBestPictureSize(parameters.getSupportedPictureSizes());

        if (maxSize!=null){
            parameters.setPictureSize(maxSize.width,maxSize.height);
        }

        try {
            camera.setParameters(parameters);
        } catch (Exception e) {
            L.e("个别机型尺寸不支持");
            try {
                //遇到上面所说的情况，只能设置一个最小的预览尺寸
                parameters.setPreviewSize(1920, 1080);
                parameters.setPictureSize(1920, 1080);
                camera.setParameters(parameters);
            } catch (Exception e1) {
                L.e("1920 报错");
            }
        }
    }
    private Camera.Size getBestPictureSize(List<Size> list){
        Camera.Size maxSize = null;
        if (list!=null&& list.size()>0){
            maxSize = list.get(0);
            for (Camera.Size s:list){
                L.e("list w:"+s.width+"  h:"+s.height);
                if (s.width>maxSize.width){
                    maxSize = s;
                }
            }
        }
        L.e("max w:"+maxSize.width+"  h:"+maxSize.height);
        return maxSize;
    }
    public void check(File file, CheckCallback checkCallback){
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        check(bitmap,checkCallback);
    }
    public void check(byte[] data,CheckCallback checkCallback){
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        check(bitmap,checkCallback);
    }
    public void check(Bitmap bitmap,CheckCallback checkCallback){
        try {
            bitmap = bitmap.copy(Bitmap.Config.RGB_565, true);
            L.e("bitmap.getWidth() = " + bitmap.getWidth()+"  bitmap.getHeight()="+bitmap.getHeight() );
            //fd = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), Constants.MAX_FACES);   //MAX_FACES识别的最大的人脸数
            //count = fd.findFaces(bitmap, faces);      //识别到人脸坐标数目   faces中保存人脸信息
            FaceDetector faceDetector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), Constants.MAX_FACES);
            FaceDetector.Face[] faces = new FaceDetector.Face[Constants.MAX_FACES];
            int realFaceNum = faceDetector.findFaces(bitmap, faces);
            L.e("realFaceNum="+realFaceNum)
;            if(realFaceNum > 0){
                checkCallback.onSuccess(realFaceNum,faces);

            }else {
                checkCallback.onFail();
            }
        } catch (Exception e) {
            L.e("setFace(): " + e.toString());
            checkCallback.onFail();
        }


    }

}
