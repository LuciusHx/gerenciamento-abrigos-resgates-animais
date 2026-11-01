package models.animal;
import java.time.LocalDate;
import java.util.List;
import excecao.TratamentoCriticoException;

public class Animal {
    private String raca;
    private String nome;
    private int idade;
    private int id;
    private Porte porte;
    private EstadoSaude estadoSaude;
    private StatusAdocao statusAdocao;
    private LocalDate dataResgate;

    //construtor/setter/validacao só dps

    public String getRaca (){
        return raca;
    }

    public String getNome(){
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public int getId() {
        return id;
    }

    public Porte getPorte() {
        return porte;
    }

    public EstadoSaude getEstadoSaude(){
        return estadoSaude;
    }

    public StatusAdocao getStatusAdoca(){
        return statusAdocao;
    }

    //contrato combinado (B2 implementa de verdade)
    public abstract String getEspecie();
    public abstract List<String> getVacinasObrigatorias();

    public boolean isDisponivel(){
        return statusAdocao == StatusAdocao.DISPONIVEL;
    }

    public boolean jaFoiAdotado(){
        return false;
    }

    public void marcarComoAdotado() {}

    public void liberarParaAdocao()
        throws TratamentoCriticoException{}
    
}
