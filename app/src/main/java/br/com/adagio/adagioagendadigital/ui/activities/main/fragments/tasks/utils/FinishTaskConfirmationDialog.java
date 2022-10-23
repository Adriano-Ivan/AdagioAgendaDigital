package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;

public class FinishTaskConfirmationDialog  extends DialogFragment {

    private OnFragmentTaskFinishInteractionListener fListener;

    private TaskDtoRead taskToFinish;

    public FinishTaskConfirmationDialog(TaskDtoRead taskToFinish){
        this.taskToFinish = taskToFinish;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.task_confirmation_finish)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        fListener.onFragmentTaskFinishInteraction(taskToFinish);
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
        if (context instanceof OnFragmentTaskFinishInteractionListener) {
            fListener = (OnFragmentTaskFinishInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onFragmentTaskDeleteInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fListener = null;
    }

    public interface OnFragmentTaskFinishInteractionListener {

        void onFragmentTaskFinishInteraction(TaskDtoRead taskToFinish);
    }
}