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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDateTime;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.enums.LimitsYearValues;
import br.com.adagio.adagioagendadigital.ui.activities.home.HomeStaticValues;

public class NumberPickerDialogToChooseYear extends DialogFragment implements DialogInterface.OnClickListener {

    private NumberPicker numberPicker;
    private TextView pickedYearLabel;
    private int pickedYear;
    private int pickedYearFromHomepage;
    private onSaveYearListener saveYearListener;

    public NumberPickerDialogToChooseYear(int pickedYearFromHomepage){
        this.pickedYearFromHomepage = pickedYearFromHomepage;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NumberPickerDialogToChooseYear(){
        if(HomeStaticValues.PICKED_YEAR_MEMO >= LimitsYearValues.MIN_YEAR.value
           && HomeStaticValues.PICKED_YEAR_MEMO <= LimitsYearValues.MAX_YEAR.value){
            pickedYearFromHomepage = HomeStaticValues.PICKED_YEAR_MEMO;
            pickedYear  = HomeStaticValues.PICKED_YEAR_MEMO;
        }
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
        pickedYearLabel = layout.findViewById(R.id.picked_year_label);

        setAttributes();
        setListener();


        return builder.create();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setListener(){
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                if(newVal >= LimitsYearValues.MIN_YEAR.value && newVal <= LimitsYearValues.MAX_YEAR.value){
                    pickedYear = newVal;
                    pickedYearLabel.setText(pickedYear+"");
                    HomeStaticValues.setPickedYearMemo(pickedYear);
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setAttributes(){
        setNumberPickerAttributes();

        if(pickedYearFromHomepage >= LimitsYearValues.MIN_YEAR.value &&
                pickedYearFromHomepage <= LimitsYearValues.MAX_YEAR.value){
            pickedYearLabel.setText(pickedYearFromHomepage+"");
        } else {
            pickedYearLabel.setText(LocalDateTime.now().getYear()+"");
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setNumberPickerAttributes(){

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
