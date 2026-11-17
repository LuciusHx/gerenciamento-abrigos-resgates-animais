package models.adocao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import contrato.Notificavel;
import models.notificacao.Notificacao;
import models.pessoa.Pessoa;
import util.Validacoes;

public class Adotante extends Pessoa implements Notificavel {
    private String endereco;
    private final List<Notificacao> caixaDeEntrada = new ArrayList<>();

    public Adotante(String nome, String cpf, String email, String telefone, String endereco){
        super(nome, cpf, email, telefone);
        Validacoes.validarString(endereco, "endereço");
        this.endereco = endereco;
    }

    @Override
    public void receberNotificacao(Notificacao n){
        caixaDeEntrada.add(n);
    }

    public String getEndereco(){
        return this.endereco;
    }

    public void setEndereco(String endereco){
        Validacoes.validarString(endereco, "endereço");
        this.endereco = endereco;
    }

    public List<Notificacao> getNotificacoes(){
        List<Notificacao> caixaDeEntradaImutavel = Collections.unmodifiableList(caixaDeEntrada);
        return caixaDeEntradaImutavel;
    }
}
