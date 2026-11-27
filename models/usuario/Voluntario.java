package models.usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import excecao.DadoInvalidoException;
import models.animal.Animal;
import models.atividade.Atividade;
import models.pessoa.Perfil;
import util.Constantes;


public class Voluntario extends Usuario {
    
    private final List<Animal> animais = new ArrayList<>();
    private final List<Atividade> historicoParticipacoes = new ArrayList<>();
    
    public Voluntario(String nome, String cpf, String email, String telefone, String login, String senha){
        super(nome, cpf, email, telefone, login, senha);
    }

    @Override
    public void exibirMenu() {
        System.out.println("==========================================");
        System.out.println("             ÁREA DO VOLUNTÁRIO");
        System.out.println("==========================================");

        System.out.println("1 - Visualizar Animais Disponíveis");
        System.out.println("2 - Registrar Solicitação de Adoção");
        System.out.println("3 - Consultar Solicitações de um Adotante");
        System.out.println("4 - Participar de Eventos");
        System.out.println("5 - Meus Animais");
        System.out.println("6 - Histórico de Participações");
        System.out.println("7 - Notificações");
        System.out.println("0 - Sair");

        System.out.println("\nEscolha uma opção:");
        System.out.print("> ");
    }

    public void adicionarAnimalResponsavel(Animal animal){
        if(animal == null){
            throw new DadoInvalidoException("Dado inválido.");
        }

        if(animais.contains(animal)){
            throw new DadoInvalidoException("Esse animal já possui cuidador.");
        }
        
        animais.add(animal);
    }

    public List<Animal> getAnimaisResponsavel(){
        List<Animal> animaisImutavel = Collections.unmodifiableList(animais);
        return animaisImutavel;
    }

    @Override
    public Perfil getPerfil() {
        return Perfil.VOLUNTARIO;
    }

    public void registrarParticipacao(Atividade atividade) {
        if (atividade == null) {
            throw new DadoInvalidoException("Atividade inválida.");
        }
        if (historicoParticipacoes.contains(atividade)) {
            throw new DadoInvalidoException("Participação em " + atividade.getTitulo() + " já registrada.");
        }
        historicoParticipacoes.add(atividade);
    }

    public List<Atividade> getHistoricoParticipacoes() {
        return Collections.unmodifiableList(historicoParticipacoes);
    }

    public boolean isAtivo() {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.minusDays(Constantes.DIAS_VOLUNTARIO_ATIVO);
        for (Atividade atividade : historicoParticipacoes) {
            LocalDate data = atividade.getData();
            if (!data.isBefore(limite) && !data.isAfter(hoje)) {
                return true;
            }
        }
        return false;
    }
}