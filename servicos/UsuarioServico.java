package servicos;

import models.pessoa.Perfil;
import models.usuario.Usuario;
import models.usuario.Voluntario;
import util.Validacoes;
import util.ValidadorCpf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import excecao.CredenciaisInvalidasException;
import excecao.DadoInvalidoException;
import excecao.PermissaoNegadaException;
import excecao.RegistroNaoEncontradoException;

public class UsuarioServico {
    private final List<Usuario> usuarios = new ArrayList<>();
    private final NotificacaoServico notificacaoServico;

    public UsuarioServico(NotificacaoServico notificacaoServico) {
        this.notificacaoServico = notificacaoServico;
    }

    public Usuario buscarPorId(int id) throws RegistroNaoEncontradoException {
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) {
                return usuario;
            }
        }
        throw new RegistroNaoEncontradoException("Usuário não encontrado.");
    }

    public Usuario buscarPorLogin(String login) throws RegistroNaoEncontradoException {
        for (Usuario usuario : usuarios) {
            if (usuario.getLogin().equals(login)) {
                return usuario;
            }
        }
        throw new RegistroNaoEncontradoException("Usuário não encontrado.");
    }

    private boolean existeCpf(Usuario usuario) {
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(usuario.getCpf())) {
                return true;
            }
        }
        return false;
    }

    private boolean existeLogin(Usuario usuario) {
        for (Usuario u : usuarios) {
            if (u.getLogin().equals(usuario.getLogin())) {
                return true;
            }
        }
        return false;

    }

    public Usuario autenticar(String login, String senha) throws CredenciaisInvalidasException {

        try {
            Usuario usuario = buscarPorLogin(login);

            if (usuario.autenticar(senha)) {
                return usuario;
            }
            throw new CredenciaisInvalidasException("Login ou senha inválidos.");
        } catch (RegistroNaoEncontradoException e) {
            throw new CredenciaisInvalidasException("Login ou senha inválidos.");
        }
    }

    public void cadastrar(Usuario usuario) {

        if (usuario == null) {
            throw new DadoInvalidoException("Usuário inválido.");
        }
        if (!ValidadorCpf.eValido(usuario.getCpf())) {
            throw new DadoInvalidoException("O CPF é inválido.");
        }

        if (existeCpf(usuario)) {
            throw new DadoInvalidoException("Já existe um usuário com esse cpf.");
        }

        if (existeLogin(usuario)) {
            throw new DadoInvalidoException("Já existe um usuário com esse login.");
        }

        usuarios.add(usuario);
        notificacaoServico.inscrever(usuario);
    }

    public void remover(Usuario executor, int id) throws PermissaoNegadaException, RegistroNaoEncontradoException {

        if (executor == null) {
            throw new DadoInvalidoException("Executor inválido.");
        }

        if (!executor.podeGerenciarUsuarios()) {
            throw new PermissaoNegadaException("Apenas administradores podem remover usuários");
        }

        Usuario usuario = buscarPorId(id);

        if (usuario == executor) {
            throw new PermissaoNegadaException("Não é possível remover você mesmo.");
        }
        usuarios.remove(usuario);
        notificacaoServico.desinscrever(usuario);

    }

    public void editar(Usuario executor, int id, String nome, String email, String telefone)
            throws PermissaoNegadaException, RegistroNaoEncontradoException {

        if (executor == null) {
            throw new DadoInvalidoException("Executor inválido.");
        }

        Usuario usuario = buscarPorId(id);

        if (!executor.podeGerenciarUsuarios() && usuario != executor) {
            throw new PermissaoNegadaException("Você só pode editar os seus próprios dados.");
        }

        if (nome != null) {
            Validacoes.validarString(nome, "nome");
        }
        if (email != null) {
            Validacoes.validarString(email, "email");
        }
        if (telefone != null) {
            Validacoes.validarString(telefone, "telefone");
        }

        // se for null ele nao edita
        if (nome != null) {
            usuario.setNome(nome);
        }
        if (email != null) {
            usuario.setEmail(email);
        }
        if (telefone != null) {
            usuario.setTelefone(telefone);
        }
    }

    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public List<Usuario> listarPorPerfil(Perfil perfil) {
        if (perfil == null) {
            throw new DadoInvalidoException("Perfil inválido.");
        }

        List<Usuario> resultado = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u.getPerfil() == perfil) {
                resultado.add(u);
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    public List<Voluntario> getVoluntarios() {
        List<Voluntario> voluntarios = new ArrayList<>();
        for (Usuario u : usuarios) {
            
            if (u.getPerfil() == Perfil.VOLUNTARIO) {
                voluntarios.add((Voluntario) u);
            }
        }
        return Collections.unmodifiableList(voluntarios);
    }
}
