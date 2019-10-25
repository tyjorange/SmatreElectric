package com.base.library;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BaseMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_main);
    }
}
