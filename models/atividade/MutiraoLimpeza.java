package models.atividade;

import java.time.LocalDate;

public class MutiraoLimpeza extends Atividade {

    public MutiraoLimpeza(String titulo, LocalDate data, String local, int vagas) {
        super(titulo, data, local, vagas);
    }

    @Override
    public String getTipo() {
        return "Mutirão de Limpeza";
    }

    @Override
    public String getDetalhes() {
        return "Limpeza e organização das instalações do abrigo";
    }
}