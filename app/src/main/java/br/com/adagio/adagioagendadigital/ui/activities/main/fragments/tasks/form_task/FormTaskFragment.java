package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.form_task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.data.priority.PriorityDAO;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoCreate;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.models.entities.Priority;
import br.com.adagio.adagioagendadigital.models.enums.Priorities;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskManagementFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.form_task.util.FormTaskError;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.add_tag_to_task_dialog.AddTagToTaskDialog;

@RequiresApi(api = Build.VERSION_CODES.O)
public class FormTaskFragment extends Fragment implements View.OnClickListener {

    private View rootView;

    private TextView textTopCreateOrEdit;
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

    private SwitchCompat switchCompatFinishedOrNot;
    private RadioGroup radioGroupToChoosePriority;

    private RadioButton radioButtonPriorityLow;
    private RadioButton radioButtonPriorityAverage;
    private RadioButton radioButtonPriorityHigh;
    private RadioButton radioButtonPriorityCritical;

    private TextView descriptionErrorLabel;
    private TextView descriptionTooLargeErrorLabel;
    private TextView limitDateErrorLabel;
    private TextView initialDateErrorLabel;
    private TextView initialHourErrorLabel;
    private TextView limitHourErrorLabel;

    private PriorityDAO priorityDAO;
    private int priority_id;

    private List<Priority> priorities;

    private int day;private int month; private int year;private int hour; private int minute;
    private int finalDay; private int finalMonth; private int finalYear;private int finalHour;
    private int finalMinute;
    private int isFinished;

    private AddTagToTaskDialog addTagToTaskDialog;
    private ArrayList<Integer> acumulatedIdTags =new ArrayList<>();

    private TaskDtoRead possibleTaskToEdit;
    private boolean isToEdit;

    private Button buttonToOpenTagDialog;

    public Hashtable<FormTaskError,Boolean>  formTaskError = new Hashtable<FormTaskError, Boolean>();

    private LocalDateTime predefinedDay = null;

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

