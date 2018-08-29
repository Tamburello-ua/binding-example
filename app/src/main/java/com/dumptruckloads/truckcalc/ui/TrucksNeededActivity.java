package com.dumptruckloads.truckcalc.ui;


import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dumptruckloads.truckcalc.BuildConfig;
import com.dumptruckloads.truckcalc.R;
import com.dumptruckloads.truckcalc.databinding.ActivityTrucksNeededBinding;
import com.dumptruckloads.truckcalc.model.TrucksNeededViewModel;

public class TrucksNeededActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private ImageButton backButton;
    private TextView screenName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTrucksNeededBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_trucks_needed);
        binding.setModel(new TrucksNeededViewModel());

        imageButton = binding.trackActionbar.findViewById(R.id.action_bar_info);
        backButton = binding.trackActionbar.findViewById(R.id.action_bar_back);

        screenName = binding.trackActionbar.findViewById(R.id.action_bar_screen_name);
        screenName.setText(getResources().getString(R.string.label_info_tn));

        imageButton.setOnClickListener(arg0 -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(TrucksNeededActivity.this);
            LayoutInflater inflater = TrucksNeededActivity.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.information_dialog, null);
            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();

            TextView tvTitle = dialogView.findViewById(R.id.information_dialog_title_text);
            tvTitle.setText(getResources().getString(R.string.label_info_tn));

            TextView tvText = dialogView.findViewById(R.id.information_dialog_information_text);
            tvText.setText(getResources().getString(R.string.text_info_tn));

            ImageButton ibClose = dialogView.findViewById(R.id.information_dialog_close);
            ibClose.setOnClickListener(v -> alertDialog.dismiss());

            TextView tvUrl = dialogView.findViewById(R.id.information_dialog_bottom_url);
            tvUrl.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.dump_trucks_url)))));

            TextView rateusUrl = dialogView.findViewById(R.id.information_dialog_rateus_button);
            rateusUrl.setOnClickListener(v -> {
                alertDialog.dismiss();
                openAppOnMarket();
            });

            alertDialog.show();
        });

        backButton.setOnClickListener(arg0 -> {
            this.onBackPressed();
        });

        binding.textLabelDone.setOnClickListener(arg0 -> {
            this.onBackPressed();
        });
        binding.textLabelError.setOnClickListener(arg0 -> {
            this.onBackPressed();
        });
    }

    public void openAppOnMarket() {
        saveSharedPreferences(MainActivity.RATEUS_KEY, 1);

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse(MainActivity.PLAY_MARKET_LINK));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }

    private void saveSharedPreferences(String key, int value){
        SharedPreferences pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name), 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}
