package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.models.enums.Priorities;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskManagementFragment;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ListTaskAdapter extends BaseAdapter {

    private final List<TaskDtoRead> tasks = new ArrayList<>();
    private final Context context;
    private TaskManagementFragment parentFragment;

    public ListTaskAdapter(Context context){
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

    public void setFragment(TaskManagementFragment fragment){
        parentFragment=fragment;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TaskDtoRead task = tasks.get(i);

        View genereatedView = returnTaskView(viewGroup);
        defineTaskInformation(task, genereatedView);

        return genereatedView;
    }

    private String formatDateForShow(LocalDateTime localDateTime){
        //        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

        return String.format("%s/%s/%s %s:%s",
                    localDateTime.getDayOfMonth(),
                    localDateTime.getMonthValue(),
                    localDateTime.getYear(),
                    localDateTime.getHour(),
                localDateTime.getMinute()
                );
    }

    private void defineTaskInformation(TaskDtoRead task, View generatedView){
        TextView description = generatedView.findViewById(R.id.item_task_description);
        description.setText(task.getDescription());

        TextView initialMoment =generatedView.findViewById(R.id.item_task_initial_moment);
        initialMoment.setText(formatDateForShow(task.getInitialMoment()));

        TextView limitMoment =generatedView.findViewById(R.id.item_task_limit_moment);
        limitMoment.setText(formatDateForShow(task.getLimitMoment()));

        TextView finishTaskShortcut = generatedView.findViewById(R.id.item_task_finish_task);
        TextView notFinishTaskShortcut = generatedView.findViewById(R.id.item_task_not_finish_task);

        TextView priorityName =generatedView.findViewById(R.id.item_task_priority_name);

        if(task.getPriorityName().equals(Priorities.LOW.getValue())){
            priorityName.setText(R.string.low);
        } else if(task.getPriorityName().equals(Priorities.AVERAGE.getValue())){
            priorityName.setText(R.string.average);
        } else if(task.getPriorityName().equals(Priorities.HIGH.getValue())){
            priorityName.setText(R.string.high);
        } else if(task.getPriorityName().equals(Priorities.CRITICAL.getValue())){
            priorityName.setText(R.string.critical);
        }


        if(task.isFinished()){
            finishTaskShortcut.setVisibility(View.GONE);
            notFinishTaskShortcut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    parentFragment.openDialogToNotFinishTask(task);
                }
            });
        } else
        {
            notFinishTaskShortcut.setVisibility(View.GONE);
            finishTaskShortcut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    parentFragment.openDialogToFinishTask(task);
                }
            });
        }
    }

    private View returnTaskView(ViewGroup viewGroup){
        return LayoutInflater.from(context)
                .inflate(R.layout.task_item,viewGroup,false);
    }

    public void update(List<TaskDtoRead> tasks){
        this.tasks.clear();
        this.tasks.addAll(tasks);
        notifyDataSetChanged();
    }

    public void updateWithoutRepeat(TaskDtoRead task){
        List<TaskDtoRead> newTasksDtoRead = new ArrayList<TaskDtoRead>();

        for(TaskDtoRead taskDtoRead : this.tasks){
            if(taskDtoRead.getId() == task.getId()){
                taskDtoRead.setFinished(task.isFinished());
            }
            newTasksDtoRead.add(taskDtoRead);
        }

        this.tasks.clear();
        this.tasks.addAll(newTasksDtoRead);

        notifyDataSetChanged();
    }

    public void update(OperationAux operation, long intOfDeletedTask) {
        if(operation == OperationAux.DELETE_FROM_LIST_WITHOUT_QUERYING){
            List<TaskDtoRead> newTasks = new ArrayList<>();

            for(TaskDtoRead task : tasks){
                if(task.getId() != intOfDeletedTask){
                    newTasks.add(task);
                }
            }

            tasks.clear();
            this.tasks.addAll(newTasks);

            notifyDataSetChanged();
        }
    }
}
