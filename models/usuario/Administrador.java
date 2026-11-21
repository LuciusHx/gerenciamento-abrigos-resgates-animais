package models.usuario;

import contrato.EmissorRelatorio;
import servicos.RelatorioServico;

public class Administrador extends Usuario implements EmissorRelatorio{

    public Administrador(String nome, String cpf, String email, String telefone, String login, String senha){
        super(nome, cpf, email, telefone, login, senha);
    }

    @Override 
    public void exibirMenu(){
        System.out.println("==========================================");
        System.out.println("             ÁREA DO ADMINISTRADOR");
        System.out.println("==========================================");

        System.out.println("1 - Gerenciar Animais");
        System.out.println("2 - Gerenciar Usuários");
        System.out.println("3 - Aprovar Adoções");
        System.out.println("4 - Gerenciar Eventos");
        System.out.println("5 - Relatórios");
        System.out.println("6 - Estatísticas");
        System.out.println("7 - Notificações");
        System.out.println("0 - Sair");

        System.out.println("\nEscolha uma opção:");
        System.out.print("> ");
    }

    @Override 
    public boolean podeAprovarAdocao(){ return true; }
    @Override 
    public boolean podeGerenciarAnimais(){ return true; }
    @Override
    public boolean podeGerenciarUsuarios(){ return true; }

    @Override 
    public String emitirRelatorio(RelatorioServico servico){
        return servico.relatorioGeral();
    }

}
