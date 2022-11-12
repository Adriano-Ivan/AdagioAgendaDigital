package br.com.adagio.adagioagendadigital.data.notification;

import br.com.adagio.adagioagendadigital.data.task.DbTaskStructure;

public class DbNotificationStructure {

    public static final String TABLE_NAME = "notifications";


    public static String returnSqlToDrop(){
        return  String.format(
                "DROP TABLE IF EXISTS %s", DbNotificationStructure.TABLE_NAME
        );
    }

    public static String returnSqlToCreate(){
        return String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        "%s INTEGER NOT NULL, "+
                        "%s VARCHAR(30) NOT NULL, "+
                        "%s TEXT NOT NULL, "+
                        "%s TEXT NOT NULL, "+
                        "CONSTRAINT fk_task_priority "+
                        "FOREIGN KEY (%s) "+
                        "REFERENCES %s(%s)) ",
                DbNotificationStructure.TABLE_NAME, DbNotificationStructure.Columns.ID,
                DbNotificationStructure.Columns.TASK_ID, DbNotificationStructure.Columns.EMITTED_AT,
                DbNotificationStructure.Columns.MESSAGE, DbNotificationStructure.Columns.PRIORITY_NAME,
                DbNotificationStructure.Columns.TASK_ID, DbTaskStructure.TABLE_NAME,
                DbTaskStructure.Columns.ID
        );
    }

    public static final class Columns {
        public static final String ID = "_id";

        public  static final String TASK_ID = "task_id";

        public  static final String EMITTED_AT = "emitted_at";

        public  static final String MESSAGE = "message";

        public  static final String PRIORITY_NAME = "priority_name";
    }
}
