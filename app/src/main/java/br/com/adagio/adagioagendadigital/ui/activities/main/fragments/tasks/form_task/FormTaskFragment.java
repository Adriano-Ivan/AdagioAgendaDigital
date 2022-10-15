package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.form_task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoCreate;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskManagementFragment;

public class FormTaskFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private EditText descriptionEditText;
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
        defineViews();
        defineListeners();
        defineDefaultValues();
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
    }

    private void defineDefaultValues(){
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        textViewInitialDate.setText(String.format("%s/%s/%s",day,month,year));
        textViewFinalDate.setText(String.format("%s/%s/%s",day,month,year));

        textViewInitialTime.setText(String.format("%s:%s",hour,minute));
        textViewFinalTime.setText(String.format("%s:%s",23,59));

    }

    private void defineListeners(){

        submitButton.setOnClickListener(this);

        buttonToShowInitialDateDialog.setOnClickListener(this);
        buttonToShowInitialHourDialog.setOnClickListener(this);

        buttonToShowFinalDateDialog.setOnClickListener(this);
        buttonToShowFinalHourDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fragment_form_task_submit){
            TaskDtoCreate tCreate = new TaskDtoCreate(descriptionEditText.getText().toString(),
                    "2022-08-12T23:23:38",
                    "2023-09-12T12:23:28",
                    0
                    );

            tListener.onFragmentTaskFormSubmitInteraction(tCreate);
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
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        initialDateDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        textViewInitialDate.setText(day+" "+month+" "+year);
                    }
                },year,month,day);

        initialDateDialog.show();
    }

    private void showInitialHourDialog(){
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        initialTimeDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        textViewInitialTime.setText(i+" "+i1);
                    }
                }, hour, minute,true
        );

        initialTimeDialog.show();
    }

    private void showFinalDateDialog(){
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        finalDateDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        textViewFinalDate.setText(day+" "+month+" "+year);
                    }
                },year,month,day);

        finalDateDialog.show();
    }

    private void showFinalTimeDialog(){
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        finalTimeDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        textViewFinalTime.setText(i+" "+i1);
                    }
                }, hour, minute,true
        );

        finalTimeDialog.show();
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