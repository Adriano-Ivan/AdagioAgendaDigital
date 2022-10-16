package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import br.com.adagio.adagioagendadigital.data.task.TaskDAO;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoCreate;
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
        listTaskAdapter.update(taskDAO.list(limit,offset));
    }

    public void insert(TaskDtoCreate t){
        taskDAO.save(t);
        updateListAux();
    }

    public void delete(int position){
        long id = listTaskAdapter.getItemId(position);
        Log.i("DELETE", "delete: "+id);
        taskDAO.delete(id);
        updateListAux();
    }

    private void updateListAux(){
        updateList(TaskStaticValues.LIMIT_LIST,
                TaskStaticValues.OFFSET_LIST);
    }

    public void configureAdapter(ListView tasksList){

        tasksList.setAdapter(listTaskAdapter);

    }

    private void testValues(){
        if(taskDAO.list(5,0).size() == 0){
            auxTeste();
        } else {
            taskDAO.delete(8);
        }
    }

    private void auxTeste(){
//        TaskDtoCreate task = new TaskDtoCreate("TESTE", "2022-08-09T14:30:32",
//                "2022-08-11T15:43:38", 0);
//        TaskDtoCreate task2 = new TaskDtoCreate("TESTE 2", "2022-09-09T14:30:32",
//                "2022-10-11T15:43:38", 0);
//
//        taskDAO.save(task);
//        taskDAO.save(task2);
    }
}
