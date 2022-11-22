package br.com.adagio.adagioagendadigital.data.task;

public class DbTaskStructure {

    public static final String TABLE_NAME = "tasks";

    public static final class Columns {
        public static final String ID = "_id";
        public static final String ID_ALIAS = "id_task";
        public static final String DESCRIPTION = "description";
        public static final String INITIAL_MOMENT = "initial_moment";
        public static final String LIMIT_MOMENT = "limit_moment";
        public static final String IS_FINISHED = "is_finished";
        public static final String PRIORITY_ID = "priority_id";
    }
}
