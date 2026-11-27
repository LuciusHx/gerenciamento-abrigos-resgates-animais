package ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

//toda entrada do usuario vai passar por aqui e os metodos repetem a pergunta ate receber um valor valido
public final class Console {
    private static final Scanner ENTRADA = new Scanner(System.in);
    public static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Console() {
    }

    public static String lerTexto(String rotulo) {
        while (true) {
            System.out.print(rotulo + ": ");
            String texto = ENTRADA.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            erro("Este campo é obrigatório.");
        }
    }

    // Enter vazio devolve null ("não alterar" / "sem filtro")
    public static String lerTextoOpcional(String rotulo) {
        System.out.print(rotulo + " (Enter para pular): ");
        String texto = ENTRADA.nextLine().trim();
        return texto.isEmpty() ? null : texto;
    }

    public static int lerInt(String rotulo) {
        while (true) {
            if (rotulo != null) {
                System.out.print(rotulo + ": ");
            }
            String texto = ENTRADA.nextLine().trim();
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                erro("Digite um número inteiro.");
                if (rotulo == null) {
                    System.out.print("> ");
                }
            }
        }
    }

    // lê a opção depois que o exibirMenu() já imprimiu o "> "
    public static int lerOpcao() {
        return lerInt(null);
    }

    public static double lerDouble(String rotulo) {
        while (true) {
            System.out.print(rotulo + ": ");
            String texto = ENTRADA.nextLine().trim().replace(",", ".");
            try {
                return Double.parseDouble(texto);
            } catch (NumberFormatException e) {
                erro("Digite um número (ex.: 1500,00).");
            }
        }
    }

    public static LocalDate lerData(String rotulo) {
        while (true) {
            System.out.print(rotulo + " (dd/mm/aaaa, Enter = hoje): ");
            String texto = ENTRADA.nextLine().trim();
            if (texto.isEmpty()) {
                return LocalDate.now();
            }
            try {
                return LocalDate.parse(texto, FORMATO_DATA);
            } catch (DateTimeParseException e) {
                erro("Data inválida. Use o formato dd/mm/aaaa.");
            }
        }
    }

    public static <E extends Enum<E>> E escolherEnum(String rotulo, E[] valores) {
        System.out.println(rotulo + ":");
        for (int i = 0; i < valores.length; i++) {
            System.out.println("  " + (i + 1) + " - " + valores[i]);
        }
        while (true) {
            int opcao = lerInt("Escolha");
            if (opcao >= 1 && opcao <= valores.length) {
                return valores[opcao - 1];
            }
            erro("Opção inválida.");
        }
    }

    // igual ao escolherEnum mas aceita 0
    public static <E extends Enum<E>> E escolherEnumOpcional(String rotulo, E[] valores) {
        System.out.println(rotulo + ":");
        System.out.println("  0 - Qualquer");
        for (int i = 0; i < valores.length; i++) {
            System.out.println("  " + (i + 1) + " - " + valores[i]);
        }
        while (true) {
            int opcao = lerInt("Escolha");
            if (opcao == 0) {
                return null;
            }
            if (opcao >= 1 && opcao <= valores.length) {
                return valores[opcao - 1];
            }
            erro("Opção inválida.");
        }
    }

    public static boolean confirmar(String pergunta) {
        System.out.print(pergunta + " (s/n): ");
        return ENTRADA.nextLine().trim().equalsIgnoreCase("s");
    }

    public static void titulo(String texto) {
        System.out.println();
        System.out.println("---------- " + texto + " ----------");
    }

    public static void sucesso(String mensagem) {
        System.out.println("[OK] " + mensagem);
    }

    public static void erro(String mensagem) {
        System.out.println("[ERRO] " + mensagem);
    }

    public static void listar(List<?> itens, String mensagemSeVazio) {
        if (itens.isEmpty()) {
            System.out.println(mensagemSeVazio);
            return;
        }
        for (Object item : itens) {
            System.out.println("  " + item);
        }
    }

    public static void pausar() {
        System.out.print("\nPressione Enter para continuar...");
        ENTRADA.nextLine();
    }
}