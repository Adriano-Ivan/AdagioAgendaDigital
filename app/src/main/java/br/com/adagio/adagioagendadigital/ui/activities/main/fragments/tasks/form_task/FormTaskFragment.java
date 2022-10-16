package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.form_task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.data.priority.PriorityDAO;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoCreate;
import br.com.adagio.adagioagendadigital.models.entities.Priority;
import br.com.adagio.adagioagendadigital.models.enums.Priorities;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskManagementFragment;

public class FormTaskFragment extends Fragment implements View.OnClickListener {

    private View rootView;

    private EditText descriptionEditText;
    private String limitMomentDate;
    private String initialMomentDate;
    private String limitMomentTime;
    private String initialMomentTime;

    private Button submitButton;
    private OnFragmentTaskFormCreateInteractionListener tListener;

    private Button buttonToShowInitialDateDialog;
    private Button buttonToShowInitialHourDialog;
    private Button buttonToShowFinalDateDialog;
    private Button buttonToShowFinalHourDialog;

    private DatePickerDialog initialDateDialog;
    private TimePickerDialog initialTimeDialog;
    private DatePickerDialog finalDateDialog;
    private TimePickerDialog finalTimeDialog;

    private TextView textViewInitialDate;
    private TextView textViewFinalDate;
    private TextView textViewInitialTime;
    private TextView textViewFinalTime;

    private RadioGroup radioGroupToChooseIsFinishedOrNot;
    private RadioGroup radioGroupToChoosePriority;

    private RadioButton radioButtonToChooseNotFinished;
    private RadioButton radioButtonToChooseFinished;

    private RadioButton radioButtonPriorityLow;
    private RadioButton radioButtonPriorityAverage;
    private RadioButton radioButtonPriorityHigh;
    private RadioButton radioButtonPriorityCritical;

    private PriorityDAO priorityDAO;
    private int priority_id;

    private List<Priority> priorities;

    private int day;private int month; private int year;private int hour; private int minute;
    private int finalDay; private int finalMonth; private int finalYear;private int finalHour;
    private int finalMinute;
    private int isFinished;

