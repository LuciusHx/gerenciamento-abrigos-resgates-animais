package servicos;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import excecao.DadoInvalidoException;
import models.adocao.Adocao;
import models.animal.Animal;
import models.usuario.Voluntario;

public class RelatorioServico {
    private final AnimalServico animalServico;
    private final AdocaoServico adocaoServico;
    private final UsuarioServico usuarioServico;
    private final SaudeServico saudeServico;

    public RelatorioServico(AnimalServico animalServico, AdocaoServico adocaoServico,
            UsuarioServico usuarioServico, SaudeServico saudeServico) {
        if (animalServico == null || adocaoServico == null || usuarioServico == null || saudeServico == null) {
            throw new DadoInvalidoException("Todos os serviços são obrigatórios para gerar relatórios.");
        }
        this.animalServico = animalServico;
        this.adocaoServico = adocaoServico;
        this.usuarioServico = usuarioServico;
        this.saudeServico = saudeServico;
    }


    public int getTotalAnimaisResgatados() {
        return animalServico.getAnimais().size();
    }

    // devolução não apaga a adoção do histórico, então ela continua contando
    public int getTotalAdocoesRealizadas() {
        return adocaoServico.getAdocoes().size();
    }

    // do maior pro menor (empate: ordem alfabética)
    public Map<String, Integer> getEspeciesMaisResgatadas() {
        Map<String, Integer> contagem = new LinkedHashMap<>();
        for (Animal animal : animalServico.getAnimais()) {
            String especie = animal.getEspecie();
            contagem.put(especie, contagem.getOrDefault(especie, 0) + 1);
        }

        List<Map.Entry<String, Integer>> entradas = new ArrayList<>(contagem.entrySet());
        entradas.sort((a, b) -> {
            int porQuantidade = Integer.compare(b.getValue(), a.getValue());
            return porQuantidade != 0 ? porQuantidade : a.getKey().compareToIgnoreCase(b.getKey());
        });

        Map<String, Integer> ordenado = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entrada : entradas) {
            ordenado.put(entrada.getKey(), entrada.getValue());
        }
        return Collections.unmodifiableMap(ordenado);
    }

    // adoções do mês ÷ animais resgatados até o fim do mês (0 se ainda não havia resgates)
    public double getTaxaAdocaoMensal(YearMonth mes) {
        if (mes == null) {
            throw new DadoInvalidoException("O mês é obrigatório.");
        }

        int adocoesNoMes = 0;
        for (Adocao adocao : adocaoServico.getAdocoes()) {
            if (YearMonth.from(adocao.getData()).equals(mes)) {
                adocoesNoMes++;
            }
        }

        LocalDate fimDoMes = mes.atEndOfMonth();
        int resgatadosAteOFim = 0;
        for (Animal animal : animalServico.getAnimais()) {
            LocalDate resgate = animal.getDataResgate();
            if (resgate == null || !resgate.isAfter(fimDoMes)) {
                resgatadosAteOFim++;
            }
        }

        if (resgatadosAteOFim == 0) {
            return 0.0;
        }
        return (double) adocoesNoMes / resgatadosAteOFim;
    }

    public double getTaxaAdocaoMensal() {
        return getTaxaAdocaoMensal(YearMonth.now());
    }

    public int getTotalVoluntariosAtivos() {
        int ativos = 0;
        for (Voluntario voluntario : usuarioServico.getVoluntarios()) {
            if (voluntario.isAtivo()) {
                ativos++;
            }
        }
        return ativos;
    }

    // b10

    public String relatorioSaude() {
        return "";
    }

    public String relatorioGeral() {
        return "";
    }
}