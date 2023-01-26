package br.com.adagio.adagioagendadigital.models.enums;

public enum RelatoriesTypes {
    PRIORITIES("PRIORITIES"),
    FINISHED("FINISHED");

    private String value;

    RelatoriesTypes(String value) {this.value = value;}
    public String getValue(){return this.value;}
}
