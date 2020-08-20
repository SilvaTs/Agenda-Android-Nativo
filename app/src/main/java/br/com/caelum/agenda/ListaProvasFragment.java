package br.com.caelum.agenda;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import br.com.caelum.model.Prova;
import br.com.caleum.agenda.R;

import java.util.Arrays;
import java.util.List;

public class ListaProvasFragment extends Fragment {

    private ListView listViewProvas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflando o XML do layout do nosso fragment
        View layoutProvas = inflater.inflate(R.layout.fragment_lista_provas, container, false);

        // Recupera a referência para a ListView do fragment
        this.listViewProvas = (ListView) layoutProvas.findViewById(R.id.lista_provas_listview);

        // Gerando as provas que irão popular nossa lista
        Prova prova1 = new Prova("20/06/2015", "Matemática");
        prova1.setTopicos(Arrays.asList("Álgebra linear", "Cálculo", "Estatística"));

        Prova prova2 = new Prova("25/07/2015", "Português");
        prova2.setTopicos(Arrays.asList("Complemento nominal", "Orações subordinadas", "Análise sintática"));

        final List<Prova> provas = Arrays.asList(prova1, prova2);

        // Populando a lista com um ArrayAdapter que recebe a lista de provas
        this.listViewProvas.setAdapter(new ArrayAdapter<Prova>(getActivity(), android.R.layout.simple_list_item_1, provas));

        // Listener para o evento de clique mostrar a prova selecionada
        this.listViewProvas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int posicao, long id) {
                Prova selecionada = (Prova) adapter.getItemAtPosition(posicao);

                Toast.makeText(getActivity(), "Prova selecionada: " + selecionada, Toast.LENGTH_LONG).show();

                ProvasActivity calendarioProvas = (ProvasActivity)getActivity();
                calendarioProvas.populateProva(selecionada);
            }
        });

        return layoutProvas;
    }
}