package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.form_task;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.adagio.adagioagendadigital.R;

public class FormTaskFragment extends Fragment {

    public FormTaskFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form_task, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}