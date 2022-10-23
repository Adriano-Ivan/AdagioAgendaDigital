package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskManagementFragment;

public class FinishOrNotTaskConfirmationDialog extends DialogFragment {

    private OnFragmentTaskFinishOrNotInteractionListener fListener;

    private TaskDtoRead taskToFinishOrNot;
    private ActionToConfirm action;

    public FinishOrNotTaskConfirmationDialog(TaskDtoRead taskToFinish, ActionToConfirm action){
        this.taskToFinishOrNot = taskToFinish;
        this.action = action;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if(action == ActionToConfirm.FINISH){
            builder.setMessage(R.string.task_confirmation_finish);
        } else {
            builder.setMessage(R.string.task_confirmation_undone_finish);
        }

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(action == ActionToConfirm.FINISH){
                            fListener.onFragmentTaskFinishOrNotInteraction(taskToFinishOrNot,ActionToConfirm.FINISH);
                        } else {
                            fListener.onFragmentTaskFinishOrNotInteraction(taskToFinishOrNot,ActionToConfirm.NOT_FINISH);
                        }

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
        if (context instanceof OnFragmentTaskFinishOrNotInteractionListener) {
            fListener = (OnFragmentTaskFinishOrNotInteractionListener) context;
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

    public interface OnFragmentTaskFinishOrNotInteractionListener {

        void onFragmentTaskFinishOrNotInteraction(TaskDtoRead taskToFinish, ActionToConfirm action);
    }

    public enum ActionToConfirm{
        FINISH,
        NOT_FINISH
    }
}