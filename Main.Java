import java.util.EnumMap;
import java.util.Map;

import excecao.AbrigoException;
import excecao.DadoInvalidoException;
import models.pessoa.Perfil;
import models.usuario.Administrador;
import models.usuario.Usuario;
import models.usuario.Veterinario;
import models.usuario.Voluntario;
import ui.AcoesComuns;
import ui.Console;
import ui.MenuAdministrador;
import ui.MenuPerfil;
import ui.MenuVeterinario;
import ui.MenuVoluntario;
import ui.Sistema;

public class Main {
    private static final String NOME_SISTEMA = "Sistema de Gerenciamento de Abrigos e Resgates de Animais";
    private static final String VERSAO = "1.0";

    //admin inicial pq so da pra add vet e voluntario
    private static final String LOGIN_ADMIN = "admin";
    private static final String SENHA_ADMIN = "admin123";

    private static Sistema sistema;
    private static final Map<Perfil, MenuPerfil> menus = new EnumMap<>(Perfil.class);

    public static void main(String[] args) {
        sistema = new Sistema();

        menus.put(Perfil.VOLUNTARIO, new MenuVoluntario(sistema));
        menus.put(Perfil.VETERINARIO, new MenuVeterinario(sistema));
        menus.put(Perfil.ADMINISTRADOR, new MenuAdministrador(sistema));

        sistema.getUsuarioServico().cadastrar(new Administrador("Administrador do Abrigo", "11144477735",
                "admin@abrigo.org", "85999990000", LOGIN_ADMIN, SENHA_ADMIN));

        boolean executando = true;
        while (executando) {
            exibirMenuPrincipal();
            int opcao = Console.lerOpcao();
            try {
                switch (opcao) {
                    case 1:
                        login();
                        break;
                    case 2:
                        cadastrarVoluntario();
                        break;
                    case 3:
                        cadastrarVeterinario();
                        break;
                    case 4:
                        AcoesComuns.listarAnimaisDisponiveis(sistema);
                        Console.pausar();
                        break;
                    case 5:
                        sobre();
                        break;
                    // B11
                    case 0:
                        executando = false;
                        break;
                    default:
                        Console.erro("Opção inválida.");
                }
            } catch (AbrigoException e) {
                Console.erro(e.getMessage());
            } catch (DadoInvalidoException e) {
                Console.erro(e.getMessage());
            } catch (RuntimeException e) {
                Console.erro("Erro inesperado: " + e.getMessage());
            }
        }
        System.out.println("Até logo!");
    }

    private static void exibirMenuPrincipal() {
        System.out.println();
        System.out.println("==========================================");
        System.out.println("   SISTEMA DE ABRIGOS E RESGATES ANIMAIS");
        System.out.println("==========================================");
        System.out.println("1 - Login");
        System.out.println("2 - Cadastrar Voluntário");
        System.out.println("3 - Cadastrar Veterinário");
        System.out.println("4 - Visualizar Animais Disponíveis");
        System.out.println("5 - Sobre o Sistema");
        System.out.println("0 - Sair");
        System.out.println("\nEscolha uma opção:");
        System.out.print("> ");
    }

    private static void login() throws AbrigoException {
        Console.titulo("LOGIN");
        String login = Console.lerTexto("Login");
        String senha = Console.lerTexto("Senha");

        Usuario usuario = sistema.getUsuarioServico().autenticar(login, senha);

        //entrou e ja recebeu aviso de tudo
        sistema.getAlertaServico().verificarAlertas();

        Console.sucesso("Bem-vindo(a), " + usuario.getNome() + "!");
        int naoLidas = usuario.getNotificacoes().size();
        if (naoLidas > 0) {
            System.out.println("Você tem " + naoLidas + " notificação(ões). Veja no menu Notificações.");
        }
        sessao(usuario);
    }

    private static void sessao(Usuario usuario) {
        MenuPerfil menu = menus.get(usuario.getPerfil());

        while (true) {
            System.out.println();
            usuario.exibirMenu();       
            int opcao = Console.lerOpcao();
            if (opcao == 0) {
                Console.sucesso("Sessão encerrada.");
                return;
            }
            try {
                menu.executar(opcao, usuario);
            } catch (AbrigoException e) {
                Console.erro(e.getMessage());
            } catch (DadoInvalidoException e) {
                Console.erro(e.getMessage());
            } catch (RuntimeException e) {
                Console.erro("Erro inesperado: " + e.getMessage());
            }
            Console.pausar();
        }
    }

    private static void cadastrarVoluntario() {
        Console.titulo("CADASTRO DE VOLUNTÁRIO");
        Voluntario voluntario = new Voluntario(Console.lerTexto("Nome"), Console.lerTexto("CPF"),
                Console.lerTexto("E-mail"), Console.lerTexto("Telefone"),
                Console.lerTexto("Login"), Console.lerTexto("Senha"));
        sistema.getUsuarioServico().cadastrar(voluntario);
        Console.sucesso("Voluntário cadastrado! Faça login para começar.");
    }

    private static void cadastrarVeterinario() {
        Console.titulo("CADASTRO DE VETERINÁRIO");
        Veterinario veterinario = new Veterinario(Console.lerTexto("Nome"), Console.lerTexto("CPF"),
                Console.lerTexto("E-mail"), Console.lerTexto("Telefone"),
                Console.lerTexto("Login"), Console.lerTexto("Senha"), Console.lerTexto("CRMV"));
        sistema.getUsuarioServico().cadastrar(veterinario);
        Console.sucesso("Veterinário cadastrado! Faça login para começar.");
    }

    private static void sobre() {
        Console.titulo("SOBRE O SISTEMA");
        System.out.println(NOME_SISTEMA + " - versão " + VERSAO);
        System.out.println("Trabalho final de Programação Orientada a Objetos.");
        System.out.println("Login inicial do administrador: " + LOGIN_ADMIN + " / " + SENHA_ADMIN);
        Console.pausar();
    }
}