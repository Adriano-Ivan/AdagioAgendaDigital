package br.com.adagio.adagioagendadigital.ui.activities.home.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDateTime;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.enums.LimitsYearValues;

public class NumberPickerDialogToChooseYear extends DialogFragment implements DialogInterface.OnClickListener {

    private NumberPicker numberPicker;
    private int pickedYear;
    private int pickedYearFromHomepage;
    private onSaveYearListener saveYearListener;

    public NumberPickerDialogToChooseYear(int pickedYearFromHomepage){
        this.pickedYearFromHomepage = pickedYearFromHomepage;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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

                if(newVal >= LimitsYearValues.MIN_YEAR.value && newVal <= LimitsYearValues.MAX_YEAR.value){
                    pickedYear = newVal;
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setNumberPickerAttributes(){
        numberPicker.setBackgroundColor(Color.parseColor(("#EEEEEE")));
        numberPicker.setMaxValue(LimitsYearValues.MAX_YEAR.value);
        numberPicker.setMinValue(LimitsYearValues.MIN_YEAR.value);

        if(pickedYearFromHomepage >= LimitsYearValues.MIN_YEAR.value &&
           pickedYearFromHomepage <= LimitsYearValues.MAX_YEAR.value){
            numberPicker.setValue(pickedYearFromHomepage);
        } else {
            numberPicker.setValue(LocalDateTime.now().getYear());
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == dialog.BUTTON_POSITIVE){
            if(pickedYear >= LimitsYearValues.MIN_YEAR.value && pickedYear <= LimitsYearValues.MAX_YEAR.value) {
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


}
