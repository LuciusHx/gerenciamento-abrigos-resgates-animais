package contrato;

import servicos.RelatorioServico;

public interface EmissorRelatorio {
    String emitirRelatorio(RelatorioServico servico);
}