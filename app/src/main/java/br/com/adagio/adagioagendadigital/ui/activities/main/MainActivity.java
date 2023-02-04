package br.com.adagio.adagioagendadigital.ui.activities.main;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.ServiceCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.data.notification.NotificationDAO;
import br.com.adagio.adagioagendadigital.data.task.TaskDAO;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoCreate;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.models.entities.Notification;
import br.com.adagio.adagioagendadigital.models.entities.Tag;
import br.com.adagio.adagioagendadigital.models.enums.Priorities;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.home_today_dialog.HomeDayDialog;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.notifications.ListNotificationBridgeView;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.ListTagBridgeView;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.form_tag.FormTagFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.utils.DeleteTagConfirmationDialog;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.form_task.FormTaskFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.HomeFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.HomeStaticValues;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.NumberPickerDialogToChooseYear;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.notifications.NotificationsFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.relatories.RelatoriesFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.TagsFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.ListTaskBridgeView;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskManagementFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.FinishOrNotTaskConfirmationDialog;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.add_tag_to_task_dialog.AddTagToTaskDialog;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.utils.DeleteTaskConfirmationDialog;
import br.com.adagio.adagioagendadigital.ui.activities.main.utils.CurrentFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.utils.MainStaticValues;
import kotlin.reflect.TypeOfKt;

