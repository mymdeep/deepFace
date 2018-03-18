package deep.com.face_deep;

import android.media.FaceDetector;

/**
 * Created by wangfei on 2018/3/9.
 */

public interface CheckCallback {
    public void onSuccess(int count, FaceDetector.Face[] faces);
    public void onFail();
}
