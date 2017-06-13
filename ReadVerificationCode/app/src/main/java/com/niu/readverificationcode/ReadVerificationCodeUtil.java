package com.niu.readverificationcode;

/**
 * Created by niu on 2017/5/31.
 */

public class ReadVerificationCodeUtil {

    public void readCode(){
        int api = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
        if (api >= 23) {
            checkPermission();
        } else {

        }
    }

    private void checkPermission() {

    }
}
