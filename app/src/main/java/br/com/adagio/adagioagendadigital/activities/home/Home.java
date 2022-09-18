package br.com.adagio.adagioagendadigital.activities.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;

import br.com.adagio.adagioagendadigital.R;

public class Home extends AppCompatActivity implements CalendarView.OnDateChangeListener {

    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        definirViews();
        definirListeners();

        getSupportActionBar().hide();
    }

    private void definirViews(){
        calendarView = findViewById(R.id.activity_home_calendar);
    }

    private void definirListeners(){
        calendarView.setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

    }
}