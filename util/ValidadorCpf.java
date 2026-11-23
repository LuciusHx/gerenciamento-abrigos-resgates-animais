package util;

public final class ValidadorCpf {
    private ValidadorCpf(){}

    public static boolean eValido(String cpf){
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        int soma = 0;
        int resto;

        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }

        resto = (soma * 10) % 11;
        if (resto == 10) {
            resto = 0;
        }

        if (resto != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        soma = 0;

        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }

        resto = (soma * 10) % 11;
        if (resto == 10) {
            resto = 0;
        }

        return resto == Character.getNumericValue(cpf.charAt(10));
    }
}
