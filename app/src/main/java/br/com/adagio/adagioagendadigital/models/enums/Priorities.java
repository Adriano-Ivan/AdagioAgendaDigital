package br.com.adagio.adagioagendadigital.models.enums;

public enum Priorities {
    LOW("LOW"),
    AVERAGE("AVERAGE"),
    HIGH("HIGH"),
    CRITICAL("CRITICAL");

    private final String value;

    Priorities(String value){
        this.value = value;
    }

    public String getValue(){return this.value;}
}
