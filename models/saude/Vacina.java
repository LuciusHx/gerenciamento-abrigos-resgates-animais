package models.saude;

import java.time.LocalDate;
import contrato.Agendavel;
import util.Validacoes;
import models.usuario.Veterinario;
import excecao.DadoInvalidoException;
public class Vacina extends RegistroMedico implements Agendavel{
    private final String nome;
    private final LocalDate proximaDose;

    public Vacina(LocalDate data, String descricao, Veterinario veterinario, String nome, LocalDate proximaDose){
        super(data,descricao,veterinario);

        Validacoes.validarString(nome,"nome");

        if(proximaDose == null){
            throw new DadoInvalidoException("A data da próxima dose é obrigatória.");
        }
        if(!proximaDose.isAfter(data)){
            throw new DadoInvalidoException("A data da próxima dose deve ser depois da data de aplicação");
        }

        this.nome = nome;
        this.proximaDose = proximaDose;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getProximaDose() {
        return proximaDose;
    }

    @Override
    public LocalDate getData() {
        return proximaDose;
    }

    @Override
    public String resumo() {
        return "Vacina: " + nome + " | Aplicada em: " + formatar(getDataRegistro())
            + " | Próxima dose: " + formatar(proximaDose)
            + " | Veterinário: " + getVeterinario().getNome();
    }
}
