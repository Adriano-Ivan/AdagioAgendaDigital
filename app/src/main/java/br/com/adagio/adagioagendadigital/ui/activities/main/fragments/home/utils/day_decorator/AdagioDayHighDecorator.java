package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.day_decorator;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.RequiresApi;

import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.time.LocalDateTime;
import java.util.ArrayList;

import br.com.adagio.adagioagendadigital.R;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AdagioDayHighDecorator extends AdagioDayDecorator {
    public AdagioDayHighDecorator(ArrayList<LocalDateTime> dates, Drawable drawable, int color) {
        super(dates,drawable,color);
    }

    @Override
    public void decorate(DayViewFacade view) {
        super.decorate(view);

        view.addSpan(new ForegroundColorSpan(Color.BLACK));
    }
}
