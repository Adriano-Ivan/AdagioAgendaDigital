package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.day_decorator;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AdagioDayAverageDecorator extends AdagioDayDecorator{
    public AdagioDayAverageDecorator(ArrayList<LocalDateTime> dates) {
        super(dates);
        this.color = Color.parseColor("#0000FF");
    }
}
