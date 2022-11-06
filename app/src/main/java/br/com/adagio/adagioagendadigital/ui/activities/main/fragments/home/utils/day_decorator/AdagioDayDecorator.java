package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.day_decorator;

import android.graphics.Color;
import android.os.Build;
import android.text.style.BackgroundColorSpan;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public abstract class AdagioDayDecorator implements DayViewDecorator {

    private ArrayList<LocalDateTime> dates;
    protected int color;

    public AdagioDayDecorator(ArrayList<LocalDateTime> dates){
        this.dates = new ArrayList<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        boolean containsDate = containsDate(day);
        if(containsDate){
            return true;
        }
        return false;
    }

    private boolean containsDate(CalendarDay day){
        boolean contains = false;

        for(LocalDateTime d : dates){
            if(d.getYear()==day.getYear() &&
                d.getDayOfMonth()==day.getDay() &&
                 d.getMonth().getValue() == day.getMonth()
            )
            {
                Log.i("contains", "yes");
                contains = true;
                break;
            }
        }

        return contains;
    }

    @Override
    public void decorate(DayViewFacade view) {

        view.addSpan(new BackgroundColorSpan(color));
    }
}
