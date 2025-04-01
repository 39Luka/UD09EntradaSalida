// Importación de librerías necesarias
import java.io.*; // Para operaciones de entrada/salida de archivos
import java.time.LocalDate; // Para trabajar con fechas
import java.time.LocalDateTime; // Para trabajar con fechas y horas
import java.time.format.DateTimeFormatter; // Para dar formato a las fechas
import java.util.Locale; // Para configurar la localización de fechas
import java.util.Scanner; // Para leer desde el archivo
import java.util.TreeMap; // Para almacenar los datos ordenados por fecha
import java.util.regex.Matcher; // Para trabajar con expresiones regulares
import java.util.regex.Pattern; // Para definir el patrón de la expresión regular

// Clase principal que lee y procesa los registros de acceso SSH desde un archivo de log
public class LecturaLog {
    // Constante para definir el nombre del archivo de log
    static final String FICHERO_LOG = "Linux_2k.log";

    // Expresión regular para extraer la fecha, el sshd y la IP de la línea de log
    String regex = "(?<fecha>[A-Z][a-z]{2} \\d{2} \\d{2}:\\d{2}:\\d{2}).*(?<sshd>sshd).*(?<rhost>rhost=(?<ip>\\S+))";

    // Estructura que almacena las fechas de acceso y las IPs en orden
    TreeMap<LocalDateTime, String> accesosSSH;

    // Constructor de la clase que recibe el nombre del archivo de log y llama a la función de lectura
    public LecturaLog(String nombreFichero) {
        accesosSSH = new TreeMap<>();
        leerDatosFichero(nombreFichero);
    }

    // Método que lee el archivo de log línea por línea
    private void leerDatosFichero(String nombreFichero) {
        try (FileReader fileReader = new FileReader(nombreFichero);
             Scanner sc = new Scanner(fileReader)) { // Utiliza Scanner para leer el archivo línea por línea

            // Mientras haya líneas en el archivo, se procesan
            while (sc.hasNextLine()) {
                añadirFechaRhost(sc.nextLine());
            }

        } catch (FileNotFoundException e) {
            System.out.println("Fichero no encontrado"); // Mensaje si el archivo no se encuentra
        } catch (IOException e) {
            System.out.println("Fallo en la entrada y salida"); // Mensaje si hay un error de I/O
        }
    }

    // Método que procesa cada línea para extraer la fecha y la IP usando la expresión regular
    private void añadirFechaRhost(String linea) {
        Pattern pattern = Pattern.compile(regex); // Compila la expresión regular
        Matcher matcher = pattern.matcher(linea); // Aplica la expresión regular a la línea

        // Si la expresión regular encuentra una coincidencia
        if (matcher.find()) {
            String fecha = matcher.group("fecha"); // Obtiene la fecha
            String ip = matcher.group("ip"); // Obtiene la IP

            // Convierte la fecha a un formato LocalDateTime y la agrega al TreeMap
            LocalDateTime fechaFormateada = parsearFecha(fecha);
            accesosSSH.put(fechaFormateada, ip);
        }
    }

    // Método que convierte una fecha en formato de string en un objeto LocalDateTime
    private LocalDateTime parsearFecha(String fecha) {
        int añoActual = LocalDate.now().getYear();  // Obtiene el año actual
        String fechaCompleta = añoActual + " " + fecha;  // Añade el año a la fecha

        // Define el formato de la fecha
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy MMM dd HH:mm:ss", Locale.ENGLISH);
        return LocalDateTime.parse(fechaCompleta, dateTimeFormatter); // Convierte la fecha en LocalDateTime
    }

    // Método que filtra y guarda los accesos entre dos fechas en un archivo
    public void accesosDesdeHasta(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        // Nombre del archivo donde se guardarán los accesos filtrados
        String nombreArchivo = "accesos_" + fechaInicio.toLocalDate() + "_" + fechaFin.toLocalDate() + ".txt";

        try (FileWriter fileWriter = new FileWriter(nombreArchivo);
             PrintWriter printWriter = new PrintWriter(fileWriter)) { // Crea un archivo para escribir los accesos

            // Itera sobre las fechas de acceso y escribe las que están dentro del rango
            for (LocalDateTime l : accesosSSH.keySet()) {
                if ((l.isAfter(fechaInicio) || l.isEqual(fechaInicio)) && (l.isBefore(fechaFin) || l.isEqual(fechaFin))) {
                    printWriter.println(l + " " + accesosSSH.get(l)); // Escribe la fecha y la IP en el archivo
                }
            }

        } catch (IOException e) {
            System.out.println("Error al escribir el archivo"); // Mensaje si hay un error al escribir el archivo
        }
    }

    // Método principal que crea una instancia de LecturaLog y filtra los accesos en un rango de fechas
    public static void main(String[] args) {
        LecturaLog lecturaLog = new LecturaLog(FICHERO_LOG); // Crea una instancia de la clase con el archivo de log
        LocalDateTime inicio = LocalDateTime.of(2025, 6, 5, 10, 5, 5); // Define la fecha de inicio
        LocalDateTime fin = LocalDateTime.of(2025, 7, 5, 10, 5, 5); // Define la fecha de fin
        lecturaLog.accesosDesdeHasta(inicio, fin); // Filtra y guarda los accesos entre las dos fechas
    }
}
