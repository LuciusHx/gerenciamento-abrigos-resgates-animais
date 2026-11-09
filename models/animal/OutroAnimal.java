package models.animal;
import java.util.List;
import java.time.LocalDate;
import excecao.*;

public class OutroAnimal extends Animal{
    private final String especie;
    
    public OutroAnimal (String especie, String nome, String raca, int idade, Porte porte, EstadoSaude estadoSaude, LocalDate dataResgate){
        super(nome, raca, idade, porte, estadoSaude, dataResgate);
        if(especie == null || especie.isBlank()){
            throw new DadoInvalidoException("A especie do animal não pode ser vazia.");
        }
        this.especie = especie;
    }

    @Override
    public String getEspecie(){
        return especie;
    }

    @Override
    public List<String> getVacinasObrigatorias(){
        //serão definidas vacinas padrões.
        return List.of();
    }
}
