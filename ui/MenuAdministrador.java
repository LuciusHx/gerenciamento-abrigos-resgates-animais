package ui;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import contrato.EmissorRelatorio;
import excecao.AbrigoException;
import excecao.RegistroNaoEncontradoException;
import models.adocao.Adocao;
import models.animal.Animal;
import models.animal.Cachorro;
import models.animal.EstadoSaude;
import models.animal.Gato;
import models.animal.OutroAnimal;
import models.animal.Porte;
import models.animal.StatusAdocao;
import models.atividade.Atividade;
import models.atividade.CampanhaVacinacao;
import models.atividade.EventoBeneficente;
import models.atividade.FeiraAdocao;
import models.atividade.MutiraoLimpeza;
import models.pessoa.Perfil;
import models.usuario.Administrador;
import models.usuario.Usuario;
import models.usuario.Voluntario;
import servicos.RelatorioServico;

public class MenuAdministrador implements MenuPerfil {
    private final Sistema sistema;

    public MenuAdministrador(Sistema sistema) {
        this.sistema = sistema;
    }

    @Override
    public void executar(int opcao, Usuario usuario) throws AbrigoException {
        switch (opcao) {
            case 1:
                gerenciarAnimais(usuario);
                break;
            case 2:
                gerenciarUsuarios(usuario);
                break;
            case 3:
                gerenciarAdocoes(usuario);
                break;
            case 4:
                gerenciarEventos(usuario);
                break;
            case 5:
                relatorios(usuario);
                break;
            case 6:
                estatisticas();
                break;
            case 7:
                notificacoes(usuario);
                break;
            default:
                Console.erro("Opção inválida.");
        }
    }

    // ANIMAAAAAIS - ---------------------------------------------------

    private void gerenciarAnimais(Usuario admin) throws AbrigoException {
        Console.titulo("GERENCIAR ANIMAIS");
        System.out.println("1 - Listar todos");
        System.out.println("2 - Cadastrar animal");
        System.out.println("3 - Editar animal");
        System.out.println("4 - Remover animal");
        System.out.println("5 - Buscar por nome");
        System.out.println("6 - Filtrar (espécie, porte, status)");
        System.out.println("7 - Ver ficha de um animal");
        System.out.println("8 - Atribuir animal a um voluntário");
        System.out.println("0 - Voltar");

        switch (Console.lerInt("Escolha")) {
            case 1:
                AcoesComuns.listarTodosAnimais(sistema);
                break;
            case 2:
                cadastrarAnimal(admin);
                break;
            case 3:
                editarAnimal(admin);
                break;
            case 4: {
                AcoesComuns.listarTodosAnimais(sistema);
                int id = Console.lerInt("Id do animal a remover");
                sistema.getAnimalServico().remover(admin, id);
                Console.sucesso("Animal removido.");
                break;
            }
            case 5:
                Console.listar(sistema.getAnimalServico().buscarPorNome(Console.lerTexto("Nome (ou parte)")),
                        "Nenhum animal encontrado.");
                break;
            case 6: {
                String especie = Console.lerTextoOpcional("Espécie");
                Porte porte = Console.escolherEnumOpcional("Porte", Porte.values());
                StatusAdocao status = Console.escolherEnumOpcional("Status", StatusAdocao.values());
                Console.listar(sistema.getAnimalServico().filtrar(especie, porte, status),
                        "Nenhum animal com esses filtros.");
                break;
            }
            case 7:
                AcoesComuns.mostrarFichaAnimal(sistema, AcoesComuns.escolherAnimal(sistema));
                break;
            case 8:
                atribuirResponsavel();
                break;
            case 0:
                break;
            default:
                Console.erro("Opção inválida.");
        }
    }

    private void cadastrarAnimal(Usuario admin) throws AbrigoException {
        Console.titulo("CADASTRAR ANIMAL");
        System.out.println("1 - Cachorro");
        System.out.println("2 - Gato");
        System.out.println("3 - Outro");
        int tipo = Console.lerInt("Espécie");
        if (tipo < 1 || tipo > 3) {
            Console.erro("Espécie inválida.");
            return;
        }
        String especie = tipo == 3 ? Console.lerTexto("Qual espécie") : null;

        String nome = Console.lerTexto("Nome");
        String raca = Console.lerTexto("Raça");
        int idade = Console.lerInt("Idade (anos)");
        Porte porte = Console.escolherEnum("Porte", Porte.values());
        EstadoSaude estado = Console.escolherEnum("Estado de saúde", EstadoSaude.values());
        LocalDate resgate = Console.lerData("Data do resgate");

        Animal animal;
        if (tipo == 1) {
            animal = new Cachorro(nome, raca, idade, porte, estado, resgate);
        } else if (tipo == 2) {
            animal = new Gato(nome, raca, idade, porte, estado, resgate);
        } else {
            animal = new OutroAnimal(especie, nome, raca, idade, porte, estado, resgate);
        }

        sistema.getAnimalServico().cadastrar(admin, animal);
        Console.sucesso("Animal cadastrado: " + animal);
    }