@RequiresApi(api = Build.VERSION_CODES.O)
public  class MainActivity extends AppCompatActivity implements
        NumberPickerDialogToChooseYear.onSaveYearListener,
        TaskManagementFragment.OnFragmentTaskFormInteractionListener,
        FormTaskFragment.OnFragmentTaskFormCreateInteractionListener,
        FormTagFragment.OnFragmentTagFormCreateInteractionListener,
        TagsFragment.OnFragmentTagFormInteractionListener,
        AddTagToTaskDialog.OnFragmentTaskAddTagInteractionListener,
        FinishOrNotTaskConfirmationDialog.OnFragmentTaskFinishOrNotInteractionListener,
        DeleteTagConfirmationDialog.OnFragmentTagDeleteInteractionListener,
        View.OnClickListener, DeleteTaskConfirmationDialog.OnFragmentTaskDeleteInteractionListener,
        HomeDayDialog.OnFragmentTaskFormForSpecifiedDayInteractionListener
        {

    private BottomNavigationView bottomNavigationView;

    private HomeFragment homeFragment = new HomeFragment();
    private TaskManagementFragment taskFragment = new TaskManagementFragment();
    private TagsFragment tagsFragment = new TagsFragment();
    private FormTaskFragment formTaskFragment = new FormTaskFragment();
    private FormTagFragment formTagFragment = new FormTagFragment();
    private RelatoriesFragment relatoriesFragment;
    private NotificationsFragment notificationsFragment = new NotificationsFragment();
    private TextView textTop;
    private ImageButton returnScreenButton;
    private ImageButton checkRegisterButton;
    private ImageButton bottonDeleteAll;
    private TaskDAO Tdao;
    private Notification notification;
    private NotificationDAO notificationDAO;
    private  NotificationService notificationService;


    private ListTaskBridgeView listTaskBridgeView;
    private ListTagBridgeView listTagBridgeView;
    private ListNotificationBridgeView listNotificationBridgeView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = new Intent(this,NotificationService.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setNavigationAttributes();
        if(!foregroundServiceRunning()){
            ContextCompat.startForegroundService(this,intent);
        }
    }


    public boolean foregroundServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)){
            if(NotificationService.class.getName().equals(service.service.getClassName())){
                return true;
            }
        }

        return false;
    }

    protected void onStop () {
        super .onStop() ;
    }

    @Override
    public void onDestroy () {
        super .onDestroy();
        System.out.println("aplicativo fechou");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tdao =  TaskDAO.getInstance(this);
        notificationDAO = NotificationDAO.getInstance(this);
        setNavigationAttributes();
    }

    private void setNavigationAttributes(){
        textTop = findViewById(R.id.main_activity_text_top);
        textTop.setText(getResources().getString(returnCurrentTitle()));
        listTaskBridgeView = new ListTaskBridgeView(this);
        listTagBridgeView = new ListTagBridgeView(this);
        listNotificationBridgeView = new ListNotificationBridgeView(this);
        relatoriesFragment = new RelatoriesFragment(this, LocalDate.now());
        setViews();
        setListeners();

        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, returnCurrentFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                returnScreenButton.setVisibility(View.GONE);
                checkRegisterButton.setVisibility(View.GONE);
                bottonDeleteAll.setVisibility(View.GONE);
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
                        bottonDeleteAll.setVisibility(View.VISIBLE);
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
        bottonDeleteAll = findViewById(R.id.main_activity_check_deleteAll);
    }

    private void setListeners(){
        returnScreenButton.setOnClickListener(this);
        checkRegisterButton.setOnClickListener(this);
        bottonDeleteAll.setOnClickListener(this);
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

    @Override
    public void onSaveYear(int year) {
        HomeStaticValues.setPickedYearMemo(year);

        setNewStateOfCalendar();
    }

    private void setNewStateOfCalendar(){
        homeFragment.setNewStateOfCalendar(false);
        homeFragment.normalizeCalendar(false);
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
            //displayNotification();
            if(formTaskFragment.validFormInformation()){
                goToTaskOrTagManagement(GoTo.TASK);
            }
        } else if(view.getId() == R.id.main_activity_return_screen_button &&
                MainStaticValues.CURRENT_FRAGMENT == CurrentFragment.TAGS){
            goToTaskOrTagManagement(GoTo.TAG);
        } else if(view.getId() == R.id.main_activity_check_register &&
                MainStaticValues.CURRENT_FRAGMENT == CurrentFragment.TAGS){
            formTagFragment.auxSubmitTag();
            if(formTagFragment.validFormInformation()){
                goToTaskOrTagManagement(GoTo.TAG);
            }
        } else if(view.getId() == R.id.main_activity_return_screen_button
                && MainStaticValues.CURRENT_FRAGMENT == CurrentFragment.HOME
        ){
            goToHome(null);
        }else if(view.getId() == R.id.main_activity_check_register
                && MainStaticValues.CURRENT_FRAGMENT == CurrentFragment.HOME
        ){
            formTaskFragment.auxSubmitTask();

            if(formTaskFragment.validFormInformation()){
                goToHome(null);
            }
        }else if(view.getId() == R.id.main_activity_check_deleteAll){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.dialog_notify))
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            listNotificationBridgeView.deletAll();
                            notificationsFragment.configureAdapter();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                builder.show();
        }
    }

    @Override
    public void onFragmentTaskFormSubmitInteraction(TaskDtoCreate task,Integer id) {
        if(id == null){
            listTaskBridgeView.insert(task);
        } else {
            listTaskBridgeView.update(task,id);
        }

        if( MainStaticValues.CURRENT_FRAGMENT == CurrentFragment.HOME){
            goToHome(null);
        } else {
            goToTaskOrTagManagement(GoTo.TASK);
        }
    }

    // REVISAR
    private void goToHome(Object objectForFutureParameters){
        returnScreenButton.setVisibility(View.GONE);
        checkRegisterButton.setVisibility(View.GONE);

        MainStaticValues.setCurrentFragment(CurrentFragment.HOME);
        textTop.setText(getResources().getString(returnCurrentTitle()));
        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, homeFragment).commit();
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
    public void onFragmentTaskFinishOrNotInteraction(TaskDtoRead taskToFinish, FinishOrNotTaskConfirmationDialog.ActionToConfirm action) {
        if(action == FinishOrNotTaskConfirmationDialog.ActionToConfirm.FINISH){
            taskFragment.setTaskAsFinished(taskToFinish);
        } else {
            taskFragment.setTaskAsUnfinished(taskToFinish);
        }

    }

    @Override
    public void onFragmentTaskFormForSpecifiedDayInteraction(TaskManagementFragment.Action action, LocalDateTime day) {
        returnScreenButton.setVisibility(View.VISIBLE);
        checkRegisterButton.setVisibility(View.VISIBLE);

        Bundle data = new Bundle();
        formTaskFragment  = new FormTaskFragment();

        data.putSerializable("predefinedDay", day);
        formTaskFragment.setArguments(data);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments,formTaskFragment)
                .commit();
    }

    private enum GoTo{
        TAG,
        TASK
    }


}
