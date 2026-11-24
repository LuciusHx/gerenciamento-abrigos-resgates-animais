package servicos;

import models.animal.Animal;
import models.animal.Porte;
import models.animal.StatusAdocao;
import models.notificacao.TipoNotificacao;
import models.usuario.Usuario;

import util.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import excecao.DadoInvalidoException;
import excecao.PermissaoNegadaException;
import excecao.RegistroNaoEncontradoException;
import excecao.RemocaoNaoPermitidaException;

public class AnimalServico {
    private final List<Animal> animais = new ArrayList<>();
    private final NotificacaoServico notificacaoServico;

    public AnimalServico (NotificacaoServico notificacaoServico){
        this.notificacaoServico = notificacaoServico;
    }

    private void exigirPermissao(Usuario executor) throws PermissaoNegadaException {
        if (executor == null){
            throw new DadoInvalidoException("Executor Inválido.");
        }
        if (!executor.podeGerenciarAnimais()){
            throw new PermissaoNegadaException("Você não tem permissão para gerenciar animais. ");
        }
    }

    public Animal buscarPorId(int id) throws RegistroNaoEncontradoException{
        for(Animal animal : animais){
            if (animal.getId() == id){
                return animal;
            } 
        } throw new RegistroNaoEncontradoException("Animal com id " + id + " não encontrado.");
        
    }

    public void cadastrar(Usuario executor, Animal animal) throws PermissaoNegadaException{
        if(animal == null) throw new DadoInvalidoException("O animal não pode estar vazio.");
        animais.add(animal);

        if(animais.contains(animal)){
            throw new DadoInvalidoException("Esse animal já está cadastrado.");
        }
        
        notificacaoServico.notificarTodos(TipoNotificacao.NOVO_ANIMAL, "novo animal no abrigo: " + animal.getNome() + " (" + animal.getEspecie() + ").");
    }

    public void editar(Usuario executor, int id, String nome, String raca, int idade, Porte porte)
        throws PermissaoNegadaException, RegistroNaoEncontradoException{
            exigirPermissao(executor);

            Animal animal = buscarPorId(id);
            animal.atualizarDados(nome, raca, idade, porte);
        }


    public void remover(Usuario executor, int id) throws PermissaoNegadaException, RegistroNaoEncontradoException, RemocaoNaoPermitidaException{
        exigirPermissao(executor);
        Animal animal = buscarPorId(id);

        if(animal.jaFoiAdotado()) throw new RemocaoNaoPermitidaException("O animal ja foi adotado.");
        animais.remove(animal);
    }

    public List<Animal> buscarPorNome(String nome){
        Validacoes.validarString(nome,"nome");

        String busca = nome.toLowerCase();
        List<Animal> resultado = new ArrayList<>();

        for (Animal animal : animais) {
            if (animal.getNome().toLowerCase().contains(busca)){
                resultado.add(animal);
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    public List<Animal> listarDisponiveis(){
        List<Animal> resultado = new ArrayList<>();

        for (Animal animal : animais) {
            if (animal.isDisponivel()){
                resultado.add(animal);
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    
    public List<Animal> filtrar(String especie, Porte porte, StatusAdocao status) {
        List<Animal> resultado = new ArrayList<>();

        for (Animal animal : animais) {
            boolean especieOk = especie == null || animal.getEspecie().equalsIgnoreCase(especie);
            boolean porteOk = porte == null || animal.getPorte() == porte;
            boolean statusOk = status == null || animal.getStatusAdocao() == status;

            if (especieOk && porteOk && statusOk) {
                resultado.add(animal);
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    public List<Animal> filtrar(String especie) {
        return filtrar(especie, null, null);
    }

    public List<Animal> filtrar(Porte porte) {
        return filtrar(null, porte, null);
    }

    public List<Animal> filtrar(StatusAdocao status) {
        return filtrar(null, null, status);
    }

    public List<Animal> getAnimais(){
        return Collections.unmodifiableList(animais);
    }

}
