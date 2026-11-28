package models.saude;

import java.time.LocalDate;
import util.*;
import models.usuario.Veterinario;

public class Cirurgia extends RegistroMedico{
    private final String procedimento;
    private final String resultado;

    public Cirurgia(LocalDate data, String descricao, Veterinario veterinario, String procedimento, String resultado){
        super(data, descricao, veterinario);

        Validacoes.validarString(procedimento, "procedimento");
        Validacoes.validarString(resultado, "resultado");

        this.procedimento = procedimento;
        this.resultado = resultado;
    }

    public String getProcedimento(){
        return procedimento;
    }

    public String getResultado(){
        return resultado;
    }

    @Override
    public String resumo() {
        return "Cirurgia em " + formatar(getDataRegistro())
            + " | Procedimento: " + getProcedimento()
            + " | Resultado: " + getResultado()
            + " | Veterinário: " + getVeterinario().getNome();
    }
}
