package br.com.caelum.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import br.com.caelum.model.Aluno;
import br.com.caleum.agenda.BuildConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO extends SQLiteOpenHelper {

    private static final int VERSAO = 2;//1;
    private static final String TABELA = "Alunos";
    private static final String DATABASE = "CadastroCaelum";

    String ddl;

    public AlunoDAO(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    public void onCreate(SQLiteDatabase database) {
        ddl = "CREATE TABLE " + TABELA + " (id INTEGER PRIMARY KEY, nome TEXT NOT NULL, telefone TEXT, email TEXT, nota REAL, photoPath TEXT);";
        database.execSQL(ddl);
    }

    public void onUpgrade(SQLiteDatabase database, int versaoAntiga, int versaoNova) {
        ddl = "ALTER TABLE " + TABELA + " ADD COLUMN photoPath TEXT";
        //ddl = "DROP TABLE IF EXISTS " + TABELA;
        database.execSQL(ddl);

        //onCreate(database);
    }

    public void insere(Aluno aluno) {
        ContentValues values = new ContentValues();

        values.put("nome", aluno.getNome());
        values.put("telefone", aluno.getTelefone());
        values.put("email", aluno.getEmail());
        values.put("nota", aluno.getNota());
        values.put("photoPath", aluno.getPhotoPath());

        getWritableDatabase().insert(TABELA, null, values);
    }

    public List<Aluno> getLista() {
        ArrayList<Aluno> alunos = new ArrayList<Aluno>();

        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABELA + ";", null);
        c.moveToFirst();

        while(c.moveToNext()) {
            Aluno aluno = new Aluno();

            aluno.setId(c.getLong(c.getColumnIndex("id")));
            aluno.setNome(c.getString(c.getColumnIndex("nome")));
            aluno.setEmail(c.getString(c.getColumnIndex("email")));
            aluno.setTelefone(c.getString(c.getColumnIndex("telefone")));
            aluno.setNota(c.getDouble(c.getColumnIndex("nota")));
            aluno.setPhotoPath(c.getString(c.getColumnIndex("photoPath")));

            alunos.add(aluno);
        }

        c.close();
        return alunos;
    }

    public void deletar(Aluno alunoSelecionado) {
        String[] args = {alunoSelecionado.getId().toString()};

        getWritableDatabase().delete(TABELA, "id = ?", args);

        File file = new File(alunoSelecionado.getPhotoPath());
        file.delete();
    }

    public Aluno getAluno(Aluno alunoSelecionado) {
        String[] args = { alunoSelecionado.getId().toString() };

        Cursor c = getWritableDatabase().rawQuery("SELECT * FROM " + TABELA + " WHERE id=?", args);
        c.moveToFirst();

        Aluno aluno = new Aluno();

        aluno.setId(c.getLong(c.getColumnIndex("id")));
        aluno.setNome(c.getString(c.getColumnIndex("nome")));
        aluno.setTelefone(c.getString(c.getColumnIndex("telefone")));
        aluno.setEmail(c.getString(c.getColumnIndex("email")));
        aluno.setNota(c.getDouble(c.getColumnIndex("nota")));
        aluno.setPhotoPath(c.getString(c.getColumnIndex("photoPath")));

        c.close();
        return aluno;
    }

    public void altera(Aluno aluno) {
        String[] args = { aluno.getId().toString() };

        ContentValues values = new ContentValues();

        values.put("nome", aluno.getNome());
        values.put("telefone", aluno.getTelefone());
        values.put("email", aluno.getEmail());
        values.put("nota", aluno.getNota());
        values.put("photoPath", aluno.getPhotoPath());

        getWritableDatabase().update(TABELA, values,"id=?", args);
    }

    public boolean existeAluno(String telefone) {
        String[] args = { telefone };
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABELA + " WHERE telefone = ?", args);

        return (c.getCount() > 0);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            return getLista().stream().filter((aluno) -> telefone == aluno.getTelefone()).findFirst().size() > 0;
//        }
//        return  true;
    }
}
