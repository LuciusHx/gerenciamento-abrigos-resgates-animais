package servicos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import excecao.AnimalIndisponivelException;
import excecao.DadoInvalidoException;
import excecao.PermissaoNegadaException;
import excecao.RegistroNaoEncontradoException;
import excecao.TratamentoCriticoException;

import models.adocao.Adocao;
import models.adocao.Adotante;
import models.adocao.SolicitacaoAdocao;
import models.adocao.StatusSolicitacao;
import models.animal.Animal;
import models.notificacao.TipoNotificacao;
import models.usuario.Administrador;
import models.usuario.Usuario;

public class AdocaoServico {
    private final List<SolicitacaoAdocao> solicitacoes = new ArrayList<>();
    private final List<Adocao> adocoes = new ArrayList<>();
    private final NotificacaoServico notificacaoServico;

    public AdocaoServico(NotificacaoServico notificacaoServico) {
        if (notificacaoServico == null) {
            throw new DadoInvalidoException("O serviço de notificações é obrigatório.");
        }
        this.notificacaoServico = notificacaoServico;
    }

    public SolicitacaoAdocao solicitar(Animal animal, Adotante adotante) throws AnimalIndisponivelException {
        if (animal == null || adotante == null) {
            throw new DadoInvalidoException("Animal e adotante são obrigatórios.");
        }

        if (!animal.isDisponivel()) {
            throw new AnimalIndisponivelException(
                    "O animal " + animal.getNome() + " não está disponível para adoção.");
        }

        for (SolicitacaoAdocao s : solicitacoes) {
            if (s.getAnimal() == animal && s.getAdotante() == adotante && estaEmAndamento(s)) {
                throw new DadoInvalidoException(
                        adotante.getNome() + " já tem uma solicitação em andamento para " + animal.getNome() + ".");
            }
        }

        SolicitacaoAdocao solicitacao = new SolicitacaoAdocao(animal, adotante);
        solicitacoes.add(solicitacao);

        avisar(solicitacao, "Recebemos sua solicitação para adotar " + animal.getNome() + ".");
        return solicitacao;
    }

    public void aprovar(Usuario executor, int idSolicitacao)
            throws PermissaoNegadaException, RegistroNaoEncontradoException, AnimalIndisponivelException {

        exigirPermissao(executor);
        SolicitacaoAdocao solicitacao = buscarSolicitacaoPorId(idSolicitacao);
        exigirDisponivel(solicitacao.getAnimal());

        //uma solicitacao aprovada por animal de cada vez
        for (SolicitacaoAdocao s : solicitacoes) {
            if (s != solicitacao && s.getAnimal() == solicitacao.getAnimal()
                    && s.getStatus() == StatusSolicitacao.APROVADA) {
                throw new DadoInvalidoException(
                        "Já existe uma solicitação aprovada para " + solicitacao.getAnimal().getNome() + ".");
            }
        }

        solicitacao.aprovar();
        avisar(solicitacao, "Sua solicitação para adotar " + solicitacao.getAnimal().getNome() + " foi aprovada!");
    }

    public void rejeitar(Usuario executor, int idSolicitacao, String motivo)
            throws PermissaoNegadaException, RegistroNaoEncontradoException {

        exigirPermissao(executor);
        SolicitacaoAdocao solicitacao = buscarSolicitacaoPorId(idSolicitacao);

        solicitacao.rejeitar(motivo);
        avisar(solicitacao, "Sua solicitação para adotar " + solicitacao.getAnimal().getNome()
                + " foi rejeitada. Motivo: " + motivo);
    }

    public Adocao concluir(Usuario executor, int idSolicitacao)
            throws PermissaoNegadaException, RegistroNaoEncontradoException, AnimalIndisponivelException {

        exigirPermissao(executor);
        SolicitacaoAdocao solicitacao = buscarSolicitacaoPorId(idSolicitacao);
        Animal animal = solicitacao.getAnimal();

        exigirDisponivel(animal);

        // valida se a transicao ta aprovado, dai passa
        solicitacao.concluir();
        animal.marcarComoAdotado();

        // aqui to fazendo um cast, o executor vai virar adm pq ele tem o metodo la que deixa ele aprovar uma adocao e retorna true
        Adocao adocao = new Adocao(animal, solicitacao.getAdotante(), (Administrador) executor, LocalDate.now());
        adocoes.add(adocao);

        avisar(solicitacao, "Adoção concluída! " + animal.getNome() + " agora é da sua família.");
        
        // se tiver mais solicitacao do mesmo animal eh rejeitado eh tudo cuida
        //importante que so esse tratamento aqui
        for (SolicitacaoAdocao s : solicitacoes) {
            if (s.getAnimal() == animal && s.isPendente()) {
                String motivo = animal.getNome() + " foi adotado por outra pessoa.";
                s.rejeitar(motivo);
                avisar(s, "Sua solicitação para adotar " + animal.getNome() + " foi rejeitada. Motivo: " + motivo);
            }
        }

        return adocao;
    }

