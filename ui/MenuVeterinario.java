package ui;

import java.time.LocalDate;

import contrato.EmissorRelatorio;
import excecao.AbrigoException;
import models.animal.Animal;
import models.animal.EstadoSaude;
import models.saude.Cirurgia;
import models.saude.Consulta;
import models.saude.Medicamento;
import models.saude.Vacina;
import models.usuario.Usuario;
import models.usuario.Veterinario;

public class MenuVeterinario implements MenuPerfil {
    private final Sistema sistema;

    public MenuVeterinario(Sistema sistema) {
        this.sistema = sistema;
    }

    @Override
    public void executar(int opcao, Usuario usuario) throws AbrigoException {
        Veterinario veterinario = (Veterinario) usuario;

        switch (opcao) {
            case 1:
                consultarAnimais();
                break;
            case 2:
                registrarAtendimento(veterinario);
                break;
            case 3:
                atualizarTratamento(veterinario);
                break;
            case 4:
                registrarVacinacao(veterinario);
                break;
            case 5:
                emitirRelatorio(veterinario);
                break;
            case 6:
                AcoesComuns.verNotificacoes(veterinario);
                break;
            default:
                Console.erro("Opção inválida.");
        }
    }

    private void consultarAnimais() throws AbrigoException {
        Console.titulo("ANIMAIS EM TRATAMENTO OU CRÍTICOS");
        Console.listar(sistema.getSaudeServico().listarAnimaisEmTratamento(), "Nenhum.");

        Console.titulo("VACINAS PRÓXIMAS DO VENCIMENTO");
        Console.listar(sistema.getSaudeServico().listarVacinasProximasDoVencimento(), "Nenhuma.");

        if (Console.confirmar("\nVer a ficha de um animal?")) {
            Animal animal = AcoesComuns.escolherAnimal(sistema);
            AcoesComuns.mostrarFichaAnimal(sistema, animal);
        }
    }

    private void registrarAtendimento(Veterinario veterinario) throws AbrigoException {
        Animal animal = AcoesComuns.escolherAnimal(sistema);

        System.out.println("Tipo de atendimento:");
        System.out.println("  1 - Consulta");
        System.out.println("  2 - Medicamento");
        System.out.println("  3 - Cirurgia");
        int tipo = Console.lerInt("Escolha");

        LocalDate hoje = LocalDate.now();
        switch (tipo) {
            case 1: {
                String descricao = Console.lerTexto("Descrição");
                String diagnostico = Console.lerTexto("Diagnóstico");
                sistema.getSaudeServico().registrarConsulta(veterinario, animal,
                        new Consulta(hoje, descricao, veterinario, diagnostico));
                break;
            }
            case 2: {
                String descricao = Console.lerTexto("Descrição");
                String nome = Console.lerTexto("Medicamento");
                String dose = Console.lerTexto("Dose");
                LocalDate inicio = Console.lerData("Início");
                LocalDate fim = Console.lerData("Fim");
                sistema.getSaudeServico().registrarMedicamento(veterinario, animal,
                        new Medicamento(hoje, descricao, veterinario, nome, dose, inicio, fim));
                break;
            }
            case 3: {
                String descricao = Console.lerTexto("Descrição");
                String procedimento = Console.lerTexto("Procedimento");
                String resultado = Console.lerTexto("Resultado");
                sistema.getSaudeServico().registrarCirurgia(veterinario, animal,
                        new Cirurgia(hoje, descricao, veterinario, procedimento, resultado));
                break;
            }
            default:
                Console.erro("Tipo de atendimento inválido.");
                return;
        }
        Console.sucesso("Atendimento registrado no histórico de " + animal.getNome() + ".");
    }

    private void atualizarTratamento(Veterinario veterinario) throws AbrigoException {
        Animal animal = AcoesComuns.escolherAnimal(sistema);
        System.out.println("Estado atual: " + animal.getEstadoSaude());
        EstadoSaude novo = Console.escolherEnum("Novo estado de saúde", EstadoSaude.values());

        sistema.getSaudeServico().atualizarEstadoSaude(veterinario, animal, novo);
        Console.sucesso(animal.getNome() + " agora está " + novo + " (status de adoção: "
                + animal.getStatusAdocao() + ").");
    }

    private void registrarVacinacao(Veterinario veterinario) throws AbrigoException {
        Animal animal = AcoesComuns.escolherAnimal(sistema);
        System.out.println("Vacinas obrigatórias para " + animal.getEspecie() + ": "
                + animal.getVacinasObrigatorias());

        String nome = Console.lerTexto("Vacina");
        String descricao = Console.lerTexto("Descrição");
        LocalDate aplicacao = Console.lerData("Data de aplicação");
        LocalDate proximaDose = Console.lerData("Próxima dose");

        sistema.getSaudeServico().registrarVacina(veterinario, animal,
                new Vacina(aplicacao, descricao, veterinario, nome, proximaDose));
        Console.sucesso("Vacina registrada.");
    }

    private void emitirRelatorio(EmissorRelatorio emissor) {
        Console.titulo("RELATÓRIO MÉDICO");
        String relatorio = emissor.emitirRelatorio(sistema.getRelatorioServico());
        System.out.println(relatorio.isEmpty() ? "(relatório ainda não implementado - card B10)" : relatorio);
    }
}