package models.adocao;

import java.time.LocalDate;

import excecao.DadoInvalidoException;
import models.animal.Animal;
import models.usuario.Administrador;
import util.Validacoes;

public class Adocao {
    private static int contador = 0;

    private final int id;
    private final Animal animal;
    private final Adotante adotante;
    private final Administrador administrador;
    private final LocalDate data;

    private LocalDate dataDevolucao;
    private String motivoDevolucao;

    public Adocao(Animal animal, Adotante adotante, Administrador administrador, LocalDate data) {
        if (animal == null) {
            throw new DadoInvalidoException("A adoção precisa de um animal.");
        }
        if (adotante == null) {
            throw new DadoInvalidoException("A adoção precisa de um adotante.");
        }
        if (administrador == null) {
            throw new DadoInvalidoException("A adoção precisa do administrador responsável.");
        }
        if (data == null || data.isAfter(LocalDate.now())) {
            throw new DadoInvalidoException("A data da adoção é inválida.");
        }

        this.id = ++contador;
        this.animal = animal;
        this.adotante = adotante;
        this.administrador = administrador;
        this.data = data;
    }

    public void registrarDevolucao(LocalDate dataDevolucao, String motivo) {
        if (isDevolvida()) {
            throw new DadoInvalidoException("Esta adoção já teve a devolução registrada.");
        }
        if (dataDevolucao == null) {
            throw new DadoInvalidoException("A data da devolução é obrigatória.");
        }
        if (dataDevolucao.isBefore(this.data)) {
            throw new DadoInvalidoException("A devolução não pode ser anterior à data da adoção.");
        }
        if (dataDevolucao.isAfter(LocalDate.now())) {
            throw new DadoInvalidoException("A data da devolução não pode estar no futuro.");
        }
        Validacoes.validarString(motivo, "motivo da devolução");

        this.dataDevolucao = dataDevolucao;
        this.motivoDevolucao = motivo;
    }

    public boolean isDevolvida() {
        return dataDevolucao != null;
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

    public Administrador getAdministrador() {
        return administrador;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public String getMotivoDevolucao() {
        return motivoDevolucao;
    }

    @Override
    public String toString() {
        String texto = String.format("#%d | %s adotou %s em %s (concluída por %s)",
                id, adotante.getNome(), animal.getNome(), data, administrador.getNome());
        if (isDevolvida()) {
            texto += String.format(" | Devolvido em %s: %s", dataDevolucao, motivoDevolucao);
        }
        return texto;
    }
}