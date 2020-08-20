package br.com.caelum.agenda;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import br.com.caelum.Converter.AlunoConverter;
import br.com.caelum.adapter.ListaAlunosAdapter;
import br.com.caelum.dao.AlunoDAO;
import br.com.caelum.helper.FormularioHelper;
import br.com.caelum.menu.ContextActionBar;
import br.com.caelum.model.Aluno;
import br.com.caelum.security.Permissions;
import br.com.caelum.task.EnviarAlunosTask;
import br.com.caleum.agenda.R;
import org.json.JSONException;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class ListaAlunosActivity extends AppCompatActivity {

    public ListView listAlunos;
    private FloatingActionButton btnAdd;
    FormularioHelper formularioHelper;
    private List<Aluno> alunos;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);
        Permissions.doPermission(this);
/*
        String[] alunos = { "Ana", "Daniel", "Henrique", "Paula", "Rafael" };
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alunos);
*/
        listAlunos = findViewById(R.id.lista_alunos);
        btnAdd = findViewById(R.id.botao_lista);

        listAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ListaAlunosActivity.this, "Posição selecionada: " + position ,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                intent.putExtra("aluno", (Serializable) parent.getItemAtPosition(position));
                startActivity(intent);
            }
        });

        listAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {

//                Toast.makeText(ListaAlunosActivity.this, "Clique longo: " + adapter.getItemAtPosition(position) ,Toast.LENGTH_LONG).show();
//                ContextActionBar actionBar = new ContextActionBar(ListaAlunosActivity.this, (Aluno)adapter.getItemAtPosition(position));
//                ListaAlunosActivity.this.startSupportActionMode(actionBar);

                return false;

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intent);
            }
        });

        formularioHelper = new FormularioHelper(this);
        registerForContextMenu(listAlunos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_alunos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<Aluno> alunos = new AlunoDAO(this).getLista();
        new AlunoDAO(this).close();

        switch (item.getItemId()) {
            case R.id.menu_send_grades:
//                try {
//                    String json = new AlunoConverter().toJSON(alunos);
//                    Log.i("jsonResponse", json);
//                    Toast.makeText(this, json, Toast.LENGTH_LONG).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                new EnviarAlunosTask(this, alunos).execute();
                return true;

            case R.id.menu_receive_tests:
                Intent provas = new Intent(this, ProvasActivity.class);
                startActivity(provas);
                return true;
            case R.id.menu_map:
                Intent mapa = new Intent(this, MostraAlunosActivity.class);
                startActivity(mapa);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        final Aluno alunoSelecionado = (Aluno)listAlunos.getAdapter().getItem(info.position);

        MenuItem ligar = menu.add("Ligar");
        final Intent intentLigar = new Intent(Intent.ACTION_CALL);
        intentLigar.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentLigar.setData(Uri.parse("tel:" + alunoSelecionado.getTelefone()));
        ligar.setIntent(intentLigar);

        MenuItem sms = menu.add("Enviar SMS");
        final Intent intentSms = new Intent(Intent.ACTION_VIEW);
        intentSms.setData(Uri.parse("sms:" + alunoSelecionado.getTelefone()));
        intentSms.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentSms.putExtra("sms_body", "Sua nota é " + alunoSelecionado.getNota().toString());
        //sms.setIntent(intentSms);

        sms.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SmsManager smsManager = SmsManager.getDefault();
                PendingIntent sentIntent = PendingIntent.getActivity(ListaAlunosActivity.this, 0, new Intent(), 0);
                if (PhoneNumberUtils.isWellFormedSmsAddress(alunoSelecionado.getTelefone())) {
                    smsManager.sendTextMessage(alunoSelecionado.getTelefone(), null, "Sua nota é " + alunoSelecionado.getNota(), sentIntent, null);
                    Toast.makeText(ListaAlunosActivity.this, "SMS enviado com sucesso!!!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ListaAlunosActivity.this, "Falha ao enviar o SMS!!!", Toast.LENGTH_LONG).show();
                }
                return false;

            }
        });

        MenuItem mapa = menu.add("Achar no Mapa");
        final Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String endereco = "Travessa Municipal, 132";
        intentMapa.setData(Uri.parse("geo:0,0?z=14&q="+Uri.encode(endereco)));
        mapa.setIntent(intentMapa);

        MenuItem site = menu.add("Navegar no site");
        final Intent intentSite = new Intent(Intent.ACTION_VIEW);
        intentSite.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String url = "http:www.google.com";
        intentSite.setData(Uri.parse(url));
        site.setIntent(intentSite);

        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                               @Override
                                               public boolean onMenuItemClick(MenuItem item) {
                                                   new AlertDialog.Builder(ListaAlunosActivity.this)
                                                           .setIcon(android.R.drawable.ic_dialog_alert)
                                                           .setTitle("Deletar")
                                                           .setMessage("Deseja mesmo deletar?")
                                                           .setPositiveButton("Quero",
                                                                   new DialogInterface.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(DialogInterface dialog, int which) {
                                                                           AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                                                                           dao.deletar(alunoSelecionado);
                                                                           dao.close();
                                                                           loadList();
                                                                       }
                                                                   }).setNegativeButton("Não", null).show();
                                                   return false;

                                               }
                                           });
    }



    public void loadList() {
        AlunoDAO alunoDAO = new AlunoDAO(this);
        alunos = alunoDAO.getLista();
        alunoDAO.close();

        ListaAlunosAdapter adapter = new ListaAlunosAdapter(alunos, this);
        //final ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.activity_list_item, alunos);
        listAlunos.setAdapter(adapter);
    }


}
