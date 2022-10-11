package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.form_task;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoCreate;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskManagementFragment;

public class FormTaskFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private EditText descriptionEditText;
    private Button submitButton;
    private OnFragmentTaskFormCreateInteractionListener tListener;

    public FormTaskFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_form_task, container, false);

        setAttributes();

        return rootView;
    }

    @Override
    public void onResume() {
        setAttributes();
        super.onResume();
    }

    private void setAttributes(){
        defineViews();
        defineListeners();
    }

    private void defineViews(){
        descriptionEditText = rootView.findViewById(R.id.fragment_form_task_description);
        submitButton = rootView.findViewById(R.id.fragment_form_task_submit);
    }

    private void defineListeners(){
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fragment_form_task_submit){
            TaskDtoCreate tCreate = new TaskDtoCreate(descriptionEditText.getText().toString(),
                    "2022-08-12T23:23:38",
                    "2023-09-12T12:23:28",
                    0
                    );

            tListener.onFragmentTaskFormSubmitInteraction(tCreate);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TaskManagementFragment.OnFragmentTaskFormInteractionListener) {
            tListener = (OnFragmentTaskFormCreateInteractionListener) context;
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

    public interface OnFragmentTaskFormCreateInteractionListener {

        void onFragmentTaskFormSubmitInteraction(TaskDtoCreate task);
    }
}