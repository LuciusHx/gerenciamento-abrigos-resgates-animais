package models.saude;


import models.usuario.Veterinario;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import excecao.DadoInvalidoException;
import util.*;


public abstract class RegistroMedico {
    private final LocalDate data;
    private final String descricao;
    private final Veterinario veterinario;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    

    public abstract String resumo();

    protected RegistroMedico(LocalDate data, String descricao, Veterinario veterinario){
        Validacoes.validarString(descricao, "descrição");

        if(data == null || data.isAfter(LocalDate.now())) throw new DadoInvalidoException("A data do registro é obrigatória e não pode estar no futuro.");
        if(veterinario == null) throw new DadoInvalidoException("Veterinário responsável é obrigatório.");

        this.data = data;
        this.descricao = descricao;
        this.veterinario = veterinario;
    }

    protected static String formatar(LocalDate data) {
        return data.format(FORMATO_DATA);
    }

    @Override
    public String toString() {
        return resumo();
    }
    

    public LocalDate getDataRegistro(){
        return data;
    }

    public String getDescricao(){
        return descricao;
    }

    public Veterinario getVeterinario(){
        return veterinario;
    }

}
