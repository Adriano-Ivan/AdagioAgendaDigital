package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.relatories.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.Date;

import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.relatories.RelatoriesFragment;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RelatoriesDatePicker implements DatePickerDialog.OnDateSetListener {
    private RelatoriesFragment relatoriesFragment;
    private DatePickerDialog datePickerDialog;


    public RelatoriesDatePicker(RelatoriesFragment relatoriesFragment){
        this.relatoriesFragment = relatoriesFragment;
    }

    public String getDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month++;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day,month,year);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startDatePicker(){
        if (relatoriesFragment.getChartPeriod() == 0) {
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month++;
                    String sday = Integer.toString(day);
                    if (sday.length() < 2)
                        sday = "0" + sday;
                    String smonth = Integer.toString(month);
                    if (smonth.length() < 2)
                        smonth = "0" + smonth;
                    relatoriesFragment.setRelatoriesDate(LocalDate.parse(year + "-" + smonth + "-" + sday));
                    relatoriesFragment.loadPieChartData(false, relatoriesFragment.getChartPeriod());
                }
            };

            int year = relatoriesFragment.getRelatoriesDate().getYear();
            int month = relatoriesFragment.getRelatoriesDate().getMonthValue() - 1;
            int day = relatoriesFragment.getRelatoriesDate().getDayOfMonth();

            int style = AlertDialog.THEME_HOLO_LIGHT;

            datePickerDialog = new DatePickerDialog(relatoriesFragment.getContext(), style, dateSetListener, year, month, day);

            openDatePicker(relatoriesFragment.getRootView());
        }
        else if (relatoriesFragment.getChartPeriod() == 1){
            MonthYearPickerDialog monthYearPickerDialog = new MonthYearPickerDialog(relatoriesFragment.getRelatoriesDate());
            monthYearPickerDialog.setListener(this);
            monthYearPickerDialog.show(relatoriesFragment.getFragmentManager(), "MonthYearPickerDialog");
        }
        else if (relatoriesFragment.getChartPeriod() == 2){
            YearPickerDialog yearPickerDialog = new YearPickerDialog(relatoriesFragment.getRelatoriesDate());
            yearPickerDialog.setListener(this);
            yearPickerDialog.show(relatoriesFragment.getFragmentManager(), "YearPickerDialog");
        }
    }

    public String makeDateString(int day, int month, int year) {
        return day + " de "  +  getMonthString(month) + " de " + year;
    }

    public String getMonthString(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEV";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "ABR";
        if (month == 5)
            return "MAI";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AGO";
        if (month == 9)
            return "SET";
        if (month == 10)
            return "OUT";
        if (month == 11)
            return "NOV";
        else
            return "DEZ";
    }

    public String getMonthString(String month) {
        if (month.equals("01"))
            return "JAN";
        if (month.equals("02"))
            return "FEV";
        if (month.equals("03"))
            return "MAR";
        if (month.equals("04"))
            return "ABR";
        if (month.equals("05"))
            return "MAI";
        if (month.equals("06"))
            return "JUN";
        if (month.equals("07"))
            return "JUL";
        if (month.equals("08"))
            return "AGO";
        if (month.equals("09"))
            return "SET";
        if (month.equals("10"))
            return "OUT";
        if (month.equals("11"))
            return "NOV";
        else
            return "DEZ";
    }

    public void openDatePicker(View view){
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month++;
        String sday = "01";
        String smonth = Integer.toString(month);
        if (smonth.length() < 2)
            smonth = "0" + smonth;
        relatoriesFragment.setRelatoriesDate(LocalDate.parse(year + "-" + smonth + "-" + sday));
        relatoriesFragment.loadPieChartData(false, relatoriesFragment.getChartPeriod());
    }
}
