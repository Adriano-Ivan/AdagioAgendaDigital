package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.relatories.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.enums.LimitsYearValues;

public class MonthYearPickerDialog extends DialogFragment {

    private static final int MAX_YEAR = 2050;
    private DatePickerDialog.OnDateSetListener listener;
    private LocalDate localDate;

    public MonthYearPickerDialog(LocalDate localDate){
        this.localDate = localDate;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.dialog_month_year_picker, null);
        final NumberPicker monthNbp = (NumberPicker) dialog.findViewById(R.id.monthNbp);
        final NumberPicker yearNbp = (NumberPicker) dialog.findViewById(R.id.yearNbp);
        String [] months = {"jan.", "fev.", "mar.", "abr.", "mai.", "jun.", "jul.", "ago.", "set.", "out.", "nov.", "dez."};

        monthNbp.setMinValue(0);
        monthNbp.setMaxValue(11);
        monthNbp.setValue(localDate.getMonthValue() - 1);
        monthNbp.setDisplayedValues(months);
        monthNbp.setWrapSelectorWheel(false);

        int year = localDate.getYear();
        yearNbp.setMinValue(LimitsYearValues.MIN_YEAR.value);
        yearNbp.setMaxValue(LimitsYearValues.MAX_YEAR.value);
        yearNbp.setValue(year);
        yearNbp.setWrapSelectorWheel(false);

        builder.setView(dialog).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onDateSet(null, yearNbp.getValue(), monthNbp.getValue(), 0);
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MonthYearPickerDialog.this.getDialog().cancel();
            }
        });
        return builder.create();
    }
}
