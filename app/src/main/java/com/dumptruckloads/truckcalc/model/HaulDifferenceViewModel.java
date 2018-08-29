package com.dumptruckloads.truckcalc.model;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;

import com.dumptruckloads.truckcalc.BR;
import com.dumptruckloads.truckcalc.util.InputValidator;

public class HaulDifferenceViewModel extends BaseObservable {

    private String minutesDifference;
    private String avgLoadPerTruck;
    private String truckRatePerHour;
    private String costDifference = "0";

    private boolean isMinutesDifferenceValid = true;
    private boolean isAvgLoadPerTruckValid = true;
    private boolean isTruckRatePerHourValid = true;

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
    public String getMinutesDifference() {
        return minutesDifference;
    }

    public void setMinutesDifference(String minutesDifference) {
        this.minutesDifference = minutesDifference;
        notifyPropertyChanged(BR.minutesDifference);
    }

    @Bindable
    public boolean isMinutesDifferenceValid() {
        return isMinutesDifferenceValid;
    }

    @Bindable
    public TextWatcher getMinutesDifferenceTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                minutesDifference = s.toString();
                isMinutesDifferenceValid = InputValidator.isPriceValid(minutesDifference);
                notifyPropertyChanged(BR.minutesDifferenceValid);
                calculate();
            }
        };
    }

    @Bindable
    public String getAvgLoadPerTruck() {
        return avgLoadPerTruck;
    }

    public void setAvgLoadPerTruck(String avgLoadPerTruck) {
        this.avgLoadPerTruck = avgLoadPerTruck;
        notifyPropertyChanged(BR.avgLoadPerTruck);
    }

    @Bindable
    public boolean isAvgLoadPerTruckValid() {
        return isAvgLoadPerTruckValid;
    }

    @Bindable
    public TextWatcher getAvgLoadPerTruckWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                avgLoadPerTruck = s.toString();
                isAvgLoadPerTruckValid = InputValidator.isPriceValid(avgLoadPerTruck);
                if (avgLoadPerTruck.equals("0")) {
                    isAvgLoadPerTruckValid = false;
                }
                notifyPropertyChanged(BR.avgLoadPerTruckValid);
                calculate();
            }
        };
    }

    @Bindable
    public String getTruckRatePerHour() {
        return truckRatePerHour;
    }

    public void setTruckRatePerHour(String truckRatePerHour) {
        this.truckRatePerHour = truckRatePerHour;
        notifyPropertyChanged(BR.truckRatePerHour);
    }

    @Bindable
    public boolean isTruckRatePerHourValid() {
        return isTruckRatePerHourValid;
    }

    @Bindable
    public TextWatcher getTruckRatePerHourWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                truckRatePerHour = s.toString();
                isTruckRatePerHourValid = InputValidator.isPriceValid(truckRatePerHour);
                notifyPropertyChanged(BR.truckRatePerHourValid);
                calculate();
            }
        };
    }

    @Bindable
    public String getCostDifference() {
        if (null != costDifference && costDifference.length() > 0) {
            return "$" + InputValidator.getFormatedAmount(costDifference);
        } else {
            return costDifference;
        }
    }

    public void setCostDifference(String costDifference) {
        this.costDifference = costDifference;
        notifyPropertyChanged(BR.costDifference);
    }


    private boolean isAllInputValid() {
        isMinutesDifferenceValid = InputValidator.isPriceValid(minutesDifference);
        isAvgLoadPerTruckValid = InputValidator.isPriceValid(avgLoadPerTruck);
        isTruckRatePerHourValid = InputValidator.isPriceValid(truckRatePerHour);

        isAllInputValidBool = isMinutesDifferenceValid && isAvgLoadPerTruckValid && isTruckRatePerHourValid;
        notifyPropertyChanged(BR.allInputValidBool);

        allInputValidBoolForLabel = isMinutesDifferenceValid && isAvgLoadPerTruckValid && isTruckRatePerHourValid;
        notifyPropertyChanged(BR.allInputValidBoolForLabel);

        return isMinutesDifferenceValid && isAvgLoadPerTruckValid && isTruckRatePerHourValid;
    }

    private void calculate() {
        if (isAllInputValid()) {
            double minutes = Double.parseDouble(InputValidator.cleanReplaceSeparators(minutesDifference));
            double avgLoad = Double.parseDouble(InputValidator.cleanReplaceSeparators(avgLoadPerTruck));
            double hourRate = Double.parseDouble(InputValidator.cleanReplaceSeparators(truckRatePerHour));

            double result = (((minutes * 2) / 60.0) * hourRate) / avgLoad;
            result = (double) Math.round(result * 100) / 100;
            costDifference = String.valueOf(result);
            notifyPropertyChanged(BR.costDifference);
        } else {
            costDifference = "0";
            notifyPropertyChanged(BR.costDifference);
        }
    }


}
