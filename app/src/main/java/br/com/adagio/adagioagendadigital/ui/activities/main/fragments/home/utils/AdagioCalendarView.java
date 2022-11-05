package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;

import java.util.Date;

public class AdagioCalendarView extends CalendarView {


    public AdagioCalendarView(@NonNull Context context) {
        super(context);
    }

    public void setMonthDateColor(Date date, int color) {
       final int childCount = this.getChildCount();
       for (int i = 0; i < childCount; i++) {
            ViewGroup view = (ViewGroup) this.getChildAt(i);
           Log.i("teste", "setMonthDateColor: "+view.getChildCount());
       }
   }
}
