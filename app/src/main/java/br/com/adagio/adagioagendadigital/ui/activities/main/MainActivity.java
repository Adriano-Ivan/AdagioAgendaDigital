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

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoCreate;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.form_task.FormTaskFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.HomeFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.HomeStaticValues;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.views.NumberPickerDialogToChooseYear;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.notifications.NotificationsFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.relatories.RelatoriesFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.TagsFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.ListTaskBridgeView;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskManagementFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.utils.CurrentFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.utils.MainStaticValues;

public  class MainActivity extends AppCompatActivity implements
        NumberPickerDialogToChooseYear.onSaveYearListener,
        TaskManagementFragment.OnFragmentTaskFormInteractionListener,
        FormTaskFragment.OnFragmentTaskFormCreateInteractionListener,
        View.OnClickListener {

    private BottomNavigationView bottomNavigationView;

    private HomeFragment homeFragment = new HomeFragment();
    private TaskManagementFragment taskFragment = new TaskManagementFragment();
    private RelatoriesFragment relatoriesFragment = new RelatoriesFragment();
    private NotificationsFragment notificationsFragment = new NotificationsFragment();
    private FormTaskFragment ftFragment = new FormTaskFragment();
    private TagsFragment tagsFragment = new TagsFragment();
    private TextView textTop;
    private ImageButton returnScreenButton;
    private ListTaskBridgeView listBridge;

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
        listBridge = new ListTaskBridgeView(this);
        setViews();
        setListeners();

        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, returnCurrentFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                returnScreenButton.setVisibility(View.GONE);
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
    }

    private void setListeners(){
        returnScreenButton.setOnClickListener(this);
    }
    public void changeInFragmentToTaskForm(){
        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, ftFragment).commit();
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
    public void onFragmentTaskFormInteraction(TaskManagementFragment.Action action) {
        returnScreenButton.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments,ftFragment).commit();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.main_activity_return_screen_button &&
        MainStaticValues.CURRENT_FRAGMENT == CurrentFragment.TASKS){
            returnScreenButton.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments,taskFragment).commit();
        }
    }

    @Override
    public void onFragmentTaskFormSubmitInteraction(TaskDtoCreate task) {
        listBridge.insert(task);

        returnScreenButton.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, taskFragment).commit();
    }
}
