package com.dumptruckloads.truckcalc.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dumptruckloads.truckcalc.BuildConfig;
import com.dumptruckloads.truckcalc.R;
import com.dumptruckloads.truckcalc.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public static final String PLAY_MARKET_LINK = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
    private static final String COUNTER_KEY = "calculate_counter";
    public static final String RATEUS_KEY = "rate_us";
    private static final int COUNTER_MAX_VALUE = 9;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.buttonRatePerTon.setOnClickListener(v -> {
                counterAdd();
                startActivity(new Intent(MainActivity.this, RatePerTonActivity.class));
        });
        binding.buttonTrucksNeeded.setOnClickListener(v -> {
            counterAdd();
            startActivity(new Intent(MainActivity.this, TrucksNeededActivity.class));
        });
        binding.buttonHaulDifference.setOnClickListener(v -> {
            counterAdd();
            startActivity(new Intent(MainActivity.this, HaulDifferenceActivity.class));
        });

        binding.headerUrlTxt.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.dump_trucks_url)))));

        binding.headerMail.setOnClickListener(v -> composeEmail(getResources().getString(R.string.dump_trucks_email),
                                                                getResources().getString(R.string.dump_trucks_email_subject),
                                                                getResources().getString(R.string.dump_trucks_email_message)));
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getSharedPreferences(COUNTER_KEY) > COUNTER_MAX_VALUE &&  getSharedPreferences(RATEUS_KEY) < 1) {
            saveSharedPreferences(COUNTER_KEY, 0);
            showRateusDialog();
        }

    }

    public void showRateusDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rateus_dialog, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();


        TextView rateusUrl = dialogView.findViewById(R.id.rateus_letsgo_button);
        rateusUrl.setOnClickListener(v -> {
            alertDialog.dismiss();
            openAppOnMarket();
        });

        TextView later = dialogView.findViewById(R.id.rateus_later_button);
        later.setOnClickListener(v -> {
            alertDialog.dismiss();
            saveSharedPreferences(COUNTER_KEY, 0);
        });

        alertDialog.show();
    }

    public void composeEmail(String addresses, String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + addresses)); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void openAppOnMarket() {
        saveSharedPreferences(RATEUS_KEY, 1);

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_MARKET_LINK));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }

    private void counterAdd(){
        int currentCounter = getSharedPreferences(COUNTER_KEY);
        currentCounter++;
        saveSharedPreferences(COUNTER_KEY, currentCounter);
    }

    private int getSharedPreferences(String key){
        SharedPreferences pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name), 0);
        return pref.getInt(key, 0);
    }


    private void saveSharedPreferences(String key, int value){
        SharedPreferences pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name), 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}
