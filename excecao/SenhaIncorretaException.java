package excecao;

public class SenhaIncorretaException extends RuntimeException{
    public SenhaIncorretaException(String m){
        super(m);
    }
}
