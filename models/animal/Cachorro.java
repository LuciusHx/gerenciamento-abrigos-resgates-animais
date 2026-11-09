package models.animal;
import java.util.List;
import java.time.LocalDate;

public class Cachorro extends Animal{
    
    public Cachorro(String nome, String raca, int idade, Porte porte, EstadoSaude estadoSaude, LocalDate dataResgate){
        super(nome, raca, idade, porte, estadoSaude, dataResgate);
    }

    @Override
    public String getEspecie(){
        return "Cachorro";
    }

    @Override
    public List<String> getVacinasObrigatorias(){
        return List.of("Antirrábica", "V10", "Leishmaniose");
    }
}
