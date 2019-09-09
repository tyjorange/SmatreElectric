package com.base.library;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BaseMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_main);
    }
}
