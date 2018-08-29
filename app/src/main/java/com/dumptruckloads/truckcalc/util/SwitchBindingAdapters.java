package com.dumptruckloads.truckcalc.util;


import android.databinding.BindingAdapter;
import android.support.v7.widget.SwitchCompat;

public class SwitchBindingAdapters {

    @BindingAdapter("onCheckedChanged")
    public static void bindOnCheckedChangeListener(SwitchCompat switchCompat, SwitchCompat.OnCheckedChangeListener listener) {
        switchCompat.setOnCheckedChangeListener(listener);
    }
}
