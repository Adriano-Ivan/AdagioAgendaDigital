package br.com.adagio.adagioagendadigital.ui.activities.main;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.HomeFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.HomeStaticValues;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.home.views.NumberPickerDialogToChooseYear;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.notifications.NotificationsFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.relatories.RelatoriesFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tags.TagsFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks.TaskManagementFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.utils.CurrentFragment;
import br.com.adagio.adagioagendadigital.ui.activities.main.utils.MainStaticValues;

public  class MainActivity extends AppCompatActivity implements  NumberPickerDialogToChooseYear.onSaveYearListener {

    private BottomNavigationView bottomNavigationView;

    private HomeFragment homeFragment = new HomeFragment();
    private TaskManagementFragment taskFragment = new TaskManagementFragment();
    private RelatoriesFragment relatoriesFragment = new RelatoriesFragment();
    private NotificationsFragment notificationsFragment = new NotificationsFragment();
    private TagsFragment tagsFragment = new TagsFragment();

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
        setTitle(getResources().getString(returnCurrentTitle()));
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, returnCurrentFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home_button:
                        setTitle(R.string.adagio);
                        MainStaticValues.setCurrentFragment(CurrentFragment.HOME);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, homeFragment).commit();
                        return true;
                    case R.id.tasks_button:
                        setTitle(R.string.manage_your_tasks);
                        MainStaticValues.setCurrentFragment(CurrentFragment.TASKS);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments,taskFragment).commit();
                        return true;
                    case R.id.notifications_button:
                        setTitle(R.string.view_your_notifications);
                        MainStaticValues.setCurrentFragment(CurrentFragment.NOTIFICATIONS);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, notificationsFragment).commit();
                        return true;
                    case R.id.tags_button:
                        setTitle(R.string.manage_your_tags);
                        MainStaticValues.setCurrentFragment(CurrentFragment.TAGS);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, tagsFragment).commit();
                        return true;
                    case R.id.graph_relatory_button:
                        setTitle(R.string.view_your_relatories);
                        MainStaticValues.setCurrentFragment(CurrentFragment.RELATORIES);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, relatoriesFragment).commit();
                        return true;
                }

                return false;
            }
        });
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
}
