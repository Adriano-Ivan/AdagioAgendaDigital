package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.day_decorator;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AdagioDayCriticalDecorator extends AdagioDayDecorator{
    public AdagioDayCriticalDecorator(ArrayList<LocalDateTime> dates) {
        super(dates);
        this.color= Color.parseColor("#FF0000");
    }
}
