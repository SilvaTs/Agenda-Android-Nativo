package br.com.caelum.agenda;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentTransitionImpl;
import android.support.v7.app.AppCompatActivity;
import br.com.caelum.model.Prova;
import br.com.caleum.agenda.R;

public class ProvasActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provas);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (isTablet()) {
            transaction
                    .replace(R.id.provas_lista, new ListaProvasFragment())
                    .replace(R.id.provas_detalhes, new DetalhesProvaFragment());
        } else {
            transaction.replace(R.id.provas_view, new ListaProvasFragment());
        }

        transaction.commit();
    }

    private boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    public void populateProva(Prova selecionada) {
        FragmentManager manager = getSupportFragmentManager();
        DetalhesProvaFragment detalhesProvaFragment;

        if(isTablet()) {
            detalhesProvaFragment= (DetalhesProvaFragment) manager.findFragmentById(R.id.provas_detalhes);
            detalhesProvaFragment.populateFieldsWithData(selecionada);
        } else {
            Bundle args = new Bundle();
            args.putSerializable("prova", selecionada);

            detalhesProvaFragment = new DetalhesProvaFragment();
            detalhesProvaFragment.setArguments(args);

            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.provas_view, detalhesProvaFragment);
            fragmentTransaction.addToBackStack(null); // Este comando serve para que quando o botão voltar seja tocado voltemos para a fragment anterior.
            fragmentTransaction.commit();
        }
    }
}
