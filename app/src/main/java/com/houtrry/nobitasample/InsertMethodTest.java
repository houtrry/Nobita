package com.houtrry.nobitasample;

import android.util.Log;

/**
 * @author: houtrry
 * @time: 2020/8/9
 * @desc:
 */

public class InsertMethodTest {

    public void testInsertMethod() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("testInsertMethod", "start");
                String text = "123";
                Log.d("testInsertMethod", "end, "+text.length());
            }
        }).start();
    }
}
