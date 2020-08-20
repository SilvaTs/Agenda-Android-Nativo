package br.com.caelum.Converter;

import br.com.caelum.model.Aluno;
import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;

public class AlunoConverter {

    public String toJSON(List<Aluno> alunos) throws JSONException {
        JSONStringer jsonStringer = new JSONStringer();

        try {
            jsonStringer.object().key("list").array().object().key("aluno").array();
            for (Aluno aluno : alunos) {
                jsonStringer.object()
                        .key("id").value(aluno.getId())
                        .key("nome").value(aluno.getNome())
                        .key("telefone").value(aluno.getTelefone())
                        .key("email").value(aluno.getEmail())
                        .key("nota").value(aluno.getNota().toString())
                        .key("photoPath").value(aluno.getPhotoPath())
                        .endObject();
            }

            return jsonStringer.endArray().endObject().endArray().endObject().toString();
        } catch (Exception e) {
            return "";
        }
    }

}
