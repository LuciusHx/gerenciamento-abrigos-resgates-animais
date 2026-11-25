package servicos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import excecao.AtividadeLotadaException;
import excecao.DadoInvalidoException;
import excecao.InscricaoDuplicadaException;
import excecao.PermissaoNegadaException;
import excecao.RegistroNaoEncontradoException;
import models.atividade.Atividade;
import models.notificacao.TipoNotificacao;
import models.pessoa.Perfil;
import models.usuario.Usuario;
import models.usuario.Voluntario;

public class AtividadeServico {
    private final List<Atividade> atividades = new ArrayList<>();
    private final NotificacaoServico notificacaoServico;

    public AtividadeServico(NotificacaoServico notificacaoServico) {
        if (notificacaoServico == null) {
            throw new DadoInvalidoException("O serviço de notificações é obrigatório.");
        }
        this.notificacaoServico = notificacaoServico;
    }

    
    //so o adm pode fazer essas parada aqui
    public void criar(Usuario executor, Atividade atividade) throws PermissaoNegadaException {
        exigirPermissao(executor);

        if (atividade == null) {
            throw new DadoInvalidoException("Atividade inválida.");
        }
        if (!atividade.isFutura()) {
            throw new DadoInvalidoException("Não é possível criar uma atividade com data no passado.");
        }
        if (atividades.contains(atividade)) {
            throw new DadoInvalidoException("Essa atividade já foi cadastrada.");
        }

        atividades.add(atividade);

        notificacaoServico.notificarTodos(TipoNotificacao.AVISO_GERAL,
                String.format("Nova atividade: %s - %s em %s (%s)",
                        atividade.getTipo(), atividade.getTitulo(), atividade.getData(), atividade.getLocal()));
    }

    //so voluntario aqui
    public void inscrever(Usuario executor, int idAtividade) throws PermissaoNegadaException,
            RegistroNaoEncontradoException, AtividadeLotadaException, InscricaoDuplicadaException {

        if (executor == null) {
            throw new DadoInvalidoException("Executor inválido.");
        }
        if (executor.getPerfil() != Perfil.VOLUNTARIO) {
            throw new PermissaoNegadaException("Apenas voluntários podem se inscrever em atividades.");
        }
        Voluntario voluntario = (Voluntario) executor;

        Atividade atividade = buscarPorId(idAtividade);

        if (atividade.isConcluida() || !atividade.isFutura()) {
            throw new DadoInvalidoException("A atividade " + atividade.getTitulo() + " não está mais aberta para inscrições.");
        }
        if (atividade.estaInscrito(voluntario)) {
            throw new InscricaoDuplicadaException(
                    voluntario.getNome() + " já está inscrito em " + atividade.getTitulo() + ".");
        }
        if (!atividade.temVaga()) {
            throw new AtividadeLotadaException(
                    "A atividade " + atividade.getTitulo() + " está lotada (" + atividade.getVagas() + " vagas).");
        }

        atividade.adicionarInscrito(voluntario);
    }


    //so adm consegue concluir uma ativade
    public void concluir(Usuario executor, int idAtividade)
            throws PermissaoNegadaException, RegistroNaoEncontradoException {

        exigirPermissao(executor);
        Atividade atividade = buscarPorId(idAtividade);

        if (atividade.getData().isAfter(LocalDate.now())) {
            throw new DadoInvalidoException("A atividade " + atividade.getTitulo() + " ainda não aconteceu.");
        }

        atividade.marcarComoConcluida();

        for (Voluntario voluntario : atividade.getInscritos()) {
            voluntario.registrarParticipacao(atividade);
        }
    }

    public Atividade buscarPorId(int id) throws RegistroNaoEncontradoException {
        for (Atividade a : atividades) {
            if (a.getId() == id) {
                return a;
            }
        }
        throw new RegistroNaoEncontradoException("Atividade não encontrada.");
    }

    public List<Atividade> listarDisponiveis() {
        List<Atividade> disponiveis = new ArrayList<>();
        for (Atividade a : atividades) {
            if (a.isFutura() && !a.isConcluida() && a.temVaga()) {
                disponiveis.add(a);
            }
        }
        return Collections.unmodifiableList(disponiveis);
    }
    //voluntario ta escrito mas nao foi concluida ainda
    public List<Atividade> listarInscricoes(Voluntario voluntario) {
        List<Atividade> resultado = new ArrayList<>();
        for (Atividade a : atividades) {
            if (!a.isConcluida() && a.estaInscrito(voluntario)) {
                resultado.add(a);
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    public List<Atividade> getAtividades() {
        return Collections.unmodifiableList(atividades);
    }

    private void exigirPermissao(Usuario executor) throws PermissaoNegadaException {
        if (executor == null) {
            throw new DadoInvalidoException("Executor inválido.");
        }
        if (!executor.podeGerenciarAtividades()) {
            throw new PermissaoNegadaException("Apenas administradores podem gerenciar atividades.");
        }
    }
}