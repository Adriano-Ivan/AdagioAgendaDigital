package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home;

import android.graphics.drawable.Drawable;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.threeten.bp.LocalDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.data.task.TaskDAO;
import br.com.adagio.adagioagendadigital.models.enums.LimitsYearValues;
import br.com.adagio.adagioagendadigital.models.enums.Priorities;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.day_decorator.AdagioDayAverageDecorator;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.day_decorator.AdagioDayCriticalDecorator;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.day_decorator.AdagioDayHighDecorator;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.day_decorator.AdagioDayLowDecorator;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.day_decorator.DefaultDayWithoutDefaultSystemPriority;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.day_decorator.DefaultTodayDecorator;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.home_today_dialog.HomeTodayDialog;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.NumberPickerDialogToChooseYear;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeFragment extends Fragment implements /*CalendarView.OnDateChangeListener,*/ View.OnClickListener
       {

    private View rootView;
    private MaterialCalendarView materialCalendarView;

    private Button buttonToChooseYear;
    private TextView textViewTodayDate;

    private LocalDateTime pickedDate;
    private HomeTodayDialog todayDialog;
    private boolean dialogIsShown;

    private ArrayList<Integer> finishedTasksIds  = new ArrayList<>();
    private ArrayList<Integer> tasksToStartIds=new ArrayList<>();

    private TaskDAO taskDAO ;

    private ArrayList<LocalDateTime> criticals = new ArrayList<>();
    private ArrayList<LocalDateTime> high = new ArrayList<>();
    private ArrayList<LocalDateTime> average = new ArrayList<>();
    private ArrayList<LocalDateTime> low = new ArrayList<>();

    Drawable drawable = null;

    private LocalDateTime previousSelectedDayWithoutDefaultSystemPriority = null;

    private DefaultDayWithoutDefaultSystemPriority defaultDayWithoutDefaultSystemPriority =
            new DefaultDayWithoutDefaultSystemPriority( );

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
       taskDAO = TaskDAO.getInstance(getContext());
       defineViews();
       defineListeners();
       defineDefaultValues();

       setNewStateOfCalendar();
       setMonthsDropdownProperties();
       defineDayColors();
   }

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
        materialCalendarView=rootView.findViewById(R.id.fragment_home_calendar);
        buttonToChooseYear = rootView.findViewById(R.id.fragment_home_button_choose_year);
        textViewTodayDate = rootView.findViewById(R.id.fragment_home_indicative_date);

        LocalDateTime minDate = LocalDateTime.of(LimitsYearValues.MIN_YEAR.getValue(), 1,1,0,0, 0);
        LocalDateTime maxDate = LocalDateTime.of(LimitsYearValues.MAX_YEAR.getValue(), 12, 31, 23, 59, 59);

        materialCalendarView.state().edit()
                .setMaximumDate(
                        CalendarDay.from(maxDate.getYear(),
                                maxDate.getMonthValue(),
                                maxDate.getDayOfMonth())
                )
                .setMinimumDate(
                        CalendarDay.from(minDate.getYear(),
                                minDate.getMonthValue(),
                                minDate.getDayOfMonth())
                )
                .commit();

    }

    private void defineListeners(){
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                if(!oneOfSetOfTaskMomentsContainsTasksWithDefaultDefinedPriorities(date.getYear(),
                        date.getMonth(),date.getDay())) {
                    previousSelectedDayWithoutDefaultSystemPriority=LocalDateTime.of(
                        date.getYear(),date.getMonth(),date.getDay(),0,0,0,0
                    );
                    configureAndDefineAppearanceOfDayWithoutDefaultSystemPriority(date.getYear(),
                            date.getMonth(),date.getDay());
                } else {
                    previousSelectedDayWithoutDefaultSystemPriority=null;
                    materialCalendarView.removeDecorator(defaultDayWithoutDefaultSystemPriority);
                }

                onSelectedDayChange(date.getYear(),
                        date.getMonth(),
                        date.getDay());
            }
        });
        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                updatePickedDate();
                defineDayColors();

                if(previousSelectedDayWithoutDefaultSystemPriority != null) {

                    configureAndDefineAppearanceOfDayWithoutDefaultSystemPriority(previousSelectedDayWithoutDefaultSystemPriority.getYear(),
                            previousSelectedDayWithoutDefaultSystemPriority.getMonth().getValue(),
                            previousSelectedDayWithoutDefaultSystemPriority.getDayOfMonth());
                }
            }
        });
        buttonToChooseYear.setOnClickListener(this);
    }

    private void configureAndDefineAppearanceOfDayWithoutDefaultSystemPriority(int year,int month, int day){
        materialCalendarView.removeDecorator(defaultDayWithoutDefaultSystemPriority);

        drawable=ContextCompat.getDrawable(getContext(),
                R.drawable.default_day_without_default_system_priority_container);

        defaultDayWithoutDefaultSystemPriority.defineDrawable(drawable);
        defaultDayWithoutDefaultSystemPriority.defineDate(
                LocalDateTime.of(year,month,day,0,0,0)
        );

        materialCalendarView.addDecorator(defaultDayWithoutDefaultSystemPriority);
    }

    private void defineDefaultValues(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);

        updatePickedDate();
        textViewTodayDate.setText(String.format(
                "%s %s/%s/%s",
                getResources().getString(R.string.today_text),
                returnDayOrMonth(day),returnDayOrMonth(month),year));
    }

    private void updatePickedDate(){
        pickedDate = LocalDateTime.of(
                materialCalendarView.getCurrentDate().getYear(),
                materialCalendarView.getCurrentDate().getMonth(),
                materialCalendarView.getCurrentDate().getDay(),
                LocalDateTime.now().getHour(),
                LocalDateTime.now().getMinute()
        );

    }

  // Método chamado após uma data ser clicada
   public void onSelectedDayChange( int year, int month, int dayOfMonth) {
       pickedDate = LocalDateTime.of(year,month,dayOfMonth,0,0,0);

       todayDialog = new HomeTodayDialog(pickedDate,
               taskDAO.returnFinishedOrUnfinishedTasksIds(pickedDate,
                       true
               ),
               taskDAO.returnFinishedOrUnfinishedTasksIds(pickedDate,
                       false
               )
       );
       todayDialog.setParentFragment(this);

       if(!dialogIsShown){
           todayDialog.show(getActivity().getSupportFragmentManager(),"dialog");
           dialogIsShown = true;
       }

       HomeStaticValues.setPickedDayMemo(dayOfMonth);
   }

   private void defineDayColors(){
       materialCalendarView.removeDecorators();

       criticals = taskDAO.returnMonthLocalDateTimesOfGreatestPriorityDay(
                pickedDate.getMonthValue(), pickedDate.getYear(), Priorities.CRITICAL
        );
       high =taskDAO.returnMonthLocalDateTimesOfGreatestPriorityDay(
               pickedDate.getMonthValue(),pickedDate.getYear(),Priorities.HIGH
       );
       average = taskDAO.returnMonthLocalDateTimesOfGreatestPriorityDay(
               pickedDate.getMonthValue(),pickedDate.getYear(),Priorities.AVERAGE
       );
       low = taskDAO.returnMonthLocalDateTimesOfGreatestPriorityDay(
               pickedDate.getMonthValue(),pickedDate.getYear(),Priorities.LOW
       );


       Drawable lowDrawable = ContextCompat.getDrawable(getContext(),R.drawable.low_day_container);
       materialCalendarView.addDecorator(new AdagioDayLowDecorator(low,lowDrawable,R.color.adagio_gray));

       Drawable averageDrawable = ContextCompat.getDrawable(getContext(),R.drawable.medium_day_container);
       materialCalendarView.addDecorator(new AdagioDayAverageDecorator(average,averageDrawable,R.color.adagio_blue));

       Drawable highDrawable = ContextCompat.getDrawable(getContext(),R.drawable.high_day_container);
       materialCalendarView.addDecorator(new AdagioDayHighDecorator(high,highDrawable, R.color.adagio_yellow));

       Drawable criticalDrawable = ContextCompat.getDrawable(getContext(),R.drawable.critical_day_container);
       materialCalendarView.addDecorator(new AdagioDayCriticalDecorator(criticals,criticalDrawable,R.color.adagio_red));

       Drawable defaultTodayDrawable = ContextCompat.getDrawable(getContext(),R.drawable.default_today_container);
       materialCalendarView.addDecorator(new DefaultTodayDecorator(
               new ArrayList<>(Arrays.asList(LocalDateTime.now())),defaultTodayDrawable
       ));
   }

   // Auxilia o tratamento do cenário onde o usuário faz um duplo clique no dia (evita abrir dois dialogs sobrepostos)
    public void defineDialogIsNotShown(){
        dialogIsShown = false;
    }

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

    //
    private LocalDateTime getAuxLocalDateTime(LocalDateTime now){
           return LocalDateTime.of(HomeStaticValues.PICKED_YEAR_MEMO,  HomeStaticValues.PICKED_MONTH_MEMO,
                    HomeStaticValues.PICKED_DAY_MEMO,now.getHour(),now.getMinute());
    }

    // Após um mês ou ano ser modificado, define a visualização atual do calendário
    private void auxSetNewStateOfCalendar(){
        LocalDateTime now = LocalDateTime.now();
        pickedDate = getAuxLocalDateTime(now);

        materialCalendarView.setCurrentDate(
                CalendarDay.from(pickedDate.getYear(),pickedDate.getMonthValue(),
                        pickedDate.getDayOfMonth())
        );
        materialCalendarView.setSelectedDate(
                CalendarDay.from(pickedDate.getYear(),pickedDate.getMonthValue(),
                        pickedDate.getDayOfMonth())
        );
    }

    // Chama o método que faz a modificação da visualização do calendário, e define o dia como 1 caso o usuário vá para fevereiro, tendo antes escolhido um dia maior de outro mês (gerando exception)
    public void setNewStateOfCalendar(){

        try {
          auxSetNewStateOfCalendar();
        }catch(Exception e){
            HomeStaticValues.setPickedDayMemo(01);
            auxSetNewStateOfCalendar();
        }

    }

   private String returnDayOrMonth(int dayOrMonth) {
       if(Integer.toString(dayOrMonth).length() ==1 ){
           return "0"+dayOrMonth;
       }

       return Integer.toString(dayOrMonth);
   }

   // Itera sobre todos os conjuntos de localDateTimes categorizados por prioridade, comparando os elementos para verificar se algum coincide com a data especificada
   private boolean oneOfSetOfTaskMomentsContainsTasksWithDefaultDefinedPriorities(
           int year, int month,int day
   ){
        boolean contains =false;

        contains = setOfMomentsTasksWithPriorityContainsDate(year,month,day, low);

        if(!contains){
            contains = setOfMomentsTasksWithPriorityContainsDate(year,month, day, average);
        }

       if(!contains){
           contains = setOfMomentsTasksWithPriorityContainsDate(year,month,day, high);
       }

       if(!contains){
           contains = setOfMomentsTasksWithPriorityContainsDate(year,month, day, criticals);
       }

       return contains;
   }

   private boolean setOfMomentsTasksWithPriorityContainsDate(int year, int month, int day,
                                                             ArrayList<LocalDateTime> dates){
       boolean contains = false;
       for(LocalDateTime d: dates){
           if(parametersCoincideWithDate(year, month, day, d)){
               contains = true;
               break;
           }
       }
       return contains;
   }
   private boolean parametersCoincideWithDate(int year, int month, int day, LocalDateTime dateTime){
        if(dateTime.getYear() == year &&
                dateTime.getMonth().getValue() == month && dateTime.getDayOfMonth() == day){
            return true;
        }

        return false;
   }
}