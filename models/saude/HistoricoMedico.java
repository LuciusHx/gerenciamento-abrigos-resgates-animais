package models.saude;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import excecao.DadoInvalidoException;
import excecao.VacinaDuplicadaException;

public class HistoricoMedico {
    private final List<RegistroMedico> registros = new ArrayList<>();
    private final List<Vacina> vacinas = new ArrayList<>();
    private final List<Medicamento> medicamentos = new ArrayList<>();
    private final List<Cirurgia> cirurgias = new ArrayList<>();

    public void registrarConsulta(Consulta consulta) {
        if (consulta == null) {
            throw new DadoInvalidoException("A consulta não pode ser vazia.");
        }
        registros.add(consulta);
    }

    // regra 2: mesma vacina na mesma data de aplicação não entra duas vezes
    public void registrarVacina(Vacina vacina) throws VacinaDuplicadaException {
        if (vacina == null) {
            throw new DadoInvalidoException("A vacina não pode ser vazia.");
        }
        for (Vacina existente : vacinas) {
            boolean mesmoNome = existente.getNome().equalsIgnoreCase(vacina.getNome());
            boolean mesmaData = existente.getDataRegistro().equals(vacina.getDataRegistro());
            if (mesmoNome && mesmaData) {
                throw new VacinaDuplicadaException("A vacina " + vacina.getNome()
                        + " já foi registrada em " + vacina.getDataRegistro() + ".");
            }
        }
        vacinas.add(vacina);
        registros.add(vacina);
    }

    public void registrarMedicamento(Medicamento medicamento) {
        if (medicamento == null) {
            throw new DadoInvalidoException("O medicamento não pode ser vazio.");
        }
        medicamentos.add(medicamento);
        registros.add(medicamento);
    }

    public void registrarCirurgia(Cirurgia cirurgia) {
        if (cirurgia == null) {
            throw new DadoInvalidoException("A cirurgia não pode ser vazia.");
        }
        cirurgias.add(cirurgia);
        registros.add(cirurgia);
    }

    public List<RegistroMedico> getRegistros() {
        return Collections.unmodifiableList(registros);
    }

    public List<Vacina> getVacinas() {
        return Collections.unmodifiableList(vacinas);
    }

    public List<Medicamento> getMedicamentosAtivos() {
        List<Medicamento> ativos = new ArrayList<>();
        for (Medicamento medicamento : medicamentos) {
            if (medicamento.isAtivo()) {
                ativos.add(medicamento);
            }
        }
        return Collections.unmodifiableList(ativos);
    }

    public List<Cirurgia> getCirurgias() {
        return Collections.unmodifiableList(cirurgias);
    }
}