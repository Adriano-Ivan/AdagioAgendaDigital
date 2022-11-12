package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.home_today_dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.models.enums.Priorities;

public class TasksOfDayDialogAdapter  extends BaseAdapter {

    private final List<TaskDtoRead> tasks = new ArrayList<>();
    private final Context context;
    private HomeTodayDialog parentFragment;

    public TasksOfDayDialogAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public TaskDtoRead getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {

        return tasks.get(position).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TaskDtoRead task = tasks.get(i);

        View genereatedView = returnTaskView(viewGroup);
        defineTaskInformation(task, genereatedView);

        return genereatedView;
    }

    private void defineTaskInformation(TaskDtoRead task, View generatedView){
        TextView name = generatedView.findViewById(R.id.task_of_day_item_text);
        name.setText(task.getDescription());

        CardView cardViewIndicator = generatedView.findViewById(R.id.task_of_day_priority_indicator);
        cardViewIndicator.setCardBackgroundColor(returnColor(task));

        SwitchCompat switchFinishedOrNot = generatedView.findViewById(R.id.task_of_day_mark_as_finished_or_not);

        if(parentFragment.isAmongToFinish(task.getId())
        ){
            switchFinishedOrNot.setChecked(true);
        } else if(parentFragment.isAmongToRestart(task.getId())
        ){
            switchFinishedOrNot.setChecked(false);
        }

        switchFinishedOrNot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    parentFragment.includeTaskToFinish(task.getId());
                } else {
                    parentFragment.includeTaskToRestart(task.getId());
                }
            }
        });
    }

    private int returnColor(TaskDtoRead task){
        if(task.getPriorityName().equals(Priorities.HIGH.getValue())){
            return parentFragment.getContext().getColor(R.color.yellow);
        } else if(task.getPriorityName().equals(Priorities.LOW.getValue())){
            return parentFragment.getContext().getColor(R.color.light_gray);
        } else if(task.getPriorityName().equals(Priorities.CRITICAL.getValue())){
            return parentFragment.getContext().getColor(R.color.red);
        }

        return parentFragment.getContext().getColor(R.color.blue);
    }
    private View returnTaskView(ViewGroup viewGroup){
        return LayoutInflater.from(context)
                .inflate(R.layout.task_of_day_item,viewGroup,false);
    }

    public void update(List<TaskDtoRead> tasks){
        this.tasks.clear();
        this.tasks.addAll(tasks);
        notifyDataSetChanged();
    }

    public void setParentFragment(HomeTodayDialog fragment){
        parentFragment=fragment;
    }
}


