package models.adocao;

import java.time.LocalDate;

import excecao.DadoInvalidoException;
import models.animal.Animal;
import util.Validacoes;

public class SolicitacaoAdocao {
    private static int contador = 0;

    private final int id;
    private final Animal animal;
    private final Adotante adotante;
    private final LocalDate data;
    private StatusSolicitacao status;
    private String motivo;

    public SolicitacaoAdocao(Animal animal, Adotante adotante) {
        if (animal == null) {
            throw new DadoInvalidoException("A solicitação precisa de um animal.");
        }
        if (adotante == null) {
            throw new DadoInvalidoException("A solicitação precisa de um adotante.");
        }

        this.id = ++contador;
        this.animal = animal;
        this.adotante = adotante;
        this.data = LocalDate.now();
        this.status = StatusSolicitacao.PENDENTE;
    }

    public void aprovar() {
        exigirStatus(StatusSolicitacao.PENDENTE, "aprovar");
        this.status = StatusSolicitacao.APROVADA;
    }

    public void rejeitar(String motivo) {
        exigirStatus(StatusSolicitacao.PENDENTE, "rejeitar");
        Validacoes.validarString(motivo, "motivo da rejeição");
        this.motivo = motivo;
        this.status = StatusSolicitacao.REJEITADA;
    }

    public void concluir() {
        exigirStatus(StatusSolicitacao.APROVADA, "concluir");
        this.status = StatusSolicitacao.CONCLUIDA;
    }

    //confere se a transicao eh permitida a partir do status atual
    private void exigirStatus(StatusSolicitacao esperado, String acao) {
        if (this.status != esperado) {
            throw new DadoInvalidoException(String.format(
                    "Não é possível %s a solicitação #%d: o status atual é %s.", acao, id, status));
        }
    }

    public boolean isPendente() {
        return status == StatusSolicitacao.PENDENTE;
    }

    public int getId() {
        return id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public Adotante getAdotante() {
        return adotante;
    }

    public LocalDate getData() {
        return data;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public String getMotivo() {
        return motivo;
    }

    @Override
    public String toString() {
        String texto = String.format("#%d | %s -> %s | %s | %s",
                id, adotante.getNome(), animal.getNome(), data, status);
        if (motivo != null) {
            texto += " | Motivo: " + motivo;
        }
        return texto;
    }
}