    private void editarAnimal(Usuario admin) throws AbrigoException {
        Animal animal = AcoesComuns.escolherAnimal(sistema);
        System.out.println("Deixe em branco para manter o valor atual.");

        String nome = Console.lerTextoOpcional("Nome [" + animal.getNome() + "]");
        String raca = Console.lerTextoOpcional("Raça [" + animal.getRaca() + "]");
        String idadeTexto = Console.lerTextoOpcional("Idade [" + animal.getIdade() + "]");
        boolean mudarPorte = Console.confirmar("Alterar o porte (" + animal.getPorte() + ")?");
        Porte porte = mudarPorte ? Console.escolherEnum("Porte", Porte.values()) : animal.getPorte();

        int idade = animal.getIdade();
        if (idadeTexto != null) {
            try {
                idade = Integer.parseInt(idadeTexto);
            } catch (NumberFormatException e) {
                Console.erro("Idade inválida. A idade não foi alterada.");
            }
        }

        sistema.getAnimalServico().editar(admin, animal.getId(),
                nome != null ? nome : animal.getNome(),
                raca != null ? raca : animal.getRaca(),
                idade, porte);
        Console.sucesso("Animal atualizado: " + animal);
    }

    private void atribuirResponsavel() throws AbrigoException {
        Animal animal = AcoesComuns.escolherAnimal(sistema);

        List<Voluntario> voluntarios = sistema.getUsuarioServico().getVoluntarios();
        Console.titulo("VOLUNTÁRIOS");
        if (voluntarios.isEmpty()) {
            System.out.println("Nenhum voluntário cadastrado.");
            return;
        }
        for (Voluntario v : voluntarios) {
            System.out.println("  #" + v.getId() + " " + v.getNome());
        }
        int id = Console.lerInt("Id do voluntário");

        Voluntario escolhido = null;
        for (Voluntario v : voluntarios) {
            if (v.getId() == id) {
                escolhido = v;
            }
        }
        if (escolhido == null) {
            throw new RegistroNaoEncontradoException("Voluntário não encontrado.");
        }
        escolhido.adicionarAnimalResponsavel(animal);
        Console.sucesso(animal.getNome() + " agora está sob a responsabilidade de " + escolhido.getNome() + ".");
    }

    // ususarios ----------------------------------------------------------

    private void gerenciarUsuarios(Usuario admin) throws AbrigoException {
        Console.titulo("GERENCIAR USUÁRIOS");
        System.out.println("1 - Listar todos");
        System.out.println("2 - Listar por perfil");
        System.out.println("3 - Cadastrar administrador");
        System.out.println("4 - Editar usuário");
        System.out.println("5 - Remover usuário");
        System.out.println("0 - Voltar");

        switch (Console.lerInt("Escolha")) {
            case 1:
                listarUsuarios(sistema.getUsuarioServico().getUsuarios());
                break;
            case 2:
                Perfil perfil = Console.escolherEnum("Perfil", Perfil.values());
                listarUsuarios(sistema.getUsuarioServico().listarPorPerfil(perfil));
                break;
            case 3: {
                Console.titulo("NOVO ADMINISTRADOR");
                Administrador novo = new Administrador(Console.lerTexto("Nome"), Console.lerTexto("CPF"),
                        Console.lerTexto("E-mail"), Console.lerTexto("Telefone"),
                        Console.lerTexto("Login"), Console.lerTexto("Senha"));
                sistema.getUsuarioServico().cadastrar(novo);
                Console.sucesso("Administrador cadastrado.");
                break;
            }
            case 4: {
                listarUsuarios(sistema.getUsuarioServico().getUsuarios());
                int id = Console.lerInt("Id do usuário");
                System.out.println("Deixe em branco para manter o valor atual.");
                sistema.getUsuarioServico().editar(admin, id, Console.lerTextoOpcional("Nome"),
                        Console.lerTextoOpcional("E-mail"), Console.lerTextoOpcional("Telefone"));
                Console.sucesso("Usuário atualizado.");
                break;
            }
            case 5: {
                listarUsuarios(sistema.getUsuarioServico().getUsuarios());
                int id = Console.lerInt("Id do usuário a remover");
                sistema.getUsuarioServico().remover(admin, id);
                Console.sucesso("Usuário removido.");
                break;
            }
            case 0:
                break;
            default:
                Console.erro("Opção inválida.");
        }
    }