    public FormTaskFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_form_task, container, false);

        setAttributes();

        return rootView;
    }

    @Override
    public void onResume() {
        setAttributes();
        super.onResume();
    }

    private void setAttributes(){
        defineAttributes();
        defineViews();
        defineListeners();
        defineDefaultValues();
    }

    private void defineAttributes(){
        priorityDAO =PriorityDAO.getInstance(getActivity());

        priorities = new ArrayList<>(priorityDAO.list());

        for(Priority priority:priorities){
            Log.i("PRIORITY: ", priority.getId() + ", "+priority.getName());
        }
    }

    private void defineViews(){
        descriptionEditText = rootView.findViewById(R.id.fragment_form_task_description);
        submitButton = rootView.findViewById(R.id.fragment_form_task_submit);

        buttonToShowInitialDateDialog = rootView.findViewById(R.id.fragment_form_task_choose_initial_date);
        buttonToShowInitialHourDialog = rootView.findViewById(R.id.fragment_form_task_choose_initial_hour);
        buttonToShowFinalDateDialog = rootView.findViewById(R.id.fragment_form_task_choose_final_date);
        buttonToShowFinalHourDialog = rootView.findViewById(R.id.fragment_form_task_choose_final_hour);

        textViewInitialDate = rootView.findViewById(R.id.fragment_form_task_text_view_initial_date);
        textViewFinalDate = rootView.findViewById(R.id.fragment_form_text_view_final_date);
        textViewInitialTime = rootView.findViewById(R.id.fragment_form_task_text_view_initial_hour);
        textViewFinalTime = rootView.findViewById(R.id.fragment_form_task_text_view_final_hour);

        radioGroupToChooseIsFinishedOrNot = rootView.findViewById(R.id.fragment_form_task_group_choose_is_finished_or_not);
        radioGroupToChoosePriority = rootView.findViewById(R.id.fragment_form_task_group_choose_priority);

        radioButtonToChooseFinished = rootView.findViewById(R.id.fragment_form_task_choose_is_finished);
        radioButtonToChooseNotFinished = rootView.findViewById(R.id.fragment_form_task_choose_is_not_finished);

        radioButtonPriorityLow=rootView.findViewById(R.id.fragment_form_task_choose_short);
        radioButtonPriorityAverage=rootView.findViewById(R.id.fragment_form_task_choose_average);
        radioButtonPriorityHigh=rootView.findViewById(R.id.fragment_form_task_choose_high);
        radioButtonPriorityCritical=rootView.findViewById(R.id.fragment_form_task_choose_critical);

        radioButtonPriorityLow.setChecked(true);
        radioButtonToChooseNotFinished.setChecked(true);
    }

    private void defineDefaultValues(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            final LocalDateTime today = LocalDateTime.now();

            day = today.getDayOfMonth();
            finalDay = today.getDayOfMonth();

            month = today.getMonthValue();
            finalMonth = today.getMonthValue();

            year = today.getYear();
            finalYear =today.getYear();

            hour = today.getHour();
            finalHour  = 23;

            minute = today.getMinute();
            finalMinute=59;

            textViewInitialDate.setText(String.format("%s/%s/%s", returnDayOrMonthOrHourOrMinute(day),
                    returnDayOrMonthOrHourOrMinute(month),year));
            textViewFinalDate.setText(String.format("%s/%s/%s",returnDayOrMonthOrHourOrMinute(finalDay),
                    returnDayOrMonthOrHourOrMinute(finalMonth),finalYear));

            textViewInitialTime.setText(String.format("%s:%s",returnDayOrMonthOrHourOrMinute(hour),
                    returnDayOrMonthOrHourOrMinute(minute)));
            textViewFinalTime.setText(String.format("%s:%s",finalHour,finalMinute));

            initialMomentDate = String.format("%s-%s-%s",
                    year,
                    returnDayOrMonthOrHourOrMinute(month),
                    returnDayOrMonthOrHourOrMinute(day)
                    );
            limitMomentDate = String.format("%s-%s-%s",
                    year,
                    returnDayOrMonthOrHourOrMinute(finalMonth),
                    returnDayOrMonthOrHourOrMinute(finalDay)
            );

            initialMomentTime = String.format("%s:%s:%s",
                    returnDayOrMonthOrHourOrMinute(hour),
                    returnDayOrMonthOrHourOrMinute(minute),
                    "00");
            limitMomentTime = String.format("%s:%s:%s",
                    returnDayOrMonthOrHourOrMinute(finalHour),
                    returnDayOrMonthOrHourOrMinute(finalMinute),
                    "00");

            isFinished = 0;

            priority_id = priorities.get(1).getId();
        }
    }


    private void defineListeners(){
        submitButton.setOnClickListener(this);

        buttonToShowInitialDateDialog.setOnClickListener(this);
        buttonToShowInitialHourDialog.setOnClickListener(this);

        buttonToShowFinalDateDialog.setOnClickListener(this);
        buttonToShowFinalHourDialog.setOnClickListener(this);

        radioGroupToChooseIsFinishedOrNot.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.fragment_form_task_choose_is_finished){
                    isFinished = 1;
                } else {
                    isFinished = 0;
                }
            }
        });

        radioGroupToChoosePriority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                defineLevelPriority(i);
            }
        });
    }

    private void defineLevelPriority(int i){
        if(i == radioButtonPriorityLow.getId()){
            definePriority(Priorities.LOW);
        } else if(i == radioButtonPriorityAverage.getId()){
            definePriority(Priorities.AVERAGE);
        } else if(i == radioButtonPriorityHigh.getId()){
            definePriority(Priorities.HIGH);
        } else if(i == radioButtonPriorityCritical.getId()){
            definePriority(Priorities.CRITICAL);
        }
    }

    private void definePriority(Priorities priorityEnum){
        for(Priority priority : priorities){
            if(priority.getName() == priorityEnum.getValue()){
                priority_id = priority.getId();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fragment_form_task_submit){
            submitTask();
        } else if(view.getId() == R.id.fragment_form_task_choose_initial_date){

            showInitialDateDialog();
        } else if(view.getId() == R.id.fragment_form_task_choose_initial_hour){
            showInitialHourDialog();
        } else if(view.getId() == R.id.fragment_form_task_choose_final_date){
            showFinalDateDialog();
        } else if(view.getId() == R.id.fragment_form_task_choose_final_hour){
            showFinalTimeDialog();
        }
    }

    private void showInitialDateDialog(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            initialDateDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int yearL, int monthL, int dayL) {
                            day=dayL;month=monthL+1;year=yearL;
                            initialMomentDate = String.format("%s-%s-%s",
                                    year,
                                    returnDayOrMonthOrHourOrMinute(month),
                                    returnDayOrMonthOrHourOrMinute(day)
                                    );
                            textViewInitialDate.setText(String.format("%s/%s/%s",
                                    returnDayOrMonthOrHourOrMinute(day),
                                    returnDayOrMonthOrHourOrMinute(month),
                                    year
                                    ));
                        }
                    },year,month-1,day);

            initialDateDialog.show();
        }
    }

    private void showInitialHourDialog(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            initialTimeDialog = new TimePickerDialog(getActivity(),
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourL, int minuteL) {
                            hour=hourL; minute=minuteL;
                            initialMomentTime=String.format("%s:%s:%s",
                                    returnDayOrMonthOrHourOrMinute(hour),
                                    returnDayOrMonthOrHourOrMinute(minute),
                                    "00"
                                    );

                            textViewInitialTime.setText(String.format("%s:%s",
                                    returnDayOrMonthOrHourOrMinute(hour),
                                    returnDayOrMonthOrHourOrMinute(minute)));
                        }
                    }, hour, minute,true
            );

            initialTimeDialog.show();
        }
    }

    private void showFinalDateDialog(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            finalDateDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int yearL, int monthL, int dayL) {
                            finalDay=dayL;finalMonth=monthL+1;finalYear=yearL;
                            limitMomentDate = String.format("%s-%s-%s",
                                    year,
                                    returnDayOrMonthOrHourOrMinute(finalMonth),
                                    returnDayOrMonthOrHourOrMinute(finalDay)
                                    );
                            textViewFinalDate.setText(String.format("%s/%s/%s",
                                    returnDayOrMonthOrHourOrMinute(finalDay),
                                            returnDayOrMonthOrHourOrMinute(finalMonth),
                                            finalYear));
                        }
                    },finalYear,finalMonth-1,finalDay);

            finalDateDialog.show();
        }
    }

    private void showFinalTimeDialog(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            finalTimeDialog = new TimePickerDialog(getActivity(),
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourL, int minuteL) {
                            finalHour = hourL; minute=minuteL;
                            limitMomentTime = String.format("%s:%s:%s",
                                    returnDayOrMonthOrHourOrMinute(finalHour),
                                    returnDayOrMonthOrHourOrMinute(finalMinute),
                                    "00"
                                    );

                            textViewFinalTime.setText(String.format("%s:%s",
                                    returnDayOrMonthOrHourOrMinute(finalHour),
                                            returnDayOrMonthOrHourOrMinute(finalMinute)));
                        }
                    }, finalHour, finalMinute,true
            );

            finalTimeDialog.show();
        }
    }

    private String returnDayOrMonthOrHourOrMinute(int dayOrMonthOrHourOrMinute) {
        if(Integer.toString(dayOrMonthOrHourOrMinute).length() ==1 ){
            return "0"+dayOrMonthOrHourOrMinute;
        }

        return Integer.toString(dayOrMonthOrHourOrMinute);
    }

    public void auxSubmitTask(){
        submitTask();
    }

    private void submitTask(){
        TaskDtoCreate tCreate = new TaskDtoCreate(descriptionEditText.getText().toString(),
                returnInitialMoment(),
                returnLimitMoment(),
                priority_id,
                isFinished
        );

        tListener.onFragmentTaskFormSubmitInteraction(tCreate);
    }

    private String returnInitialMoment(){
        return String.format("%sT%s",initialMomentDate,initialMomentTime);
    }

    private String returnLimitMoment(){
        return String.format("%sT%s",limitMomentDate,limitMomentTime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TaskManagementFragment.OnFragmentTaskFormInteractionListener) {
            tListener = (OnFragmentTaskFormCreateInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        tListener = null;
    }

    public interface OnFragmentTaskFormCreateInteractionListener {

        void onFragmentTaskFormSubmitInteraction(TaskDtoCreate task);
    }
}