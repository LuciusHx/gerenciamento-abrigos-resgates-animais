import java.time.LocalDate;


import excecao.AnimalIndisponivelException;
import excecao.PermissaoNegadaException;
import excecao.RemocaoNaoPermitidaException;
import excecao.TratamentoCriticoException;
import excecao.VacinaDuplicadaException;

import models.adocao.Adotante;
import models.adocao.SolicitacaoAdocao;
import models.animal.Animal;
import models.animal.Cachorro;
import models.animal.EstadoSaude;
import models.animal.Gato;
import models.animal.OutroAnimal;
import models.animal.Porte;
import models.atividade.CampanhaVacinacao;
import models.atividade.MutiraoLimpeza;
import models.saude.Consulta;
import models.saude.Medicamento;
import models.saude.Vacina;
import models.usuario.Administrador;
import models.usuario.Veterinario;
import models.usuario.Voluntario;
import servicos.AdocaoServico;
import servicos.AnimalServico;
import servicos.AtividadeServico;
import servicos.NotificacaoServico;
import servicos.RelatorioServico;
import servicos.SaudeServico;
import servicos.UsuarioServico;
import servicos.AlertaServico;

public class Demo {
    private final UsuarioServico usuarioServico;
    private final AnimalServico animalServico;
    private final SaudeServico saudeServico;
    private final AdocaoServico adocaoServico;
    private final AtividadeServico atividadeServico;
    private final AlertaServico alertaServico;

    // personagens e animais do cenário
    private Administrador admin;
    private Veterinario vet;
    private Voluntario voluntario;
    private Adotante adotante;
    private Animal rex;
    private Animal nala;
    private Animal bolinha;
    private Animal thor;
    private Vacina vacinaDoRex;

    public Demo(UsuarioServico usuarioServico, AnimalServico animalServico, SaudeServico saudeServico,
            AdocaoServico adocaoServico, AtividadeServico atividadeServico, AlertaServico alertaServico) {
        this.usuarioServico = usuarioServico;
        this.animalServico = animalServico;
        this.saudeServico = saudeServico;
        this.adocaoServico = adocaoServico;
        this.atividadeServico = atividadeServico;
        this.alertaServico = alertaServico;
    }

    // ---------- 1. dados de exemplo (é isto que a opção do Main chama) ----------
    public void carregarDados() throws Exception {
        LocalDate hoje = LocalDate.now();

        // um usuário de cada perfil (cadastrar também inscreve nas notificações)
        admin = new Administrador("Ana Souza", "52998224725", "ana@abrigo.com",
                "88999990000", "ana", "123");
        vet = new Veterinario("Marina Lima", "39053344705", "marina@abrigo.com",
                "88999992222", "marina", "123", "CRMV-CE 1234");
        voluntario = new Voluntario("Carlos Dias", "11144477735", "carlos@abrigo.com",
                "88999991111", "carlos", "123");
        usuarioServico.cadastrar(admin);
        usuarioServico.cadastrar(vet);
        usuarioServico.cadastrar(voluntario);

        adotante = new Adotante("Paula Reis", "12345678909", "paula@email.com",
                "88999993333", "Rua das Flores, 10");

        // 4 animais: cachorro, gato, outro e um em estado crítico
        rex = new Cachorro("Rex", "Vira-lata", 3, Porte.MEDIO, EstadoSaude.SAUDAVEL, hoje.minusDays(40));
        nala = new Gato("Nala", "Siamês", 2, Porte.PEQUENO, EstadoSaude.SAUDAVEL, hoje.minusDays(30));
        bolinha = new OutroAnimal("Coelho", "Bolinha", "Angorá", 1, Porte.PEQUENO,
                EstadoSaude.SAUDAVEL, hoje.minusDays(20));
        thor = new Cachorro("Thor", "Pastor", 5, Porte.GRANDE, EstadoSaude.CRITICO, hoje.minusDays(10));
        animalServico.cadastrar(admin, rex);
        animalServico.cadastrar(admin, nala);
        animalServico.cadastrar(admin, bolinha);
        animalServico.cadastrar(admin, thor);

        // vacinas e registros médicos (a V10 do Rex vence em 5 dias: gera alerta)
        vacinaDoRex = new Vacina(hoje.minusDays(25), "Primeira dose", vet, "V10", hoje.plusDays(5));
        saudeServico.registrarVacina(vet, rex, vacinaDoRex);
        saudeServico.registrarVacina(vet, nala,
                new Vacina(hoje.minusDays(15), "Primeira dose", vet, "V4", hoje.plusDays(60)));
        saudeServico.registrarConsulta(vet, thor,
                new Consulta(hoje.minusDays(9), "Entrada no abrigo", vet, "Desnutrição grave"));
        saudeServico.registrarMedicamento(vet, thor,
                new Medicamento(hoje.minusDays(9), "Tratamento inicial", vet,
                        "Soro e vitaminas", "10 ml por dia", hoje.minusDays(9), null));

        // atividades (o mutirão é em 3 dias: gera alerta de evento próximo)
        MutiraoLimpeza mutirao = new MutiraoLimpeza("Mutirão de limpeza", hoje.plusDays(3),
                "Sede do abrigo", 10);
        CampanhaVacinacao campanha = new CampanhaVacinacao("Campanha antirrábica", hoje.plusDays(20),
                "Praça central", 5, "Antirrábica");
        atividadeServico.criar(admin, mutirao);
        atividadeServico.criar(admin, campanha);
        atividadeServico.inscrever(voluntario, mutirao.getId());
        int alertas = alertaServico.verificarAlertas();
        System.out.println("Alertas enviados: " + alertas);

        System.out.println("Dados de demonstração carregados: 3 usuários, 4 animais, 2 atividades.");
    }

