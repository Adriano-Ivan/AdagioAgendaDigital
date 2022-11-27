package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.day_decorator;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AdagioDayAverageDecorator extends AdagioDayDecorator{
    public AdagioDayAverageDecorator(ArrayList<LocalDateTime> dates, Drawable drawable,int color) {
        super(dates,drawable,color);
    }
}
