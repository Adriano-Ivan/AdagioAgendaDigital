package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks;

public class TaskStaticValues {

    public static boolean CONTAINER_OPTIONS_IS_GONE = false;
    public static int LIMIT_LIST = 10;
    public static int OFFSET_LIST= 0;

    public static void setContainerOptionsIsGone(boolean isGone){
        CONTAINER_OPTIONS_IS_GONE = isGone;
    }

    public static void setLimitList(int limit){
        LIMIT_LIST = limit;
    }

    public static void setOffsetList(int offset){
        OFFSET_LIST = offset;
    }
}
