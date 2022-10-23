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
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.entities.Tag;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.utils.DeleteTagConfirmationModal;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskStaticValues;


public class TagsFragment extends Fragment
        implements DeleteTagConfirmationModal.OnFragmentTagDeleteInteractionListener, View.OnClickListener {

    private View rootView;
    private ListTagBridgeView listTagBridgeView;
    private ListView listTags;
    private FloatingActionButton fabButtonGoToTags;
    private OnFragmentTagFormInteractionListener fListener;
    private DeleteTagConfirmationModal deleteTagConfirmationModal;

    private Tag possibleTagToEdit;

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
    }

    private void defineViews(){
        fabButtonGoToTags = rootView.findViewById(R.id.fragment_tag_fab_button_tag);
        listTags  = rootView.findViewById(R.id.fragment_tags_list_tags);
    }

    private void defineListeners(){
        fabButtonGoToTags.setOnClickListener(this);
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
            deleteTagConfirmationModal = new DeleteTagConfirmationModal(info.position);
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

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fragment_tag_fab_button_tag){
            fListener.onFragmentTagFormInteraction(Action.GO_TO_TAG,null);
        }
    }

    public interface OnFragmentTagFormInteractionListener {

        void onFragmentTagFormInteraction(Action action, Tag tag);
    }

    public enum Action {
        GO_TO_TAG,
    }
}