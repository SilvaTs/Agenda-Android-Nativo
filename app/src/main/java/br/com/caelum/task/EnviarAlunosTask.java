package br.com.caelum.task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import br.com.caelum.model.Aluno;
import br.com.caelum.support.WebClient;
import br.com.caleum.agenda.R;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

//AsyncTask<'Tipo de parâmetro para o execute - doInBackground', 'Tipo de objeto para o onProgressUpdate', 'String - Saída dos dados da nova thread' >
public class EnviarAlunosTask extends AsyncTask<Object, Object, String> {

    private static Context _context;
    private static List<Aluno> _alunosList;
    private ProgressBar progressBar;

    public EnviarAlunosTask(Context context, List<Aluno> alunosList) {
        super();

        this._context = context;
        this._alunosList = alunosList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Configura e exibe um ProgressBar
        RelativeLayout relativeLayout = ((Activity)this._context).findViewById(R.id.layout_lista_alunos);
        progressBar = new ProgressBar((Activity)this._context);
        progressBar.setIndeterminate(true);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300, 300);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(progressBar, layoutParams);
        progressBar.setVisibility(View.VISIBLE);
    }

    // Parâmetro que saiu da nova thread, o parâmetro de entrada deste método será o retorno do método doInBackground
    // UI thread - thread principal
    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        progressBar.setVisibility(View.GONE); // Seta para a view para se foi, dispose o progressBar
        Toast.makeText(this._context, response, Toast.LENGTH_LONG ).show();
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    // Retorno que irá sair da nova thread, o retorno deste método será usado no método onPostExecute
    // thread secundária
    @Override
    protected String doInBackground(Object[] objects) {
        String ret = "";

        try {
            ret = new WebClient().post(this._alunosList);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }
}
