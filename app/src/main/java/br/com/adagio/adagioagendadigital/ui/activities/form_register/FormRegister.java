package br.com.adagio.adagioagendadigital.ui.activities.form_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import br.com.adagio.adagioagendadigital.R;

// Activity não está sendo usada no momento, por conta de mudanças no direcionamento do projeto,
// mas será mantida pelo valor que agrega a trabalhos futuros e pelo fato de que manter em um branch
// separado poderia causar confusões em resoluções futuras de merge ou rebase. Portanto, compensa mantê-la, não
// havendo problemas graves em sua presença no projeto.
public class FormRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_register);

        definirTitulo();
    }

    private void definirTitulo(){
        setTitle(R.string.register_activity_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register_user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_register_user_save_user){
            Log.i("Opção ", "salvar ");
        }
        return super.onOptionsItemSelected(item);
    }
}