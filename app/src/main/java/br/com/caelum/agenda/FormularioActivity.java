package br.com.caelum.agenda;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import br.com.caelum.dao.AlunoDAO;
import br.com.caelum.helper.FormularioHelper;
import br.com.caelum.model.Aluno;
import br.com.caleum.agenda.BuildConfig;
import br.com.caleum.agenda.R;

import java.io.File;

public class FormularioActivity extends AppCompatActivity {

    private FormularioHelper formularioHelper;
    private String photoPath;
    private static final int CODE = 321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        formularioHelper = new FormularioHelper(this);

        FloatingActionButton btAddPhoto = formularioHelper.getPhotoButton();

        final Aluno aluno = (Aluno)getIntent().getSerializableExtra("aluno");
        if (aluno != null) {
            formularioHelper.setAluno(aluno);
        }

        btAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoPath = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                File file = new File(photoPath);
                Uri uriImage = FileProvider.getUriForFile(FormularioActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
                startActivityForResult(intent, CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_formulario_ok:

                Aluno aluno = formularioHelper.getAluno();

                if (formularioHelper.validateInputs()) {
                    AlunoDAO alunoDAO = new AlunoDAO(this);
                    if (aluno.getId() == null) {
                        alunoDAO.insere(aluno);
                        Toast.makeText(this, "Aluno cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                    } else {
                        alunoDAO.altera(aluno);
                        Toast.makeText(this, "Aluno alterado com sucesso!", Toast.LENGTH_LONG).show();
                    }
                    alunoDAO.close();
                    finish();
                } else {
                    formularioHelper.showErrorMessage();
                }
                return false;
            case R.id.menu_formulario_cancel:
                Toast.makeText(this, "Cancel clicado", Toast.LENGTH_LONG).show();
                finish();
                return false;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int refCode, int resCode, Intent data) {
        if (refCode == CODE) {
            if (resCode == Activity.RESULT_OK) {
                formularioHelper.loadPhoto(photoPath);
            }
        }
    }
}
