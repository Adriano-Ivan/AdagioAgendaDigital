package br.com.adagio.adagioagendadigital.ui.activities.home.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import br.com.adagio.adagioagendadigital.R;

public class NumberPickerDialogToChooseYear extends DialogFragment implements DialogInterface.OnClickListener {

    private NumberPicker numberPicker;
    private int pickedYear;
    private onSaveYearListener saveYearListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton(R.string.confirmar, this)
                .setNegativeButton(R.string.cancel, this);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.number_picker_to_choose_year_view,null);

        builder.setView(layout);

        numberPicker = layout.findViewById(R.id.number_picker_to_choose_year);
        setNumberPickerAttributes();
        setListener();


        return builder.create();
    }

    private void setListener(){
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                if(newVal >= LimitsValues.MIN_YEAR.value && newVal <= LimitsValues.MAX_YEAR.value){
                    pickedYear = newVal;
                }
            }
        });
    }

    private void setNumberPickerAttributes(){
        numberPicker.setBackgroundColor(Color.parseColor(("#EEEEEE")));
        numberPicker.setMaxValue(LimitsValues.MAX_YEAR.value);
        numberPicker.setMinValue(LimitsValues.MIN_YEAR.value);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == dialog.BUTTON_POSITIVE){
            if(pickedYear >= LimitsValues.MIN_YEAR.value && pickedYear <= LimitsValues.MAX_YEAR.value) {
                saveYearListener.onSaveYear(pickedYear);
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof onSaveYearListener){
            saveYearListener = (onSaveYearListener) context;
        } else {
            throw new RuntimeException("The activity must implement the interfaces");
        }
    }

    public interface onSaveYearListener{
        void onSaveYear(int ano);
    }

    public enum LimitsValues {
        MAX_YEAR(2099),
        MIN_YEAR(1980);

        private final int value;

        LimitsValues(final int value){
            this.value = value;
        }

        public int getValue() {return value;}
    }
}
