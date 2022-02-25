package com.pluginstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Serializable ces = getIntent().getSerializableExtra("ces");
        Log.e("测试", "" + ces.toString());
    }
}