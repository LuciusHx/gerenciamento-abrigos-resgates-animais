package models.atividade;

import java.time.LocalDate;

import util.Validacoes;

public class CampanhaVacinacao extends Atividade {
    private final String vacinaAlvo;

    public CampanhaVacinacao(String titulo, LocalDate data, String local, int vagas, String vacinaAlvo) {
        super(titulo, data, local, vagas);
        Validacoes.validarString(vacinaAlvo, "vacina-alvo");
        this.vacinaAlvo = vacinaAlvo;
    }

    public String getVacinaAlvo() {
        return vacinaAlvo;
    }

    @Override
    public String getTipo() {
        return "Campanha de Vacinação";
    }

    @Override
    public String getDetalhes() {
        return "Vacina-alvo: " + vacinaAlvo;
    }
}