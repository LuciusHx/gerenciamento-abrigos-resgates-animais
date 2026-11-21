package servicos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import excecao.DadoInvalidoException;

import contrato.Notificavel;
import models.notificacao.Notificacao;
import models.notificacao.TipoNotificacao;

public class NotificacaoServico {
    private final List<Notificavel> inscritos = new ArrayList<>();
    private final List<Notificacao> historico = new ArrayList<>();


    public void inscrever(Notificavel n){
        if(n == null){
            throw new DadoInvalidoException("O dado é nulo e inválido.");
        }

        if(inscritos.contains(n)){
            throw new DadoInvalidoException("Destinatário já está inscrito");
        }
        
        inscritos.add(n);
    }

    public void notificar(Notificavel destino, TipoNotificacao tipo, String mensagem){
        if(destino == null){
            throw new DadoInvalidoException("O destino é inválido");
        }

        Notificacao notificacao = new Notificacao(tipo, mensagem);
        historico.add(notificacao);

        destino.receberNotificacao(notificacao);
        
    }

    public void notificarTodos(TipoNotificacao tipo, String mensagem){
        Notificacao notificacao = new Notificacao(tipo, mensagem);
        historico.add(notificacao);

        for(Notificavel inscrito : inscritos){
            inscrito.receberNotificacao(notificacao);
        }

    }

    public List<Notificacao> getHistorico(){
        List<Notificacao> historicoImutavel = Collections.unmodifiableList(historico);
        return historicoImutavel;
    }
    public List<Notificavel> getInscritos(){
        List<Notificavel> inscritosImutavel = Collections.unmodifiableList(inscritos);
        return inscritosImutavel;
    }
}
