package br.com.caelum.support;

import br.com.caelum.Converter.AlunoConverter;
import br.com.caelum.model.Aluno;
import org.json.JSONException;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class WebClient {
    private static final String URL = "https://www.caelum.com.br/mobile";

    public String post(List<Aluno> alunoList) throws IOException, JSONException {
        //Colocar em uma classe e método separado
        URL url = new URL(this.URL);
        HttpURLConnection httpsURLConnection = (HttpURLConnection)url.openConnection();
        httpsURLConnection.setRequestMethod("POST");
        httpsURLConnection.setRequestProperty("Content-type", "application/json");
        httpsURLConnection.setRequestProperty("Accept", "application/json");
        httpsURLConnection.setDoOutput(true);
        httpsURLConnection.setDoInput(true);

        PrintStream printStream = new PrintStream(httpsURLConnection.getOutputStream());
        printStream.println(new AlunoConverter().toJSON(alunoList));

        httpsURLConnection.connect();

        return new Scanner(httpsURLConnection.getInputStream()).next();
    }
}
