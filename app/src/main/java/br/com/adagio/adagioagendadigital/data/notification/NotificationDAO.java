package br.com.adagio.adagioagendadigital.data.notification;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.util.ArrayList;

import br.com.adagio.adagioagendadigital.data.DbLayer;
import br.com.adagio.adagioagendadigital.data.task.DbTaskStructure;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.models.entities.Notification;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NotificationDAO {
    private static NotificationDAO instance;
    private SQLiteDatabase db;

    private NotificationDAO(Context context){
        DbLayer dbLayer = DbLayer.getInstance(context);

        db = dbLayer.getReadableDatabase();
    }

    public static NotificationDAO getInstance(Context context){
        if(instance ==null){
            instance = new NotificationDAO(context);
        }

        return instance;
    }

    public void save(Notification notification) {
        ContentValues values = new ContentValues();
        values.put(DbNotificationStructure.Columns.EMITTED_AT, notification.getEmitted_at().toString());
        values.put(DbNotificationStructure.Columns.TASK_ID, notification.getTask_id());
        values.put(DbNotificationStructure.Columns.MESSAGE, notification.getMessage());
        values.put(DbNotificationStructure.Columns.PRIORITY_NAME, notification.getPriority_name());

        long id = db.insert(DbNotificationStructure.TABLE_NAME, null, values);

    }

    public void delete(long id){
        db.delete(DbNotificationStructure.TABLE_NAME,String.format(
                "%s = %s", DbNotificationStructure.Columns.ID,
                id
        ) ,null);
    }

    public void deleteAll(){
        db.execSQL(DbNotificationStructure.returnSqlToDrop());
        db.execSQL(DbNotificationStructure.returnSqlToCreate());
    }

    private Notification fromCursor(Cursor c){
        @SuppressLint("Range") int id = c.getInt(c.getColumnIndex(DbNotificationStructure.Columns.ID));
        @SuppressLint("Range") int task_id = c.getInt(c.getColumnIndex(DbNotificationStructure.Columns.TASK_ID));
        @SuppressLint("Range") String emitted_at = c.getString(c.getColumnIndex(DbNotificationStructure.Columns.EMITTED_AT));
        @SuppressLint("Range") String message = c.getString(c.getColumnIndex(DbNotificationStructure.Columns.MESSAGE));
        @SuppressLint("Range") String priority_name = c.getString(c.getColumnIndex(DbNotificationStructure.Columns.PRIORITY_NAME));

        LocalDateTime emitted_at_date = LocalDateTime.parse(emitted_at);

        return new Notification(task_id, emitted_at_date,message,priority_name);
    }
}
