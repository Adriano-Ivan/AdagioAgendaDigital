package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.home_today_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDateTime;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.add_tag_to_task_dialog.ListTagToTaskBridgeView;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.add_tag_to_task_dialog.TagsToTaskStaticValues;

public class HomeTodayDialog extends DialogFragment {

    private ListView tasksList;
    private TasksOfDayBridgeView taskOfDayBridgeView;
    private LocalDateTime day;

    public HomeTodayDialog(LocalDateTime pickedDate){
        day = pickedDate;
        Log.i("picked date", pickedDate.toString());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.fragment_home_today_dialog,null);

        builder.setView(layout);

        defineViews(layout);

        taskOfDayBridgeView = new TasksOfDayBridgeView(getActivity());
        taskOfDayBridgeView.configureAdapter(tasksList,this);

        updateTasksList();

        return builder.create();
    }

    private void defineViews(View layout){
        tasksList =layout.findViewById(R.id.tasks_of_day_task_body_list);
    }

    private void updateTasksList(){
        taskOfDayBridgeView.updateList(TasksOfDayStaticValues.LIMIT_LIST,TasksOfDayStaticValues.OFFSET_LIST,
                day);
    }

    private void defineDefaultVisualizations(){

    }
}
