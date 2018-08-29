package com.dumptruckloads.truckcalc.util;


import android.databinding.BindingAdapter;
import android.text.TextWatcher;
import android.widget.EditText;

public class EditTextBindingAdapters {

    @BindingAdapter("textChangedListener")
    public static void bindTextWatcher(EditText editText, TextWatcher textWatcher) {
        editText.addTextChangedListener(textWatcher);
    }
}