    private void listarUsuarios(List<? extends Usuario> usuarios) {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário.");
        }
        for (Usuario u : usuarios) {
            System.out.println("  #" + u.getId() + " " + u.getNome() + " (" + u.getPerfil() + ") - login: "
                    + u.getLogin() + " - " + u.getEmail());
        }
    }

    // adocoes -----------------------------------

    private void gerenciarAdocoes(Usuario admin) throws AbrigoException {
        Console.titulo("ADOÇÕES");
        System.out.println("1 - Solicitações pendentes");
        System.out.println("2 - Registrar nova solicitação");
        System.out.println("3 - Ver candidatos de um animal");
        System.out.println("4 - Aprovar solicitação");
        System.out.println("5 - Rejeitar solicitação");
        System.out.println("6 - Concluir adoção");
        System.out.println("7 - Registrar devolução");
        System.out.println("8 - Histórico de adoções");
        System.out.println("0 - Voltar");

        switch (Console.lerInt("Escolha")) {
            case 1:
                Console.listar(sistema.getAdocaoServico().listarPendentes(), "Nenhuma solicitação pendente.");
                break;
            case 2:
                AcoesComuns.registrarSolicitacao(sistema);
                break;
            case 3: {
                Animal animal = AcoesComuns.escolherAnimal(sistema);
                Console.listar(sistema.getAdocaoServico().listarCandidatos(animal), "Nenhum candidato.");
                break;
            }
            case 4:
                mostrarSolicitacoes();
                sistema.getAdocaoServico().aprovar(admin, Console.lerInt("Id da solicitação"));
                Console.sucesso("Solicitação aprovada. O adotante foi notificado.");
                break;
            case 5: {
                mostrarSolicitacoes();
                int id = Console.lerInt("Id da solicitação");
                sistema.getAdocaoServico().rejeitar(admin, id, Console.lerTexto("Motivo"));
                Console.sucesso("Solicitação rejeitada. O adotante foi notificado.");
                break;
            }
            case 6: {
                mostrarSolicitacoes();
                Adocao adocao = sistema.getAdocaoServico().concluir(admin, Console.lerInt("Id da solicitação"));
                Console.sucesso("Adoção concluída: " + adocao);
                break;
            }
            case 7: {
                Console.listar(sistema.getAdocaoServico().getAdocoes(), "Nenhuma adoção registrada.");
                int id = Console.lerInt("Id da adoção");
                sistema.getAdocaoServico().registrarDevolucao(admin, id, Console.lerTexto("Motivo da devolução"));
                Console.sucesso("Devolução registrada. O animal voltou para adoção.");
                break;
            }
            case 8:
                Console.listar(sistema.getAdocaoServico().getAdocoes(), "Nenhuma adoção registrada.");
                break;
            case 0:
                break;
            default:
                Console.erro("Opção inválida.");
        }
    }

    private void mostrarSolicitacoes() {
        Console.listar(sistema.getAdocaoServico().getSolicitacoes(), "Nenhuma solicitação registrada.");
    }

    // eventooos ------------------------------

    private void gerenciarEventos(Usuario admin) throws AbrigoException {
        Console.titulo("EVENTOS E ATIVIDADES");
        System.out.println("1 - Listar todas");
        System.out.println("2 - Criar atividade");
        System.out.println("3 - Concluir atividade (registra a participação dos inscritos)");
        System.out.println("0 - Voltar");

        switch (Console.lerInt("Escolha")) {
            case 1:
                Console.listar(sistema.getAtividadeServico().getAtividades(), "Nenhuma atividade cadastrada.");
                break;
            case 2:
                criarAtividade(admin);
                break;
            case 3:
                Console.listar(sistema.getAtividadeServico().getAtividades(), "Nenhuma atividade cadastrada.");
                sistema.getAtividadeServico().concluir(admin, Console.lerInt("Id da atividade"));
                Console.sucesso("Atividade concluída. Participações registradas.");
                break;
            case 0:
                break;
            default:
                Console.erro("Opção inválida.");
        }
    }

    private void criarAtividade(Usuario admin) throws AbrigoException {
        System.out.println("Tipo:");
        System.out.println("  1 - Mutirão de limpeza");
        System.out.println("  2 - Campanha de vacinação");
        System.out.println("  3 - Feira de adoção");
        System.out.println("  4 - Evento beneficente");
        int tipo = Console.lerInt("Escolha");
        if (tipo < 1 || tipo > 4) {
            Console.erro("Tipo inválido.");
            return;
        }

        String titulo = Console.lerTexto("Título");
        LocalDate data = Console.lerData("Data");
        String local = Console.lerTexto("Local");
        int vagas = Console.lerInt("Vagas");

        Atividade atividade;
        if (tipo == 1) {
            atividade = new MutiraoLimpeza(titulo, data, local, vagas);
        } else if (tipo == 2) {
            atividade = new CampanhaVacinacao(titulo, data, local, vagas, Console.lerTexto("Vacina-alvo"));
        } else if (tipo == 3) {
            FeiraAdocao feira = new FeiraAdocao(titulo, data, local, vagas);
            AcoesComuns.listarAnimaisDisponiveis(sistema);
            while (!sistema.getAnimalServico().listarDisponiveis().isEmpty()
                    && Console.confirmar("Adicionar um animal à feira?")) {
                feira.adicionarAnimal(sistema.getAnimalServico().buscarPorId(Console.lerInt("Id do animal")));
            }
            atividade = feira;
        } else {
            atividade = new EventoBeneficente(titulo, data, local, vagas, Console.lerDouble("Meta de arrecadação (R$)"));
        }

        sistema.getAtividadeServico().criar(admin, atividade);
        Console.sucesso("Atividade criada e divulgada: " + atividade);
    }

    // 5 6 e 7 --------------------------------

    private void relatorios(Usuario usuario) {
        Console.titulo("RELATÓRIO GERAL");
        EmissorRelatorio emissor = (EmissorRelatorio) usuario;
        String relatorio = emissor.emitirRelatorio(sistema.getRelatorioServico());
        System.out.println(relatorio.isEmpty() ? "(relatório ainda não implementado - card B10)" : relatorio);
    }

    private void estatisticas() {
        RelatorioServico relatorio = sistema.getRelatorioServico();
        Console.titulo("ESTATÍSTICAS");
        System.out.println("Animais resgatados: " + relatorio.getTotalAnimaisResgatados());
        System.out.println("Adoções realizadas: " + relatorio.getTotalAdocoesRealizadas());
        System.out.printf("Taxa de adoção no mês: %.1f%%%n", relatorio.getTaxaAdocaoMensal() * 100);
        System.out.println("Voluntários ativos: " + relatorio.getTotalVoluntariosAtivos());
        System.out.println("Espécies mais resgatadas:");
        Map<String, Integer> especies = relatorio.getEspeciesMaisResgatadas();
        if (especies.isEmpty()) {
            System.out.println("  (nenhum animal)");
        }
        for (Map.Entry<String, Integer> e : especies.entrySet()) {
            System.out.println("  " + e.getKey() + ": " + e.getValue());
        }
    }

    private void notificacoes(Usuario admin) throws AbrigoException {
        Console.titulo("NOTIFICAÇÕES");
        System.out.println("1 - Minhas notificações");
        System.out.println("2 - Publicar aviso geral");
        System.out.println("3 - Verificar alertas (eventos e vacinas)");
        System.out.println("4 - Histórico de tudo que foi enviado");
        System.out.println("0 - Voltar");

        switch (Console.lerInt("Escolha")) {
            case 1:
                AcoesComuns.verNotificacoes(admin);
                break;
            case 2:
                sistema.getAlertaServico().publicarAviso(admin, Console.lerTexto("Mensagem"));
                Console.sucesso("Aviso enviado a todos os inscritos.");
                break;
            case 3:
                int enviados = sistema.getAlertaServico().verificarAlertas();
                Console.sucesso(enviados + " alerta(s) novo(s) enviado(s).");
                break;
            case 4:
                Console.listar(sistema.getNotificacaoServico().getHistorico(), "Nada enviado ainda.");
                break;
            case 0:
                break;
            default:
                Console.erro("Opção inválida.");
        }
    }
}