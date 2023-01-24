package br.com.adagio.adagioagendadigital.ui.activities.main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
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
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import java.security.Key;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.data.task.TaskDAO;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoCreate;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.models.entities.Tag;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.utils.home_today_dialog.HomeDayDialog;
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
    private TaskDAO Tdao;

    public  int x = 0;

    private ListTaskBridgeView listTaskBridgeView;
    private ListTagBridgeView listTagBridgeView;

    private final String CHANNEL_ID = "primary_channel_id";
    private final String KEY_GROUP = "KEY";
    private final int NOTIFICATION_ID = 001;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setNavigationAttributes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tdao =  TaskDAO.getInstance(this);
        start();
        setNavigationAttributes();
    }

    private void setNavigationAttributes(){
        textTop = findViewById(R.id.main_activity_text_top);
        textTop.setText(getResources().getString(returnCurrentTitle()));
        listTaskBridgeView = new ListTaskBridgeView(this);
        listTagBridgeView = new ListTagBridgeView(this);
        relatoriesFragment = new RelatoriesFragment(this);

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

    @Override
    public void onSaveYear(int year) {
        HomeStaticValues.setPickedYearMemo(year);

        setNewStateOfCalendar();
    }

    private void setNewStateOfCalendar(){
        homeFragment.setNewStateOfCalendar();
        homeFragment.normalizeCalendar();
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

        }
    }

    @Override
    public void onFragmentTaskFormSubmitInteraction(TaskDtoCreate task,Integer id) {
        if(id == null){
            listTaskBridgeView.insert(task);
        } else {
            listTaskBridgeView.update(task,id);
        }

        goToTaskOrTagManagement(GoTo.TASK);
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


    private  void notificationTaskStarted(String nome, String prioridade, int x){
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.user_icon)
                .setContentTitle("A tarefa "+nome+" foi iniciada")
                .setContentText("Sua Prioridade é "+ prioridade)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroup(KEY_GROUP)
                .setGroupSummary(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(x, builder.build());
    }

    private  void notificationTask15minToFinish(String nome, String prioridade, int x){
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.user_icon)
                .setContentTitle("Falta 15 minutos para o fim da tarefa "+nome)
                .setContentText("Sua Prioridade é "+ prioridade)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroup(KEY_GROUP)
                .setGroupSummary(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(x, builder.build());
    }

    private  void notificationTaskFinished(String nome, String prioridade, int x){
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.user_icon)
                .setContentTitle("Tempo da tarefa"+nome+"acabou")
                .setContentText("Sua Prioridade é "+ prioridade)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroup(KEY_GROUP)
                .setGroupSummary(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(x, builder.build());
    }


    private void createNotificationChannel(){
            CharSequence name = "Notification";
            String description = "Channel Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
    }

    public void start() {
        Timer t = new Timer();
        TimerTask tk = new TimerTask() {
            @Override
            public void run() {
                System.out.println("morri");
                getTaskNotification();
            }
        };
        t.schedule(tk,0,60000);
    }

    private void getTaskNotification(){
        List <TaskDtoRead> taks = Tdao.listAllFromToday(LocalDateTime.now().getHour());
        for(TaskDtoRead T : taks){
            SystemClock.sleep(2000);
            if(T.getInitialMoment().getMinute() == LocalDateTime.now().getMinute()){
                notificationTaskStarted(T.getDescription(),T.getPriorityName(),x);
                x++;
            }
            if(T.getLimitMoment().getMinute()-15 == LocalDateTime.now().getMinute()){
                notificationTask15minToFinish(T.getDescription(),T.getPriorityName(),x);
                x++;
            }
            if(T.getLimitMoment().getMinute() == LocalDateTime.now().getMinute()){
                notificationTaskFinished(T.getDescription(),T.getPriorityName(),x);
                x++;
            }
        }
    }
}
