package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.data.task.TaskDAO;
import kotlinx.coroutines.internal.ThreadSafeHeap;


public class TaskManagementFragment extends Fragment implements View.OnClickListener {

    private LinearLayout containerOptions;
    private Button buttonHideContainerOptions;
    private ListView listTasks;
    private View rootView;
    private ListTaskBridgeView listTaskBridgeView;

    public TaskManagementFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_task_management, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setAttributes();
    }

    private void setAttributes(){
        defineViews();
        setConfigurationVisibilitity(true);
        defineListeners();

        configureAdapter();
        updateTasksList();
    }

    private void defineViews(){
        containerOptions = rootView.findViewById(R.id.fragment_task_container_options);
        buttonHideContainerOptions = rootView.findViewById(R.id.fragment_task_hide_options);
        listTasks = rootView.findViewById(R.id.fragment_task_list_tasks);
    }

    private void defineListeners(){
         buttonHideContainerOptions.setOnClickListener(this);
    }

    private void  setContainerOptionsIsGone (){
        containerOptions.setVisibility(View.GONE);
        TaskStaticValues.setContainerOptionsIsGone(true);
        buttonHideContainerOptions.setText(getResources().getString(R.string.task_show_options));
    }

    private void setConfigurationVisibilitity(boolean hideOnly){
        if(!hideOnly ){
            if(!TaskStaticValues.CONTAINER_OPTIONS_IS_GONE){
                setContainerOptionsIsGone();
            } else if(TaskStaticValues.CONTAINER_OPTIONS_IS_GONE){
                containerOptions.setVisibility(View.VISIBLE);
                TaskStaticValues.setContainerOptionsIsGone(false);
                buttonHideContainerOptions.setText(getResources().getString(R.string.fragment_task_hide_options));
            }

        } else {
           setContainerOptionsIsGone();
        }
    }

    private void configureAdapter(){
        listTaskBridgeView = new ListTaskBridgeView(getActivity());

        listTaskBridgeView.configureAdapter(listTasks);
    }

    private void updateTasksList(){
        listTaskBridgeView.updateList(TaskStaticValues.LIMIT_LIST, TaskStaticValues.OFFSET_LIST);
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fragment_task_hide_options){

           setConfigurationVisibilitity(false);
        }
    }
}