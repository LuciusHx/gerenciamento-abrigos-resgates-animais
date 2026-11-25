package models.saude;

import java.time.LocalDate;
import util.*;
import models.usuario.Veterinario;

public class Consulta extends RegistroMedico{
    private final String diagnostico;

    public Consulta(LocalDate data, String descricao, Veterinario veterinario, String diagnostico){
        super(data, descricao, veterinario);
        Validacoes.validarString(diagnostico, "diagnóstico");

        this.diagnostico = diagnostico;
    }

    public String getDiagnostico(){
        return diagnostico;
    }

    @Override
    public String resumo(){
        return "Data da consulta: " + getDataRegistro() + "| Nome veterinário responsável: " + getVeterinario().getNome() + "| Diagnóstico: " +  getDiagnostico() + "|";
    }


}
