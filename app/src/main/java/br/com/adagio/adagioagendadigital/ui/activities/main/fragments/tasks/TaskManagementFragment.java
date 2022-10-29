package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.DeleteTaskConfirmationDialog;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.FinishOrNotTaskConfirmationDialog;


public class TaskManagementFragment extends Fragment
        implements View.OnClickListener, DeleteTaskConfirmationDialog.OnFragmentTaskDeleteInteractionListener {

    private LinearLayout containerOptions;
    private Button buttonHideContainerOptions;
    private Button buttonGoToFormTasks;
    private FloatingActionButton fabButtonGoToTasks;
    private ListView listTasks;
    private View rootView;
    private ListTaskBridgeView listTaskBridgeView;
    private OnFragmentTaskFormInteractionListener mListener;
    private DeleteTaskConfirmationDialog deleteTaskConfirmationModal;
    private FinishOrNotTaskConfirmationDialog finishTaskConfirmationDialog;

    private RadioGroup radioGroupOrderDatetimeOptions;
    private RadioGroup radioGroupOrderPriorityOptions;

    private LinearLayout linearLayoutContainerPagination;

    private ImageButton imageButtonNextPage;
    private ImageButton imageButtonPreviousPage;

    private TextView textViewCurrentPage;

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
        updatePaginationVisibility();
    }

    private void defineViews(){
        containerOptions = rootView.findViewById(R.id.fragment_task_container_options);
        buttonHideContainerOptions = rootView.findViewById(R.id.fragment_task_hide_options);
        listTasks = rootView.findViewById(R.id.fragment_task_list_tasks);
        fabButtonGoToTasks = rootView.findViewById(R.id.fragment_task_fab_button_tasks);
        buttonGoToFormTasks = rootView.findViewById((R.id.fragment_task_user_wants_to_create_task));
        radioGroupOrderDatetimeOptions = rootView.findViewById(R.id.fragment_task_order_datetime_options);
        radioGroupOrderPriorityOptions=rootView.findViewById(R.id.fragment_task_order_by_priority_options);
        linearLayoutContainerPagination = rootView.findViewById(R.id.fragment_task_container_pagination);
        imageButtonNextPage = rootView.findViewById(R.id.fragment_task_management_next_page);
        imageButtonPreviousPage = rootView.findViewById(R.id.fragment_task_management_previous_page);
        textViewCurrentPage = rootView.findViewById(R.id.fragment_task_management_text_page);
    }

    private void defineListeners(){
        buttonHideContainerOptions.setOnClickListener(this);
        buttonGoToFormTasks.setOnClickListener(this);
        fabButtonGoToTasks.setOnClickListener(this);
        imageButtonNextPage.setOnClickListener(this);
        imageButtonPreviousPage.setOnClickListener(this);

        radioGroupOrderDatetimeOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.i("datetime order", "onCheckedChanged: "+i);
            }
        });

        radioGroupOrderPriorityOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.i("priority order", "onCheckedChanged: "+i);
            }
        });
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
        hideOrShowFabAndPaginationContainer();
    }

    private void hideOrShowFabAndPaginationContainer(){
        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) listTasks.getLayoutParams();
        int bottomMargin = getMarginValue(getActivity(),getActivity().getResources().getDimension(R.dimen.listViewMarginBottom));
        if(TaskStaticValues.CONTAINER_OPTIONS_IS_GONE){
            marginParams.setMargins(marginParams.leftMargin,
                    marginParams.topMargin,marginParams.rightMargin,
                    bottomMargin);
            fabButtonGoToTasks.setVisibility(View.VISIBLE);
            linearLayoutContainerPagination.setVisibility(View.VISIBLE);
        } else {
            marginParams.setMargins(marginParams.leftMargin,
                    marginParams.topMargin,marginParams.rightMargin,
                    0);
            fabButtonGoToTasks.setVisibility(View.GONE);
            linearLayoutContainerPagination.setVisibility(View.GONE);
        }
    }

    public  int getMarginValue(Context context,float dimenId) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dimenId,
                resources.getDisplayMetrics()
        );
    }

    private void configureAdapter() {
        listTaskBridgeView = new ListTaskBridgeView(getActivity());

        listTaskBridgeView.configureAdapter(listTasks,this);

        registerForContextMenu(listTasks);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();

        inflater.inflate(R.menu.menu_task_item, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.menu_task_delete){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                    item.getMenuInfo();
            deleteTaskConfirmationModal = new DeleteTaskConfirmationDialog(info.position);
            deleteTaskConfirmationModal.show(getActivity().getSupportFragmentManager(), "dialog");

        } else if(item.getItemId() == R.id.menu_task_edit){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                    item.getMenuInfo();

            TaskDtoRead task = listTaskBridgeView.get(info.position);
            mListener.onFragmentTaskFormInteraction(Action.GO_TO_TASK, task);
        }

        return super.onContextItemSelected(item);
    }

    private void updateTasksList(){
        listTaskBridgeView.updateList(TaskStaticValues.LIMIT_LIST, TaskStaticValues.OFFSET_LIST);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == buttonHideContainerOptions.getId()){

           setConfigurationVisibilitity(false);
        } else if(view.getId() == buttonGoToFormTasks.getId()){
            mListener.onFragmentTaskFormInteraction(Action.GO_TO_TASK,null);
        }else if(view.getId() == fabButtonGoToTasks.getId()){
            mListener.onFragmentTaskFormInteraction(Action.GO_TO_TASK,null);
        } else if(view.getId() == imageButtonNextPage.getId()){
            listTaskBridgeView.updateList(TaskStaticValues.LIMIT_LIST,
                    TaskStaticValues.OFFSET_LIST + TaskStaticValues.LIMIT_LIST);
            updatePaginationVisibility();
        } else if(view.getId() == imageButtonPreviousPage.getId()){
            listTaskBridgeView.updateList(TaskStaticValues.LIMIT_LIST,
                    TaskStaticValues.OFFSET_LIST - TaskStaticValues.LIMIT_LIST);
            updatePaginationVisibility();
        }
    }

    private void updatePaginationVisibility(){
        if (!listTaskBridgeView.thereArePreviousOrNextPage()) {
            linearLayoutContainerPagination.setVisibility(View.GONE);
        } else {
            if(!listTaskBridgeView.thereIsNextPage()){
                imageButtonNextPage.setVisibility(View.GONE);
            } else {
                imageButtonNextPage.setVisibility(View.VISIBLE);
            }

            if(!listTaskBridgeView.thereIsPreviousPage()){
                imageButtonPreviousPage.setVisibility(View.GONE);
            } else {
                imageButtonPreviousPage.setVisibility(View.VISIBLE);
            }
        }

        textViewCurrentPage.setText(Integer.toString(TaskStaticValues.CURRENT_PAGE));
    }

    public void setTaskAsFinished(TaskDtoRead task){
        listTaskBridgeView.setTaskAsFinished(task);
    }

    public void setTaskAsUnfinished(TaskDtoRead task){
        listTaskBridgeView.setTaskAsUnfinished(task);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentTaskFormInteractionListener) {
            mListener = (OnFragmentTaskFormInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentTaskFormInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFragmentTaskDeleteInteraction(int position) {
       listTaskBridgeView.delete(position);
    }

    public void openDialogToFinishTask(TaskDtoRead task) {
        finishTaskConfirmationDialog = new FinishOrNotTaskConfirmationDialog(task, FinishOrNotTaskConfirmationDialog.ActionToConfirm.FINISH);
        finishTaskConfirmationDialog.show(getActivity().getSupportFragmentManager(),"dialog");
    }

    public void openDialogToNotFinishTask(TaskDtoRead task) {
        finishTaskConfirmationDialog = new FinishOrNotTaskConfirmationDialog(task, FinishOrNotTaskConfirmationDialog.ActionToConfirm.NOT_FINISH);
        finishTaskConfirmationDialog.show(getActivity().getSupportFragmentManager(),"dialog");
    }

    public interface OnFragmentTaskFormInteractionListener {

        void onFragmentTaskFormInteraction(Action action, TaskDtoRead task);
    }

    public enum Action {
        GO_TO_TASK,
    }
}