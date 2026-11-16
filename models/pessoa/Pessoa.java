package models.pessoa;

import excecao.DadoInvalidoException;

public abstract class Pessoa {
    private String nome;
    private final String cpf;
    private String email;
    private String telefone;

    public Pessoa(String nome, String cpf, String email, String telefone){
        validarString(nome, "nome");
        validarString(cpf, "cpf");
        validarString(email, "email");
        validarString(telefone, "telefone");
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        validarString(nome, "nome");
        this.nome = nome;
    }

    public String getCpf() {
        return this.cpf;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        validarString(email, "email");
        this.email = email;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public void setTelefone(String telefone) {
        validarString(telefone, "telefone");
        this.telefone = telefone;
        
    }

    protected final void validarString(String s, String nomeDoCampo){
        //trim é pra tirar os espaços
        if(s == null || s.trim().isEmpty()){
            throw new DadoInvalidoException(String.format("O dado %s é inválido.", nomeDoCampo));
        }
    }
}
