import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LecturaLog {
    static final String FICHERO_LOG = "Linux_2k.log";
    String regex = "(?<fecha>[A-Z][a-z]{2} \\d{2} \\d{2}:\\d{2}:\\d{2}).*(?<sshd>sshd).*(?<rhost>rhost=(?<ip>\\S+))";

    TreeMap<LocalDateTime, String> accesosSSH;

    public LecturaLog(String nombreFichero) {
        accesosSSH = new TreeMap<>();
        leerDatosFichero(nombreFichero);
    }

    private void leerDatosFichero(String nombreFichero) {
        try (FileReader fileReader = new FileReader(nombreFichero);
             Scanner sc = new Scanner(fileReader)) {

            while (sc.hasNextLine()) {
                añadirFechaRhost(sc.nextLine());
            }

        } catch (FileNotFoundException e) {
            System.out.println("Fichero no encontrado");
        } catch (IOException e) {
            System.out.println("Fallo en la entrada y salida");
        }
    }

    private void añadirFechaRhost(String linea) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(linea);

        if (matcher.find()) {
            String fecha = matcher.group("fecha");
            String ip = matcher.group("ip");

            LocalDateTime fechaFormateada = parsearFecha(fecha);
            accesosSSH.put(fechaFormateada, ip);
        }
    }

    private LocalDateTime parsearFecha(String fecha) {
        int añoActual = LocalDate.now().getYear();  // Se obtiene el año actual
        String fechaCompleta = añoActual + " " + fecha;  // Se concatena el año

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy MMM dd HH:mm:ss", Locale.ENGLISH);
        return LocalDateTime.parse(fechaCompleta, dateTimeFormatter);
    }

    public void accesosDesdeHasta(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        String nombreArchivo = "accesos_" + fechaInicio.toLocalDate() + "_" + fechaFin.toLocalDate() + ".txt";

        try (FileWriter fileWriter = new FileWriter(nombreArchivo);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            for (LocalDateTime l : accesosSSH.keySet()) {
                if ((l.isAfter(fechaInicio) || l.isEqual(fechaInicio)) && (l.isBefore(fechaFin) || l.isEqual(fechaFin))) {
                    printWriter.println(l + " " + accesosSSH.get(l));
                }
            }

        } catch (IOException e) {
            System.out.println("Error al escribir el archivo");
        }
    }

    public static void main(String[] args) {
        LecturaLog lecturaLog = new LecturaLog(FICHERO_LOG);
        LocalDateTime inicio = LocalDateTime.of(2025, 6, 5, 10, 5, 5);
        LocalDateTime fin = LocalDateTime.of(2025, 7, 5, 10, 5, 5);
        lecturaLog.accesosDesdeHasta(inicio, fin);
    }
}
