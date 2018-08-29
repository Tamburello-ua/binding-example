package com.dumptruckloads.truckcalc.model;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;

import com.dumptruckloads.truckcalc.BR;
import com.dumptruckloads.truckcalc.util.InputValidator;

public class RatePerTonViewModel extends BaseObservable {

    private String hourRate;
    private String tolls;
    private String roadTime;
    private String loadingTime;
    private String uploadingTime;
    private String avgLoad;
    private String roundTripsPerDay;

    private String costPerTon = "0";
    private String hoursPerRound = "00h : 00m";
    private String revenuePerDay = "0";

    private boolean isHourRateValid = true;
    private boolean isTollsValid = true;
    private boolean isRoadTimeValid = true;
    private boolean isLoadingTimeValid = true;
    private boolean isUploadingTimeValid = true;
    private boolean isAvgLoadValid = true;
    private boolean isRoundTripsPerDayValid = true;

    private boolean isMetricSystem = true;
    private boolean isAllInputValidBool = true;
    private boolean allInputValidBoolForLabel = false;


    private boolean isAllInputValid() {
        isHourRateValid = InputValidator.isPriceValid(hourRate);
        isTollsValid = InputValidator.isCountValid(tolls);
        isRoadTimeValid = InputValidator.isPriceValid(roadTime);
        isLoadingTimeValid = InputValidator.isPriceValid(loadingTime);
        isUploadingTimeValid = InputValidator.isPriceValid(uploadingTime);
        isAvgLoadValid = InputValidator.isPriceValid(avgLoad);
        isRoundTripsPerDayValid = InputValidator.isCountValid(roundTripsPerDay);

        isAllInputValidBool =
                isHourRateValid && isTollsValid && isRoadTimeValid && isLoadingTimeValid && isUploadingTimeValid && isAvgLoadValid && isRoundTripsPerDayValid;
        notifyPropertyChanged(BR.allInputValidBool);

        allInputValidBoolForLabel =
                isHourRateValid && isTollsValid && isRoadTimeValid && isLoadingTimeValid && isUploadingTimeValid && isAvgLoadValid && isRoundTripsPerDayValid;
        notifyPropertyChanged(BR.allInputValidBoolForLabel);

        return isHourRateValid && isTollsValid && isRoadTimeValid && isLoadingTimeValid && isUploadingTimeValid && isAvgLoadValid;
    }

    private void calculate() {
        if (isAllInputValid()) {

            double hourRateValue = Double.parseDouble(InputValidator.cleanReplaceSeparators(hourRate));
            double tollsValue = Double.parseDouble(InputValidator.cleanReplaceSeparators(tolls));
            double roadTimeValue = Double.parseDouble(InputValidator.cleanReplaceSeparators(roadTime));
            double loadingValue = Double.parseDouble(InputValidator.cleanReplaceSeparators(loadingTime));
            double uploadingValue = Double.parseDouble(InputValidator.cleanReplaceSeparators(uploadingTime));
            double avgLoadValue = Double.parseDouble(InputValidator.cleanReplaceSeparators(avgLoad));

            double tonCost = ((((uploadingValue + loadingValue) + (roadTimeValue * 2)) / 60) * (hourRateValue) + (tollsValue)) / avgLoadValue;
            double roundedCostPerTon = (double) Math.round(tonCost * 100) / 100;

            costPerTon = String.format("%.2f", roundedCostPerTon);
            notifyPropertyChanged(BR.costPerTon);

            double needMinutes = ((roadTimeValue * 2) + (loadingValue ) + (uploadingValue ));
            int hours = (int) (needMinutes / 60); //since both are ints, you get an int
            int minutes = (int) (needMinutes % 60);

            String strMinutesZero = "";
            if (minutes < 10) {
                strMinutesZero = "0";
            }
            String strHourssZero = "";
            if (hours < 10) {
                strHourssZero = "0";
            }
            hoursPerRound = strHourssZero + hours + "h : " + strMinutesZero + minutes + "m";

            notifyPropertyChanged(BR.hoursPerRound);

            if (isRoundTripsPerDayValid) {
                double tripsPerDay = Double.parseDouble(InputValidator.cleanReplaceSeparators(roundTripsPerDay));

                double revenue = tripsPerDay * tonCost * avgLoadValue;
                revenue = (double) Math.round(revenue * 100) / 100;

                revenuePerDay = String.format("%.2f", revenue);
                notifyPropertyChanged(BR.revenuePerDay);

            } else {
                revenuePerDay = "0";
                notifyPropertyChanged(BR.revenuePerDay);
            }
        } else {
            costPerTon = "0";
            notifyPropertyChanged(BR.costPerTon);

            hoursPerRound = "0";
            notifyPropertyChanged(BR.hoursPerRound);

            revenuePerDay = "0";
            notifyPropertyChanged(BR.revenuePerDay);
        }
    }

