package br.com.caelum.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import br.com.caelum.model.Aluno;
import br.com.caleum.agenda.R;

public class FormularioHelper {
    private Activity _activity;
    private FloatingActionButton btAddPhoto;
    private EditText editNome, editEmail, editTelefone;
    private ImageView contactPhoto;
    private RatingBar ratingNota;
    private Aluno aluno;

    public FormularioHelper(Activity activity) {
        _activity = activity;
        aluno = new Aluno();

        editNome = _activity.findViewById(R.id.nome);
        editEmail = _activity.findViewById(R.id.email);
        editTelefone = _activity.findViewById(R.id.telefone);
        ratingNota = _activity.findViewById(R.id.nota);
        btAddPhoto = _activity.findViewById(R.id.btAddPhoto);
        contactPhoto = _activity.findViewById(R.id.contactPhoto);
    }

    public Aluno getAluno() {
        aluno.setNome(editNome.getText().toString());
        aluno.setEmail(editEmail.getText().toString());
        aluno.setTelefone(editTelefone.getText().toString());
        aluno.setNota(Double.valueOf(ratingNota.getRating()));
        aluno.setPhotoPath((String)contactPhoto.getTag());

        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
        editNome.setText(aluno.getNome());
        editEmail.setText(aluno.getEmail());
        editTelefone.setText(aluno.getTelefone());
        ratingNota.setRating((float)aluno.getNota().doubleValue());
        if (aluno.getPhotoPath() != null ){
            loadPhoto(aluno.getPhotoPath());
        }
    }

    public boolean validateInputs() {
        return !editNome.getText().toString().isEmpty();
    }

    public void showErrorMessage() {
        editNome.setError("Nome deve ser preenchido!");
    }

    public FloatingActionButton getPhotoButton () {
        return btAddPhoto;
    }

    public void loadPhoto(String photoPath) {
        Bitmap bm = BitmapFactory.decodeFile(photoPath);
        Bitmap imagePhoto = Bitmap.createScaledBitmap(bm, 300, 300, true);
        contactPhoto.setImageBitmap(imagePhoto);
        contactPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
        contactPhoto.setTag(photoPath);
    }
}
