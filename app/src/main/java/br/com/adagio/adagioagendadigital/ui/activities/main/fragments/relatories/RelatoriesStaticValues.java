package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.relatories;

import java.time.LocalDate;

import br.com.adagio.adagioagendadigital.models.enums.RelatoriesTypes;

public class RelatoriesStaticValues {
    public static LocalDate relatoriesDate = LocalDate.now();
    public static RelatoriesTypes relatoriesTypes = RelatoriesTypes.FINISHED;
    public static int chartPeriod = 0;

    public static boolean isClicked = false;
}
