package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils;

public enum TypeListTaskManagementOrderDate {
    TODAY("TODAY"),
    LIMIT_MOMENT_DESC("LIMIT_MOMENT_DESC"),
    LIMIT_MOMENT_ASC("LIMIT_MOMENT_ASC"),
    INITIAL_MOMENT_DESC("INITIAL_MOMENT_DESC"),
    INITIAL_MOMENT_ASC("INITIAL_MOMENT_ASC");

    private final String value;

    TypeListTaskManagementOrderDate(String value){this.value = value;}

    private String getValue(){
        return value;
    }
}
