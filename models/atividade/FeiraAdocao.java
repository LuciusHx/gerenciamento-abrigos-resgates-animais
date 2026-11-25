package models.atividade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import excecao.DadoInvalidoException;
import models.animal.Animal;

public class FeiraAdocao extends Atividade {
    private final List<Animal> animaisExpostos = new ArrayList<>();

    public FeiraAdocao(String titulo, LocalDate data, String local, int vagas) {
        super(titulo, data, local, vagas);
    }

    // so expoe animal que pode ser adotado
    public void adicionarAnimal(Animal animal) {
        if (animal == null) {
            throw new DadoInvalidoException("Animal inválido.");
        }
        if (!animal.isDisponivel()) {
            throw new DadoInvalidoException("O animal " + animal.getNome() + " não está disponível para adoção.");
        }
        if (animaisExpostos.contains(animal)) {
            throw new DadoInvalidoException("O animal " + animal.getNome() + " já está na feira.");
        }
        animaisExpostos.add(animal);
    }

    public List<Animal> getAnimaisExpostos() {
        return Collections.unmodifiableList(animaisExpostos);
    }

    @Override
    public String getTipo() {
        return "Feira de Adoção";
    }

    @Override
    public String getDetalhes() {
        if (animaisExpostos.isEmpty()) {
            return "Nenhum animal definido ainda";
        }
        List<String> nomes = new ArrayList<>();
        for (Animal a : animaisExpostos) {
            nomes.add(a.getNome());
        }
        return "Animais expostos: " + String.join(", ", nomes);
    }
}