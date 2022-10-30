package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.entities.Tag;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.utils.DeleteTagConfirmationDialog;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskStaticValues;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.add_tag_to_task_dialog.TagsToTaskStaticValues;


public class TagsFragment extends Fragment
        implements DeleteTagConfirmationDialog.OnFragmentTagDeleteInteractionListener, View.OnClickListener {

    private View rootView;
    private ListTagBridgeView listTagBridgeView;
    private ListView listTags;
    private FloatingActionButton fabButtonGoToTags;
    private OnFragmentTagFormInteractionListener fListener;
    private DeleteTagConfirmationDialog deleteTagConfirmationModal;

    private Tag possibleTagToEdit;

    private TextView textViewCurrentPage;
    private LinearLayout linearLayoutContainerPagination;

    private ImageButton imageButtonNextPage;
    private ImageButton imageButtonPreviousPage;

    private ImageButton imageButtonNextPageDisabled;
    private ImageButton imageButtonPreviousPageDisabled;

    public TagsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView =  inflater.inflate(R.layout.fragment_tags, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        setAttributes();
    }

    private void setAttributes(){
        possibleTagToEdit = null;

        defineViews();
        defineListeners();

        configureAdapter();
        updateTagsList();
        definePaginationVisualizations();
    }

    private void defineViews(){
        fabButtonGoToTags = rootView.findViewById(R.id.fragment_tag_fab_button_tag);
        listTags  = rootView.findViewById(R.id.fragment_tags_list_tags);

        linearLayoutContainerPagination=rootView.findViewById(R.id.fragment_tags_container_pagination);
        textViewCurrentPage = rootView.findViewById(R.id.fragment_tags_text_page);

        imageButtonNextPage=rootView.findViewById(R.id.fragment_tags_next_page);
        imageButtonNextPageDisabled=rootView.findViewById(R.id.fragment_tags_next_page_disabled);

        imageButtonPreviousPage=rootView.findViewById(R.id.fragment_tags_previous_page);
        imageButtonPreviousPageDisabled=rootView.findViewById(R.id.fragment_tags_previous_page_disabled);
    }

    private void defineListeners(){
        fabButtonGoToTags.setOnClickListener(this);

        imageButtonNextPage.setOnClickListener(this);
        imageButtonPreviousPage.setOnClickListener(this);
    }

    private void configureAdapter() {
        listTagBridgeView = new ListTagBridgeView(getActivity());

        listTagBridgeView.configureAdapter(listTags);
        registerForContextMenu(listTags);
    }

    private void updateTagsList(){
        listTagBridgeView.updateList(TaskStaticValues.LIMIT_LIST, TaskStaticValues.OFFSET_LIST);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();

        inflater.inflate(R.menu.menu_tag_item, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Log.i("SELECTED", "onContextItemSelected: ");

        if(item.getItemId() == R.id.menu_tag_delete){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                    item.getMenuInfo();
            deleteTagConfirmationModal = new DeleteTagConfirmationDialog(info.position);
            deleteTagConfirmationModal.show(getActivity().getSupportFragmentManager(), "dialog");

        } else if(item.getItemId() == R.id.menu_tag_edit){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                    item.getMenuInfo();

            possibleTagToEdit = listTagBridgeView.get(info.position);
            fListener.onFragmentTagFormInteraction(Action.GO_TO_TAG,possibleTagToEdit);
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == fabButtonGoToTags.getId()){
            fListener.onFragmentTagFormInteraction(Action.GO_TO_TAG,null);
        } else if(view.getId() == imageButtonNextPage.getId()){
            listTagBridgeView.updateList(TagStaticValues.LIMIT_LIST,
                    TagStaticValues.OFFSET_LIST + TagStaticValues.LIMIT_LIST);
            definePaginationVisualizations();
        } else if(view.getId() == imageButtonPreviousPage.getId()){
            listTagBridgeView.updateList(TagStaticValues.LIMIT_LIST,
                    TagStaticValues.OFFSET_LIST - TagStaticValues.LIMIT_LIST);
            definePaginationVisualizations();
        }
    }

    private void definePaginationVisualizations(){
        if (!listTagBridgeView.thereArePreviousOrNextPage()) {
            linearLayoutContainerPagination.setVisibility(View.GONE);
        } else {
            if(!listTagBridgeView.thereIsNextPage()){
                imageButtonNextPage.setVisibility(View.GONE);
                imageButtonNextPageDisabled.setVisibility(View.VISIBLE);
            } else {
                imageButtonNextPage.setVisibility(View.VISIBLE);
                imageButtonNextPageDisabled.setVisibility(View.GONE);
            }

            if(!listTagBridgeView.thereIsPreviousPage()){
                imageButtonPreviousPage.setVisibility(View.GONE);
                imageButtonPreviousPageDisabled.setVisibility(View.VISIBLE);
            } else {
                imageButtonPreviousPage.setVisibility(View.VISIBLE);
                imageButtonPreviousPageDisabled.setVisibility(View.GONE);
            }
        }

        textViewCurrentPage.setText(Integer.toString(TagStaticValues.CURRENT_PAGE));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentTagFormInteractionListener) {
            fListener = (OnFragmentTagFormInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentTaskFormInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fListener = null;
    }

    @Override
    public void onFragmentTagDeleteInteraction(int position) {
        listTagBridgeView.delete(position);
    }

    public interface OnFragmentTagFormInteractionListener {

        void onFragmentTagFormInteraction(Action action, Tag tag);
    }

    public enum Action {
        GO_TO_TAG,
    }
}