package models.notificacao;
import java.time.LocalDateTime;

public class Notificacao {
    private final int id;
    private static int contador = 0;
    private final TipoNotificacao tipo;
    private final String mensagem;
    private final LocalDateTime data; 


    public Notificacao(TipoNotificacao tipo, String mensagem){
        this.tipo = tipo;
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
        return String.format("Id: %d | Tipo: %s | Mensagem: %s | Data: %s.", this.id, this.tipo, this.mensagem, this.data);
    }
}
