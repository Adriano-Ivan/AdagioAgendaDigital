package br.com.adagio.adagioagendadigital.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Arrays;
import java.util.List;

import br.com.adagio.adagioagendadigital.data.notification.DbNotificationStructure;
import br.com.adagio.adagioagendadigital.data.priority.DbPriorityStructure;
import br.com.adagio.adagioagendadigital.data.tag.DbTagStructure;
import br.com.adagio.adagioagendadigital.data.task.DbTaskStructure;
import br.com.adagio.adagioagendadigital.data.task_tag.DbTaskTagStructure;
import br.com.adagio.adagioagendadigital.models.enums.Priorities;

public class DbLayer extends SQLiteOpenHelper
{
    public static final String DB_NAME = "adagio_mobile_db";
    public static final int DB_VERSION = 1;

    private static DbLayer instance;

    private static final String SQL_DROP_TAGS_TB = String.format(
      "DROP TABLE IF EXISTS %s", DbTagStructure.TABLE_NAME
    );
    private static final String SQL_CREATE_TAGS_TB = String.format(
        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, "+
         "%s VARCHAR(30) UNIQUE NOT NULL) ", DbTagStructure.TABLE_NAME,
            DbTagStructure.Columns.ID,DbTagStructure.Columns.NAME
    );

    private static final String SQL_DROP_PRIORITIES_TB = String.format(
            "DROP TABLE IF EXISTS %s", DbPriorityStructure.TABLE_NAME
    );

    private static final String SQL_CREATE_PRIORITIES_TB  = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    "%s TEXT NOT NULL)", DbPriorityStructure.TABLE_NAME,
            DbPriorityStructure.Columns.ID, DbPriorityStructure.Columns.NAME
    );

    private static final String SQL_DROP_TASKS_TB = String.format(
            "DROP TABLE IF EXISTS %s", DbTaskStructure.TABLE_NAME
    );
    private static final String SQL_CREATE_TASKS_TB = String.format(
           "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   "%s TEXT NOT NULL, "+
                   "%s VARCHAR(30) NOT NULL, "+
                   "%s VARCHAR(30) NOT NULL, "+
                   "%s INTEGER NOT NULL, " +
                   "%s INTEGER NOT NULL, "+
                   "CONSTRAINT fk_priorities "+
                   "FOREIGN KEY (%s) "+
                   "REFERENCES %s(%s))"
            , DbTaskStructure.TABLE_NAME,
            DbTaskStructure.Columns.ID, DbTaskStructure.Columns.DESCRIPTION,
            DbTaskStructure.Columns.INITIAL_MOMENT, DbTaskStructure.Columns.LIMIT_MOMENT,
            DbTaskStructure.Columns.IS_FINISHED,  DbTaskStructure.Columns.PRIORITY_ID,
            DbTaskStructure.Columns.PRIORITY_ID, DbPriorityStructure.TABLE_NAME,
            DbPriorityStructure.Columns.ID
    );

    private static final String SQL_DROP_TASKS_TAGS_TB = String.format(
            "DROP TABLE IF EXISTS %s", DbTaskTagStructure.TABLE_NAME
    );

    private static final String SQL_CREATE_TASKS_TAGS_TB = String.format(
      "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, "+
              "%s INTEGER NOT NULL, "+
              "%s INTEGER NOT NULL, " +
              "CONSTRAINT fk_tasks " +
              "FOREIGN KEY (%s) "+
              "REFERENCES %s(%s)," +
              "CONSTRAINT fk_tags, " +
              "FOREIGN KEY (%s) " +
              "REFERENCES %s(%s)" +
              ")", DbTaskTagStructure.TABLE_NAME, DbTaskTagStructure.Columns.ID,
            DbTaskTagStructure.Columns.TASK_ID, DbTaskTagStructure.Columns.TAG_ID,
            DbTaskTagStructure.Columns.TASK_ID, DbTaskStructure.TABLE_NAME,
            DbTaskStructure.Columns.ID, DbTaskTagStructure.Columns.TAG_ID,
            DbTagStructure.TABLE_NAME, DbTagStructure.Columns.ID
    );

    private static final String SQL_DROP_NOTIFICATIONS_TB = DbNotificationStructure.returnSqlToDrop();

    private static final String SQL_CREATE_NOTIFICATIONS_TB = DbNotificationStructure.returnSqlToCreate();

    public static DbLayer getInstance(Context context){
        if(instance == null){
            instance = new DbLayer(context,DB_NAME,null,DB_VERSION);
        }

        return instance;
    }

    private DbLayer(Context context, String name,
                    SQLiteDatabase.CursorFactory factory,
                    int version
                    ){
        super(context, name, factory,version);
    }

    private void createPriorities(SQLiteDatabase sqLiteDatabase){
        List<Priorities> priorities = Arrays.asList(Priorities.values());

        for(Priorities priority : priorities){
            ContentValues values = new ContentValues();
            values.put(DbPriorityStructure.Columns.NAME, priority.getValue());

            sqLiteDatabase.insert(DbPriorityStructure.TABLE_NAME, null, values);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_DROP_TAGS_TB);
        sqLiteDatabase.execSQL(SQL_CREATE_TAGS_TB);

        sqLiteDatabase.execSQL(SQL_DROP_PRIORITIES_TB);
        sqLiteDatabase.execSQL(SQL_CREATE_PRIORITIES_TB);
        createPriorities(sqLiteDatabase);

        sqLiteDatabase.execSQL(SQL_DROP_TASKS_TB);
        sqLiteDatabase.execSQL(SQL_CREATE_TASKS_TB);

        sqLiteDatabase.execSQL(SQL_DROP_TASKS_TAGS_TB);
        sqLiteDatabase.execSQL(SQL_CREATE_TASKS_TAGS_TB);

        sqLiteDatabase.execSQL(SQL_DROP_NOTIFICATIONS_TB);
        sqLiteDatabase.execSQL(SQL_CREATE_NOTIFICATIONS_TB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
}
