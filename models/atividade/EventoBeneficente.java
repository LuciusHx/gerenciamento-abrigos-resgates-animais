package models.atividade;

import java.time.LocalDate;

import excecao.DadoInvalidoException;

public class EventoBeneficente extends Atividade {
    private final double metaArrecadacao;

    public EventoBeneficente(String titulo, LocalDate data, String local, int vagas, double metaArrecadacao) {
        super(titulo, data, local, vagas);
        if (metaArrecadacao <= 0) {
            throw new DadoInvalidoException("A meta de arrecadação precisa ser maior que zero.");
        }   
        this.metaArrecadacao = metaArrecadacao;
    }

    public double getMetaArrecadacao() {
        return metaArrecadacao;
    }

    @Override
    public String getTipo() {
        return "Evento Beneficente";
    }

    @Override
    public String getDetalhes() {
        return String.format("Meta de arrecadação: R$ %.2f", metaArrecadacao);
    }
}