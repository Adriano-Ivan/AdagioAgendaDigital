package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.day_decorator;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.RequiresApi;

import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DefaultTodayDecorator extends AdagioDayDecorator{

    public DefaultTodayDecorator(ArrayList<LocalDateTime> dates, Drawable drawable){
        super(dates, drawable);
    }

    @Override
    public void decorate(DayViewFacade view) {
        super.decorate(view);

        view.addSpan(new ForegroundColorSpan(Color.BLACK));
    }
}