    public SwitchCompat.OnCheckedChangeListener getMetricCheckedChangeListener() {
        return (group, checkedId) -> setMetricSystem(!checkedId);
    }

    @Bindable
    public String getHourRate() {
        return hourRate;
    }

    public TextWatcher getHourRateTextWatcher() {
        return new OnTextChanged() {
            @Override
            public void afterTextChanged(Editable s) {
                hourRate = s.toString();
                isHourRateValid = InputValidator.isPriceValid(hourRate);
                notifyPropertyChanged(BR.hourRateValid);
                calculate();
            }
        };
    }

    @Bindable
    public String getTolls() {
        return tolls;
    }

    @Bindable
    public TextWatcher getTollsTextWatcher() {
        return new OnTextChanged() {
            @Override
            public void afterTextChanged(Editable s) {
                tolls = s.toString();
                isTollsValid = InputValidator.isPriceValid(tolls);
                notifyPropertyChanged(BR.tollsValid);
                calculate();
            }
        };
    }

    @Bindable
    public String getRoadTime() {
        return roadTime;
    }

    @Bindable
    public TextWatcher getRoadTimeTextWatcher() {
        return new OnTextChanged() {
            @Override
            public void afterTextChanged(Editable s) {
                roadTime = s.toString();
                isRoadTimeValid = InputValidator.isPriceValid(roadTime);
                notifyPropertyChanged(BR.roadTimeValid);
                calculate();
            }
        };
    }

    @Bindable
    public String getLoadingTime() {
        return loadingTime;
    }

    @Bindable
    public TextWatcher getLoadingTimeTextWatcher() {
        return new OnTextChanged() {
            @Override
            public void afterTextChanged(Editable s) {
                loadingTime = s.toString();
                isLoadingTimeValid = InputValidator.isPriceValid(loadingTime);
                notifyPropertyChanged(BR.loadingTimeValid);
                calculate();
            }
        };
    }

    @Bindable
    public String getUploadingTime() {
        return uploadingTime;
    }

    @Bindable
    public TextWatcher getUploadingTimeTextWatcher() {
        return new OnTextChanged() {
            @Override
            public void afterTextChanged(Editable s) {
                uploadingTime = s.toString();
                isUploadingTimeValid = InputValidator.isPriceValid(uploadingTime);
                notifyPropertyChanged(BR.uploadingTimeValid);
                calculate();
            }
        };
    }

    @Bindable
    public String getAvgLoad() {
        return avgLoad;
    }

    @Bindable
    public TextWatcher getAvgLoadTextWatcher() {
        return new OnTextChanged() {
            @Override
            public void afterTextChanged(Editable s) {
                avgLoad = s.toString();
                isAvgLoadValid = InputValidator.isPriceValid(avgLoad);
                if (avgLoad.equals("0")) {
                    isAvgLoadValid = false;
                }
                notifyPropertyChanged(BR.avgLoadValid);
                calculate();
            }
        };
    }

    @Bindable
    public String getRoundTripsPerDay() {
        return roundTripsPerDay;
    }

    @Bindable
    public TextWatcher getRoundTripsPerDayTextWatcher() {
        return new OnTextChanged() {
            @Override
            public void afterTextChanged(Editable s) {
                roundTripsPerDay = s.toString();
                isRoundTripsPerDayValid = InputValidator.isCountValid(roundTripsPerDay);
                notifyPropertyChanged(BR.roundTripsPerDayValid);
                calculate();
            }
        };
    }

    @Bindable
    public String getCostPerTon() {
        if (null != costPerTon && costPerTon.length() > 0) {
            return "$" + InputValidator.getFormatedAmount(costPerTon);
        } else {
            return costPerTon;
        }
    }

