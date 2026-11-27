package ui;

import java.util.List;

import excecao.AbrigoException;
import excecao.AnimalIndisponivelException;
import excecao.DadoInvalidoException;
import excecao.RegistroNaoEncontradoException;
import models.adocao.Adotante;
import models.adocao.SolicitacaoAdocao;
import models.animal.Animal;
import models.notificacao.Notificacao;
import models.saude.RegistroMedico;
import models.usuario.Usuario;
import util.ValidadorCpf;

//tela comumente usadas pra nao repetir codigo
public final class AcoesComuns {

    private AcoesComuns() {
    }

    public static void listarAnimaisDisponiveis(Sistema sistema) {
        Console.titulo("ANIMAIS DISPONÍVEIS PARA ADOÇÃO");
        Console.listar(sistema.getAnimalServico().listarDisponiveis(), "Nenhum animal disponível no momento.");
    }

    public static void listarTodosAnimais(Sistema sistema) {
        Console.titulo("TODOS OS ANIMAIS");
        Console.listar(sistema.getAnimalServico().getAnimais(), "Nenhum animal cadastrado.");
    }

    public static Animal escolherAnimal(Sistema sistema) throws RegistroNaoEncontradoException {
        listarTodosAnimais(sistema);
        if (sistema.getAnimalServico().getAnimais().isEmpty()) {
            throw new RegistroNaoEncontradoException("Não há animais cadastrados.");
        }
        int id = Console.lerInt("Id do animal");
        return sistema.getAnimalServico().buscarPorId(id);
    }

    public static void mostrarFichaAnimal(Sistema sistema, Animal animal) {
        Console.titulo("FICHA DE " + animal.getNome().toUpperCase());
        System.out.println(animal);
        System.out.println("Idade: " + animal.getIdade() + " | Porte: " + animal.getPorte()
                + " | Saúde: " + animal.getEstadoSaude()
                + " | Resgatado em: " + animal.getDataResgate().format(Console.FORMATO_DATA));

        System.out.println("Histórico médico:");
        List<RegistroMedico> registros = animal.getHistoricoMedico().getRegistros();
        if (registros.isEmpty()) {
            System.out.println("  (sem registros)");
        }
        for (RegistroMedico registro : registros) {
            System.out.println("  " + registro.resumo());   
        }

        List<String> pendentes = sistema.getSaudeServico().listarVacinasObrigatoriasPendentes(animal);
        System.out.println("Vacinas obrigatórias pendentes: " + (pendentes.isEmpty() ? "nenhuma" : pendentes));
    }

    //as mais recentes vao aparecer primeiro
    public static void verNotificacoes(Usuario usuario) {
        Console.titulo("NOTIFICAÇÕES DE " + usuario.getNome().toUpperCase());
        List<Notificacao> notificacoes = usuario.getNotificacoes();
        if (notificacoes.isEmpty()) {
            System.out.println("Nenhuma notificação.");
            return;
        }
        for (int i = notificacoes.size() - 1; i >= 0; i--) {
            System.out.println("  " + notificacoes.get(i));
        }
    }

    //parte de adocao aqui -------------------

    // procura o adotante pelo CPF nas solicitacoes se nao tiver ele cadastra 
    public static Adotante obterAdotante(Sistema sistema) {
        String cpf = Console.lerTexto("CPF do adotante");
        Adotante existente = buscarAdotantePorCpf(sistema, cpf);
        if (existente != null) {
            System.out.println("Adotante já conhecido: " + existente.getNome());
            return existente;
        }
        if (!ValidadorCpf.eValido(cpf)) {
            throw new DadoInvalidoException("O CPF é inválido.");
        }
        System.out.println("Novo adotante. Informe os dados:");
        String nome = Console.lerTexto("Nome");
        String email = Console.lerTexto("E-mail");
        String telefone = Console.lerTexto("Telefone");
        String endereco = Console.lerTexto("Endereço");
        return new Adotante(nome, cpf, email, telefone, endereco);
    }

    public static Adotante buscarAdotantePorCpf(Sistema sistema, String cpf) {
        for (SolicitacaoAdocao s : sistema.getAdocaoServico().getSolicitacoes()) {
            if (s.getAdotante().getCpf().equals(cpf)) {
                return s.getAdotante();
            }
        }
        return null;
    }

    public static void registrarSolicitacao(Sistema sistema) throws AbrigoException {
        Console.titulo("NOVA SOLICITAÇÃO DE ADOÇÃO");
        listarAnimaisDisponiveis(sistema);
        if (sistema.getAnimalServico().listarDisponiveis().isEmpty()) {
            return;
        }
        Animal animal = sistema.getAnimalServico().buscarPorId(Console.lerInt("Id do animal"));
        if (!animal.isDisponivel()) {
            throw new AnimalIndisponivelException("O animal " + animal.getNome() + " não está disponível para adoção.");
        }
        Adotante adotante = obterAdotante(sistema);
        SolicitacaoAdocao solicitacao = sistema.getAdocaoServico().solicitar(animal, adotante);
        Console.sucesso("Solicitação registrada: " + solicitacao);
    }

    public static void consultarSolicitacoesDeAdotante(Sistema sistema) {
        Console.titulo("SOLICITAÇÕES DE UM ADOTANTE");
        String cpf = Console.lerTexto("CPF do adotante");
        Adotante adotante = buscarAdotantePorCpf(sistema, cpf);
        if (adotante == null) {
            System.out.println("Nenhuma solicitação encontrada para esse CPF.");
            return;
        }
        Console.listar(sistema.getAdocaoServico().listarPorAdotante(adotante), "Nenhuma solicitação.");

        System.out.println("Notificações enviadas a " + adotante.getNome() + ":");
        Console.listar(adotante.getNotificacoes(), "  (nenhuma)");
    }
}