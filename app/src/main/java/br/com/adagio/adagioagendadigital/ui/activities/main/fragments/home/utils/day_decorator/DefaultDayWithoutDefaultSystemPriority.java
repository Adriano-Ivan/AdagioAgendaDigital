package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.day_decorator;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DefaultDayWithoutDefaultSystemPriority implements DayViewDecorator {

    private Drawable drawable;
    private LocalDateTime date;

    public DefaultDayWithoutDefaultSystemPriority() {

    }

    public void defineDrawable(Drawable drawable){
        this.drawable = drawable;
    }

    public void defineDate(LocalDateTime date){
        this.date = date;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        if(day.getDay() == date.getDayOfMonth() && day.getMonth() == date.getMonth().getValue() &&
        day.getDay() == date.getDayOfMonth()){
            return true;
        }

        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(this.drawable);
        view.setSelectionDrawable(drawable);
    }
}
