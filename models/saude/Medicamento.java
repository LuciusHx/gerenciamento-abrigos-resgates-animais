package models.saude;

import java.time.LocalDate;
import util.Validacoes;
import models.usuario.Veterinario;
import excecao.DadoInvalidoException;
public class Medicamento extends RegistroMedico {
    private final String nome;
    private final String dose;
    private final LocalDate inicio;
    private final LocalDate fim;
    
    public Medicamento(LocalDate data, String descricao, Veterinario veterinario, String nome, String dose, LocalDate inicio, LocalDate fim){
        super(data, descricao, veterinario);
        Validacoes.validarString(nome, "nome");
        Validacoes.validarString(dose, "dose");

        if(inicio == null){
            throw new DadoInvalidoException("A data de início do medicamento é obrigatória. ");
        }

        if(fim != null && fim.isBefore(inicio)){
            throw new DadoInvalidoException("A data de fim do medicamento não pode ser antes do início.");
        }

        this.nome = nome;
        this.dose = dose;
        this.inicio = inicio;
        this.fim = fim;
    }

    public String getNome(){
        return nome;
    }

    public String getDose(){
        return dose;
    }

    public LocalDate getInicio(){
        return inicio;
    }

    public LocalDate getFim(){
        return fim;
    }

    public boolean isAtivo() {
        return fim == null || !fim.isBefore(LocalDate.now());
    }
    
    @Override
    public String resumo(){
        String periodoFim = (fim == null) ? "em andamento" : fim.toString();
        return "Medicamento: " + nome + "| Dose: " + dose + "| Inicio: " + inicio + "| Fim: " + periodoFim + "| Veterinário: " + getVeterinario().getNome();
    }
}