    // ---------- 2. demonstração ----------
    public void demonstrar() throws Exception {
        fluxoDeAdocao();
        regrasDeNegocio();
        resumoFinal();
    }

    // fluxo feliz: solicitar, aprovar, concluir e devolver
    private void fluxoDeAdocao() throws Exception {
        System.out.println("\n=== FLUXO FELIZ: ADOÇÃO DO COMEÇO AO FIM ===");

        SolicitacaoAdocao solicitacao = adocaoServico.solicitar(rex, adotante);
        System.out.println("1. Solicitada: " + solicitacao.getStatus());

        adocaoServico.aprovar(admin, solicitacao.getId());
        System.out.println("2. Aprovada:   " + solicitacao.getStatus());

        adocaoServico.concluir(admin, solicitacao.getId());
        System.out.println("3. Concluída:  " + solicitacao.getStatus() + " | " + rex);

        adocaoServico.registrarDevolucao(admin, solicitacao.getId(), "O adotante mudou de cidade");
        System.out.println("4. Devolvida:  " + rex + " | adoções no histórico: " + adocaoServico.getAdocoes().size());
}

    // um try/catch por regra de negócio
    private void regrasDeNegocio() throws Exception {
        System.out.println("\n=== AS 6 REGRAS DE NEGÓCIO ===");

        // regra 1: animal que já foi adotado não sai do histórico (mesmo depois de devolvido)
        try {
            animalServico.remover(admin, rex.getId());
            naoLancou(1);
        } catch (RemocaoNaoPermitidaException e) {
            bloqueou(1, "adotado não sai do histórico", e);
        }

        // regra 2: a mesma vacina na mesma data de aplicação não entra duas vezes
        try {
            saudeServico.registrarVacina(vet, rex,
                    new Vacina(vacinaDoRex.getDataRegistro(), "Repetida", vet,
                            vacinaDoRex.getNome(), LocalDate.now().plusDays(30)));
            naoLancou(2);
        } catch (VacinaDuplicadaException e) {
            bloqueou(2, "vacina duplicada na mesma data", e);
        }

        // regra 3: não se pede adoção de animal indisponível (o Thor é crítico)
        try {
            adocaoServico.solicitar(thor, adotante);
            naoLancou(3);
        } catch (AnimalIndisponivelException e) {
            bloqueou(3, "adoção de animal indisponível", e);
        }

        // regra 4: só veterinário altera dado médico
        try {
            saudeServico.registrarConsulta(voluntario, rex,
                    new Consulta(LocalDate.now(), "Tentativa", vet, "Teste"));
            naoLancou(4);
        } catch (PermissaoNegadaException e) {
            bloqueou(4, "só veterinário altera dado médico", e);
        }

        // regra 5: só administrador aprova e conclui adoção
        SolicitacaoAdocao pendente = adocaoServico.solicitar(nala, adotante);
        try {
            adocaoServico.aprovar(voluntario, pendente.getId());
            naoLancou(5);
        } catch (PermissaoNegadaException e) {
            bloqueou(5, "só administrador aprova adoção", e);
        }

        // regra 6: animal crítico não vai para adoção
        try {
            thor.liberarParaAdocao();
            naoLancou(6);
        } catch (TratamentoCriticoException e) {
            bloqueou(6, "crítico não vai para adoção", e);
        }
    }

    private void resumoFinal() {
        System.out.println("\n=== NOTIFICAÇÕES E RELATÓRIOS ===");
        System.out.println("Notificações recebidas pelo voluntário: " + voluntario.getNotificacoes().size());

        RelatorioServico relatorio = new RelatorioServico(animalServico, adocaoServico,
                usuarioServico, saudeServico);
        System.out.println(admin.emitirRelatorio(relatorio));
        System.out.println(vet.emitirRelatorio(relatorio));
    }

    private static void bloqueou(int regra, String nome, Exception e) {
        System.out.println("Regra " + regra + " (" + nome + ") bloqueada: " + e.getMessage());
    }

    private static void naoLancou(int regra) {
        System.out.println("ERRO: a regra " + regra + " não lançou exceção.");
    }

    // roda a demonstração sozinha, sem passar pelo Main
    public static void main(String[] args) throws Exception {
        NotificacaoServico notificacao = new NotificacaoServico();
        UsuarioServico usuarioServico = new UsuarioServico(notificacao);
        AnimalServico animalServico = new AnimalServico(notificacao);
        SaudeServico saudeServico = new SaudeServico(animalServico);
        AdocaoServico adocaoServico = new AdocaoServico(notificacao);
        AtividadeServico atividadeServico = new AtividadeServico(notificacao);
        AlertaServico alertaServico = new AlertaServico(notificacao, atividadeServico, animalServico);

        Demo demo = new Demo(usuarioServico, animalServico, saudeServico, adocaoServico, atividadeServico, alertaServico);
        demo.carregarDados();
        demo.demonstrar();
    }
}