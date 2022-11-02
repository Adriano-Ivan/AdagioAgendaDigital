package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;
import java.util.Calendar;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.enums.LimitsYearValues;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.home_today_dialog.HomeTodayDialog;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.NumberPickerDialogToChooseYear;

public class HomeFragment extends Fragment implements CalendarView.OnDateChangeListener, View.OnClickListener
       {

    private View rootView;
    private CalendarView calendarView;
    private Button buttonToChooseYear;
    private TextView textViewTodayDate;

    private LocalDateTime pickedDate;

    public HomeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        setAttributes();

        return rootView;
    }

   @Override
   public void onCreate(@Nullable Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
   }

   @Override
   public void onResume() {
       super.onResume();
       setAttributes();
   }

   private void setAttributes(){
       defineViews();
       defineListeners();
       defineDefaultValues();
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           setNewStateOfCalendar();
       }
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           setMonthsDropdownProperties();
       }

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           setMaxAndMinDateOfCalendar();
       }
   }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthsDropdownProperties (){
        String[] months = getResources().getStringArray(R.array.months);

        ArrayAdapter<String> adapter   = new ArrayAdapter<>(getActivity(),
                R.layout.dropdown_months_item,months);

        TextInputLayout containerOptions = rootView.findViewById(R.id.fragment_home_choose_month);
        AutoCompleteTextView monthsOptions = rootView.findViewById(R.id.fragment_home_months_options);

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

    private void defineViews(){
        calendarView = rootView.findViewById(R.id.fragment_home_calendar);
        buttonToChooseYear = rootView.findViewById(R.id.fragment_home_button_choose_year);
        textViewTodayDate = rootView.findViewById(R.id.fragment_home_indicative_date);
    }

    private void defineListeners(){
        calendarView.setOnDateChangeListener(this);

        buttonToChooseYear.setOnClickListener(this);
    }

    private void defineDefaultValues(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);

        textViewTodayDate.setText(String.format(
                "%s %s/%s/%s",
                getResources().getString(R.string.today_text),
                day,month,year));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMaxAndMinDateOfCalendar(){
        LocalDateTime minDate = LocalDateTime.of(LimitsYearValues.MIN_YEAR.getValue(), 1,1,0,0, 0);
        LocalDateTime maxDate = LocalDateTime.of(LimitsYearValues.MAX_YEAR.getValue(), 12, 31, 23, 59, 59);

        Calendar calendarForMin = Calendar.getInstance();
        Calendar calendarForMax = Calendar.getInstance();

        calendarForMin.set(minDate.getYear(), minDate.getMonth().getValue() -1,
                            minDate.getDayOfMonth());
        calendarForMax.set(maxDate.getYear(), maxDate.getMonth().getValue() - 1,
                maxDate.getDayOfMonth());

        calendarView.setMinDate(calendarForMin.getTimeInMillis());
        calendarView.setMaxDate(calendarForMax.getTimeInMillis());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        Log.i("PERIOD: ", "onSelectedDayChange: "+year+" "+month+" "+dayOfMonth);

        pickedDate = LocalDateTime.of(year,month+1,dayOfMonth,0,0,0);
        HomeTodayDialog todayDialog = new HomeTodayDialog(pickedDate);
        todayDialog.show(getActivity().getSupportFragmentManager(),"dialog");
        HomeStaticValues.setPickedDayMemo(dayOfMonth);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fragment_home_button_choose_year){
            NumberPickerDialogToChooseYear dialog = new NumberPickerDialogToChooseYear(HomeStaticValues.PICKED_YEAR_MEMO);
            dialog.show(getActivity().getSupportFragmentManager(), "dialog");
        } else if(v.getId() == R.id.activity_home_menu_button){
            if(!HomeStaticValues.SIDE_BAR_IS_OPEN){
                HomeStaticValues.setSideBarIsOpen(true);

            } else {
                HomeStaticValues.setSideBarIsOpen(false);
            }
        }
    }

    private LocalDateTime getAuxLocalDateTime(LocalDateTime now){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           return LocalDateTime.of(HomeStaticValues.PICKED_YEAR_MEMO,  HomeStaticValues.PICKED_MONTH_MEMO,
                    HomeStaticValues.PICKED_DAY_MEMO,now.getHour(),now.getMinute());
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void auxSetNewStateOfCalendar(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime newDate =  getAuxLocalDateTime(now);
        pickedDate = getAuxLocalDateTime(now);

        Calendar calendar = Calendar.getInstance();
        calendar.set(HomeStaticValues.PICKED_YEAR_MEMO,newDate.getMonth().getValue()-1,newDate.getDayOfMonth());

        calendarView.setDate(calendar.getTimeInMillis());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setNewStateOfCalendar(){

        try {
          auxSetNewStateOfCalendar();
        }catch(Exception e){
            HomeStaticValues.setPickedDayMemo(01);
            auxSetNewStateOfCalendar();
        }

    }

}