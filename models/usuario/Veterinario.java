package models.usuario;

import contrato.EmissorRelatorio;
import servicos.RelatorioServico;
import util.Validacoes;

public class Veterinario extends Usuario implements EmissorRelatorio{
    //registro do veterinario 
    private String crmv;

    public Veterinario(String nome, String cpf, String email, String telefone, String login, String senha, String crmv){
        super(nome, cpf, email, telefone, login, senha);
        Validacoes.validarString(crmv, "CRMV");
        this.crmv = crmv;
    }

    @Override
    public void exibirMenu() {
        System.out.println("==========================================");
        System.out.println("             ÁREA DO VETERINÁRIO");
        System.out.println("==========================================");

        System.out.println("1 - Consultar Animais");
        System.out.println("2 - Registrar Atendimento");
        System.out.println("3 - Atualizar Tratamento");
        System.out.println("4 - Registrar Vacinação");
        System.out.println("5 - Emitir Relatório Médico");
        System.out.println("6 - Notificações");
        System.out.println("0 - Sair");

        System.out.println("\nEscolha uma opção:");
        System.out.print("> ");
        
    }

    @Override
    public boolean podeAlterarDadosMedicos(){ return true; }

    public String getCRMV(){
        return this.crmv;
    }

    public void setCRMV(String crmv){
        Validacoes.validarString(crmv, "CRMV");
        this.crmv = crmv;
    }

    @Override 
    public String emitirRelatorio(RelatorioServico servico){
        return servico.relatorioSaude();
    }
}
