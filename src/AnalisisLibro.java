import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

public class AnalisisLibro {
    TreeMap<String, Integer> diccionarioLibro;
    String nombreFichero;

    public AnalisisLibro(String nombreFichero) {
        this.nombreFichero = nombreFichero;
    }

    private void contarApariciones(String palabra) {
        diccionarioLibro.put(palabra,diccionarioLibro.getOrDefault(palabra,0)+1);
    }
    private void leerFichero(){
        try(FileReader fileReader = new FileReader(this.nombreFichero);
        Scanner sc = new Scanner(fileReader)) {

            while (sc.hasNext()) contarApariciones(sc.next());

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
