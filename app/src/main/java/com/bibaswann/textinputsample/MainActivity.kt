package com.bibaswann.textinputsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Sample: How to add image and set image click

        input3.setImageClickListener(View.OnClickListener { Toast.makeText(this@MainActivity, "You clicked me", Toast.LENGTH_LONG).show() })
    }
}
