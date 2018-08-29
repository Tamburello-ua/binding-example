package com.dumptruckloads.truckcalc.model;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;

import com.dumptruckloads.truckcalc.BR;
import com.dumptruckloads.truckcalc.util.InputValidator;

public class TrucksNeededViewModel extends BaseObservable {

    private String minTruckLoad;
    private String avgTruckLoad;
    private String roundTripTime;

    private String tonsPerHour = "0";
    private String numberOfTrucks = "0";

    private boolean isMinTruckLoadValid = true;
    private boolean isAvgTruckLoadValid = true;
    private boolean isRoundTripTimeValid = true;

    private boolean isMetricSystem = true;
    private boolean isAllInputValidBool = true;
    private boolean allInputValidBoolForLabel = false;

    @Bindable
    public boolean isMetricSystem() {
        return isMetricSystem;
    }

    @Bindable
    public boolean isAllInputValidBool() {
        return isAllInputValidBool;
    }

    @Bindable
    public boolean isAllInputValidBoolForLabel() {
        return allInputValidBoolForLabel;
    }

    public void setMetricSystem(boolean metricSystem) {
        isMetricSystem = metricSystem;
        notifyPropertyChanged(BR.metricSystem);
    }

    public SwitchCompat.OnCheckedChangeListener getMetricCheckedChangeListener() {
        return (group, checkedId) -> setMetricSystem(!checkedId);
    }

    @Bindable
    public String getMinTruckLoad() {
        return minTruckLoad;
    }

    public void setMinTruckLoad(String minTruckLoad) {
        this.minTruckLoad = minTruckLoad;
        notifyPropertyChanged(BR.minTruckLoad);
    }

    @Bindable
    public TextWatcher getMinTruckLoadTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                minTruckLoad = s.toString();
                isMinTruckLoadValid = InputValidator.isPriceValid(minTruckLoad);
                notifyPropertyChanged(BR.minTruckLoadValid);
                calculate();
            }
        };
    }

    @Bindable
    public String getAvgTruckLoad() {
        return minTruckLoad;
    }

    public void setAvgTruckLoad(String avgTruckLoad) {
        this.avgTruckLoad = avgTruckLoad;
        notifyPropertyChanged(BR.avgTruckLoad);
    }

    @Bindable
    public TextWatcher getAvgTruckLoadTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                avgTruckLoad = s.toString();
                isAvgTruckLoadValid = InputValidator.isPriceValid(avgTruckLoad);
                if (avgTruckLoad.equals("0")) {
                    isAvgTruckLoadValid = false;
                }
                notifyPropertyChanged(BR.avgTruckLoadValid);
                calculate();
            }
        };
    }

    @Bindable
    public String getRoundTripTime() {
        return minTruckLoad;
    }

    public void setRoundTripTime(String roundTripTime) {
        this.roundTripTime = roundTripTime;
        notifyPropertyChanged(BR.roundTripTime);
    }

    @Bindable
    public TextWatcher getRoundTripTimeTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                roundTripTime = s.toString();
                isRoundTripTimeValid = InputValidator.isPriceValid(roundTripTime);
                notifyPropertyChanged(BR.roundTripTimeValid);
                calculate();
            }
        };
    }

    @Bindable
    public String getTonsPerHour() {
        return InputValidator.getFormatedAmount(tonsPerHour);
    }

    @Bindable
    public String getNumberOfTrucks() {
        return InputValidator.getFormatedAmount(numberOfTrucks);
    }

    @Bindable
    public boolean isMinTruckLoadValid() {
        return isMinTruckLoadValid;
    }

    @Bindable
    public boolean isAvgTruckLoadValid() {
        return isAvgTruckLoadValid;
    }

    @Bindable
    public boolean isRoundTripTimeValid() {
        return isRoundTripTimeValid;
    }

    private boolean isAllInputValid() {
        isAvgTruckLoadValid = InputValidator.isPriceValid(avgTruckLoad);
        isMinTruckLoadValid = InputValidator.isPriceValid(minTruckLoad);

        isAllInputValidBool = isMinTruckLoadValid && isAvgTruckLoadValid;
        notifyPropertyChanged(BR.allInputValidBool);

        allInputValidBoolForLabel = isMinTruckLoadValid && isAvgTruckLoadValid;
        notifyPropertyChanged(BR.allInputValidBoolForLabel);

        return isMinTruckLoadValid && isAvgTruckLoadValid;
    }

    private void calculate() {
        if (isAllInputValid()) {

            double minLoad = Double.parseDouble(InputValidator.cleanReplaceSeparators(minTruckLoad));
            double avgLoad = Double.parseDouble(InputValidator.cleanReplaceSeparators(avgTruckLoad));

            double tons = (60 / minLoad) * avgLoad;
            double roundedTons = (double) Math.round(tons * 100) / 100;

            tonsPerHour = String.valueOf(roundedTons);
            notifyPropertyChanged(BR.tonsPerHour);

            isRoundTripTimeValid = InputValidator.isPriceValid(roundTripTime);
            if (isRoundTripTimeValid) {
                double tripTime = Double.parseDouble(InputValidator.cleanReplaceSeparators(roundTripTime));

                double result = (tons / avgLoad) / (60 / tripTime);
                result = (double) Math.round(result * 100) / 100;

                numberOfTrucks = String.valueOf(result);
                notifyPropertyChanged(BR.numberOfTrucks);
            } else {
                numberOfTrucks = "0";
                notifyPropertyChanged(BR.numberOfTrucks);
            }
        } else {
            tonsPerHour = "0";
            notifyPropertyChanged(BR.tonsPerHour);

            numberOfTrucks = "0";
            notifyPropertyChanged(BR.numberOfTrucks);
        }
    }

}
