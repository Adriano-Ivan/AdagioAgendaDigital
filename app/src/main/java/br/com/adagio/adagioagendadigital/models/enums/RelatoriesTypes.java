package br.com.adagio.adagioagendadigital.models.enums;

public enum RelatoriesTypes {
    PRIORITIES("PRIORITIES"),
    FINISHED("FINISHED"),
    FINISHED_BY_PRIORITY("FINISHED BY PRIORITY"),
    MOST_USED_TAGS("MOST USED TAGS");

    private String value;

    RelatoriesTypes(String value) {this.value = value;}
    public String getValue(){return this.value;}
}
