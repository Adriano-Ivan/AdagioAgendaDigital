package br.com.adagio.adagioagendadigital.data.task;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import br.com.adagio.adagioagendadigital.data.DbLayer;

public class TaskDto {

    private static TaskDto instance;

    private SQLiteDatabase db;

    private TaskDto(Context context){
        DbLayer dbLayer = DbLayer.getInstance(context);

        db = dbLayer.getReadableDatabase();
    }

    public static TaskDto getInstance(Context context){
        if(instance ==null){
            instance = new TaskDto(context);
        }

        return instance;
    }
}
