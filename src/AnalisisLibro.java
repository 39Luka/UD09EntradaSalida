import java.io.*;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class AnalisisLibro {
    TreeMap<String,Integer> diccionarioLibro = new TreeMap<>();
    String nombreFichero;

    public AnalisisLibro(String nombreFichero){
        this.nombreFichero=nombreFichero;
        leerFichero();
    }

    private void leerFichero(){
       String lineaLimpia;
       String[] palabras;
        try (FileReader fileReader = new FileReader(nombreFichero);
             Scanner sc = new Scanner(fileReader)){

            while (sc.hasNext()){
               lineaLimpia = eliminarSimbolos(sc.nextLine());
               palabras = separarPalabras(lineaLimpia);
               añadirPalabras(palabras);

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String eliminarSimbolos(String linea){
        return linea.replaceAll("[\\p{P}\\p{S}]", "");
    }
    private String[] separarPalabras(String linea){
        return linea.split(" ");
    }
    private void añadirPalabras(String[] palabras){
        for(String s : palabras){
            diccionarioLibro.put(s,diccionarioLibro.getOrDefault(s,0)+1);
        }
    }



    public void guardarResultado(String nombreFichero){
        try(FileWriter fileWriter =new FileWriter(nombreFichero);
            PrintWriter printWriter = new PrintWriter(fileWriter)) {

            for (String s : diccionarioLibro.keySet()) printWriter.println(s +" "+ diccionarioLibro.get(s));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    public static void main(String[] args) {
        AnalisisLibro analisisLibro = new AnalisisLibro("Quijote.txt");
        analisisLibro.guardarResultado("a.txt");
    }
}
