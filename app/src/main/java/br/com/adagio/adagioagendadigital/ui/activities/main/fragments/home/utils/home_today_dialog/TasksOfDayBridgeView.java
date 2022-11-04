package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.home_today_dialog;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import java.time.LocalDateTime;
import java.util.ArrayList;

import br.com.adagio.adagioagendadigital.data.tag.TagDAO;
import br.com.adagio.adagioagendadigital.data.task.TaskDAO;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoCreate;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.models.entities.Tag;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.TagStaticValues;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.add_tag_to_task_dialog.AddTagToTaskDialog;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.add_tag_to_task_dialog.ListTagToTaskAdapter;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.add_tag_to_task_dialog.TagsToTaskStaticValues;

public class TasksOfDayBridgeView {

    private final TaskDAO taskDAO ;
    private final TasksOfDayDialogAdapter tasksOfDayDialogAdapter;
    private final Context context;
    private LocalDateTime day;

    public TasksOfDayBridgeView(Context context,LocalDateTime day){
        this.context = context;
        this.taskDAO = TaskDAO.getInstance(context);
        this.tasksOfDayDialogAdapter = new TasksOfDayDialogAdapter(context);
        this.day = day;
    }

    public void updateList(int limit, int offset, LocalDateTime day){
        if(offset >= 0){
            tasksOfDayDialogAdapter.update(taskDAO.list(limit,offset,day));
            TasksOfDayStaticValues.setOffsetList(offset);
        }
    }


    public TaskDtoRead get(int position){
        return tasksOfDayDialogAdapter.getItem(position);
    }

    private void updateListAux(LocalDateTime day){
        updateList(TasksOfDayStaticValues.LIMIT_LIST,
                TasksOfDayStaticValues.OFFSET_LIST,day);
    }

    public void configureAdapter(ListView tasksList, HomeTodayDialog fragment){
        tasksList.setAdapter(tasksOfDayDialogAdapter);
        tasksOfDayDialogAdapter.setParentFragment(fragment);
    }

    public void update(TaskDtoCreate task, int id,LocalDateTime day) {
        taskDAO.update(task,id);
        updateListAux(day);
    }

    public boolean thereArePreviousOrNextPage() {

        return thereIsPreviousPage() || thereIsNextPage();
    }

    public boolean thereIsNextPage() {
        Log.i("quantity", taskDAO.getQuantityOfTasksOfTheDay(day)+"");
        if(taskDAO.getQuantityOfTasksOfTheDay(day) >
                TasksOfDayStaticValues.NEXT_POSSIBLE_QUANTITY ){
            return true;
        }

        return false;
    }

    public boolean thereIsPreviousPage() {
        if(TasksOfDayStaticValues.OFFSET_LIST == 0){
            return false;
        }

        return true;
    }

    public void finishTasksByIds(ArrayList<Integer> tasksToFinishIds) {
        for(Integer id:tasksToFinishIds){
            taskDAO.updateToFinishedById(id);
        }
    }

    public void restartTasksByIds(ArrayList<Integer> tasksToRestartIds){
        for(Integer id: tasksToRestartIds){
            taskDAO.updateToUnfinishedById(id);
        }
    }
}
