package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks;

import android.content.Context;
import android.os.Build;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import br.com.adagio.adagioagendadigital.data.task.TaskDAO;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoCreate;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.adapter.ListTaskAdapter;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.adapter.OperationAux;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.TypeListTaskManagementOrderDate;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.TypeListTaskManagementOrderPriority;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ListTaskBridgeView {

    private final TaskDAO taskDAO ;
    private final ListTaskAdapter listTaskAdapter;
    private final Context context;
    private TypeListTaskManagementOrderDate typeListTaskOrder  = null;
    private TypeListTaskManagementOrderPriority typeListTaskManagementOrderPriority = null;

    public ListTaskBridgeView(Context context){
        this.context = context;
        this.taskDAO = TaskDAO.getInstance(context);
        this.listTaskAdapter = new ListTaskAdapter(context);
    }

    public void updateList(int limit, int offset, boolean isToAddIfTodayIsPriority){

        if(offset >= 0){
            TaskStaticValues.setOffsetList(offset);
            if(typeListTaskOrder == null){
                listTaskAdapter.update(taskDAO.list(limit,offset,null,null,null,false));
            } else {
                listTaskAdapter.update(taskDAO.list(limit,offset,null, typeListTaskOrder,typeListTaskManagementOrderPriority,isToAddIfTodayIsPriority));
            }
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

        if(TaskStaticValues.CURRENT_PAGE > 1){
            updateList(TaskStaticValues.LIMIT_LIST,
                    TaskStaticValues.OFFSET_LIST - TaskStaticValues.LIMIT_LIST,
                    false
            );
            updateList(TaskStaticValues.LIMIT_LIST,
                    TaskStaticValues.OFFSET_LIST + TaskStaticValues.LIMIT_LIST,
                    true
            );
        }
    }

    private void updateListWithoutQueryingAgainAux(long intOfDeletedTask) {
        listTaskAdapter.update(OperationAux.DELETE_FROM_LIST_WITHOUT_QUERYING, intOfDeletedTask);
    }

    public void updateListAux(){
        updateList(TaskStaticValues.LIMIT_LIST,
                TaskStaticValues.OFFSET_LIST,typeListTaskOrder == TypeListTaskManagementOrderDate.TODAY);
    }

    public void configureAdapter(ListView tasksList, TaskManagementFragment fragment){
        tasksList.setAdapter(listTaskAdapter);
        listTaskAdapter.setFragment(fragment);
    }

    public void setTaskAsFinished(TaskDtoRead task){
        taskDAO.updateToFinished(task);

        task.setFinished(true);
        updateListAuxWithoutRepeat(task);
    }

    public void setTaskAsUnfinished(TaskDtoRead task) {
        taskDAO.updateToUnfinished(task);

        task.setFinished(false);
        updateListAuxWithoutRepeat(task);
    }

    private void updateListAuxWithoutRepeat(TaskDtoRead task){
        listTaskAdapter.updateWithoutRepeat(task);
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

    public void setOrderDateType(TypeListTaskManagementOrderDate typeListTaskOrder) {
        this.typeListTaskOrder = typeListTaskOrder;
    }

    public void setOrderPriorityType(TypeListTaskManagementOrderPriority typeListTaskOrderPriority) {
        this.typeListTaskManagementOrderPriority = typeListTaskOrderPriority;
    }
}
