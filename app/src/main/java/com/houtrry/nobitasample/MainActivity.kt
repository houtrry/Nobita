package com.houtrry.nobitasample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTestJava.setOnClickListener {
            val asmTest = AsmTest2()
            asmTest.asmTestMethod()
            asmTest.asmTestMethod1(1000)
            asmTest.asmTestMethod2()
            asmTest.asmTestMethod3(1000)
        }
        btnTestKotlin.setOnClickListener {
            val asmTest = AsmTest()
            asmTest.asmTest()
        }
        btnTestInsert.setOnClickListener {
            val asmTest = InsertMethodTest()
            asmTest.testInsertMethod()
        }
    }
}