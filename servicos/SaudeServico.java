package servicos;

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

public class SaudeServico {

    // só quem tem permissão médica altera dados de saúde
    private void exigirPermissao(Usuario executor, Animal animal) throws PermissaoNegadaException {
        if (executor == null) {
            throw new DadoInvalidoException("Executor inválido.");
        }
        if (!executor.podeAlterarDadosMedicos()) {
            throw new PermissaoNegadaException("Apenas veterinários podem alterar dados médicos.");
        }
        if (animal == null) {
            throw new DadoInvalidoException("O animal não pode ser vazio.");
        }
    }

    public void registrarConsulta(Usuario executor, Animal animal, Consulta consulta)
            throws PermissaoNegadaException {
        exigirPermissao(executor, animal);
        animal.getHistoricoMedico().registrarConsulta(consulta);
    }

    // (vacina duplicada) é conferida pelo HistoricoMedico
    public void registrarVacina(Usuario executor, Animal animal, Vacina vacina)
            throws PermissaoNegadaException, VacinaDuplicadaException {
        exigirPermissao(executor, animal);
        animal.getHistoricoMedico().registrarVacina(vacina);
    }

    public void registrarMedicamento(Usuario executor, Animal animal, Medicamento medicamento)
            throws PermissaoNegadaException {
        exigirPermissao(executor, animal);
        animal.getHistoricoMedico().registrarMedicamento(medicamento);
    }

    public void registrarCirurgia(Usuario executor, Animal animal, Cirurgia cirurgia)
            throws PermissaoNegadaException {
        exigirPermissao(executor, animal);
        animal.getHistoricoMedico().registrarCirurgia(cirurgia);
    }

    // se virar crítico estando disponível, o Animal passa pra indisponível
    public void atualizarEstadoSaude(Usuario executor, Animal animal, EstadoSaude novoEstado)
            throws PermissaoNegadaException {
        exigirPermissao(executor, animal);
        animal.definirEstadoSaude(novoEstado);
    }
}