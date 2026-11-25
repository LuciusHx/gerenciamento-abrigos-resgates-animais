package models.atividade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import contrato.Agendavel;
import excecao.DadoInvalidoException;
import models.usuario.Voluntario;
import util.Validacoes;

public abstract class Atividade implements Agendavel {
    private static int contador = 0;

    private final int id;
    private String titulo;
    private LocalDate data;
    private String local;
    private final int vagas;
    private final List<Voluntario> inscritos = new ArrayList<>();
    private boolean concluida = false;

    protected Atividade(String titulo, LocalDate data, String local, int vagas) {
        Validacoes.validarString(titulo, "título");
        Validacoes.validarString(local, "local");
        if (data == null) {
            throw new DadoInvalidoException("A data da atividade é obrigatória.");
        }
        if (vagas <= 0) {
            throw new DadoInvalidoException("A atividade precisa ter pelo menos uma vaga.");
        }

        this.id = ++contador;
        this.titulo = titulo;
        this.data = data;
        this.local = local;
        this.vagas = vagas;
    }

    public abstract String getTipo();

    public abstract String getDetalhes();

    public boolean temVaga() {
        return inscritos.size() < vagas;
    }

    public int getVagasRestantes() {
        return vagas - inscritos.size();
    }

    public boolean estaInscrito(Voluntario voluntario) {
        return inscritos.contains(voluntario);
    }

    public void adicionarInscrito(Voluntario voluntario) {
        if (voluntario == null) {
            throw new DadoInvalidoException("Voluntário inválido.");
        }
        if (concluida) {
            throw new DadoInvalidoException("A atividade " + titulo + " já foi concluída.");
        }
        if (estaInscrito(voluntario)) {
            throw new DadoInvalidoException(voluntario.getNome() + " já está inscrito em " + titulo + ".");
        }
        if (!temVaga()) {
            throw new DadoInvalidoException("A atividade " + titulo + " não tem mais vagas.");
        }
        inscritos.add(voluntario);
    }

    public boolean isFutura() {
        return !data.isBefore(LocalDate.now());
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void marcarComoConcluida() {
        if (concluida) {
            throw new DadoInvalidoException("A atividade " + titulo + " já foi concluída.");
        }
        this.concluida = true;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        Validacoes.validarString(titulo, "título");
        this.titulo = titulo;
    }

    @Override
    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        if (data == null) {
            throw new DadoInvalidoException("A data da atividade é obrigatória.");
        }
        this.data = data;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        Validacoes.validarString(local, "local");
        this.local = local;
    }

    public int getVagas() {
        return vagas;
    }

    public List<Voluntario> getInscritos() {
        return Collections.unmodifiableList(inscritos);
    }

    @Override
    public String toString() {
        return String.format("#%d [%s] %s | %s | %s | Vagas: %d/%d | %s",
                id, getTipo(), titulo, data, local, inscritos.size(), vagas, getDetalhes());
    }
}