package util;

import excecao.DadoInvalidoException;

public final class Validacoes {
    public static void validarString(String s, String nomeDoCampo){
        //trim é pra tirar os espaços
        if(s == null || s.trim().isEmpty()){
            throw new DadoInvalidoException(String.format("O dado %s é inválido.", nomeDoCampo));
        }
    }
}
