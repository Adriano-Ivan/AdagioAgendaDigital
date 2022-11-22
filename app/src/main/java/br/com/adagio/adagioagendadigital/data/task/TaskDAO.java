package br.com.adagio.adagioagendadigital.data.task;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.renderscript.RenderScript;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.adagio.adagioagendadigital.data.DbLayer;
import br.com.adagio.adagioagendadigital.data.priority.DbPriorityStructure;
import br.com.adagio.adagioagendadigital.data.priority.PriorityDAO;
import br.com.adagio.adagioagendadigital.data.task_tag.DbTaskTagStructure;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoCreate;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.models.entities.Priority;
import br.com.adagio.adagioagendadigital.models.entities.Task;
import br.com.adagio.adagioagendadigital.models.enums.Priorities;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskStaticValues;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.TypeListTaskManagementOrderDate;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.TypeListTaskManagementOrderPriority;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TaskDAO {

    private static TaskDAO instance;
    private static PriorityDAO priorityDAO;
    private SQLiteDatabase db;

    private TaskDAO(Context context){
        DbLayer dbLayer = DbLayer.getInstance(context);

        db = dbLayer.getReadableDatabase();
    }

    public static TaskDAO getInstance(Context context){
        if(instance ==null && priorityDAO == null){
            instance = new TaskDAO(context);
            priorityDAO = PriorityDAO.getInstance(context);
        }

        return instance;
    }

    // Encapsula as listagens de tasks, recebendo parâmetros para guiar a construção do retorno
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<TaskDtoRead> list(int limit, int offset, LocalDateTime day,
                                  TypeListTaskManagementOrderDate typeListTaskManagementOrder,
                                  TypeListTaskManagementOrderPriority typeListTaskManagementOrderPriority,
                                  boolean isToAddIfTodayIsPriority){

        String orderPriorityByAscendingOrDescending = "";

        if(typeListTaskManagementOrderPriority != null){
            orderPriorityByAscendingOrDescending =
                    typeListTaskManagementOrderPriority.getValue()
                            == TypeListTaskManagementOrderPriority.PRIORITY_DESC.getValue() ? "DESC" : "ASC";
        } else {
            orderPriorityByAscendingOrDescending = "ASC";
        }

        List<TaskDtoRead> tasks = new ArrayList<>();
        String query = "";
        String queryTodayManagementScreen = "";

        String taskTableAlias = "t";
        String priorityTableAlias = "p";

        String innerJoinPrioritiesToReplace = "INNER_JOIN_PRIORITY_STATEMENT";
        String innerJoinPrioritiesStatement = String.format(
                "INNER JOIN %s as %s ON %s.%s = %s.%s",
                DbPriorityStructure.TABLE_NAME, priorityTableAlias,taskTableAlias,DbTaskStructure.Columns.PRIORITY_ID,
                priorityTableAlias, DbPriorityStructure.Columns.ID
        );

        String orderByPrioritiesToReplace = "ORDER_BY_PRIORITTY_STATEMENT";
        String orderByPrioritiesStatement = String.format(
                        "CASE %s.%s " +
                        "WHEN '%s' THEN 0 " +
                        "WHEN '%s' THEN 1 " +
                        "WHEN '%s' THEN 2 " +
                        "WHEN '%s' THEN 3 " +
                        "ELSE 0 " +
                        "END %s ", priorityTableAlias,DbPriorityStructure.Columns.NAME,
                Priorities.LOW.getValue(),Priorities.AVERAGE.getValue(), Priorities.HIGH.getValue(),
                Priorities.CRITICAL.getValue(), orderPriorityByAscendingOrDescending
        );

        // teste - ii
        // Se for diferente de nulo, é porque o retorno se destina para o dialog do dia exibido na home
        if(day == null){
            if(typeListTaskManagementOrder == TypeListTaskManagementOrderDate.TODAY){

                queryTodayManagementScreen = String.format("SELECT %s.%s as %s, %s.* FROM %s %s %s WHERE " +
                                "date(%s.%s) = date('%s') ORDER BY %s" +
                                "LIMIT %s OFFSET %s"
                        ,taskTableAlias, DbTaskStructure.Columns.ID, DbTaskStructure.Columns.ID_ALIAS,taskTableAlias,  DbTaskStructure.TABLE_NAME,
                                taskTableAlias, innerJoinPrioritiesToReplace,
                        taskTableAlias,DbTaskStructure.Columns.INITIAL_MOMENT,
                        LocalDateTime.now().toLocalDate().toString(),orderByPrioritiesToReplace,
                        limit,offset
                ).replace(innerJoinPrioritiesToReplace,innerJoinPrioritiesStatement)
                        .replace(orderByPrioritiesToReplace, orderByPrioritiesStatement);
            }

        } else {

            String dateToSearch = day.toLocalDate().toString();

            query = String.format("SELECT * FROM %s WHERE " +
                            "date(%s) = date('%s') " +
                            "LIMIT %s OFFSET %s"
                    ,DbTaskStructure.TABLE_NAME,
                    DbTaskStructure.Columns.INITIAL_MOMENT,
                    dateToSearch, limit,offset
                    );

        }

        if(day == null){
            int quantityOfToday = 0;

            if(typeListTaskManagementOrder == TypeListTaskManagementOrderDate.TODAY){
                quantityOfToday = insertInTasksArray(tasks, queryTodayManagementScreen);

                int offsetToRest = returnOffsetOfRest(offset,isToAddIfTodayIsPriority);

                query = String.format("SELECT * FROM %s WHERE date(%s) != date('%s') ORDER BY datetime(%s) ASC," +
                                "datetime(%s) ASC LIMIT %s OFFSET %s;",
                        DbTaskStructure.TABLE_NAME, DbTaskStructure.Columns.INITIAL_MOMENT,
                        LocalDateTime.now().toLocalDate().toString(),
                        DbTaskStructure.Columns.INITIAL_MOMENT,
                        DbTaskStructure.Columns.LIMIT_MOMENT,
                        limit - quantityOfToday, offsetToRest);

            } else if(typeListTaskManagementOrder==TypeListTaskManagementOrderDate.INITIAL_MOMENT_ASC){
                query = String.format("SELECT * FROM %s ORDER BY datetime(%s) ASC" +
                                " LIMIT %s OFFSET %s;",
                        DbTaskStructure.TABLE_NAME, DbTaskStructure.Columns.INITIAL_MOMENT,
                        limit , offset);
            } else if(typeListTaskManagementOrder==TypeListTaskManagementOrderDate.INITIAL_MOMENT_DESC){
                query = String.format("SELECT * FROM %s ORDER BY datetime(%s) DESC" +
                                " LIMIT %s OFFSET %s;",
                        DbTaskStructure.TABLE_NAME, DbTaskStructure.Columns.INITIAL_MOMENT,
                        limit , offset);
            } else if(typeListTaskManagementOrder==TypeListTaskManagementOrderDate.LIMIT_MOMENT_ASC){
                query = String.format("SELECT * FROM %s ORDER BY datetime(%s) ASC" +
                                " LIMIT %s OFFSET %s;",
                        DbTaskStructure.TABLE_NAME, DbTaskStructure.Columns.LIMIT_MOMENT,
                        limit , offset);
            } else if(typeListTaskManagementOrder==TypeListTaskManagementOrderDate.LIMIT_MOMENT_DESC){
                query = String.format("SELECT * FROM %s ORDER BY datetime(%s) DESC" +
                                " LIMIT %s OFFSET %s;",
                        DbTaskStructure.TABLE_NAME, DbTaskStructure.Columns.LIMIT_MOMENT,
                        limit , offset);
            }
            else {
                query = String.format("SELECT * FROM %s LIMIT %s OFFSET %s;",
                        DbTaskStructure.TABLE_NAME, limit, offset);
            }

        }


        try(Cursor c = db.rawQuery(query, null)){
            int quantityOfTasks = c.getCount();

            if(c.moveToFirst()){

                do {
                    TaskDtoRead task = fromCursor(c,false);
                    tasks.add(task);
                }while(c.moveToNext());
            }

            if(day == null && typeListTaskManagementOrder== TypeListTaskManagementOrderDate.TODAY && isToAddIfTodayIsPriority){
                TaskStaticValues.addToAuxOfRestAfterTodayMemo(quantityOfTasks);
            }

            return tasks;
        }

    }

    public ArrayList<LocalDateTime> returnMonthLocalDateTimesOfGreatestPriorityDay(
            int month, int year, Priorities priority ){
        ArrayList<LocalDateTime> localDateTimes = new ArrayList<>();

        String query = String.format(
                "SELECT %s.%s FROM %s as %s INNER JOIN %s as %s ON %s.%s = %s.%s" +
                        " WHERE strftime('YEAR', %s) LIKE '%s' AND" +
                        " strftime('MONTH',%s) LIKE '%s' AND %s.%s LIKE '%s';" ,
                't', DbTaskStructure.Columns.INITIAL_MOMENT, DbTaskStructure.TABLE_NAME,
                't', DbPriorityStructure.TABLE_NAME,'p', 'p',DbPriorityStructure.Columns.ID,
                't', DbTaskStructure.Columns.PRIORITY_ID,DbTaskStructure.Columns.INITIAL_MOMENT,
                year, DbTaskStructure.Columns.INITIAL_MOMENT,
                month, 'p', DbPriorityStructure.Columns.NAME,priority.getValue()
        ).replaceAll("YEAR", "%Y").replaceAll("MONTH","%m");


        try (Cursor c = db.rawQuery(query, null)){
            if(c.moveToFirst()){
                do{
                    LocalDateTime localDateTimeFromQuery = getLocalDateTimeFromQuery(c);

                    if(!arrayListContainsLocalDateTimeWithYearAndMonth(localDateTimes,
                            localDateTimeFromQuery)){
                        localDateTimes.add(localDateTimeFromQuery);
                    }
                } while(c.moveToNext());
            }
        }

        return localDateTimes;
    }

    private boolean arrayListContainsLocalDateTimeWithYearAndMonth(ArrayList<LocalDateTime> arrayList,LocalDateTime localDateTimeFromQuery) {
        for(LocalDateTime ldt : arrayList){

            if(ldt.getMonth() == localDateTimeFromQuery.getMonth() && ldt.getYear() == localDateTimeFromQuery.getYear()
            && ldt.getDayOfMonth() == localDateTimeFromQuery.getDayOfMonth()){
                return true;
            }
        }
        return false;
    }

    private LocalDateTime getLocalDateTimeFromQuery(Cursor c) {
        @SuppressLint("Range") String initialMoment = c.getString(c.getColumnIndex(DbTaskStructure.Columns.INITIAL_MOMENT));

        LocalDateTime value = LocalDateTime.parse(initialMoment);
        return value;
    }

    private int insertInTasksArray(List<TaskDtoRead> tasks, String queryTodayManagementScreen) {
        int quantityOfToday;
        try(Cursor c = db.rawQuery(queryTodayManagementScreen, null)){
            quantityOfToday = c.getCount();
            if(c.moveToFirst()){

                do {
                    TaskDtoRead task = fromCursor(c,true);
                    tasks.add(task);
                }while(c.moveToNext());
            }

        }
        return quantityOfToday;
    }

    private int returnOffsetOfRest(int offset,boolean isToAddIfTodayIsPriority){
        if(offset ==0){
            return offset;
        } else {
            if(!isToAddIfTodayIsPriority){
                return TaskStaticValues.returnPreviousMemberOfLastFromAuxOffsetOfRestAfterTodayMemo(
                        TaskStaticValues.AUX_OFFSET_OF_REST_AFTER_TODAY
                );
            }

            return TaskStaticValues.AUX_OFFSET_OF_REST_AFTER_TODAY;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Integer> returnFinishedOrUnfinishedTasksIds(LocalDateTime date,boolean finishedOrNot){
        String dateToSearch = date.toLocalDate().toString();
        ArrayList<Integer> ids = new ArrayList<>();

        String query = String.format("SELECT * FROM %s WHERE " +
                        "date(%s) = date('%s') AND %s = %s"
                ,DbTaskStructure.TABLE_NAME,
                DbTaskStructure.Columns.INITIAL_MOMENT,
                dateToSearch, DbTaskStructure.Columns.IS_FINISHED,
                finishedOrNot ? 1 : 0
        );

        try(Cursor c = db.rawQuery(query, null)){

            if(c.moveToFirst()){
                do {
                    @SuppressLint("Range") int id = c.getInt(
                            c.getColumnIndex(DbTaskStructure.Columns.ID)
                    );
                    ids.add(id);
                }while(c.moveToNext());
            }

        }

        return ids;
    }

    @SuppressLint("Range")
    private  TaskDtoRead fromCursor(Cursor c, boolean letSpecifiedTaskId){

        int id = c.getInt(c.getColumnIndex(DbTaskStructure.Columns.ID));

        if(letSpecifiedTaskId){
            id = c.getInt(c.getColumnIndex(DbTaskStructure.Columns.ID_ALIAS));
        }

        String description = c.getString(c.getColumnIndex(DbTaskStructure.Columns.DESCRIPTION));
        String initialMoment = c.getString(c.getColumnIndex(DbTaskStructure.Columns.INITIAL_MOMENT));
        String limitMoment = c.getString(c.getColumnIndex(DbTaskStructure.Columns.LIMIT_MOMENT));
        int isFinished = c.getInt(c.getColumnIndex(DbTaskStructure.Columns.IS_FINISHED));
        int priority_id = c.getInt(c.getColumnIndex(DbTaskStructure.Columns.PRIORITY_ID));

        LocalDateTime initialMomentDateTime=null;
        LocalDateTime limitMomentDateTime=null;

        initialMomentDateTime = LocalDateTime.parse(initialMoment);
        limitMomentDateTime = LocalDateTime.parse(limitMoment);

        ArrayList<Integer> tagIds = returnTagIds(id);
        String priorityName = returnPriorityName(priority_id);

        boolean isFinishedBoolean = isFinished == 0 ? false : true;

        TaskDtoRead t = new TaskDtoRead(id,description,initialMomentDateTime,limitMomentDateTime,isFinishedBoolean,priority_id,
                tagIds,priorityName);

        return t;
    }

    private String returnPriorityName(int id){
        List<Priority> priorities = priorityDAO.list();
        String name = "";

        for(Priority priority : priorities){
            if(priority.getId() == id){
                name = priority.getName();
            }
        }
        return name;
    }

    public void save(TaskDtoCreate task) {
        ContentValues values = new ContentValues();
        values.put(DbTaskStructure.Columns.DESCRIPTION, task.getDescription());
        values.put(DbTaskStructure.Columns.INITIAL_MOMENT, task.getInitialMoment());
        values.put(DbTaskStructure.Columns.LIMIT_MOMENT, task.getLimitMoment());
        values.put(DbTaskStructure.Columns.IS_FINISHED, task.isFinished());
        values.put(DbTaskStructure.Columns.PRIORITY_ID, task.getPriority_id());

        long id = db.insert(DbTaskStructure.TABLE_NAME, null, values);

        insertTaskTags(id, task.getTags());
    }

    private  ArrayList<Integer> returnTagIds(Integer id){
        String query = String.format("SELECT * FROM %s WHERE %s = %s;",
                DbTaskTagStructure.TABLE_NAME,DbTaskTagStructure.Columns.TASK_ID, id);

        ArrayList<Integer> ids = new ArrayList<>();

        try(Cursor c = db.rawQuery(query, null)){

            if(c.moveToFirst()){
                do {
                    @SuppressLint("Range") int tagId = c.getInt(
                            c.getColumnIndex(DbTaskTagStructure.Columns.TAG_ID)
                    );
                    ids.add(tagId);
                }while(c.moveToNext());
            }

        }

        return new ArrayList<>(ids);
    }

    public int getQuantityOfTasks(){
        return returnQuantityOfTasks(String.format(
                "SELECT COUNT(*) FROM %s", DbTaskStructure.TABLE_NAME));
    }

    public int getQuantityOfTasksOfTheDay(LocalDateTime day) {
        String dateToSearch = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateToSearch = day.toLocalDate().toString();

            String query = String.format("SELECT count(*) FROM %s WHERE " +
                            "date(%s) = date('%s') "
                    ,DbTaskStructure.TABLE_NAME,
                    DbTaskStructure.Columns.INITIAL_MOMENT,
                    dateToSearch
            );

            return returnQuantityOfTasks(query);
        }

        return 0;
    }

    private int returnQuantityOfTasks(String sql){
        Cursor count = db.rawQuery(sql,null);

        count.moveToFirst();
        int quantity = count.getInt(0);

        count.close();

        return quantity;
    }

    private boolean tagAndTaskVinculationAlreadyExists(long task, int tag){
        String query = String.format("SELECT * FROM %s WHERE %s = %s and %s = %s;",
                DbTaskTagStructure.TABLE_NAME,DbTaskTagStructure.Columns.TASK_ID, task,
                                    DbTaskTagStructure.Columns.TAG_ID, tag);

        ArrayList<Integer> ids = new ArrayList<>();

        try(Cursor c = db.rawQuery(query, null)){

            if(c.moveToFirst()){
                do {
                    @SuppressLint("Range") int tagId = c.getInt(
                            c.getColumnIndex(DbTaskTagStructure.Columns.TAG_ID)
                    );
                    ids.add(tagId);
                }while(c.moveToNext());
            }

        }

        return ids.size() > 0;
    }

    private void insertTaskTags(long id, ArrayList<Integer> tags){
        for(Integer tagId: tags){
            if(!tagAndTaskVinculationAlreadyExists(id, tagId)){
                ContentValues values = new ContentValues();

                values.put(DbTaskTagStructure.Columns.TASK_ID,id);
                values.put(DbTaskTagStructure.Columns.TAG_ID, tagId);

                db.insert(DbTaskTagStructure.TABLE_NAME, null, values);
            }

        }
    }

    public TaskDtoRead get(int id){
        String query = String.format("SELECT * FROM %s WHERE %s = %s;",
                DbTaskStructure.TABLE_NAME,DbTaskStructure.Columns.ID, id);

        TaskDtoRead task = null;
        try(Cursor c = db.rawQuery(query, null)){

            if(c.moveToFirst()){
                do {
                    task = fromCursor(c,false);

                }while(c.moveToNext());
            }

        }

        return task;
    }

    public void delete(long id){
        Log.i("delete", id+"");
      db.delete(DbTaskStructure.TABLE_NAME,String.format(
              "%s = %s", DbTaskStructure.Columns.ID,
              id
      ) ,null);
    }

    private void deleteUndoneVinculation(Integer taskId, ArrayList<Integer> tagIds){
        for(Integer tagId : tagIds){
            db.delete(DbTaskTagStructure.TABLE_NAME,String.format(
                    "%s = %s and %s = %s ", DbTaskTagStructure.Columns.TASK_ID,
                    taskId, DbTaskTagStructure.Columns.TAG_ID,tagId
            ) ,null);
        }
    }

    public void update(TaskDtoCreate t, Integer id) {
        ContentValues values = new ContentValues();
        values.put(DbTaskStructure.Columns.PRIORITY_ID, t.getPriority_id());
        values.put(DbTaskStructure.Columns.IS_FINISHED,t.isFinished());
        values.put(DbTaskStructure.Columns.DESCRIPTION,t.getDescription());
        values.put(DbTaskStructure.Columns.INITIAL_MOMENT,t.getInitialMoment());
        values.put(DbTaskStructure.Columns.LIMIT_MOMENT,t.getLimitMoment());

        db.update(DbTaskStructure.TABLE_NAME,values,
                 DbTaskStructure.Columns.ID + " = ?",
                new String[] { String.valueOf(id)});

        insertTaskTags(id, t.getTags());
        ArrayList<Integer> undoneVinculations = returnUndoneVinculation(id,t.getTags());
        deleteUndoneVinculation(id, undoneVinculations);
    }

    private ArrayList<Integer> returnUndoneVinculation(Integer id, ArrayList<Integer> tagIds){
        ArrayList<Integer> undoneVinculations = new ArrayList<>();

        String query = String.format("SELECT * FROM %s WHERE %s = %s;",
                DbTaskTagStructure.TABLE_NAME,DbTaskTagStructure.Columns.TASK_ID, id);

        ArrayList<Integer> oldTags = new ArrayList<>();

        try(Cursor c = db.rawQuery(query, null)){

            if(c.moveToFirst()){
                do {
                    @SuppressLint("Range") int tagId = c.getInt(
                            c.getColumnIndex(DbTaskTagStructure.Columns.TAG_ID)
                    );
                    oldTags.add(tagId);
                }while(c.moveToNext());
            }

        }

        for(Integer oldTag:oldTags){
            if(!newTagsHaveOldTag(oldTag, tagIds)){
                undoneVinculations.add(oldTag);
            }
        }

        return new ArrayList<>(undoneVinculations);
    }

    private boolean newTagsHaveOldTag(Integer tagId, ArrayList<Integer> newTagIds){
        for(Integer id : newTagIds){
            if(id == tagId){
                return true;
            }
        }
        return false;
    }

    //queries EXCLUSIVAS para RELATÓRIOS
    //futuramente substituir por ENUMS para facilitar compreensão
    public int getQuantityOfTasksBy (/*int dayMonthYear, int recordType, LocalDateTime reference*/ int month, int year, Priorities priority){
        int quantity = returnQuantityOfTasks(String.format("SELECT COUNT(*) FROM %s as %s INNER JOIN %s as %s ON %s.%s = %s.%s" +
                        " WHERE strftime('YEAR', %s) LIKE '%s' AND" +
                        " strftime('MONTH',%s) LIKE '%s' AND %s.%s LIKE '%s';" ,
                /*'t', DbTaskStructure.Columns.INITIAL_MOMT,*/ DbTaskStructure.TABLE_NAME,
                't', DbPriorityStructure.TABLE_NAME,'p', 'p',DbPriorityStructure.Columns.ID,
                't', DbTaskStructure.Columns.PRIORITY_ID,DbTaskStructure.Columns.INITIAL_MOMENT,
                year, DbTaskStructure.Columns.INITIAL_MOMENT,month, 'p', DbPriorityStructure.Columns.NAME, priority.getValue())
                .replaceAll("YEAR", "%Y").replaceAll("MONTH","%m"));
        return quantity;
    }

    public void updateToFinished(TaskDtoRead task){
        finishOrNot(task.getId(), 1);
    }

    public void updateToUnfinished(TaskDtoRead task) {
        finishOrNot(task.getId(), 0);
    }

    public void updateToFinishedById(Integer id){
        finishOrNot(id, 1);
    }

    public void updateToUnfinishedById(Integer id){
        finishOrNot(id, 0);
    }

    private void finishOrNot(Integer id, int opr){
        ContentValues values = new ContentValues();
        values.put(DbTaskStructure.Columns.IS_FINISHED, opr);

        db.update(DbTaskStructure.TABLE_NAME, values,
                DbTaskStructure.Columns.ID + " = ?",
                new String[] {String.valueOf(id)});
    }

}














