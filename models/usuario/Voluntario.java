package models.usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import excecao.DadoInvalidoException;
import models.animal.Animal;


public class Voluntario extends Usuario {
    
    private final List<Animal> animais = new ArrayList<>();
    
    public Voluntario(String nome, String cpf, String email, String telefone, String login, String senha){
        super(nome, cpf, email, telefone, login, senha);
    }

    @Override
    public void exibirMenu() {
        System.out.println("==========================================");
        System.out.println("             ÁREA DO VOLUNTÁRIO");
        System.out.println("==========================================");

        System.out.println("1 - Visualizar Animais Disponíveis");
        System.out.println("2 - Solicitar Adoção");
        System.out.println("3 - Consultar Minhas Solicitações");
        System.out.println("4 - Participar de Eventos");
        System.out.println("5 - Histórico de Participações");
        System.out.println("6 - Notificações");
        System.out.println("0 - Sair");

        System.out.println("\nEscolha uma opção:");
        System.out.print("> ");

        System.out.println();
    }

    public void adicionarAnimalResponsavel(Animal animal){
        if(animal == null){
            new DadoInvalidoException("Dado inválido.");
        }

        if(animais.contains(animal)){
            new DadoInvalidoException("Esse animal já possui cuidador.");
        }
        
        animais.add(animal);
    }

    public List<Animal> getAnimaisResponsavel(){
        List<Animal> animaisImutavel = Collections.unmodifiableList(animais);
        return animaisImutavel;
    }
}
