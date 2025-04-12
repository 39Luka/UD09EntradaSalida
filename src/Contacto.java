import java.io.IOException;
import java.io.RandomAccessFile;

class Contacto {
    //tamaño fijo de los campos
    final static int LONGITUD_NOMBRE = 20;
    final static int LONGITUD_DIRECCION = 30;
    final static int LONGITUD_TELEFONO = 10;
    //tamaño de registro. Es necesario conocer el tamaño para mover el cursor del fichero correctamente
    final static int SIZE_REGISTRO =4+//tamaño int
            //tamaño de los string
            (LONGITUD_NOMBRE + LONGITUD_DIRECCION + LONGITUD_TELEFONO)
            +6;//2 bytes por cada string que guarda el tamaño del string en el fichero

    private int id;
    private String nombre;
    private String direccion;
    private String telefono;

    public Contacto(int id, String nombre, String direccion, String telefono) {
        this.id = id;
        setNombre(nombre);
        setDireccion(direccion);
        setTelefono(telefono);
    }

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
    //Los setter se encargan de ajusta los string al tamaño exacto
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
    //ajusta la longitud de la cadena a la longitud deseada
    public static String ajustarLongitud(String cadena, int longitud) {
        if (cadena.length() >= longitud) {
            // Si la longitud de la cadena es mayor o igual a la longitud deseada, se recorta
            return cadena.substring(0, longitud);
        } else {
            // Si la longitud de la cadena es menor que la longitud deseada, se rellena con espacios
            StringBuilder sb = new StringBuilder(cadena);
            while (sb.length() < longitud) {
                sb.append(' ');
            }
            return sb.toString();
        }
    }
    @Override
    public String toString() {
        return id + " " + nombre + " " + direccion + " " + telefono;
    }
    // Método para escribir el objeto Contacto en un RandomAccessFile en la posición actual
    public static void escribirContacto(RandomAccessFile raf, Contacto contacto) throws IOException {
        raf.writeInt(contacto.getId());
        //writeUTF escribe en el fichero el tamaño del string al inicio del string 
        raf.writeUTF(contacto.getNombre());
        raf.writeUTF(contacto.getDireccion());
        raf.writeUTF(contacto.getTelefono());

    }
    //Escribe un contacto en la posisión indicada
    public static void escribirContacto(RandomAccessFile raf,Contacto contacto,int posicion) throws IOException {
        //Posicionamos el cursor en la posicion indicada              
        if(raf.length() > (posicion*SIZE_REGISTRO)){
            raf.seek(posicion*SIZE_REGISTRO);
            escribirContacto(raf,contacto);
        }else throw new IOException("Posicion fuera de rango");
    }

    // Método para leer el objeto Contacto desde un RandomAccessFile en la posición actual
    public  static Contacto leerContacto(RandomAccessFile raf) throws IOException {
        int id = raf.readInt();
        //el metodo readUTF lee el string de tamaño indicado al inicio
        String nombre = raf.readUTF();
        String direccion = raf.readUTF();
        String telefono = raf.readUTF();

        return new Contacto(id, nombre, direccion, telefono);
    }
    //Leer un contacto posicionando el cursor en la posicion indicada
    public static Contacto leerContacto(RandomAccessFile raf, int posicion) throws IOException {
        //Posicionamos el cursor en la posición indicada
        if(raf.length() > (posicion*SIZE_REGISTRO)){
            raf.seek(posicion*SIZE_REGISTRO);
            return leerContacto(raf);
        }else throw new IOException("Posicion fuera de rango");

    }
}