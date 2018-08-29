package com.dumptruckloads.truckcalc.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dumptruckloads.truckcalc.R;

public class SplashActivity extends AppCompatActivity {

    private static final long START_DELAY_MS = 2000;

    private final Handler delayHandler = new Handler(msg -> {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
        return false;
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViewById(R.id.logo_url_txt).setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.dump_trucks_url)))));
    }

    @Override
    protected void onStart() {
        super.onStart();
        delayHandler.sendEmptyMessageDelayed(0, START_DELAY_MS);
    }

    @Override
    protected void onStop() {
        super.onStop();
        delayHandler.removeMessages(0);
    }
}
