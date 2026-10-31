package contrato;
import java.time.LocalDate;

public interface Agendavel {
LocalDate getData();
default boolean venceEm(int dias) { 
    return true;
 }
}