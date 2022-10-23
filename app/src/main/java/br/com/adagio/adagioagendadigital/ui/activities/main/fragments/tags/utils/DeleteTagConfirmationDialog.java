package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import br.com.adagio.adagioagendadigital.R;

public class DeleteTagConfirmationDialog extends DialogFragment {

    private OnFragmentTagDeleteInteractionListener dListener;

    private int positionOfTagToDelete;

    public DeleteTagConfirmationDialog(int position){
        positionOfTagToDelete=position;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.tag_confirmation_delete)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dListener.onFragmentTagDeleteInteraction(positionOfTagToDelete);
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
        if (context instanceof OnFragmentTagDeleteInteractionListener) {
            dListener = (OnFragmentTagDeleteInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onFragmentTagDeleteInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dListener = null;
    }

    public interface OnFragmentTagDeleteInteractionListener {

        void onFragmentTagDeleteInteraction(int position);
    }
}

