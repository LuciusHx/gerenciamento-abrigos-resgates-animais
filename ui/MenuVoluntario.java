package ui;

import excecao.AbrigoException;
import models.animal.Animal;
import models.atividade.Atividade;
import models.usuario.Usuario;
import models.usuario.Voluntario;

public class MenuVoluntario implements MenuPerfil {
    private final Sistema sistema;

    public MenuVoluntario(Sistema sistema) {
        this.sistema = sistema;
    }

    @Override
    public void executar(int opcao, Usuario usuario) throws AbrigoException {
        Voluntario voluntario = (Voluntario) usuario;

        switch (opcao) {
            case 1:
                AcoesComuns.listarAnimaisDisponiveis(sistema);
                break;
            case 2:
                AcoesComuns.registrarSolicitacao(sistema);
                break;
            case 3:
                AcoesComuns.consultarSolicitacoesDeAdotante(sistema);
                break;
            case 4:
                participarDeEventos(voluntario);
                break;
            case 5:
                meusAnimais(voluntario);
                break;
            case 6:
                historicoDeParticipacoes(voluntario);
                break;
            case 7:
                AcoesComuns.verNotificacoes(voluntario);
                break;
            default:
                Console.erro("Opção inválida.");
        }
    }

    private void participarDeEventos(Voluntario voluntario) throws AbrigoException {
        Console.titulo("ATIVIDADES COM VAGAS");
        Console.listar(sistema.getAtividadeServico().listarDisponiveis(), "Nenhuma atividade com vagas.");

        System.out.println("\nMinhas inscrições:");
        Console.listar(sistema.getAtividadeServico().listarInscricoes(voluntario), "  (nenhuma)");

        if (sistema.getAtividadeServico().listarDisponiveis().isEmpty()) {
            return;
        }
        if (Console.confirmar("\nDeseja se inscrever em uma atividade?")) {
            int id = Console.lerInt("Id da atividade");
            sistema.getAtividadeServico().inscrever(voluntario, id);
            Console.sucesso("Inscrição confirmada!");
        }
    }

    private void meusAnimais(Voluntario voluntario) {
        Console.titulo("ANIMAIS SOB MINHA RESPONSABILIDADE");
        if (voluntario.getAnimaisResponsavel().isEmpty()) {
            System.out.println("Nenhum animal atribuído a você. Fale com o administrador.");
            return;
        }
        for (Animal animal : voluntario.getAnimaisResponsavel()) {
            AcoesComuns.mostrarFichaAnimal(sistema, animal);
        }
    }

    private void historicoDeParticipacoes(Voluntario voluntario) {
        Console.titulo("MEU HISTÓRICO DE PARTICIPAÇÕES");
        if (voluntario.getHistoricoParticipacoes().isEmpty()) {
            System.out.println("Você ainda não participou de nenhuma atividade concluída.");
        }
        for (Atividade atividade : voluntario.getHistoricoParticipacoes()) {
            System.out.println("  " + atividade);
        }
        System.out.println("Situação: " + (voluntario.isAtivo() ? "voluntário ATIVO" : "voluntário inativo"));
    }
}