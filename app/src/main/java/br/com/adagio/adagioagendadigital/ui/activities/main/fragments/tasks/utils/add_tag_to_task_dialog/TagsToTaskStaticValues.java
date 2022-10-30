package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.add_tag_to_task_dialog;

public class TagsToTaskStaticValues {

    public static int LIMIT_LIST = 8;
    public static int OFFSET_LIST= 0;
    public static int NEXT_POSSIBLE_QUANTITY = LIMIT_LIST;
    public static int CURRENT_PAGE = 1;

    public static void setLimitList(int limit){
        LIMIT_LIST = limit;
        NEXT_POSSIBLE_QUANTITY = OFFSET_LIST + LIMIT_LIST;
    }

    public static void setOffsetList(int offset){
        OFFSET_LIST = offset;
        NEXT_POSSIBLE_QUANTITY = OFFSET_LIST + LIMIT_LIST;
        CURRENT_PAGE = NEXT_POSSIBLE_QUANTITY / LIMIT_LIST;
    }
}
