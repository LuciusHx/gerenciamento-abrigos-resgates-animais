package servicos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.animal.Animal;
import models.animal.EstadoSaude;
import models.saude.Cirurgia;
import models.saude.Consulta;
import models.saude.Medicamento;
import models.saude.Vacina;
import models.usuario.Usuario;

import excecao.DadoInvalidoException;
import excecao.PermissaoNegadaException;
import excecao.VacinaDuplicadaException;

import util.Constantes;

public class SaudeServico {

    private final AnimalServico animalServico;

    public SaudeServico(AnimalServico animalServico) {
        if (animalServico == null) {
            throw new DadoInvalidoException("O serviço de animais é obrigatório.");
        }

        this.animalServico = animalServico;
    }

    // Verifica se o executor tem permissão médica
    private void exigirPermissao(Usuario executor, Animal animal)
            throws PermissaoNegadaException {

        if (executor == null) {
            throw new DadoInvalidoException("Executor inválido.");
        }

        if (!executor.podeAlterarDadosMedicos()) {
            throw new PermissaoNegadaException(
                "Apenas veterinários podem alterar dados médicos."
            );
        }

        if (animal == null) {
            throw new DadoInvalidoException("O animal não pode ser vazio.");
        }
    }

    // Registra uma consulta
    public void registrarConsulta(Usuario executor, Animal animal, Consulta consulta)
            throws PermissaoNegadaException {

        exigirPermissao(executor, animal);
        animal.getHistoricoMedico().registrarConsulta(consulta);
    }

    // Registra uma vacina
    public void registrarVacina(Usuario executor, Animal animal, Vacina vacina)
            throws PermissaoNegadaException, VacinaDuplicadaException {

        exigirPermissao(executor, animal);
        animal.getHistoricoMedico().registrarVacina(vacina);
    }

    // Registra um medicamento
    public void registrarMedicamento(Usuario executor, Animal animal, Medicamento medicamento)
            throws PermissaoNegadaException {

        exigirPermissao(executor, animal);
        animal.getHistoricoMedico().registrarMedicamento(medicamento);
    }

    // Registra uma cirurgia
    public void registrarCirurgia(Usuario executor, Animal animal, Cirurgia cirurgia)
            throws PermissaoNegadaException {

        exigirPermissao(executor, animal);
        animal.getHistoricoMedico().registrarCirurgia(cirurgia);
    }

    // Atualiza o estado de saúde
    public void atualizarEstadoSaude(Usuario executor, Animal animal, EstadoSaude novoEstado)
            throws PermissaoNegadaException {

        exigirPermissao(executor, animal);
        animal.definirEstadoSaude(novoEstado);
    }

    // B8: reúne todas as vacinas de todos os animais
    public List<Vacina> getVacinas() {

        List<Vacina> vacinas = new ArrayList<>();

        for (Animal animal : animalServico.getAnimais()) {
            vacinas.addAll(animal.getHistoricoMedico().getVacinas());
        }

        return Collections.unmodifiableList(vacinas);
    }

    // B8: lista vacinas cuja próxima dose está próxima do vencimento
    public List<Vacina> listarVacinasProximasDoVencimento() {

        List<Vacina> proximas = new ArrayList<>();

        for (Vacina vacina : getVacinas()) {
            if (vacina.venceEm(Constantes.DIAS_ALERTA_VACINA)) {
                proximas.add(vacina);
            }
        }

        return Collections.unmodifiableList(proximas);
    }

    // B8: identifica vacinas obrigatórias que ainda não foram registradas
    public List<String> listarVacinasObrigatoriasPendentes(Animal animal) {

        if (animal == null) {
            throw new DadoInvalidoException("O animal não pode ser vazio.");
        }

        List<String> pendentes = new ArrayList<>();

        List<String> obrigatorias = animal.getVacinasObrigatorias();
        List<Vacina> registradas = animal.getHistoricoMedico().getVacinas();

        for (String obrigatoria : obrigatorias) {

            boolean encontrada = false;

            for (Vacina vacina : registradas) {
                if (vacina.getNome().equalsIgnoreCase(obrigatoria)) {
                    encontrada = true;
                    break;
                }
            }

            if (!encontrada) {
                pendentes.add(obrigatoria);
            }
        }

        return Collections.unmodifiableList(pendentes);
    }

    // B8: lista animais em tratamento ou em estado crítico
    public List<Animal> listarAnimaisEmTratamento() {

        List<Animal> emTratamento = new ArrayList<>();

        for (Animal animal : animalServico.getAnimais()) {

            EstadoSaude estado = animal.getEstadoSaude();

            if (estado == EstadoSaude.EM_TRATAMENTO ||
                estado == EstadoSaude.CRITICO) {

                emTratamento.add(animal);
            }
        }

        return Collections.unmodifiableList(emTratamento);
    }
}
