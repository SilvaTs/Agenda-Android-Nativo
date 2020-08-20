package br.com.caelum.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.caelum.agenda.ListaAlunosActivity;
import br.com.caelum.model.Aluno;
import br.com.caleum.agenda.R;

import java.util.List;

public class ListaAlunosAdapter extends BaseAdapter {
    private List<Aluno> alunos;
    private Activity activity;

    public ListaAlunosAdapter(List<Aluno> listAlunos, Activity activity) {
        this.alunos = listAlunos;
        this.activity = activity;
    }

    @Override
    public Object getItem(int position) {
        return alunos.get(position);
    }

    @Override
    public int getCount() {
        return alunos.size();
    }

    @Override
    public long getItemId(int position) {
        return alunos.get(position).getId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Aluno aluno = alunos.get(position);
        Bitmap image, smallerImage;

        LayoutInflater layoutInflater = activity.getLayoutInflater();
//        View view = layoutInflater.inflate(android.R.layout.activity_list_item, parent, false);
        View view = layoutInflater.inflate(R.layout.item, parent, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (position % 2 == 0) {
                view.setBackgroundColor(activity.getResources().getColor(R.color.linhaPar, activity.getTheme()));
            } else {
                view.setBackgroundColor(activity.getResources().getColor(R.color.linhaImpar, activity.getTheme()));
            }
        }

        TextView textView =  view.findViewById(R.id.item_name);
        ImageView imageView = view.findViewById(R.id.item_photo);

        textView.setText(aluno.getNome());

        if(aluno.getPhotoPath() != null) {
            image = BitmapFactory.decodeFile(aluno.getPhotoPath());
        } else {
            image = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_no_image);
        }

        smallerImage = Bitmap.createScaledBitmap(image, 160, 160, true);
        imageView.setImageBitmap(smallerImage);

        return view;
    }
}
