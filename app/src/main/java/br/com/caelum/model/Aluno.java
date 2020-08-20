package br.com.caelum.model;

import java.io.Serializable;

public class Aluno implements Serializable {
    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private Double nota;
    private String photoPath;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {

        return id;
    }

    public String getNome() {
        return nome;
    }

    public Double getNota() {
        return nota;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoPath() { return photoPath; }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    @Override
    public String toString() {
        return nome;
    }
}