    //na devoluçao eh mais cabuloso
    public void registrarDevolucao(Usuario executor, int idAdocao, String motivo)
            throws PermissaoNegadaException, RegistroNaoEncontradoException, TratamentoCriticoException {

        exigirPermissao(executor);
        Adocao adocao = buscarAdocaoPorId(idAdocao);

        //adocao continua na lista e so ganha os dados da devolucao        
        adocao.registrarDevolucao(LocalDate.now(), motivo);

        Animal animal = adocao.getAnimal();
        notificacaoServico.notificar(adocao.getAdotante(), TipoNotificacao.ADOCAO_ATUALIZADA,
                "A devolução de " + animal.getNome() + " foi registrada.");

        animal.liberarParaAdocao();
    }

    public SolicitacaoAdocao buscarSolicitacaoPorId(int id) throws RegistroNaoEncontradoException {
        for (SolicitacaoAdocao s : solicitacoes) {
            if (s.getId() == id) {
                return s;
            }
        }
        throw new RegistroNaoEncontradoException("Solicitação não encontrada.");
    }

    public Adocao buscarAdocaoPorId(int id) throws RegistroNaoEncontradoException {
        for (Adocao a : adocoes) {
            if (a.getId() == id) {
                return a;
            }
        }
        throw new RegistroNaoEncontradoException("Adoção não encontrada.");
    }

    //vai retornar os canditados pendentes ou aprovados do animal
    public List<SolicitacaoAdocao> listarCandidatos(Animal animal) {
        if (animal == null) {
            throw new DadoInvalidoException("Animal inválido.");
        }
        List<SolicitacaoAdocao> candidatos = new ArrayList<>();
        for (SolicitacaoAdocao s : solicitacoes) {
            if (s.getAnimal() == animal && estaEmAndamento(s)) {
                candidatos.add(s);
            }
        }
        return Collections.unmodifiableList(candidatos);
    }

    public List<SolicitacaoAdocao> listarPendentes() {
        List<SolicitacaoAdocao> pendentes = new ArrayList<>();
        for (SolicitacaoAdocao s : solicitacoes) {
            if (s.isPendente()) {
                pendentes.add(s);
            }
        }
        return Collections.unmodifiableList(pendentes);
    }

    public List<SolicitacaoAdocao> listarPorAdotante(Adotante adotante) {
        List<SolicitacaoAdocao> resultado = new ArrayList<>();
        for (SolicitacaoAdocao s : solicitacoes) {
            if (s.getAdotante() == adotante) {
                resultado.add(s);
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    public List<SolicitacaoAdocao> getSolicitacoes() {
        return Collections.unmodifiableList(solicitacoes);
    }

    public List<Adocao> getAdocoes() {
        return Collections.unmodifiableList(adocoes);
    }

    // funcoes auxiliares ---------
    private void exigirPermissao(Usuario executor) throws PermissaoNegadaException {
        if (executor == null) {
            throw new DadoInvalidoException("Executor inválido.");
        }
        if (!executor.podeAprovarAdocao()) {
            throw new PermissaoNegadaException("Apenas administradores podem decidir sobre adoções.");
        }
    }

    private void exigirDisponivel(Animal animal) throws AnimalIndisponivelException {
        if (!animal.isDisponivel()) {
            throw new AnimalIndisponivelException(
                    "O animal " + animal.getNome() + " não está mais disponível para adoção.");
        }
    }

    private boolean estaEmAndamento(SolicitacaoAdocao s) {
        return s.getStatus() == StatusSolicitacao.PENDENTE || s.getStatus() == StatusSolicitacao.APROVADA;
    }

    private void avisar(SolicitacaoAdocao solicitacao, String mensagem) {
        notificacaoServico.notificar(solicitacao.getAdotante(), TipoNotificacao.ADOCAO_ATUALIZADA, mensagem);
    }
}