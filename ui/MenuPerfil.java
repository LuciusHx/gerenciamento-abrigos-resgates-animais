package ui;

import excecao.AbrigoException;
import models.usuario.Usuario;

//cada perfil funciona diferente e a main vai saber quem eh quem pelo getPerfil() do user logado
public interface MenuPerfil {
    void executar(int opcao, Usuario usuario) throws AbrigoException;
}