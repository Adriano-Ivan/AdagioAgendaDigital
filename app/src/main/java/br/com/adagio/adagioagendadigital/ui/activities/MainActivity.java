package br.com.adagio.adagioagendadigital.ui.activities;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.time.LocalDateTime;
import java.util.Calendar;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.ui.activities.home.Home;
import br.com.adagio.adagioagendadigital.ui.activities.home.HomeStaticValues;
import br.com.adagio.adagioagendadigital.ui.activities.home.views.NumberPickerDialogToChooseYear;

public  class MainActivity extends AppCompatActivity implements  NumberPickerDialogToChooseYear.onSaveYearListener {

    private BottomNavigationView bottomNavigationView;
    private Home homeFragment = new Home();

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
        setTitle(getResources().getString(R.string.adagio));
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        homeFragment = new Home();

        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home_button:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments, homeFragment).commit();
                        return true;
                    case R.id.tasks_button:

                        return true;
                    case R.id.notifications_button:

                        return true;
                    case R.id.tags_button:

                        return true;

                    case R.id.graph_relatory_button:

                        return true;
                }

                return false;
            }
        });
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