        definePossibleAttributesToEdition();
        definePossibleAttributeForPredefinedDay();
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
        setDictionaryFormErrors();
    }

    private void setDictionaryFormErrors() {
        formTaskError.put(FormTaskError.DESCRIPTION, false);
        formTaskError.put(FormTaskError.INITIAL_DATE, false);
        formTaskError.put(FormTaskError.INITIAL_HOUR, false);
        formTaskError.put(FormTaskError.LIMIT_DATE,false);
        formTaskError.put(FormTaskError.LIMIT_HOUR, false);
    }

    private void definePossibleAttributesToEdition(){
        if(getArguments() != null){
            possibleTaskToEdit = (TaskDtoRead) getArguments().getSerializable("taskToEdit");
        }

        if( possibleTaskToEdit != null)    {
            isToEdit = true;
        } else {
            isToEdit = false;
            possibleTaskToEdit = null;
        }
    }

    private void definePossibleAttributeForPredefinedDay(){
        if(getArguments() != null){
            predefinedDay = (LocalDateTime) getArguments().getSerializable("predefinedDay");
        }
    }

    private void defineAttributes(){
        priorityDAO =PriorityDAO.getInstance(getActivity());

        priorities = new ArrayList<>(priorityDAO.list());
    }

    private void defineViews(){
        textTopCreateOrEdit =rootView.findViewById(R.id.fragment_form_create_or_edit_your_task_text_view);
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

        switchCompatFinishedOrNot = rootView.findViewById(R.id.fragment_form_task_is_finished_or_not);
        radioGroupToChoosePriority = rootView.findViewById(R.id.fragment_form_task_group_choose_priority);

        radioButtonPriorityLow=rootView.findViewById(R.id.fragment_form_task_choose_short);
        radioButtonPriorityAverage=rootView.findViewById(R.id.fragment_form_task_choose_average);
        radioButtonPriorityHigh=rootView.findViewById(R.id.fragment_form_task_choose_high);
        radioButtonPriorityCritical=rootView.findViewById(R.id.fragment_form_task_choose_critical);

        buttonToOpenTagDialog = rootView.findViewById(R.id.fragment_form_task_button_to_open_tags_dialog);

        descriptionErrorLabel = rootView.findViewById(R.id.fragment_form_task_description_error_label);
        descriptionTooLargeErrorLabel = rootView.findViewById(R.id.fragment_form_task_description_too_large_error_label);
        initialDateErrorLabel = rootView.findViewById(R.id.fragment_form_task_initial_date_error_label);
        limitDateErrorLabel = rootView.findViewById(R.id.fragment_form_task_final_date_error_label);
        initialHourErrorLabel = rootView.findViewById(R.id.fragment_form_task_initial_hour_error_label);
        limitHourErrorLabel = rootView.findViewById(R.id.fragment_form_task_final_hour_error_label);

        if(possibleTaskToEdit == null){
            switchCompatFinishedOrNot.setChecked(false);
        } else if(isToEdit){
            switchCompatFinishedOrNot.setChecked(possibleTaskToEdit.isFinished());
        }
    }

    private void defineDefaultValues(){
        final LocalDateTime today = predefinedDay != null ? predefinedDay : LocalDateTime.now();

        defineMomentProperties(today);

        textViewInitialDate.setText(String.format("%s/%s/%s", returnDayOrMonthOrHourOrMinute(day),
                returnDayOrMonthOrHourOrMinute(month),year));
        textViewFinalDate.setText(String.format("%s/%s/%s",returnDayOrMonthOrHourOrMinute(finalDay),
                returnDayOrMonthOrHourOrMinute(finalMonth),finalYear));

        textViewInitialTime.setText(String.format("%s:%s",returnDayOrMonthOrHourOrMinute(hour),
                returnDayOrMonthOrHourOrMinute(minute)));
        textViewFinalTime.setText(String.format("%s:%s",returnDayOrMonthOrHourOrMinute(finalHour),
                returnDayOrMonthOrHourOrMinute(finalMinute)));

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

        defineDescriptionAndIsFinishedAndPriorityAndTagIds();
    }

    private void defineDescriptionAndIsFinishedAndPriorityAndTagIds(){
        if(possibleTaskToEdit == null){
            isFinished = 0;

            priority_id = priorities.get(0).getId();
            descriptionEditText.setText("");
            radioGroupToChoosePriority.check(radioButtonPriorityLow.getId());
            textTopCreateOrEdit.setText(getResources().getString(R.string.create_your_task));
            acumulatedIdTags = new ArrayList<>();
        } else {
            isFinished = possibleTaskToEdit.isFinished() ? 1 : 0;
            acumulatedIdTags =new ArrayList<>(possibleTaskToEdit.getTags()) ;
            defineTaskToEditPriority(possibleTaskToEdit.getPriority_id());
            descriptionEditText.setText(possibleTaskToEdit.getDescription());
            textTopCreateOrEdit.setText(getResources().getString(R.string.edit_your_task));
        }
    }

    private void defineTaskToEditPriority(int priority_id) {
        Priorities priority = null;

        for(Priority priorityEnt : priorities){
            if(priorityEnt.getId() == priority_id){
                if(priorityEnt.getName().equals(Priorities.LOW.getValue())){
                    priority = Priorities.LOW;
                } else if(priorityEnt.getName().equals(Priorities.AVERAGE.getValue())){
                    priority = Priorities.AVERAGE;
                } else if(priorityEnt.getName().equals(Priorities.HIGH.getValue())){
                    priority = Priorities.HIGH;
                } else if(priorityEnt.getName().equals(Priorities.CRITICAL.getValue())){
                    priority = Priorities.CRITICAL;
                }
            }
        }

        definePriority(priority);
    }

    private void defineMomentProperties(LocalDateTime today) {
        boolean isTodayInFact =
                today.getDayOfMonth() == LocalDateTime.now().getDayOfMonth() &&
                        today.getYear() == LocalDateTime.now().getYear() &&
                        today.getMonthValue() == LocalDateTime.now().getMonthValue();

        if(possibleTaskToEdit == null){
            Log.i("isToday", isTodayInFact+"");
            day = today.getDayOfMonth();
            finalDay = today.getDayOfMonth();

            month = today.getMonthValue();
            finalMonth = today.getMonthValue();

            year = today.getYear();
            finalYear =today.getYear();

            hour = isTodayInFact ? today.getHour()  : LocalDateTime.now().getHour();
            finalHour  = 23;

            minute = isTodayInFact ?  today.getMinute() : LocalDateTime.now().getMinute();
            finalMinute=59;
        }else {
            day = possibleTaskToEdit.getInitialMoment().getDayOfMonth();
            finalDay = possibleTaskToEdit.getLimitMoment().getDayOfMonth();

            month = possibleTaskToEdit.getInitialMoment().getMonthValue();
            finalMonth = possibleTaskToEdit.getLimitMoment().getMonthValue();

            year = possibleTaskToEdit.getInitialMoment().getYear();
            finalYear = possibleTaskToEdit.getLimitMoment().getYear();

            Log.i("isToday", isTodayInFact+"");
            hour =  possibleTaskToEdit.getInitialMoment().getHour();
            finalHour =possibleTaskToEdit.getLimitMoment().getHour();

            minute = possibleTaskToEdit.getInitialMoment().getMinute() ;
            finalMinute =  possibleTaskToEdit.getLimitMoment().getMinute();
        }
    }


    private void defineListeners(){
        submitButton.setOnClickListener(this);

        buttonToShowInitialDateDialog.setOnClickListener(this);
        buttonToShowInitialHourDialog.setOnClickListener(this);

        buttonToShowFinalDateDialog.setOnClickListener(this);
        buttonToShowFinalHourDialog.setOnClickListener(this);
        buttonToOpenTagDialog.setOnClickListener(this);

        switchCompatFinishedOrNot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isFinished = 1;
                } else{
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
            if(priority.getName().equals(priorityEnum.getValue())){
                priority_id = priority.getId();

                if(priorityEnum == Priorities.LOW){
                    radioGroupToChoosePriority.check(radioButtonPriorityLow.getId());
                } else if(priorityEnum == Priorities.AVERAGE){
                    radioGroupToChoosePriority.check(radioButtonPriorityAverage.getId());
                } else if(priorityEnum == Priorities.HIGH){
                    radioGroupToChoosePriority.check(radioButtonPriorityHigh.getId());
                } else if(priorityEnum == Priorities.CRITICAL){
                    radioGroupToChoosePriority.check(radioButtonPriorityCritical.getId());
                }

                break;
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
        } else if(view.getId() == R.id.fragment_form_task_button_to_open_tags_dialog){
            addTagToTaskDialog = new AddTagToTaskDialog(acumulatedIdTags);
            addTagToTaskDialog.show(getActivity().getSupportFragmentManager(),"dialog");
        }
    }

    public void defineTagIds(ArrayList<Integer> ids){
        acumulatedIdTags = new ArrayList<>(ids);
    }

    private void showInitialDateDialog(){
        initialDateDialog = new DatePickerDialog(getActivity(), R.style.DatePickerColor,
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

    private void showInitialHourDialog(){
        initialTimeDialog = new TimePickerDialog(getActivity(), R.style.DatePickerColor,
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

    private void showFinalDateDialog(){
        finalDateDialog = new DatePickerDialog(getActivity(), R.style.DatePickerColor,
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

    private void showFinalTimeDialog(){
        finalTimeDialog = new TimePickerDialog(getActivity(), R.style.DatePickerColor,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourL, int minuteL) {
                        finalHour = hourL; finalMinute=minuteL;
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

    private String returnDayOrMonthOrHourOrMinute(int dayOrMonthOrHourOrMinute) {
        if(Integer.toString(dayOrMonthOrHourOrMinute).length() ==1 ){
            return "0"+dayOrMonthOrHourOrMinute;
        }

        return Integer.toString(dayOrMonthOrHourOrMinute);
    }

    private LocalDate returnAuxiliarInitialDateToVerify(){
        return LocalDate.of(year, month, day);
    }

    private LocalDate returnAuxiliarLimitDateToVerify(){
        return  LocalDate.of(finalYear,finalMonth,finalDay);
    }

    private boolean initialDateIsBefore(LocalDate initialDateToVerify){
        return initialDateToVerify
                .isBefore(returnAuxiliarLimitDateToVerify());
    }

    private boolean initialDateIsEqual(LocalDate initialDateToVerify){
        return initialDateToVerify.isEqual(returnAuxiliarLimitDateToVerify());
    }

    private boolean limitDateIsAfter(LocalDate limitDateToVerify){
        return limitDateToVerify.isAfter(returnAuxiliarInitialDateToVerify());
    }

    private boolean limitDateIsEqual(LocalDate limitDateToVerify){
        return limitDateToVerify.isEqual(returnAuxiliarInitialDateToVerify());
    }

    private boolean initialDateIsBeforeOrEqual(){
        LocalDate initialDateToVerify = returnAuxiliarInitialDateToVerify();

        return  initialDateIsBefore(initialDateToVerify)
                || initialDateIsEqual(initialDateToVerify);
    }

    private boolean limitDateIsAfterOrEqual(){
        LocalDate limitDateToVerify = returnAuxiliarLimitDateToVerify();

        return limitDateIsAfter(limitDateToVerify) ||
                limitDateIsEqual(limitDateToVerify);
    }

    private boolean taskDateIsInvalid(MomentDate momentDate){
        if(momentDate == MomentDate.INITIAL){
            if(initialDateIsBeforeOrEqual()){
                return false;
            }
        } else if(momentDate == MomentDate.LIMIT) {
            if(limitDateIsAfterOrEqual()){
                return false;
            }
        }

        return true;
    }

    private boolean initialTimeIsAfter(LocalTime initialTimeToVerify, LocalTime limitTimeToVerify){
        if(initialTimeToVerify.isAfter(limitTimeToVerify)){
            return true;
        }
        return false;
    }

    private boolean limitTimeIsBefore(LocalTime limitTimeToVerify, LocalTime initialTimeToVerify){
        if(limitTimeToVerify.isBefore(initialTimeToVerify)){
            return true;
        }

        return false;
    }

    private boolean taskTimeIsInvalid (MomentTime momentTime){
        LocalTime initialTimeToVerify = LocalTime.of(hour, minute);
        LocalTime limitTimeToVerify = LocalTime.of(finalHour, finalMinute);

        if(momentTime == MomentTime.INITIAL){

            if(initialDateIsEqual(returnAuxiliarInitialDateToVerify())){
                return initialTimeIsAfter(initialTimeToVerify,limitTimeToVerify);
            } else if(initialDateIsBefore(returnAuxiliarInitialDateToVerify())){
                return false;
            }

            return initialTimeIsAfter(initialTimeToVerify,limitTimeToVerify);

        } else if(momentTime == MomentTime.LIMIT){

            if(initialDateIsEqual(returnAuxiliarInitialDateToVerify())){
                return limitTimeIsBefore(limitTimeToVerify, initialTimeToVerify);
            } else if(initialDateIsBefore(returnAuxiliarInitialDateToVerify())){
                return false;
            }

            return limitTimeIsBefore(limitTimeToVerify,initialTimeToVerify);
        }

        return true;
    }

    public boolean validFormInformation (){
        boolean validForm = true;

        if(descriptionEditText.getText().toString().trim().length() > 150){
            validForm = false;
            formTaskError.put(FormTaskError.DESCRIPTION_TOO_LARGE, true);
        } else {
            formTaskError.put(FormTaskError.DESCRIPTION_TOO_LARGE, false);
        }

        if(descriptionEditText.getText().toString().trim().equals("")){
            validForm = false;
            formTaskError.put(FormTaskError.DESCRIPTION, true);
        } else {
            formTaskError.put(FormTaskError.DESCRIPTION, false);
        }

        if(taskDateIsInvalid(MomentDate.INITIAL)){
            validForm = false;
            formTaskError.put(FormTaskError.INITIAL_DATE, true);
        } else {
            formTaskError.put(FormTaskError.INITIAL_DATE, false);
        }

        if(taskDateIsInvalid(MomentDate.LIMIT)){
            validForm = false;
            formTaskError.put(FormTaskError.LIMIT_DATE, true);
        } else {
            formTaskError.put(FormTaskError.LIMIT_DATE, false);
        }

        if(taskTimeIsInvalid(MomentTime.INITIAL)){
            validForm = false;
            formTaskError.put(FormTaskError.INITIAL_HOUR, true);
        } else {
            formTaskError.put(FormTaskError.INITIAL_HOUR, false);
        }

        if(taskTimeIsInvalid(MomentTime.LIMIT)){
            validForm = false;
            formTaskError.put(FormTaskError.LIMIT_HOUR, true);
        } else {
            formTaskError.put(FormTaskError.LIMIT_HOUR, false);
        }

        return validForm;
    }

    private void propagateErrorWarnings(){
        if(formTaskError.get(FormTaskError.DESCRIPTION_TOO_LARGE)){
            descriptionTooLargeErrorLabel.setVisibility(View.VISIBLE);
        } else{
            descriptionTooLargeErrorLabel.setVisibility(View.GONE);
        }

        if(formTaskError.get(FormTaskError.DESCRIPTION)){
            descriptionErrorLabel.setVisibility(View.VISIBLE);
        } else {
            descriptionErrorLabel.setVisibility(View.GONE);
        }

        if(formTaskError.get(FormTaskError.INITIAL_DATE)){
            initialDateErrorLabel.setVisibility(View.VISIBLE);
        } else {
            initialDateErrorLabel.setVisibility(View.GONE);
        }

        if(formTaskError.get(FormTaskError.LIMIT_DATE)){
            limitDateErrorLabel.setVisibility(View.VISIBLE);
        } else {
            limitDateErrorLabel.setVisibility(View.GONE);
        }

        if (formTaskError.get(FormTaskError.INITIAL_HOUR)) {
            initialHourErrorLabel.setVisibility(View.VISIBLE);
        } else {
            initialHourErrorLabel.setVisibility(View.GONE);
        }

        if(formTaskError.get(FormTaskError.LIMIT_HOUR)){
            limitHourErrorLabel.setVisibility(View.VISIBLE);
        } else{
            limitHourErrorLabel.setVisibility(View.GONE);
        }
    }

    public void auxSubmitTask(){
        submitTask();
    }

    private void submitTask(){
        if(validFormInformation()){
            TaskDtoCreate tCreate = new TaskDtoCreate(descriptionEditText.getText().toString().trim(),
                    returnInitialMoment(),
                    returnLimitMoment(),
                    priority_id,
                    isFinished,
                    acumulatedIdTags
            );
            if(possibleTaskToEdit == null){
                tListener.onFragmentTaskFormSubmitInteraction(tCreate,null);
            } else {
                tListener.onFragmentTaskFormSubmitInteraction(tCreate, possibleTaskToEdit.getId());
            }
        } else {
            Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            propagateErrorWarnings();
        }
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

        void onFragmentTaskFormSubmitInteraction(TaskDtoCreate task,Integer id);
    }

    private enum MomentDate {
        LIMIT,
        INITIAL
    }

    private enum MomentTime {
        LIMIT,
        INITIAL
    }
}