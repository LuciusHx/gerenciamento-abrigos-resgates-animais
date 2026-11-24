package contrato;

import java.time.LocalDate;

public interface Agendavel {

    LocalDate getData();

    default boolean venceEm(int dias) {
        LocalDate hoje = LocalDate.now();
        LocalDate data = getData();
        return data != null && !data.isBefore(hoje) && !data.isAfter(hoje.plusDays(dias));
    }
}