    @Bindable
    public String getHoursPerRound() {
        return hoursPerRound;
    }

    @Bindable
    public String getRevenuePerDay() {
        if (null != revenuePerDay && revenuePerDay.length() > 0) {

            return "$" + InputValidator.getFormatedAmount(revenuePerDay);
        } else {
            return revenuePerDay;
        }
    }

    @Bindable
    public boolean isHourRateValid() {
        return isHourRateValid;
    }

    @Bindable
    public boolean isTollsValid() {
        return isTollsValid;
    }

    @Bindable
    public boolean isRoadTimeValid() {
        return isRoadTimeValid;
    }

    @Bindable
    public boolean isLoadingTimeValid() {
        return isLoadingTimeValid;
    }

    @Bindable
    public boolean isUploadingTimeValid() {
        return isUploadingTimeValid;
    }

    @Bindable
    public boolean isAvgLoadValid() {
        return isAvgLoadValid;
    }

    @Bindable
    public boolean isRoundTripsPerDayValid() {
        return isRoundTripsPerDayValid;
    }

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

    public void setHourRate(String hourRate) {
        this.hourRate = hourRate;
        notifyPropertyChanged(BR.hourRate);
    }

    public void setTolls(String tolls) {
        this.tolls = tolls;
        notifyPropertyChanged(BR.tolls);
    }

    public void setRoadTime(String roadTime) {
        this.roadTime = roadTime;
        notifyPropertyChanged(BR.roadTime);
    }

    public void setLoadingTime(String loadingTime) {
        this.loadingTime = loadingTime;
        notifyPropertyChanged(BR.loadingTime);
    }

    public void setUploadingTime(String uploadingTime) {
        this.uploadingTime = uploadingTime;
        notifyPropertyChanged(BR.uploadingTime);
    }

    public void setAvgLoad(String avgLoad) {
        this.avgLoad = avgLoad;
        notifyPropertyChanged(BR.avgLoad);
    }

    public void setRoundTripsPerDay(String roundTripsPerDay) {
        this.roundTripsPerDay = roundTripsPerDay;
        notifyPropertyChanged(BR.roundTripsPerDay);
    }

    public void setCostPerTon(String costPerTon) {
        this.costPerTon = costPerTon;
        notifyPropertyChanged(BR.costPerTon);
    }

    public void setHoursPerRound(String hoursPerRound) {
        this.hoursPerRound = hoursPerRound;
        notifyPropertyChanged(BR.hoursPerRound);
    }

    public void setRevenuePerDay(String revenuePerDay) {
        this.revenuePerDay = revenuePerDay;
        notifyPropertyChanged(BR.revenuePerDay);
    }

    public void setHourRateValid(boolean hourRateValid) {
        isHourRateValid = hourRateValid;
        notifyPropertyChanged(BR.hourRateValid);
    }

    public void setTollsValid(boolean tollsValid) {
        isTollsValid = tollsValid;
        notifyPropertyChanged(BR.tollsValid);
    }

    public void setRoadTimeValid(boolean roadTimeValid) {
        isRoadTimeValid = roadTimeValid;
        notifyPropertyChanged(BR.roadTimeValid);
    }

    public void setLoadingTimeValid(boolean loadingTimeValid) {
        isLoadingTimeValid = loadingTimeValid;
        notifyPropertyChanged(BR.loadingTimeValid);
    }

    public void setUploadingTimeValid(boolean uploadingTimeValid) {
        isUploadingTimeValid = uploadingTimeValid;
        notifyPropertyChanged(BR.uploadingTimeValid);
    }

    public void setAvgLoadValid(boolean avgLoadValid) {
        isAvgLoadValid = avgLoadValid;
        notifyPropertyChanged(BR.avgLoadValid);
    }

    public void setRoundTripsPerDayValid(boolean roundTripsPerDayValid) {
        isRoundTripsPerDayValid = roundTripsPerDayValid;
        notifyPropertyChanged(BR.roundTripsPerDayValid);
    }

    public void setMetricSystem(boolean metricSystem) {
        isMetricSystem = metricSystem;
        notifyPropertyChanged(BR.metricSystem);
    }

}
