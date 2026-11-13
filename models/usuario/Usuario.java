package models.usuario;

import models.notificacao.Notificacao;
import models.pessoa.Pessoa;

import excecao.SenhaIncorretaException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import contrato.Notificavel;

public abstract class Usuario extends Pessoa implements Notificavel{
    private final int id;
    private static int contador = 0;
    private String login;
    private String senha;
    private final List<Notificacao> caixaDeEntrada = new ArrayList<>();

    public Usuario(String nome, String cpf, String email, String telefone, String login, String senha){
        super(nome, cpf, email, telefone);

        this.id = ++contador;
        this.login = login;
        this.senha = senha;

    }

    public int getId(){
        return this.id;
    }

    public String getLogin(){
        return this.login; 
    }

    public List<Notificacao> getNotificacoes(){
        List<Notificacao> caixaDeEntradaImutavel = Collections.unmodifiableList(caixaDeEntrada);
        return caixaDeEntradaImutavel;
    }

    public void setLogin(String login){
        this.login = login;
    }

    public void setSenha(String senhaAtual, String novaSenha) {
        if (this.senha.equals(senhaAtual)) {
            this.senha = novaSenha;
        } else {
            throw new SenhaIncorretaException("Senha incorreta.");
        }
    }

    public boolean autenticar(String senha){
        if(this.senha.equals(senha)){
            return true;
        }
        return false;
    }

    @Override 
    public void receberNotificacao(Notificacao n){
        caixaDeEntrada.add(n);
    }

    public abstract void exibirMenu();

    public boolean podeAprovarAdocao(){ return false;}
    public boolean podeAlterarDadosMedicos(){ return false;}
    public boolean podeGerenciarAnimais(){ return false; }
    public boolean podeGerenciarUsuarios(){ return false; }
}
