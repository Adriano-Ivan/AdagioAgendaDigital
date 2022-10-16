package br.com.adagio.adagioagendadigital.data.priority;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.adagio.adagioagendadigital.data.DbLayer;
import br.com.adagio.adagioagendadigital.data.task.DbTaskStructure;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.models.entities.Priority;

public class PriorityDAO {

    private static PriorityDAO dao;

    private SQLiteDatabase db;

    private PriorityDAO(Context context){
        DbLayer dbLayer = DbLayer.getInstance(context);
        db = dbLayer.getReadableDatabase();
    }

    public static PriorityDAO getInstance(Context context){
        if(dao == null){
            dao = new PriorityDAO(context);
        }

        return dao;
    }

    public List<Priority> list(){

        List<Priority> priorities = new ArrayList<>();

        String query = String.format("SELECT * FROM %s;",
                DbPriorityStructure.TABLE_NAME);

        try(Cursor c = db.rawQuery(query, null)){

            if(c.moveToFirst()){
                do {
                    Priority priority= fromCursor(c);
                    priorities.add(priority);
                }while(c.moveToNext());
            }

            return priorities;
        }

    }

    private static Priority fromCursor(Cursor c){
        @SuppressLint("Range") int id = c.getInt(c.getColumnIndex(DbPriorityStructure.Columns.ID));
        @SuppressLint("Range") String name = c.getString(c.getColumnIndex(DbPriorityStructure.Columns.NAME));

        return new Priority(id,name);
    }
}
