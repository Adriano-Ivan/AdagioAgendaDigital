package br.com.adagio.adagioagendadigital.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.adagio.adagioagendadigital.data.tag.DbTagStructure;
import br.com.adagio.adagioagendadigital.data.task.DbTaskStructure;
import br.com.adagio.adagioagendadigital.data.task_tag.DbTaskTagStructure;

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
         "%s TEXT NOT NULL, "+
         "%s TEXT NOT NULL) ", DbTagStructure.TABLE_NAME,
            DbTagStructure.Columns.ID,DbTagStructure.Columns.NAME,
            DbTagStructure.Columns.CREATED_AT
    );

    private static final String SQL_DROP_TASKS_TB = String.format(
            "DROP TABLE IF EXISTS %s", DbTaskStructure.TABLE_NAME
    );
    private static final String SQL_CREATE_TASKS_TB = String.format(
           "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   "%s TEXT NOT NULL, "+
                   "%s TEXT NOT NULL, "+
                   "%s TEXT NOT NULL, "+
                   "%s INTEGER NOT NULL)" , DbTaskStructure.TABLE_NAME,
            DbTaskStructure.Columns.ID, DbTaskStructure.Columns.DESCRIPTION,
            DbTaskStructure.Columns.INITIAL_MOMENT, DbTaskStructure.Columns.LIMIT_MOMENT,
            DbTaskStructure.Columns.IS_FINISHED
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

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_DROP_TAGS_TB);
        sqLiteDatabase.execSQL(SQL_CREATE_TAGS_TB);

        sqLiteDatabase.execSQL(SQL_DROP_TASKS_TB);
        sqLiteDatabase.execSQL(SQL_CREATE_TASKS_TB);

        sqLiteDatabase.execSQL(SQL_DROP_TASKS_TAGS_TB);
        sqLiteDatabase.execSQL(SQL_CREATE_TASKS_TAGS_TB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
}
