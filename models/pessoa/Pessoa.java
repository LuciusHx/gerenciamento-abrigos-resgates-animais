package models.pessoa;

public abstract class Pessoa {
    private String nome;
    private final String cpf;
    private String email;
    private String telefone;

    public Pessoa(String nome, String cpf, String email, String telefone){
        // if(nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || telefone.isEmpty()){
            // fazer quando o bern fizer o tratamento de DadoInvalido
        // }
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return this.cpf;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
