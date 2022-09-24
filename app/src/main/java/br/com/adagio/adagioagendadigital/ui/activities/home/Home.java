package br.com.adagio.adagioagendadigital.ui.activities.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.ui.activities.home.views.NumberPickerDialogToChooseYear;
import kotlin.time.MonoTimeSourceKt;

public class Home extends AppCompatActivity implements CalendarView.OnDateChangeListener, View.OnClickListener,
        NumberPickerDialogToChooseYear.onSaveYearListener {

    private CalendarView calendarView;
    private Button buttonToChooseYear;
    private Button buttonToChooseMonth;

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
        buttonToChooseMonth = findViewById(R.id.activity_home_button_choose_month);
        buttonToChooseYear = findViewById(R.id.activity_home_button_choose_year);
    }

    private void definirListeners(){
        calendarView.setOnDateChangeListener(this);

        buttonToChooseYear.setOnClickListener(this);
        buttonToChooseMonth.setOnClickListener(this);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.activity_home_button_choose_year){
            NumberPickerDialogToChooseYear dialog = new NumberPickerDialogToChooseYear();
            dialog.show(getSupportFragmentManager(), "dialog");
        }
    }

    @Override
    public void onSaveYear(int ano) {
        LocalDateTime newDate =  LocalDateTime.of(ano, 0,0,0,0);
        Log.i("ANO ESCOLHIDO", "onSaveYear: "+ano);
    }
}