package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.form_tag;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Hashtable;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoCreate;
import br.com.adagio.adagioagendadigital.models.entities.Tag;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.ListTagBridgeView;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.form_tag.utils.FormTagErrors;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskManagementFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.form_task.FormTaskFragment;

public class FormTagFragment extends Fragment implements View.OnClickListener {

    private OnFragmentTagFormCreateInteractionListener tListener;

    private View rootView;
    private TextView createOrEditYourTagTextView;
    private TextView nameTextView;
    private Button buttonSubmit;

    private ListTagBridgeView listTagBridgeView;
    private boolean isToEdit;
    private Tag tagToEdit;

    private Hashtable<FormTagErrors, Boolean> formTagErrors = new Hashtable<>();

    private TextView nameErrorEmptyLabel;
    private TextView nameErrorAlreadyExistsLabel;

    public FormTagFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView= inflater.inflate(R.layout.fragment_form_tag, container, false);

        definePossibleAttributesToEdition();

        setAttributes();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setAttributes();
    }

    private void setAttributes(){
        defineViews();
        defineListeners();
        defineFields();
        setFormErrors();

        listTagBridgeView =new ListTagBridgeView(getActivity());
    }

    private void setFormErrors (){
        formTagErrors.put(FormTagErrors.EMPTY_TAG, false);
        formTagErrors.put(FormTagErrors.TAG_ALREADY_EXISTS, false);
    }

    private void definePossibleAttributesToEdition(){
        if(getArguments() != null){
            isToEdit = true;
            tagToEdit = (Tag) getArguments().getSerializable("tagToEdit");

            Log.i("tag", tagToEdit.getName()+" "+tagToEdit.getId());
        } else {
            isToEdit = false;
            tagToEdit=null;
        }
    }

    private void defineViews(){
        nameTextView = rootView.findViewById(R.id.fragment_form_tag_name);
        buttonSubmit = rootView.findViewById(R.id.fragment_form_tag_submit);
        createOrEditYourTagTextView=rootView.findViewById(R.id.create_or_edit_your_tag_text_view);
        nameErrorEmptyLabel = rootView.findViewById(R.id.fragment_form_tag_empty);
        nameErrorAlreadyExistsLabel = rootView.findViewById(R.id.fragment_form_tag_already_exists_error_label);
    }

    private void defineListeners(){
        buttonSubmit.setOnClickListener(this);
    }

    private void defineFields(){
        if(isToEdit){
            createOrEditYourTagTextView.setText(getResources()
                    .getString(R.string.edit_your_tag));

            nameTextView.setText(tagToEdit.getName());
            Log.i("INPUT NAME", nameTextView.getText().toString());
        } else {
            createOrEditYourTagTextView.setText(getResources()
                    .getString(R.string.create_your_tag));
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fragment_form_tag_submit){
            submitTag();
        }
    }

    public boolean validFormInformation(){
        boolean isValid = true;
        String nameTag = nameTextView.getText().toString().trim();

        if(nameTag.equals("")){
            isValid = false;
            formTagErrors.put(FormTagErrors.EMPTY_TAG, true);
        } else {
            formTagErrors.put(FormTagErrors.EMPTY_TAG, false);
        }

        if(!listTagBridgeView.uniqueTag(nameTag)){
            isValid = false;
            formTagErrors.put(FormTagErrors.TAG_ALREADY_EXISTS, true);
        } else {
            formTagErrors.put(FormTagErrors.TAG_ALREADY_EXISTS, false);
        }

        return isValid;
    }

    private void propagateErrorWarnings(){

        if (formTagErrors.get(FormTagErrors.TAG_ALREADY_EXISTS)) {
            nameErrorAlreadyExistsLabel.setVisibility(View.VISIBLE);
        } else {
            nameErrorAlreadyExistsLabel.setVisibility(View.GONE);
        }

        if(formTagErrors.get(FormTagErrors.EMPTY_TAG)){
            nameErrorEmptyLabel.setVisibility(View.VISIBLE);
        } else{
            nameErrorEmptyLabel.setVisibility(View.GONE);
        }
    }

    private void submitTag(){
        if(validFormInformation()){
            Tag tag = new Tag(nameTextView.getText().toString());

            if(isToEdit){
                tListener.onFragmentTagFormEditInteraction(tag, tagToEdit);
            } else {
                tListener.onFragmentTagFormSubmitInteraction(tag);
            }
        } else {
            Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            propagateErrorWarnings();
        }

    }

    public void auxSubmitTag() {
        submitTag();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentTagFormCreateInteractionListener) {
            tListener = (OnFragmentTagFormCreateInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        tListener = null;
    }


    public interface OnFragmentTagFormCreateInteractionListener {

        void onFragmentTagFormSubmitInteraction(Tag tag);
        void onFragmentTagFormEditInteraction(Tag tag,Tag tagToEdit);
    }
}