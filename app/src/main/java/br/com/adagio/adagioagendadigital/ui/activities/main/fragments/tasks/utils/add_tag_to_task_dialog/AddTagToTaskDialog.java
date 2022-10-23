package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.add_tag_to_task_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskStaticValues;

public class AddTagToTaskDialog extends DialogFragment {

    private ListView tagsToTaskList;
    private ListTagToTaskBridgeView listTagToTaskBridgeView;
    private OnFragmentTaskAddTagInteractionListener addTagListener;
    private ArrayList<Integer> tagIds = new ArrayList<>();

    public AddTagToTaskDialog(ArrayList<Integer> oldValues){
        tagIds = new ArrayList<>(oldValues);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.form_task_add_tag_title);

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addTagListener.onFragmentTaskAddInteraction(tagIds);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.fragment_form_task_add_tag_dialog_body,null);

        builder.setView(layout);

        tagsToTaskList = layout.findViewById(R.id.fragment_form_add_tag_body_list);

        listTagToTaskBridgeView = new ListTagToTaskBridgeView(getActivity());
        listTagToTaskBridgeView.configureAdapter(tagsToTaskList,this);

        updateTagsList();

        return builder.create();
    }

    private void updateTagsList(){
        listTagToTaskBridgeView.updateList(TagsToTaskStaticValues.LIMIT_LIST,TagsToTaskStaticValues.OFFSET_LIST);
    }

    public void insertTagId(Integer id){
        tagIds.add(id);
        printIds();
    }

    public boolean containsTagId(Integer id){
        for(Integer i : tagIds){
            if(i == id){
                return true;
            }
        }
        return false;
    }

    public void removeTagId(Integer id){
        tagIds.remove(id);
        printIds();
    }

    private void printIds(){
        for(Integer id : tagIds){
            Log.i("IDs", "id: "+id);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof OnFragmentTaskAddTagInteractionListener){
            addTagListener = (OnFragmentTaskAddTagInteractionListener) context;
        } else {
            throw new RuntimeException(
                    context.toString() + " must implement onFragmentTaskAddInteraction"
            );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addTagListener = null;
    }

    public interface OnFragmentTaskAddTagInteractionListener{
        public void onFragmentTaskAddInteraction(ArrayList<Integer> ids);
    }
}
