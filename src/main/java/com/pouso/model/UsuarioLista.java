package com.pouso.model;

public class UsuarioLista {

    private String cpf;
    private String nome;
    private String email;
    private String cidade;

    public UsuarioLista(String cpf, String nome, String email, String cidade) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.cidade = cidade;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCidade() {
        return cidade;
    }
}
