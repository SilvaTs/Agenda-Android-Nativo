package br.com.caelum.menu;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import br.com.caelum.agenda.ListaAlunosActivity;
import br.com.caelum.dao.AlunoDAO;
import br.com.caelum.model.Aluno;

public class ContextActionBar implements ActionMode.Callback {

    private Aluno _alunoSelecionado;
    private ListaAlunosActivity _activity;

    public ContextActionBar(ListaAlunosActivity activity, Aluno alunoSelecionado) {
        this._activity = activity;
        this._alunoSelecionado = alunoSelecionado;
    }

    @Override
    public boolean onCreateActionMode(final ActionMode mode, Menu menu) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menu;

        MenuItem ligar = menu.add("Ligar");
        MenuItem sms = menu.add("Enviar SMS");
        MenuItem mapa = menu.add("Achar no Mapa");
        MenuItem site = menu.add("Navegar no site");

        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(_activity)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deletar")
                        .setMessage("Deseja mesmo deletar?")
                        .setPositiveButton("Quero",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AlunoDAO dao = new AlunoDAO(_activity);
                                        dao.deletar(_alunoSelecionado);
                                        dao.close();
                                        _activity.loadList();
                                    }
                                }).setNegativeButton("Não", null).show();
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }
}
