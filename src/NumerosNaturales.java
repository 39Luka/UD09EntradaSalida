import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase para gestionar una lista de números naturales.
 * Permite leer números desde un archivo, ingresar números por consola,
 * mostrar la lista, calcular la media, el máximo y guardar los datos en un archivo.
 */
public class NumerosNaturales {

    private ArrayList<Integer> listaNum;
    private final static String FICHERO = "numNaturales.txt";

    /**
     * Constructor que inicializa la lista de números y lee los datos del archivo.
     */
    public NumerosNaturales() {
        listaNum = new ArrayList<>();
        leerFichero();
    }

    /**
     * Lee los números desde un archivo de texto y los agrega a la lista.
     */
    private void leerFichero() {
        try (FileReader fileReader = new FileReader(FICHERO);
             Scanner scanner = new Scanner(fileReader)) {
            while (scanner.hasNext()) {
                listaNum.add(scanner.nextInt());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichero no encontrado");
        } catch (IOException e) {
            System.out.println("Fallo en la entrada y salida");
        }
    }

    /**
     * Permite al usuario ingresar números naturales por consola.
     * Los números negativos son ignorados, y el ciclo termina cuando el usuario ingresa -1.
     */
    public void leerNumerosUsuario() {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Introduzca los números (Cierre -1): ");
            int num;
            do {
                num = sc.nextInt();
                if (num >= 0) listaNum.add(num);
            } while (num != -1);
        }
    }

    /**
     * Guarda la lista de números en el archivo de texto.
     */
    public void guardarLista() {
        try (FileWriter fileWriter = new FileWriter(FICHERO);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            for (int i : listaNum) {
                printWriter.println(i);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichero no encontrado");
        } catch (IOException e) {
            System.out.println("Fallo en la entrada y salida");
        }
    }

    /**
     * Muestra todos los números de la lista en la consola.
     */
    public void mostrarLista() {
        listaNum.stream().forEach(System.out::println);
    }

    /**
     * Calcula y muestra la media de los números de la lista.
     */
    public void media() {
        System.out.println(listaNum.stream().mapToInt(Integer::intValue).average().orElse(0.0));
    }

    /**
     * Muestra el número máximo de la lista.
     */
    public void max() {
        System.out.println(listaNum.stream().mapToInt(Integer::intValue).max().orElse(0));
    }

    /**
     * Método principal que ejecuta las operaciones de leer números, mostrar la lista,
     * guardar la lista en un archivo, calcular la media y el máximo.
     */
    public static void main(String[] args) {
        NumerosNaturales numerosNaturales = new NumerosNaturales();
        numerosNaturales.leerNumerosUsuario();
        numerosNaturales.mostrarLista();
        numerosNaturales.guardarLista();
        numerosNaturales.media();
        numerosNaturales.max();
    }
}
