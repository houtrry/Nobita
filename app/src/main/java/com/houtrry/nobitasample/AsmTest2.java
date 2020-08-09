package com.houtrry.nobitasample;

import android.annotation.TargetApi;
import android.util.Log;

import com.houtrry.nobita.NobitaConsumingTime;

/**
 * @author: houtrry
 * @date: 2020/6/30 13:38
 * @version: $
 * @description:
 */
public class AsmTest2 {

    @NobitaConsumingTime
    public void asmTestMethod() {
        for (int i = 0; i < 1000; i++) {
            Log.d("asmTestMethod", "sss->"+i);
        }
    }

    @NobitaConsumingTime
    public void asmTestMethod1(int count) {
        for (int i = 0; i < count; i++) {
            Log.d("asmTestMethod1", "sss->"+i);
        }
    }

    public int asmTestMethod2() {
        for (int i = 0; i < 1000; i++) {
            Log.d("asmTestMethod2", "sss->"+i);
        }
        return 300;
    }

    @NobitaConsumingTime
    @TargetApi(11)
    public int asmTestMethod3(int count) {

        for (int i = 0; i < count; i++) {
            Log.d("asmTestMethod3", "sss->"+i);
        }

        return count;
    }

    @NobitaConsumingTime
    public int asmTestMethod4(int count) {

        int result = 0;
        for (int i = 0; i < count; i++) {
            Log.d("asmTestMethod4", "sss->"+i);
            result += i;
        }

        return result;
    }

    @NobitaConsumingTime
    public String asmTestMethod5(int count) {

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            Log.d("asmTestMethod5", "sss->"+i);
            if (i %10 == 0) {
                result.append("->").append(i).append(" ");
            }
        }

        return result.toString();
    }

}
