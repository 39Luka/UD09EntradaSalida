import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Clase que representa un contacto con un ID, nombre, dirección y teléfono.
 * Implementa Comparable para permitir la comparación por nombre.
 */
class Contacto implements Comparable<Contacto> {

    // Tamaño fijo de los campos
    final static int LONGITUD_NOMBRE = 20;
    final static int LONGITUD_DIRECCION = 30;
    final static int LONGITUD_TELEFONO = 10;

    // Tamaño del registro en bytes para acceder correctamente al archivo
    final static int SIZE_REGISTRO = 4 + // tamaño del int id
            (LONGITUD_NOMBRE + LONGITUD_DIRECCION + LONGITUD_TELEFONO) +
            6; // 2 bytes por cada campo String (UTF incluye longitud)

    private int id;
    private String nombre;
    private String direccion;
    private String telefono;

    /**
     * Constructor de la clase Contacto.
     *
     * @param id        ID del contacto
     * @param nombre    Nombre del contacto
     * @param direccion Dirección del contacto
     * @param telefono  Teléfono del contacto
     */
    public Contacto(int id, String nombre, String direccion, String telefono) {
        this.id = id;
        setNombre(nombre);
        setDireccion(direccion);
        setTelefono(telefono);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    // Setters con ajuste automático de longitud
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = ajustarLongitud(nombre, LONGITUD_NOMBRE);
    }

    public void setDireccion(String direccion) {
        this.direccion = ajustarLongitud(direccion, LONGITUD_DIRECCION);
    }

    public void setTelefono(String telefono) {
        this.telefono = ajustarLongitud(telefono, LONGITUD_TELEFONO);
    }

    /**
     * Ajusta la longitud de una cadena al tamaño especificado.
     *
     * @param cadena   Cadena a ajustar
     * @param longitud Longitud deseada
     * @return Cadena ajustada
     */
    public static String ajustarLongitud(String cadena, int longitud) {
        if (cadena.length() >= longitud) {
            return cadena.substring(0, longitud);
        } else {
            StringBuilder sb = new StringBuilder(cadena);
            while (sb.length() < longitud) {
                sb.append(' ');
            }
            return sb.toString();
        }
    }

    /**
     * Representación en texto del contacto.
     */
    @Override
    public String toString() {
        return id + " " + nombre + " " + direccion + " " + telefono;
    }

    /**
     * Compara este contacto con otro por nombre (alfabéticamente).
     *
     * @param otroContacto Otro contacto con el que se compara
     * @return Un número negativo, cero o positivo si este contacto es menor, igual
     *         o mayor al otro.
     */
    @Override
    public int compareTo(Contacto otroContacto) {
        return this.nombre.trim().compareToIgnoreCase(otroContacto.nombre.trim());
    }

    /**
     * Escribe un contacto en la posición actual del archivo.
     *
     * @param raf      Archivo de acceso aleatorio
     * @param contacto Contacto a escribir
     * @throws IOException Si ocurre un error al escribir
     */
    public static void escribirContacto(RandomAccessFile raf, Contacto contacto) throws IOException {
        raf.writeInt(contacto.getId());
        raf.writeUTF(contacto.getNombre());
        raf.writeUTF(contacto.getDireccion());
        raf.writeUTF(contacto.getTelefono());
    }

    /**
     * Escribe un contacto en la posición indicada del archivo.
     *
     * @param raf      Archivo de acceso aleatorio
     * @param contacto Contacto a escribir
     * @param posicion Posición (registro) donde escribir
     * @throws IOException Si la posición es inválida o hay error de escritura
     */
    public static void escribirContacto(RandomAccessFile raf, Contacto contacto, int posicion) throws IOException {
        if (raf.length() > (posicion * SIZE_REGISTRO)) {
            raf.seek(posicion * SIZE_REGISTRO);
            escribirContacto(raf, contacto);
        } else {
            throw new IOException("Posicion fuera de rango");
        }
    }

    /**
     * Lee un contacto desde la posición actual del archivo.
     *
     * @param raf Archivo de acceso aleatorio
     * @return Contacto leído
     * @throws IOException Si ocurre un error al leer
     */
    public static Contacto leerContacto(RandomAccessFile raf) throws IOException {
        int id = raf.readInt();
        String nombre = raf.readUTF();
        String direccion = raf.readUTF();
        String telefono = raf.readUTF();
        return new Contacto(id, nombre, direccion, telefono);
    }

    /**
     * Lee un contacto desde una posición específica del archivo.
     *
     * @param raf      Archivo de acceso aleatorio
     * @param posicion Posición (registro) donde leer
     * @return Contacto leído
     * @throws IOException Si la posición es inválida o hay error de lectura
     */
    public static Contacto leerContacto(RandomAccessFile raf, int posicion) throws IOException {
        if (raf.length() > (posicion * SIZE_REGISTRO)) {
            raf.seek(posicion * SIZE_REGISTRO);
            return leerContacto(raf);
        } else {
            throw new IOException("Posicion fuera de rango");
        }
    }
}
