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

public class YearPickerDialog extends DialogFragment {

    private static final int MAX_YEAR = 2050;
    private DatePickerDialog.OnDateSetListener listener;
    private LocalDate localDate;

    public YearPickerDialog(LocalDate localDate){
        this.localDate = localDate;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.dialog_year_picker, null);
        final NumberPicker yearNbp = (NumberPicker) dialog.findViewById(R.id.yearSoleNbp);

        int year = localDate.getYear();
        yearNbp.setMinValue(year-5);
        yearNbp.setMaxValue(MAX_YEAR);
        yearNbp.setValue(year);
        yearNbp.setWrapSelectorWheel(false);

        builder.setView(dialog).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onDateSet(null, yearNbp.getValue(), 0, 0);
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                YearPickerDialog.this.getDialog().cancel();
            }
        });
        return builder.create();
    }
}
