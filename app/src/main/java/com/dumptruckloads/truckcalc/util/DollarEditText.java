package com.dumptruckloads.truckcalc.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import org.apache.commons.lang3.StringUtils;

public class DollarEditText extends NumericEditText{

    public DollarEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

         @Override
        public void afterTextChanged(Editable s) {
             super.afterTextChanged(s);

             if (validateLock) {
                 return;
             }

             // valid decimal number should not have more than 2 decimal separators
             if (StringUtils.countMatches(s.toString(), String.valueOf(mDecimalSeparator)) > 1) {
                 validateLock = true;
                 setText(mPreviousText); // cancel change and revert to previous input
                 setSelection(mPreviousText.length());
                 validateLock = false;
                 return;
             }

             if (s.length() == 0) {
                 handleNumericValueCleared();
                 return;
             }

             setTextInternal("$" + format(s.toString()));
             if(needSelectionPosition < getText().length()) {
                 setSelection(getText().length() - needSelectionPosition);
             } else {
                 setSelection(getText().length());
             }

             handleNumericValueChanged();

        }

}


