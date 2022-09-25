package br.com.adagio.adagioagendadigital.ui.activities.home;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

import br.com.adagio.adagioagendadigital.models.enums.LimitsYearValues;

@RequiresApi(api = Build.VERSION_CODES.O)
public  class HomeStaticValues {

    public static int PICKED_YEAR_MEMO = LocalDateTime.now().getYear();
    public static int PICKED_DAY_MEMO = LocalDateTime.now().getDayOfMonth();
    public static int PICKED_MONTH_MEMO = LocalDateTime.now().getMonth().getValue();

    public static void setPickedYearMemo(int year){
        PICKED_YEAR_MEMO = year;
    }

    public static void setPickedDayMemo(int day){
        PICKED_DAY_MEMO = day;
    }

    public static void setPickedMonthMemo(int month){
        PICKED_MONTH_MEMO = month;
    }
}
