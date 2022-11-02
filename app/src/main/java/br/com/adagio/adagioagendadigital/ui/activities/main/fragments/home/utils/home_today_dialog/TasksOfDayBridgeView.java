package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.home_today_dialog;

import android.content.Context;
import android.widget.ListView;

import java.time.LocalDateTime;

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

    public TasksOfDayBridgeView(Context context){
        this.context = context;
        this.taskDAO = TaskDAO.getInstance(context);
        this.tasksOfDayDialogAdapter = new TasksOfDayDialogAdapter(context);
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
        if(taskDAO.getQuantityOfTasks() >
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
}
