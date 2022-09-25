package br.com.adagio.adagioagendadigital.ui.activities.home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.ui.activities.home.views.NumberPickerDialogToChooseYear;
import kotlin.time.MonoTimeSourceKt;

public class Home extends AppCompatActivity implements CalendarView.OnDateChangeListener, View.OnClickListener,
        NumberPickerDialogToChooseYear.onSaveYearListener {

    private CalendarView calendarView;
    private Button buttonToChooseYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();

        definirViews();
        definirListeners();
        setMonthsDropdownProperties();

        getSupportActionBar().hide();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthsDropdownProperties (){
        String[] months = getResources().getStringArray(R.array.months);

        ArrayAdapter<String> adapter   = new ArrayAdapter<>(this,
                R.layout.dropdown_months_item,months);

        TextInputLayout containerOptions = findViewById(R.id.activity_home_choose_month);
        AutoCompleteTextView monthsOptions = findViewById(R.id.activity_home_months_options);

        monthsOptions.setAdapter(adapter);


        monthsOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeStaticValues.setPickedMonthMemo(position+1);
                setNewStateOfCalendar();
                Log.i("CLICKED", "onItemClick: "+parent.getSelectedItem());
            }
        });

        monthsOptions.setText(months[HomeStaticValues.PICKED_MONTH_MEMO-1],false);

    }

    private void definirViews(){
        calendarView = findViewById(R.id.activity_home_calendar);
        buttonToChooseYear = findViewById(R.id.activity_home_button_choose_year);
    }

    private void definirListeners(){
        calendarView.setOnDateChangeListener(this);

        buttonToChooseYear.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        Log.i("PERIOD: ", "onSelectedDayChange: "+year+" "+month+" "+dayOfMonth);
        HomeStaticValues.setPickedDayMemo(dayOfMonth);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.activity_home_button_choose_year){
            NumberPickerDialogToChooseYear dialog = new NumberPickerDialogToChooseYear(HomeStaticValues.PICKED_YEAR_MEMO);
            dialog.show(getSupportFragmentManager(), "dialog");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setNewStateOfCalendar(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime newDate =  LocalDateTime.of(HomeStaticValues.PICKED_YEAR_MEMO,  HomeStaticValues.PICKED_MONTH_MEMO,
                now.getDayOfMonth(),now.getHour(),now.getMinute());

        Calendar calendar = Calendar.getInstance();
        calendar.set(HomeStaticValues.PICKED_YEAR_MEMO,newDate.getMonth().getValue()-1,newDate.getDayOfMonth());

        calendarView.setDate(calendar.getTimeInMillis());

        Log.i("PERÍODO: ", "onSaveYear: "+HomeStaticValues.PICKED_YEAR_MEMO+" "+now.getMonth().getValue());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSaveYear(int year) {
        HomeStaticValues.setPickedYearMemo(year);

        setNewStateOfCalendar();
    }
}