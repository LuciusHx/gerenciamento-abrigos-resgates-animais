package models.animal;
import java.time.LocalDate;
import java.util.List;
import excecao.TratamentoCriticoException;
import excecao.DadoInvalidoException;

public abstract class Animal {

    // contador estático o "número da próxima ficha" e o total cadastrado
    private static int totalCadastrados = 0;

    private final int id;
    private String raca;
    private String nome;
    private int idade;
    private Porte porte;
    private EstadoSaude estadoSaude;
    private StatusAdocao statusAdocao;
    private final LocalDate dataResgate;
    private boolean adotadoAlgumaVez = false;

    
    protected Animal (String nome, String raca, int idade, Porte porte, EstadoSaude estadoSaude, LocalDate dataResgate){
        if(nome == null || nome.isBlank()){
            throw new DadoInvalidoException("O nome do animal não pode ser vazio.");
        }
        if(raca == null || raca.isBlank()){
            throw new DadoInvalidoException("A raça do animal não pode ser vazia.");
        }
        if(idade < 0){
            throw new DadoInvalidoException("A idade do animal não pode ser inferior a zero");
        }
        if(porte == null || estadoSaude == null){
            throw new DadoInvalidoException("O porte e o estado de saúde não podem estar vazios.");
        }
        if(dataResgate == null || dataResgate.isAfter(LocalDate.now())){
            throw new DadoInvalidoException("Data inválida.");
        }

        this.nome = nome;
        this.raca = raca;
        this.idade = idade;
        this.porte = porte;
        this.estadoSaude = estadoSaude;
        this.dataResgate = dataResgate;

        if(estadoSaude == EstadoSaude.CRITICO){
            this.statusAdocao = StatusAdocao.INDISPONIVEL;
        }
        else{
            this.statusAdocao = StatusAdocao.DISPONIVEL;
        }

        totalCadastrados++;
        this.id = totalCadastrados;
    }

    
    //polimofirsmo:
    public abstract String getEspecie();
    public abstract List<String> getVacinasObrigatorias();


    public boolean isDisponivel(){
        return statusAdocao == StatusAdocao.DISPONIVEL;
    }

    public boolean jaFoiAdotado(){
        return adotadoAlgumaVez;
    }


    public void marcarComoAdotado(){
        if(!isDisponivel()){
            throw new DadoInvalidoException("O animal " + nome + " não está disponível.");
        }
        this.statusAdocao = StatusAdocao.ADOTADO;
        this.adotadoAlgumaVez = true;
    }

    public void liberarParaAdocao() throws TratamentoCriticoException{
        if(estadoSaude == EstadoSaude.CRITICO){
            throw new TratamentoCriticoException("O animal " + nome + " está em estado crítico e não pode ser adotado");    
        }
        this.statusAdocao = StatusAdocao.DISPONIVEL;
        
    }

    public static int getTotalCadastrados() { 
        return totalCadastrados; 
    }

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

    public StatusAdocao getStatusAdocao(){
        return statusAdocao;
    }

    public LocalDate getDataResgate(){
        return dataResgate;
    }

    @Override
    public String toString(){
        return "#" + id + " " + nome + " (" + getEspecie() + ", " + raca + ") - " + statusAdocao;
    }

}
