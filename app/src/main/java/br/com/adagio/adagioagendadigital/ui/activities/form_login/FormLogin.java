package br.com.adagio.adagioagendadigital.ui.activities.form_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.ui.activities.form_register.FormRegister;
import br.com.adagio.adagioagendadigital.ui.activities.home.Home;

public class FormLogin extends AppCompatActivity implements View.OnClickListener {

    private TextView textToRegisterForm;
    private AppCompatButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        iniciarFragments();
        atribuirListeners();

        getSupportActionBar().hide();
    }

    private void iniciarFragments(){
        textToRegisterForm = findViewById(R.id.activitiy_form_login_text_to_register_screen);
        loginButton = findViewById((R.id.activity_form_login_button_login));
    }

    private void atribuirListeners(){
        textToRegisterForm.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.activitiy_form_login_text_to_register_screen){
            Intent intent = new Intent(this, FormRegister.class);
            startActivity(intent);
        } else if(v.getId() == R.id.activity_form_login_button_login){
            goHomePage();
        }
    }

    private void goHomePage(){
        Intent intent = new Intent(this,
                Home.class);
        startActivity(intent);
    }
}