package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.add_tag_to_task_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskStaticValues;

public class AddTagToTaskDialog extends DialogFragment implements View.OnClickListener {

    private ListView tagsToTaskList;
    private ListTagToTaskBridgeView listTagToTaskBridgeView;
    private OnFragmentTaskAddTagInteractionListener addTagListener;
    private ArrayList<Integer> tagIds = new ArrayList<>();

    private LinearLayout linearLayoutContainerPagination;

    private ImageButton imageButtonNextPage;
    private ImageButton imageButtonPreviousPage;

    private ImageButton imageButtonNextPageDisabled;
    private ImageButton imageButtonPreviousPageDisabled;

    private TextView textViewCurrentPage;

    public AddTagToTaskDialog(ArrayList<Integer> oldValues){
        tagIds = new ArrayList<>(oldValues);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

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

        defineViews(layout);
        defineListeners();

        listTagToTaskBridgeView = new ListTagToTaskBridgeView(getActivity());
        listTagToTaskBridgeView.configureAdapter(tagsToTaskList,this);

        defineDefaultVisualizations();

        updateTagsList();

        return builder.create();
    }

    private void defineViews(View layout){
        tagsToTaskList = layout.findViewById(R.id.fragment_form_add_tag_body_list);

        linearLayoutContainerPagination=layout.findViewById(R.id.fragment_tags_to_task_container_pagination);
        textViewCurrentPage=layout.findViewById(R.id.fragment_tags_to_task_text_page);

        imageButtonNextPage = layout.findViewById(R.id.fragment_tags_to_task_next_page);
        imageButtonPreviousPage = layout.findViewById(R.id.fragment_tags_to_task_previous_page);

        imageButtonNextPageDisabled = layout.findViewById(R.id.fragment_tags_to_task_next_page_disabled);
        imageButtonPreviousPageDisabled=layout.findViewById(R.id.fragment_tags_to_task_previous_page_disabled);
    }

    private void defineListeners(){
        imageButtonNextPage.setOnClickListener(this);
        imageButtonPreviousPage.setOnClickListener(this);
    }

    private void defineDefaultVisualizations(){
        if (!listTagToTaskBridgeView.thereArePreviousOrNextPage()) {
            linearLayoutContainerPagination.setVisibility(View.GONE);
        } else {
            if(!listTagToTaskBridgeView.thereIsNextPage()){
                imageButtonNextPage.setVisibility(View.GONE);
                imageButtonNextPageDisabled.setVisibility(View.VISIBLE);
            } else {
                imageButtonNextPage.setVisibility(View.VISIBLE);
                imageButtonNextPageDisabled.setVisibility(View.GONE);
            }

            if(!listTagToTaskBridgeView.thereIsPreviousPage()){
                imageButtonPreviousPage.setVisibility(View.GONE);
                imageButtonPreviousPageDisabled.setVisibility(View.VISIBLE);
            } else {
                imageButtonPreviousPage.setVisibility(View.VISIBLE);
                imageButtonPreviousPageDisabled.setVisibility(View.GONE);
            }
        }

        textViewCurrentPage.setText(Integer.toString(TagsToTaskStaticValues.CURRENT_PAGE));
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
    }

    private void printIds(){
        for(Integer id : tagIds){
            Log.i("IDs", "id: "+id);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() ==imageButtonNextPage.getId()){
            listTagToTaskBridgeView.updateList(TagsToTaskStaticValues.LIMIT_LIST,
                    TagsToTaskStaticValues.OFFSET_LIST + TagsToTaskStaticValues.LIMIT_LIST);
            defineDefaultVisualizations();
        } else if(view.getId() == imageButtonPreviousPage.getId()){
            listTagToTaskBridgeView.updateList(TagsToTaskStaticValues.LIMIT_LIST,
                    TagsToTaskStaticValues.OFFSET_LIST - TagsToTaskStaticValues.LIMIT_LIST);
            defineDefaultVisualizations();
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
