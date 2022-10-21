package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.form_tag;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoCreate;
import br.com.adagio.adagioagendadigital.models.entities.Tag;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskManagementFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.form_task.FormTaskFragment;

public class FormTagFragment extends Fragment implements View.OnClickListener {

    private OnFragmentTagFormCreateInteractionListener tListener;

    private View rootView;
    private TextView nameTextView;
    private Button buttonSubmit;

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

        setAttributes();
        return rootView;
    }

    private void setAttributes(){
        defineViews();
        defineListeners();
    }

    private void defineViews(){
        nameTextView = rootView.findViewById(R.id.fragment_form_tag_name);
        buttonSubmit = rootView.findViewById(R.id.fragment_form_tag_submit);
    }

    private void defineListeners(){
        buttonSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fragment_form_tag_submit){
            submitTag();
        }
    }

    private void submitTag(){
        Tag tag = new Tag(nameTextView.getText().toString());

        tListener.onFragmentTagFormSubmitInteraction(tag);
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
    }
}