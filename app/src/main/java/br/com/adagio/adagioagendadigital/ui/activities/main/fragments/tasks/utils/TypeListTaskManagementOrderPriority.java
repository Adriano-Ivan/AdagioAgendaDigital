package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils;

public enum TypeListTaskManagementOrderPriority {
    PRIORITY_ASC("PRIORITY_ASC"),
    PRIORITY_DESC("PRIORITY_DESC");

    private final String value;

    TypeListTaskManagementOrderPriority(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
