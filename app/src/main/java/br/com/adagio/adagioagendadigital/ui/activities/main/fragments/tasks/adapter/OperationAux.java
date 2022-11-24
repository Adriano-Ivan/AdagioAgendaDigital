package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.adapter;

public enum OperationAux {
    DELETE_FROM_LIST_WITHOUT_QUERYING("DELETE_FROM_WITHOUT_QUERYING");

    private final String value;

    OperationAux(String value){
        this.value = value;
    }

    String getValue(){
        return this.value;
    }
}
