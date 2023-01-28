package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.home_today_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDateTime;
import java.util.ArrayList;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.HomeFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskManagementFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.form_task.FormTaskFragment;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeDayDialog extends DialogFragment implements View.OnClickListener {

    private ListView tasksList;
    private TasksOfDayBridgeView taskOfDayBridgeView;
    private LocalDateTime day;

    private OnFragmentTaskFormForSpecifiedDayInteractionListener dayListener;

    private ArrayList<Integer> tasksToFinishIds  = new ArrayList<>();
    private ArrayList<Integer> tasksToStartAgainIds=new ArrayList<>();

    private LinearLayout linearLayoutContainerPagination;
    private LinearLayout withoutTaskForThisDayContainer;

    private ImageButton imageButtonNextPage;
    private ImageButton imageButtonPreviousPage;

    private ImageButton imageButtonNextPageDisabled;
    private ImageButton imageButtonPreviousPageDisabled;

    private TextView textViewCurrentPage;
    private TextView tasksModalTitle;

    private Button saveDefinitionsButton;
    private Button buttonCloseDialog;

    private Button buttonGoToFormTaskForThisDay;
    private Button buttonGoToRelatoryGeneration;

    private HomeFragment parentFragment;

    public HomeDayDialog(LocalDateTime pickedDate,
                         ArrayList<Integer> finishedTasks,
                         ArrayList<Integer> tasksToStartIds
                           ){
        this.tasksToStartAgainIds = new ArrayList<>(tasksToStartIds);
        this.tasksToFinishIds=new ArrayList<>(finishedTasks);
        day = pickedDate;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.fragment_home_today_dialog,null);

        builder.setView(layout);

        TasksOfDayStaticValues.goBackToDefaultValue();
        defineViews(layout);
        defineListeners();

        taskOfDayBridgeView = new TasksOfDayBridgeView(getActivity(),day);
        taskOfDayBridgeView.configureAdapter(tasksList,this);

        defineDefaultVisualizations();
        updateTasksList();

        if(taskOfDayBridgeView.getAdapterCount() == 0){
            withoutTaskForThisDayContainer.setVisibility(View.VISIBLE);
        } else {
            withoutTaskForThisDayContainer.setVisibility(View.GONE);

        }

        return builder.create();
    }

    private void defineViews(View layout){
        tasksList =layout.findViewById(R.id.tasks_of_day_task_body_list);

        linearLayoutContainerPagination=layout.findViewById(R.id.fragment_today_dialog_container_pagination);
        withoutTaskForThisDayContainer = layout.findViewById(R.id.without_tasks_for_this_day_container);

        imageButtonNextPage=layout.findViewById(R.id.fragment_today_dialog_next_page);
        imageButtonNextPageDisabled=layout.findViewById(R.id.fragment_today_dialog_next_page_disabled);
        imageButtonPreviousPage=layout.findViewById(R.id.fragment_today_dialog_previous_page);
        imageButtonPreviousPageDisabled=layout.findViewById(R.id.fragment_today_dialog_previous_page_disabled);
        buttonCloseDialog=layout.findViewById(R.id.tasks_of_day_close_dialog);
        tasksModalTitle = layout.findViewById(R.id.tasks_modal_title);

        buttonGoToFormTaskForThisDay = layout.findViewById(R.id.tasks_of_day_create_task_in_this_day);
        textViewCurrentPage=layout.findViewById(R.id.fragment_today_dialog_text_page);
        saveDefinitionsButton=layout.findViewById(R.id.tasks_of_day_save_finished_or_not_tasks);
    }

    private void defineListeners(){
        imageButtonNextPage.setOnClickListener(this);
        imageButtonPreviousPage.setOnClickListener(this);
        saveDefinitionsButton.setOnClickListener(this);
        buttonCloseDialog.setOnClickListener(this);
        buttonGoToFormTaskForThisDay.setOnClickListener(this);
    }

    private void updateTasksList(){
        taskOfDayBridgeView.updateList(TasksOfDayStaticValues.LIMIT_LIST,TasksOfDayStaticValues.OFFSET_LIST,
                day);

    }

    private void defineDefaultVisualizations(){
        if (!taskOfDayBridgeView.thereArePreviousOrNextPage()) {
            linearLayoutContainerPagination.setVisibility(View.GONE);
        } else {
            if(!taskOfDayBridgeView.thereIsNextPage()){
                imageButtonNextPage.setVisibility(View.GONE);
                imageButtonNextPageDisabled.setVisibility(View.VISIBLE);
            } else {
                imageButtonNextPage.setVisibility(View.VISIBLE);
                imageButtonNextPageDisabled.setVisibility(View.GONE);
            }

            if(!taskOfDayBridgeView.thereIsPreviousPage()){
                imageButtonPreviousPage.setVisibility(View.GONE);
                imageButtonPreviousPageDisabled.setVisibility(View.VISIBLE);
            } else {
                imageButtonPreviousPage.setVisibility(View.VISIBLE);
                imageButtonPreviousPageDisabled.setVisibility(View.GONE);
            }
        }

        tasksModalTitle.setText(
                String.format("%s : %s",getResources().getString(R.string.tasks_for), formatDateValue())
        );

        textViewCurrentPage.setText(Integer.toString(TasksOfDayStaticValues.CURRENT_PAGE));
    }

    public void includeTaskToFinish(int id) {
        if(!tasksToFinishIds.contains(id)){
            tasksToFinishIds.add(id);
        }

        if(tasksToStartAgainIds.contains(id)){
            tasksToStartAgainIds.remove(tasksToStartAgainIds.indexOf(id));
        }

    }

    public void includeTaskToRestart(int id) {
        if(!tasksToStartAgainIds.contains(id)){
            tasksToStartAgainIds.add(id);
        }

        if(tasksToFinishIds.contains(id)){
            tasksToFinishIds.remove(tasksToFinishIds.indexOf(id));
        }

    }

    @Override
    public void onClick(View view) {
        if(view.getId() ==imageButtonNextPage.getId()){
            taskOfDayBridgeView.updateList(TasksOfDayStaticValues.LIMIT_LIST,
                    TasksOfDayStaticValues.OFFSET_LIST + TasksOfDayStaticValues.LIMIT_LIST,
                    day
                    );
            defineDefaultVisualizations();
        } else if(view.getId() == imageButtonPreviousPage.getId()){
            taskOfDayBridgeView.updateList(TasksOfDayStaticValues.LIMIT_LIST,
                    TasksOfDayStaticValues.OFFSET_LIST - TasksOfDayStaticValues.LIMIT_LIST,
                    day
                    );
            defineDefaultVisualizations();
        } else if(view.getId() == saveDefinitionsButton.getId()){
            confirmDefinitions();
            this.dismiss();
        } else if(view.getId() == buttonCloseDialog.getId()){
            this.dismiss();
        } else if(view.getId() == buttonGoToFormTaskForThisDay.getId()){
            this.dismiss();
            goToFormTaskForThisDay();
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        parentFragment.defineDialogIsNotShown();
    }

    private String formatDateValue(){
        String dayOfMonthString = Integer.toString(day.getDayOfMonth());
        String monthOfTask = Integer.toString(day.getMonthValue());

        return String.format("%s/%s/%s",
                  dayOfMonthString.length() == 1 ? "0"+dayOfMonthString : dayOfMonthString,
                monthOfTask.length() == 1? "0"+monthOfTask : monthOfTask,
                day.getYear()
                );
    }

    private void goToFormTaskForThisDay(){
        dayListener.onFragmentTaskFormForSpecifiedDayInteraction(TaskManagementFragment.Action.GO_TO_TASK,day);
    }

    public boolean isAmongToFinish(int id) {
        return tasksToFinishIds.contains(id);
    }

    public boolean isAmongToRestart(int id){
        return tasksToStartAgainIds.contains(id);
    }

    private void confirmDefinitions(){
        confirmToFinish();
        confirmToRestart();
    }

    private void confirmToFinish(){
        taskOfDayBridgeView.finishTasksByIds(tasksToFinishIds);
    }

    private void confirmToRestart(){
        taskOfDayBridgeView.restartTasksByIds(tasksToStartAgainIds);
    }

    public void setParentFragment(HomeFragment parentFragment){
        this.parentFragment =parentFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentTaskFormForSpecifiedDayInteractionListener) {
            dayListener = (OnFragmentTaskFormForSpecifiedDayInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentTaskFormInteractionListener");
        }
    }

    public interface OnFragmentTaskFormForSpecifiedDayInteractionListener {

        void onFragmentTaskFormForSpecifiedDayInteraction(TaskManagementFragment.Action action, LocalDateTime day);
    }
}
