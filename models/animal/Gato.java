package models.animal;
import java.util.List;
import java.time.LocalDate;

public class Gato extends Animal{
    
    public Gato(String nome, String raca, int idade, Porte porte, EstadoSaude estadoSaude, LocalDate dataResgate){
        super(nome,raca,idade,porte,estadoSaude,dataResgate);
    }

    @Override
    public String getEspecie(){
        return "Gato";
    }

    @Override
    public List<String> getVacinasObrigatorias(){
        return List.of("Antirrábica", "V4", "FeLV");
    }
}
