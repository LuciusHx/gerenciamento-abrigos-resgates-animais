package excecao;

public class DadoInvalidoException extends RuntimeException {
    public DadoInvalidoException (String mensagem){
        super(mensagem);
    }
}
