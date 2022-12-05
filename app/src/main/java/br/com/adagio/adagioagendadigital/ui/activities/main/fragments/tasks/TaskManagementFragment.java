package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.DeleteTaskConfirmationDialog;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.FinishOrNotTaskConfirmationDialog;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.TypeListTaskManagementOrderDate;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.TypeListTaskManagementOrderPriority;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TaskManagementFragment extends Fragment
        implements View.OnClickListener, DeleteTaskConfirmationDialog.OnFragmentTaskDeleteInteractionListener {

    private LinearLayout containerOptions;
    private Button buttonHideContainerOptions;
    private Button buttonGoToFormTasks;
    private Button updateListButton;

    private FloatingActionButton fabButtonGoToTasks;
    private ListView listTasks;
    private View rootView;
    private ListTaskBridgeView listTaskBridgeView;
    private OnFragmentTaskFormInteractionListener mListener;
    private DeleteTaskConfirmationDialog deleteTaskConfirmationModal;
    private FinishOrNotTaskConfirmationDialog finishTaskConfirmationDialog;

    private RadioGroup radioGroupOrderDatetimeOptions;
    private RadioGroup radioGroupOrderPriorityOptions;

    private RadioButton radioButtonOrderByToday;
    private RadioButton radioButtonOrderByLimitMomentAsc;
    private RadioButton radioButtonOrderByLimitMomentDesc;
    private RadioButton radioButtonOrderByInitialMomentAsc;
    private RadioButton radioButtonOrderByInitialMomentDesc;

    private RadioButton radioButtonOrderByPriorityAsc;
    private RadioButton radioButtonOrderByPriorityDesc;

    private TypeListTaskManagementOrderDate typeListTaskOrderDate = TypeListTaskManagementOrderDate.TODAY;
    private TypeListTaskManagementOrderPriority typeListTaskOrderPriority = TypeListTaskManagementOrderPriority.PRIORITY_ASC;

    private LinearLayout linearLayoutContainerPagination;

    private ImageButton imageButtonNextPage;
    private ImageButton imageButtonPreviousPage;

    private ImageButton imageButtonNextPageDisabled;
    private ImageButton imageButtonPreviousPageDisabled;

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

    // Chamada de métodos para definição
    private void setAttributes(){
        typeListTaskOrderDate = TypeListTaskManagementOrderDate.TODAY;
        typeListTaskOrderPriority = TypeListTaskManagementOrderPriority.PRIORITY_ASC;
        defineViews();
        setConfigurationVisibilitity(true);
        defineListeners();

        configureAdapter();
        updateTasksList();
        updatePaginationVisibility();
    }

    // Definição dos elementos da tela
    private void defineViews(){
        containerOptions = rootView.findViewById(R.id.fragment_task_container_options);

        buttonHideContainerOptions = rootView.findViewById(R.id.fragment_task_hide_options);
        listTasks = rootView.findViewById(R.id.fragment_task_list_tasks);
        fabButtonGoToTasks = rootView.findViewById(R.id.fragment_task_fab_button_tasks);
        buttonGoToFormTasks = rootView.findViewById((R.id.fragment_task_user_wants_to_create_task));

        // Radio Groups
        radioGroupOrderDatetimeOptions = rootView.findViewById(R.id.fragment_task_order_datetime_options);
        radioGroupOrderPriorityOptions=rootView.findViewById(R.id.fragment_task_order_by_priority_options);

        // Elementos de paginação
        linearLayoutContainerPagination = rootView.findViewById(R.id.fragment_task_container_pagination);
        imageButtonNextPage = rootView.findViewById(R.id.fragment_task_management_next_page);
        imageButtonPreviousPage = rootView.findViewById(R.id.fragment_task_management_previous_page);
        textViewCurrentPage = rootView.findViewById(R.id.fragment_task_management_text_page);

        // RadioButtons de ordenação de data
        radioButtonOrderByToday = rootView.findViewById(R.id.fragment_task_order_by_today);
        radioButtonOrderByInitialMomentAsc =rootView.findViewById(R.id.fragment_task_order_by_initial_moment_asc);
        radioButtonOrderByLimitMomentAsc=rootView.findViewById(R.id.fragment_task_order_by_limit_moment_asc);
        radioButtonOrderByInitialMomentDesc = rootView.findViewById(R.id.fragment_task_order_by_initial_moment_desc);
        radioButtonOrderByLimitMomentDesc=rootView.findViewById(R.id.fragment_task_order_by_limit_moment_desc);

        radioGroupOrderDatetimeOptions.check(radioButtonOrderByToday.getId());

        radioButtonOrderByPriorityAsc = rootView.findViewById(R.id.fragment_task_order_by_priority_asc);
        radioButtonOrderByPriorityDesc = rootView.findViewById(R.id.fragment_task_order_by_priority_desc);

        radioGroupOrderPriorityOptions.check(radioButtonOrderByPriorityAsc.getId());

        updateListButton = rootView.findViewById(R.id.fragment_task_update_list);
        imageButtonNextPageDisabled = rootView.findViewById(R.id.fragment_task_management_next_page_disabled);
        imageButtonPreviousPageDisabled =  rootView.findViewById(R.id.fragment_task_management_previous_page_disabled);
    }

    // Definição de listeners
    private void defineListeners(){
        buttonHideContainerOptions.setOnClickListener(this);
        buttonGoToFormTasks.setOnClickListener(this);
        fabButtonGoToTasks.setOnClickListener(this);
        imageButtonNextPage.setOnClickListener(this);
        imageButtonPreviousPage.setOnClickListener(this);
        updateListButton.setOnClickListener(this);

        // Listeners dos radioGroups

        // Data
        radioGroupOrderDatetimeOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if(radioButtonOrderByToday.getId() == id){
                    typeListTaskOrderDate = TypeListTaskManagementOrderDate.TODAY;
                } else if(radioButtonOrderByInitialMomentAsc.getId() == id){
                    typeListTaskOrderDate = TypeListTaskManagementOrderDate.INITIAL_MOMENT_ASC;
                } else if(radioButtonOrderByInitialMomentDesc.getId() == id){
                    typeListTaskOrderDate=TypeListTaskManagementOrderDate.INITIAL_MOMENT_DESC;
                } else if(radioButtonOrderByLimitMomentAsc.getId() == id){
                    typeListTaskOrderDate=TypeListTaskManagementOrderDate.LIMIT_MOMENT_ASC;
                } else if(radioButtonOrderByLimitMomentDesc.getId() ==id){
                    typeListTaskOrderDate=TypeListTaskManagementOrderDate.LIMIT_MOMENT_DESC;
                }
            }
        });

        // Prioridade
        radioGroupOrderPriorityOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if(radioButtonOrderByPriorityAsc.getId() == id){
                    typeListTaskOrderPriority=TypeListTaskManagementOrderPriority.PRIORITY_ASC;
                } else if(radioButtonOrderByPriorityDesc.getId() == id){
                    typeListTaskOrderPriority=TypeListTaskManagementOrderPriority.PRIORITY_DESC;
                }
            }
        });
    }

    // Esconder container de configuração de tipos de ordenação
    private void  setContainerOptionsIsGone (){
        containerOptions.setVisibility(View.GONE);
        TaskStaticValues.setContainerOptionsIsGone(true);
        buttonHideContainerOptions.setText(getResources().getString(R.string.task_show_options));
        listTasks.setVisibility(View.VISIBLE);
    }

    private void setConfigurationVisibilitity(boolean hideOnly){
        if(!hideOnly ){
            if(!TaskStaticValues.CONTAINER_OPTIONS_IS_GONE){
                setContainerOptionsIsGone();
            } else if(TaskStaticValues.CONTAINER_OPTIONS_IS_GONE){
                containerOptions.setVisibility(View.VISIBLE);
                TaskStaticValues.setContainerOptionsIsGone(false);
                buttonHideContainerOptions.setText(getResources().getString(R.string.fragment_task_hide_options));
                listTasks.setVisibility(View.GONE);
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

    // Chamadas para configuração do adapter que contém da lista de tasks
    private void configureAdapter() {
        listTaskBridgeView = new ListTaskBridgeView(getActivity());

        listTaskBridgeView.setOrderDateType(typeListTaskOrderDate);
        listTaskBridgeView.setOrderPriorityType(typeListTaskOrderPriority);

        listTaskBridgeView.configureAdapter(listTasks,this);

        registerForContextMenu(listTasks);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();

        inflater.inflate(R.menu.menu_task_item, menu);
    }

    // Após long-press no item de task, é aberto o menu com opções de edição e deleção
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
        TaskStaticValues.goBackToDefaultValue();
        listTaskBridgeView.setOrderDateType(typeListTaskOrderDate);
        listTaskBridgeView.updateList(TaskStaticValues.LIMIT_LIST, TaskStaticValues.OFFSET_LIST,
                typeListTaskOrderDate == TypeListTaskManagementOrderDate.TODAY);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == buttonHideContainerOptions.getId()){
           setConfigurationVisibilitity(false);
           updatePaginationVisibility();
        } else if(view.getId() == buttonGoToFormTasks.getId()){
            mListener.onFragmentTaskFormInteraction(Action.GO_TO_TASK,null);
        }else if(view.getId() == fabButtonGoToTasks.getId()){
            mListener.onFragmentTaskFormInteraction(Action.GO_TO_TASK,null);
        } else if(view.getId() == imageButtonNextPage.getId()){
            listTaskBridgeView.updateList(TaskStaticValues.LIMIT_LIST,
                    TaskStaticValues.OFFSET_LIST + TaskStaticValues.LIMIT_LIST,
                    true
                    );
            updatePaginationVisibility();
        } else if(view.getId() == imageButtonPreviousPage.getId()){
            listTaskBridgeView.updateList(TaskStaticValues.LIMIT_LIST,
                    TaskStaticValues.OFFSET_LIST - TaskStaticValues.LIMIT_LIST,
                    false
                    );
            updatePaginationVisibility();
        } else if(view.getId() == updateListButton.getId()){
            TaskStaticValues.goBackToDefaultValue();
            listTaskBridgeView.setOrderDateType(typeListTaskOrderDate);
            listTaskBridgeView.setOrderPriorityType(typeListTaskOrderPriority);
            listTaskBridgeView.updateListAux();

            setConfigurationVisibilitity(false);
            updatePaginationVisibility();
        }
    }

    private void updatePaginationVisibility(){
        if (!listTaskBridgeView.thereArePreviousOrNextPage()) {
            linearLayoutContainerPagination.setVisibility(View.GONE);
        } else {
            if(!listTaskBridgeView.thereIsNextPage()){
                imageButtonNextPage.setVisibility(View.GONE);
                imageButtonNextPageDisabled.setVisibility(View.VISIBLE);
            } else {
                imageButtonNextPage.setVisibility(View.VISIBLE);
                imageButtonNextPageDisabled.setVisibility(View.GONE);
            }

            if(!listTaskBridgeView.thereIsPreviousPage()){
                imageButtonPreviousPage.setVisibility(View.GONE);
                imageButtonPreviousPageDisabled.setVisibility(View.VISIBLE);
            } else {
                imageButtonPreviousPage.setVisibility(View.VISIBLE);
                imageButtonPreviousPageDisabled.setVisibility(View.GONE);
            }
        }

        textViewCurrentPage.setText(Integer.toString(TaskStaticValues.CURRENT_PAGE));
    }

    public void setTaskAsFinished(TaskDtoRead task){
        listTaskBridgeView.setTaskAsFinished(task);
        updatePaginationVisibility();
    }

    public void setTaskAsUnfinished(TaskDtoRead task){
        listTaskBridgeView.setTaskAsUnfinished(task);
        updatePaginationVisibility();
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

       if(TaskStaticValues.CURRENT_PAGE == 1){
           TaskStaticValues.goBackToDefaultValue();
           listTaskBridgeView.setOrderDateType(typeListTaskOrderDate);
           listTaskBridgeView.setOrderPriorityType(typeListTaskOrderPriority);
           listTaskBridgeView.updateListAux();
       }

       updatePaginationVisibility();
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