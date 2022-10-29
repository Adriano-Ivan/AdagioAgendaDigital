package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import br.com.adagio.adagioagendadigital.data.task.TaskDAO;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoCreate;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.adapter.ListTaskAdapter;

public class ListTaskBridgeView {

    private final TaskDAO taskDAO ;
    private final ListTaskAdapter listTaskAdapter;
    private final Context context;

    public ListTaskBridgeView(Context context){
        this.context = context;
        this.taskDAO = TaskDAO.getInstance(context);
        this.listTaskAdapter = new ListTaskAdapter(context);
    }

    public void updateList(int limit,int offset){
        Log.i("quantity of tasks", "quantity: "+taskDAO.getQuantityOfTasks());
        Log.i("offset", "value: "+offset);
        Log.i("Next quantity", "value: "+(offset+TaskStaticValues.LIMIT_LIST));

        if(offset >= 0){
            listTaskAdapter.update(taskDAO.list(limit,offset));
            TaskStaticValues.setOffsetList(offset);
        }
    }

    public void insert(TaskDtoCreate t){
        taskDAO.save(t);
        updateListAux();
    }

    public void update(TaskDtoCreate t, Integer id){
        taskDAO.update(t, id);
        updateListAux();
    }

    public TaskDtoRead get(int position){
        return listTaskAdapter.getItem(position);
    }

    public void delete(int position){
        long id = listTaskAdapter.getItemId(position);

        taskDAO.delete(id);
        updateListAux();
    }

    private void updateListAux(){
        updateList(TaskStaticValues.LIMIT_LIST,
                TaskStaticValues.OFFSET_LIST);
    }

    public void configureAdapter(ListView tasksList, TaskManagementFragment fragment){
        tasksList.setAdapter(listTaskAdapter);
        listTaskAdapter.setFragment(fragment);
    }

    public void setTaskAsFinished(TaskDtoRead task){
        taskDAO.updateToFinished(task);
        updateListAux();
    }

    public void setTaskAsUnfinished(TaskDtoRead task) {
        taskDAO.updateToUnfinished(task);
        updateListAux();
    }

    public boolean thereArePreviousOrNextPage() {

        return thereIsPreviousPage() || thereIsNextPage();
    }

    public boolean thereIsNextPage() {
        if(taskDAO.getQuantityOfTasks() >
               TaskStaticValues.NEXT_POSSIBLE_QUANTITY ){
            return true;
        }

        return false;
    }

    public boolean thereIsPreviousPage() {
        if(TaskStaticValues.OFFSET_LIST == 0){
            return false;
        }

        return true;
    }
}
