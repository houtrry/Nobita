package com.houtrry.nobitasample

import com.houtrry.nobita.NobitaConsumingTime

/**
 * @author: houtrry
 * @date: 2020/6/30 13:36
 * @version: $
 * @description:
 */
@NobitaConsumingTime
class AsmTest {
    fun asmTest() {
        print("===asmTest start")
        repeat(1000) {
            print("it: $it")
        }
    }
}