package br.com.adagio.adagioagendadigital.models.enums;

public enum LimitsYearValues {
    MAX_YEAR(2099),
    MIN_YEAR(1980);

    public final int value;

    LimitsYearValues(final int value){
        this.value = value;
    }

    public int getValue() {return value;}
}