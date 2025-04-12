import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase que representa una Agenda de contactos.
 * Lee y escribe contactos desde un fichero de acceso aleatorio.
 */
public class Agenda {

    /** Nombre del fichero donde se guardan los contactos */
    private static final String NOMBRE_FICHERO = "agenda.dat";

    /** Lista de contactos en memoria */
    private List<Contacto> contactos;

    /**
     * Constructor de Agenda.
     * Carga los contactos del fichero si existen.
     */
    public Agenda() {
        contactos = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(NOMBRE_FICHERO, "rw")) {
            long total = raf.length() / Contacto.SIZE_REGISTRO;

            // Leemos todos los contactos uno por uno
            for (int i = 0; i < total; i++) {
                Contacto c = Contacto.leerContacto(raf, i);
                if (c.getId() != -1) { // Solo guardamos los que no están borrados
                    contactos.add(c);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer la agenda: " + e.getMessage());
        }
    }

    /**
     * Crea un nuevo contacto en la agenda y lo guarda al final del fichero.
     * @param contacto Contacto nuevo a añadir.
     */
    public void creaContacto(Contacto contacto) {
        if (!contactos.contains(contacto)) {
            contactos.add(contacto);
            try (RandomAccessFile raf = new RandomAccessFile(NOMBRE_FICHERO, "rw")) {
                raf.seek(raf.length()); // Vamos al final del fichero
                Contacto.escribirContacto(raf, contacto);
            } catch (IOException e) {
                System.out.println("Error al guardar el contacto: " + e.getMessage());
            }
        } else {
            System.out.println("El contacto ya existe.");
        }
    }

    /**
     * Busca un contacto por su id.
     * @param id ID del contacto a buscar.
     * @return El contacto si existe, si no, null.
     */
    public Contacto buscarContacto(int id) {
        for (Contacto c : contactos) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    /**
     * Modifica un contacto si existe.
     * @param contacto Contacto modificado.
     */
    public void modificaContacto(Contacto contacto) {
        int pos = contactos.indexOf(contacto);
        if (pos != -1) {
            contactos.set(pos, contacto);
            try (RandomAccessFile raf = new RandomAccessFile(NOMBRE_FICHERO, "rw")) {
                Contacto.escribirContacto(raf, contacto, pos);
            } catch (IOException e) {
                System.out.println("Error al modificar el contacto: " + e.getMessage());
            }
        } else {
            System.out.println("El contacto no existe.");
        }
    }

    /**
     * Borra un contacto marcando su ID como -1 en el fichero.
     * @param contacto Contacto a borrar.
     */
    public void borrarContacto(Contacto contacto) {
        int pos = contactos.indexOf(contacto);
        if (pos != -1) {
            contactos.remove(pos);
            try (RandomAccessFile raf = new RandomAccessFile(NOMBRE_FICHERO, "rw")) {
                contacto.setId(-1); // Marcamos como borrado
                Contacto.escribirContacto(raf, contacto, pos);
            } catch (IOException e) {
                System.out.println("Error al borrar el contacto: " + e.getMessage());
            }
        } else {
            System.out.println("Contacto no encontrado.");
        }
    }

    /**
     * Ordena la agenda por nombre y guarda toda la lista en el fichero.
     */
    public void ordenarAgenda() {
        Collections.sort(contactos); // Se necesita que Contacto implemente Comparable
        try (RandomAccessFile raf = new RandomAccessFile(NOMBRE_FICHERO, "rw")) {
            for (int i = 0; i < contactos.size(); i++) {
                Contacto.escribirContacto(raf, contactos.get(i), i);
            }
        } catch (IOException e) {
            System.out.println("Error al ordenar y guardar la agenda: " + e.getMessage());
        }
    }

    /**
     * Muestra todos los contactos de la agenda.
     */
    public void mostrarAgenda() {
        contactos.forEach(System.out::println);
    }

    /**
     * Muestra los contactos cuyo nombre contenga el texto indicado.
     * @param nombre Texto a buscar en los nombres.
     */
    public void mostrarCoincidencias(String nombre) {
        contactos.stream()
                .filter(c -> c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .forEach(System.out::println);
    }

    /**
     * Método principal para ejecutar la agenda.
     * Menú interactivo por consola.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Agenda agenda = new Agenda();
        int opcion;

        do {
            System.out.println("""
                    1. Mostrar Agenda
                    2. Ordenar Agenda
                    3. Crear Contacto
                    4. Modificar Contacto
                    5. Buscar contacto por nombre
                    6. Salir
                    Elige opción: """);
            opcion = sc.nextInt();
            sc.nextLine(); // Limpiar buffer

            switch (opcion) {
                case 1 -> agenda.mostrarAgenda();
                case 2 -> agenda.ordenarAgenda();
                case 3 -> {
                    System.out.print("ID: ");
                    int id = sc.nextInt(); sc.nextLine();
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Dirección: ");
                    String direccion = sc.nextLine();
                    System.out.print("Teléfono: ");
                    String telefono = sc.nextLine();
                    agenda.creaContacto(new Contacto(id, nombre, direccion, telefono));
                }
                case 4 -> {
                    System.out.print("ID del contacto a modificar: ");
                    int id = sc.nextInt(); sc.nextLine();
                    System.out.print("Nuevo nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Nueva dirección: ");
                    String direccion = sc.nextLine();
                    System.out.print("Nuevo teléfono: ");
                    String telefono = sc.nextLine();
                    agenda.modificaContacto(new Contacto(id, nombre, direccion, telefono));
                }
                case 5 -> {
                    System.out.print("Texto a buscar en nombres: ");
                    String texto = sc.nextLine();
                    agenda.mostrarCoincidencias(texto);
                }
                case 6 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 6);

        sc.close();
    }
}
