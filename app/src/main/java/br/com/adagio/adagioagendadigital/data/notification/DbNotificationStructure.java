package br.com.adagio.adagioagendadigital.data.notification;

public class DbNotificationStructure {

    public static final String TABLE_NAME = "notifications";

    public static final class Columns {
        public static final String ID = "_id";

        public  static final String TASK_ID = "task_id";

        public  static final String EMITTED_AT = "emitted_at";

        public  static final String MESSAGE = "message";

        public  static final String PRIORITY_NAME = "priority_name";
    }
}
