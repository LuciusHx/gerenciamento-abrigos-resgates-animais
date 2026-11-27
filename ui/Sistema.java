package ui;

import servicos.AdocaoServico;
import servicos.AlertaServico;
import servicos.AnimalServico;
import servicos.AtividadeServico;
import servicos.NotificacaoServico;
import servicos.RelatorioServico;
import servicos.SaudeServico;
import servicos.UsuarioServico;

//cria todos os servicos de uma lapada so e liga tudo pelo construtor
public class Sistema {
    private final NotificacaoServico notificacaoServico;
    private final UsuarioServico usuarioServico;
    private final AnimalServico animalServico;
    private final AdocaoServico adocaoServico;
    private final AtividadeServico atividadeServico;
    private final SaudeServico saudeServico;
    private final RelatorioServico relatorioServico;
    private final AlertaServico alertaServico;

    public Sistema() {
        this.notificacaoServico = new NotificacaoServico();
        this.usuarioServico = new UsuarioServico(notificacaoServico);
        this.animalServico = new AnimalServico(notificacaoServico);
        this.adocaoServico = new AdocaoServico(notificacaoServico);
        this.atividadeServico = new AtividadeServico(notificacaoServico);
        this.saudeServico = new SaudeServico(animalServico);
        this.relatorioServico = new RelatorioServico(animalServico, adocaoServico, usuarioServico, saudeServico);
        this.alertaServico = new AlertaServico(notificacaoServico, atividadeServico, animalServico);
    }

    public NotificacaoServico getNotificacaoServico() {
        return notificacaoServico;
    }

    public UsuarioServico getUsuarioServico() {
        return usuarioServico;
    }

    public AnimalServico getAnimalServico() {
        return animalServico;
    }

    public AdocaoServico getAdocaoServico() {
        return adocaoServico;
    }

    public AtividadeServico getAtividadeServico() {
        return atividadeServico;
    }

    public SaudeServico getSaudeServico() {
        return saudeServico;
    }

    public RelatorioServico getRelatorioServico() {
        return relatorioServico;
    }

    public AlertaServico getAlertaServico() {
        return alertaServico;
    }
}