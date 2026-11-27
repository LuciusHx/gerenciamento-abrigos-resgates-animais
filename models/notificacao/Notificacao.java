package models.notificacao;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import excecao.DadoInvalidoException;
import util.Validacoes;

public class Notificacao {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static int contador = 0;

    private final int id;
    private final TipoNotificacao tipo;
    private final String mensagem;
    private final LocalDateTime data; 


    public Notificacao(TipoNotificacao tipo, String mensagem){
        if(tipo == null){
            throw new DadoInvalidoException("O tipo é inválido.");
        }
        this.tipo = tipo;
        Validacoes.validarString(mensagem, "notificação");
        this.mensagem = mensagem;
        this.data = LocalDateTime.now();
    
        this.id = ++contador;
    }

    public TipoNotificacao getTipo(){
        return this.tipo;
    }

    public String getMensagem(){
        return this.mensagem;
    }

    public int getId(){
        return this.id;
    }

    public LocalDateTime getData(){
        return this.data;
    }

    @Override 
    public String toString(){
        return String.format("[%s] #%d %s: %s", data.format(FORMATO), id, tipo, mensagem);
    }
}