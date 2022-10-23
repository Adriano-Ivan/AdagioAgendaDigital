package br.com.adagio.adagioagendadigital.ui.activities.main;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoCreate;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.models.entities.Tag;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.ListTagBridgeView;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.form_tag.FormTagFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.utils.DeleteTagConfirmationDialog;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.form_task.FormTaskFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.HomeFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.HomeStaticValues;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.views.NumberPickerDialogToChooseYear;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.notifications.NotificationsFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.relatories.RelatoriesFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.TagsFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.ListTaskBridgeView;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskManagementFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.FinishTaskConfirmationDialog;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.add_tag_to_task_dialog.AddTagToTaskDialog;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.DeleteTaskConfirmationDialog;
import br.com.adagio.adagioagendadigital.ui.activities.main.utils.CurrentFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.utils.MainStaticValues;

public  class MainActivity extends AppCompatActivity implements
        NumberPickerDialogToChooseYear.onSaveYearListener,
        TaskManagementFragment.OnFragmentTaskFormInteractionListener,
        FormTaskFragment.OnFragmentTaskFormCreateInteractionListener,
        FormTagFragment.OnFragmentTagFormCreateInteractionListener,
        TagsFragment.OnFragmentTagFormInteractionListener,
        AddTagToTaskDialog.OnFragmentTaskAddTagInteractionListener,
        FinishTaskConfirmationDialog.OnFragmentTaskFinishInteractionListener,
        DeleteTagConfirmationDialog.OnFragmentTagDeleteInteractionListener,
        View.OnClickListener, DeleteTaskConfirmationDialog.OnFragmentTaskDeleteInteractionListener  {

    private BottomNavigationView bottomNavigationView;

    private HomeFragment homeFragment = new HomeFragment();
    private TaskManagementFragment taskFragment = new TaskManagementFragment();
    private TagsFragment tagsFragment = new TagsFragment();
    private FormTaskFragment formTaskFragment = new FormTaskFragment();
    private FormTagFragment formTagFragment = new FormTagFragment();
    private RelatoriesFragment relatoriesFragment = new RelatoriesFragment();
    private NotificationsFragment notificationsFragment = new NotificationsFragment();
    private TextView textTop;
    private ImageButton returnScreenButton;
    private ImageButton checkRegisterButton;

    private ListTaskBridgeView listTaskBridgeView;
    private ListTagBridgeView listTagBridgeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setNavigationAttributes();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setNavigationAttributes();
    }

    private void setNavigationAttributes(){
        textTop = findViewById(R.id.main_activity_text_top);
        textTop.setText(getResources().getString(returnCurrentTitle()));
        listTaskBridgeView = new ListTaskBridgeView(this);
        listTagBridgeView = new ListTagBridgeView(this);

        setViews();
        setListeners();

        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, returnCurrentFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                returnScreenButton.setVisibility(View.GONE);
                checkRegisterButton.setVisibility(View.GONE);
                switch(item.getItemId()){
                    case R.id.home_button:
                        MainStaticValues.setCurrentFragment(CurrentFragment.HOME);
                        textTop.setText(getResources().getString(returnCurrentTitle()));
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, homeFragment).commit();
                        return true;
                    case R.id.tasks_button:
                        MainStaticValues.setCurrentFragment(CurrentFragment.TASKS);
                        textTop.setText(getResources().getString(returnCurrentTitle()));
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments,taskFragment).commit();
                        return true;
                    case R.id.notifications_button:
                        MainStaticValues.setCurrentFragment(CurrentFragment.NOTIFICATIONS);
                        textTop.setText(getResources().getString(returnCurrentTitle()));
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, notificationsFragment).commit();
                        return true;
                    case R.id.tags_button:
                        MainStaticValues.setCurrentFragment(CurrentFragment.TAGS);
                        textTop.setText(getResources().getString(returnCurrentTitle()));
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, tagsFragment).commit();
                        return true;
                    case R.id.graph_relatory_button:
                        MainStaticValues.setCurrentFragment(CurrentFragment.RELATORIES);
                        textTop.setText(getResources().getString(returnCurrentTitle()));
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, relatoriesFragment).commit();
                        return true;
                }

                return false;
            }
        });
    }

    private void setViews(){
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        returnScreenButton = findViewById(R.id.main_activity_return_screen_button);
        checkRegisterButton = findViewById(R.id.main_activity_check_register);
    }

    private void setListeners(){
        returnScreenButton.setOnClickListener(this);
        checkRegisterButton.setOnClickListener(this);
    }

    private Fragment returnCurrentFragment(){
        if(MainStaticValues.CURRENT_FRAGMENT == CurrentFragment.RELATORIES){
            return relatoriesFragment;
        } else if(MainStaticValues.CURRENT_FRAGMENT == CurrentFragment.TASKS){
            return taskFragment;
        } else if(MainStaticValues.CURRENT_FRAGMENT == CurrentFragment.TAGS){
            return tagsFragment;
        } else if(MainStaticValues.CURRENT_FRAGMENT == CurrentFragment.NOTIFICATIONS){
            return notificationsFragment;
        }

        return homeFragment;
    }

    private int returnCurrentTitle(){
        if(MainStaticValues.CURRENT_FRAGMENT == CurrentFragment.RELATORIES){
            return R.string.view_your_relatories;
        } else if(MainStaticValues.CURRENT_FRAGMENT == CurrentFragment.TAGS){
            return R.string.manage_your_tags;
        } else if(MainStaticValues.CURRENT_FRAGMENT == CurrentFragment.TASKS){
            return R.string.manage_your_tasks;
        } else if(MainStaticValues.CURRENT_FRAGMENT == CurrentFragment.NOTIFICATIONS){
            return R.string.view_your_notifications;
        }

        return R.string.adagio;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSaveYear(int year) {
        HomeStaticValues.setPickedYearMemo(year);

        setNewStateOfCalendar();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setNewStateOfCalendar(){
        homeFragment.setNewStateOfCalendar();
    }

    @Override
    public void onFragmentTaskFormInteraction(TaskManagementFragment.Action action, TaskDtoRead task) {
        returnScreenButton.setVisibility(View.VISIBLE);
        checkRegisterButton.setVisibility(View.VISIBLE);

        if(task == null){
            formTaskFragment = new FormTaskFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, formTaskFragment).commit();
        } else {
            Bundle data = new Bundle();
            formTaskFragment  = new FormTaskFragment();

            data.putSerializable("taskToEdit", task);
            formTaskFragment.setArguments(data);

            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments,formTaskFragment)
                    .commit();
        }
    }

    @Override
    public void onFragmentTagFormInteraction(TagsFragment.Action action, Tag tag) {
        returnScreenButton.setVisibility(View.VISIBLE);
        checkRegisterButton.setVisibility(View.VISIBLE);

        if(tag == null){
            formTagFragment =new FormTagFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments,formTagFragment)
                    .commit();
        } else{
            Bundle data = new Bundle();
            formTagFragment = new FormTagFragment();

            data.putSerializable("tagToEdit", tag);
            formTagFragment.setArguments(data);

            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments,formTagFragment)
                    .commit();
        }
    }

    @Override
    public void onFragmentTaskDeleteInteraction(int position) {
       taskFragment.onFragmentTaskDeleteInteraction(position);
    }

    @Override
    public void onFragmentTagDeleteInteraction(int position) {
        tagsFragment.onFragmentTagDeleteInteraction(position);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.main_activity_return_screen_button &&
        MainStaticValues.CURRENT_FRAGMENT == CurrentFragment.TASKS){
            goToTaskOrTagManagement(GoTo.TASK);
        } else if(view.getId() == R.id.main_activity_check_register &&
                MainStaticValues.CURRENT_FRAGMENT == CurrentFragment.TASKS){
            formTaskFragment.auxSubmitTask();
           goToTaskOrTagManagement(GoTo.TASK);
        } else if(view.getId() == R.id.main_activity_return_screen_button &&
                MainStaticValues.CURRENT_FRAGMENT == CurrentFragment.TAGS){
            goToTaskOrTagManagement(GoTo.TAG);
        } else if(view.getId() == R.id.main_activity_check_register &&
                MainStaticValues.CURRENT_FRAGMENT == CurrentFragment.TAGS){
            formTagFragment.auxSubmitTag();
            goToTaskOrTagManagement(GoTo.TAG);
        }
    }

    @Override
    public void onFragmentTaskFormSubmitInteraction(TaskDtoCreate task,Integer id) {
        Log.i("PRIORITY", task.getPriority_id()+"");

        if(id == null){
            listTaskBridgeView.insert(task);
        } else {
            listTaskBridgeView.update(task,id);
        }

        goToTaskOrTagManagement(GoTo.TASK);
    }

    private void goToTaskOrTagManagement(GoTo goTo){

        returnScreenButton.setVisibility(View.GONE);
        checkRegisterButton.setVisibility(View.GONE);

        if(goTo == GoTo.TASK){
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, taskFragment).commit();
        } else if(goTo == GoTo.TAG){
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, tagsFragment).commit();
        }

    }

    @Override
    public void onFragmentTagFormSubmitInteraction(Tag tag) {
        listTagBridgeView.insert(tag);
        goToTaskOrTagManagement(GoTo.TAG);
    }

    @Override
    public void onFragmentTagFormEditInteraction(Tag tag, Tag tagToEdit) {
        listTagBridgeView.update(tag, tagToEdit.getId());
        goToTaskOrTagManagement(GoTo.TAG);
    }

    @Override
    public void onFragmentTaskAddInteraction(ArrayList<Integer> ids) {
        formTaskFragment.defineTagIds(ids);
    }

    @Override
    public void onFragmentTaskFinishInteraction(TaskDtoRead taskToFinish) {
        taskFragment.setTaskAsFinished(taskToFinish);
    }

    private enum GoTo{
        TAG,
        TASK
    }
}
