package models.adocao;

import java.util.ArrayList;
import java.util.List;

import contrato.Notificavel;
import models.notificacao.Notificacao;
import models.pessoa.Pessoa;

public class Adotante extends Pessoa implements Notificavel {
    private String endereco;
    private final List<Notificacao> caixaDeEntrada = new ArrayList<>();

    public Adotante(String nome, String cpf, String email, String telefone, String endereco){
        super(nome, cpf, email, telefone);
        validarString(endereco, "endereço");
        this.endereco = endereco;
    }

    @Override
    public void receberNotificacao(Notificacao n){
        caixaDeEntrada.add(n);
    }
}
