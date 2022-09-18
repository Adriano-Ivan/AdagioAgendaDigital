package br.com.adagio.adagioagendadigital.activities.form_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import br.com.adagio.adagioagendadigital.R;
import br.com.adagio.adagioagendadigital.activities.form_register.FormRegister;

public class FormLogin extends AppCompatActivity implements View.OnClickListener {

    private TextView textToRegisterForm;

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
    }

    private void atribuirListeners(){
        textToRegisterForm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.activitiy_form_login_text_to_register_screen){
            Intent intent = new Intent(this, FormRegister.class);
            startActivity(intent);
        }
    }
}