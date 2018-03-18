package deep.com.face_deep;

import android.util.Log;

/**
 * Created by wangfei on 2018/3/18.
 */

public class L {
    public static boolean debug = true;
    public static void e(String message){
        if (debug){
            Log.e(Constants.TAG,message);
        }

    }
}
