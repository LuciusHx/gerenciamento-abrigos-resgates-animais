package servicos;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import excecao.DadoInvalidoException;
import excecao.PermissaoNegadaException;
import models.animal.Animal;
import models.atividade.Atividade;
import models.notificacao.TipoNotificacao;
import models.saude.Vacina;
import models.usuario.Usuario;
import util.Constantes;
import util.Validacoes;

public class AlertaServico {
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final NotificacaoServico notificacaoServico;
    private final AtividadeServico atividadeServico;
    private final AnimalServico animalServico;

    //guardam o que ja foi avisado pra nao repetir o alerta a cada verificacao
    private final Set<Atividade> eventosAlertados = new HashSet<>();
    private final Set<Vacina> vacinasAlertadas = new HashSet<>();

    public AlertaServico(NotificacaoServico notificacaoServico, AtividadeServico atividadeServico,
            AnimalServico animalServico) {
        if (notificacaoServico == null || atividadeServico == null || animalServico == null) {
            throw new DadoInvalidoException("Os serviços de notificação, atividades e animais são obrigatórios.");
        }
        this.notificacaoServico = notificacaoServico;
        this.atividadeServico = atividadeServico;
        this.animalServico = animalServico;
    }

    //retorna quantos alertas foram enviados
    public int verificarAlertas() {
        return verificarEventosProximos() + verificarVacinasVencendo();
    }

    private int verificarEventosProximos() {
        int enviados = 0;
        for (Atividade atividade : atividadeServico.getAtividades()) {
            if (!atividade.isConcluida()
                    && atividade.venceEm(Constantes.DIAS_ALERTA_EVENTO)
                    && eventosAlertados.add(atividade)) {

                notificacaoServico.notificarTodos(TipoNotificacao.EVENTO_PROXIMO,
                        String.format("%s \"%s\" acontece em %s, em %s. Vagas restantes: %d.",
                                atividade.getTipo(), atividade.getTitulo(),
                                atividade.getData().format(FORMATO_DATA), atividade.getLocal(),
                                atividade.getVagasRestantes()));
                enviados++;
            }
        }
        return enviados;
    }

    private int verificarVacinasVencendo() {
        int enviados = 0;
        for (Animal animal : animalServico.getAnimais()) {
            for (Vacina vacina : animal.getHistoricoMedico().getVacinas()) {
                if (vacina.venceEm(Constantes.DIAS_ALERTA_VACINA) && vacinasAlertadas.add(vacina)) {

                    notificacaoServico.notificarTodos(TipoNotificacao.VACINA_VENCENDO,
                            String.format("A próxima dose da vacina %s de %s vence em %s.",
                                    vacina.getNome(), animal.getNome(),
                                    vacina.getProximaDose().format(FORMATO_DATA)));
                    enviados++;
                }
            }
        }
        return enviados;
    }

    public void publicarAviso(Usuario executor, String mensagem) throws PermissaoNegadaException {
        if (executor == null) {
            throw new DadoInvalidoException("Executor inválido.");
        }
        if (!executor.podePublicarAvisos()) {
            throw new PermissaoNegadaException("Apenas administradores podem publicar avisos gerais.");
        }
        Validacoes.validarString(mensagem, "mensagem do aviso");

        notificacaoServico.notificarTodos(TipoNotificacao.AVISO_GERAL, mensagem);
    }
}