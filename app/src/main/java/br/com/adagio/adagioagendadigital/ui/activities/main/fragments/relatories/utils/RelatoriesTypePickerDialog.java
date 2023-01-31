package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.relatories.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.enums.RelatoriesTypes;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.relatories.RelatoriesFragment;

public class RelatoriesTypePickerDialog extends DialogFragment{

    private RadioGroup relatoryTypeRdg;
    private RadioButton selectedRdb;
    private RelatoriesFragment relatoriesFragment;

    public RelatoriesTypePickerDialog(RelatoriesFragment relatoriesFragment){
        this.relatoriesFragment = relatoriesFragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.dialog_relatory_type_picker, null);
        relatoryTypeRdg = (RadioGroup) dialog.findViewById(R.id.relatoryTypeRdg);

        builder.setView(dialog).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedRdb = dialog.findViewById(relatoryTypeRdg.getCheckedRadioButtonId());
                idToRelatoriesType(selectedRdb.getId());
                relatoriesFragment.loadPieChartData(false, relatoriesFragment.getChartPeriod());
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RelatoriesTypePickerDialog.this.getDialog().cancel();
            }
        });
        return builder.create();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public RelatoriesTypes idToRelatoriesType(int id){
        if (id == R.id.relatoryPriorityRdb){
            relatoriesFragment.setRelatoriesTypes(RelatoriesTypes.PRIORITIES);
        }
        else if (id == R.id.relatoryFinishingRdb){
            relatoriesFragment.setRelatoriesTypes(RelatoriesTypes.FINISHED);
        }
        return relatoriesFragment.getRelatoriesTypes();
    }
}
