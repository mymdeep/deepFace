package deep.com.face_deep;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.FaceDetector;
import android.util.Log;

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
            Log.e(Constants.TAG, "bitmap.getWidth() = " + bitmap.getWidth()+"  bitmap.getHeight()="+bitmap.getHeight() );
            //fd = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), Constants.MAX_FACES);   //MAX_FACES识别的最大的人脸数
            //count = fd.findFaces(bitmap, faces);      //识别到人脸坐标数目   faces中保存人脸信息
            FaceDetector faceDetector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), Constants.MAX_FACES);
            FaceDetector.Face[] faces = new FaceDetector.Face[Constants.MAX_FACES];
            int realFaceNum = faceDetector.findFaces(bitmap, faces);
            Log.e("xxxxxx","width="+bitmap.getWidth()+"    height="+bitmap.getHeight());
            Log.e("xxxxxx","realFaceNum="+realFaceNum)
;            if(realFaceNum > 0){
                checkCallback.onSuccess(realFaceNum,faces);

            }else {
                checkCallback.onFail();
            }
        } catch (Exception e) {
            Log.e(Constants.TAG, "setFace(): " + e.toString());
            checkCallback.onFail();
        }


    }

}
