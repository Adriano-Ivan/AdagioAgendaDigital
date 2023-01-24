package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.home_today_dialog;

import android.content.Context;
import android.os.Build;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.util.ArrayList;

import br.com.adagio.adagioagendadigital.data.task.TaskDAO;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoCreate;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;

@RequiresApi(api = Build.VERSION_CODES.O)
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

    public int getAdapterCount(){
        return tasksOfDayDialogAdapter.getCount();
    }

    public void updateList(int limit, int offset, LocalDateTime day){
        if(offset >= 0){
            tasksOfDayDialogAdapter.update(taskDAO.list(limit,offset,day,null,null,false));
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

    public void configureAdapter(ListView tasksList, HomeDayDialog fragment){
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
