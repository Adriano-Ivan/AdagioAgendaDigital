package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import br.com.adagio.adagioagendadigital.R;

public class DeleteTaskConfirmationDialog extends DialogFragment {

    private  OnFragmentTaskDeleteInteractionListener dListener;

    private int positionOfTaskToDelete;

    public DeleteTaskConfirmationDialog(int position){
        positionOfTaskToDelete=position;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.task_confirmation_delete)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dListener.onFragmentTaskDeleteInteraction(positionOfTaskToDelete);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof  OnFragmentTaskDeleteInteractionListener) {
            dListener = ( OnFragmentTaskDeleteInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onFragmentTaskDeleteInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dListener = null;
    }

    public interface OnFragmentTaskDeleteInteractionListener {

        void onFragmentTaskDeleteInteraction(int position);
    }
}
