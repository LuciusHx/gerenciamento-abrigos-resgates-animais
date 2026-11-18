package models.pessoa;

import util.Validacoes;

public abstract class Pessoa {
    private String nome;
    private final String cpf;
    private String email;
    private String telefone;

    public Pessoa(String nome, String cpf, String email, String telefone){
        Validacoes.validarString(nome, "nome");
        Validacoes.validarString(cpf, "cpf");
        Validacoes.validarString(email, "email");
        Validacoes.validarString(telefone, "telefone");
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        Validacoes.validarString(nome, "nome");
        this.nome = nome;
    }

    public String getCpf() {
        return this.cpf;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        Validacoes.validarString(email, "email");
        this.email = email;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public void setTelefone(String telefone) {
        Validacoes.validarString(telefone, "telefone");
        this.telefone = telefone;
        
    }

    
}
