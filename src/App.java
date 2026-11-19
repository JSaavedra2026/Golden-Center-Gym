import dao.ClienteDao;
import model.Cliente;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import dao.MembresiaDao;
import model.Membresia;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ClienteDao clienteDao = new ClienteDao();
    private static final MembresiaDao membresiaDao = new MembresiaDao();

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("\n=== GOLDEN CENTER GYM ===");
            System.out.println("1. Gestión de Clientes");
            System.out.println("2. Gestión de Membresías");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1:
                    menuClientes();
                    break;
                case 2:
                    menuMembresias();
                    break;
                case 0:
                    System.out.println("Hasta luego.");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    // ---------- MENÚ DE CLIENTES ----------
    private static void menuClientes() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE CLIENTES ---");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Listar clientes");
            System.out.println("3. Buscar cliente (documento o nombre)");
            System.out.println("4. Actualizar cliente");
            System.out.println("5. Desactivar cliente");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");
            opcion = leerEntero();

            try {
                switch (opcion) {
                    case 1:
                        registrarCliente();
                        break;
                    case 2:
                        mostrarClientes(clienteDao.listar());
                        break;
                    case 3:
                        buscarCliente();
                        break;
                    case 4:
                        actualizarCliente();
                        break;
                    case 5:
                        desactivarCliente();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (SQLException e) {
                System.out.println("Error en la base de datos: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void registrarCliente() throws SQLException {
        Cliente cliente = new Cliente();
        System.out.print("Nombre completo: ");
        cliente.setNombreCompleto(scanner.nextLine());
        System.out.print("Documento: ");
        cliente.setDocumento(scanner.nextLine());
        System.out.print("Correo: ");
        cliente.setCorreo(scanner.nextLine());
        System.out.print("Teléfono: ");
        cliente.setTelefono(scanner.nextLine());
        cliente.setEstado("Activo");

        if (clienteDao.insertar(cliente)) {
            System.out.println("Cliente registrado correctamente.");
        }
    }

    private static void buscarCliente() throws SQLException {
        System.out.print("Ingrese documento o nombre: ");
        String texto = scanner.nextLine();
        mostrarClientes(clienteDao.buscar(texto));
    }

    private static void actualizarCliente() throws SQLException {
        System.out.print("ID del cliente a actualizar: ");
        int id = leerEntero();
        Cliente cliente = new Cliente();
        cliente.setIdCliente(id);
        System.out.print("Nuevo nombre completo: ");
        cliente.setNombreCompleto(scanner.nextLine());
        System.out.print("Nuevo documento: ");
        cliente.setDocumento(scanner.nextLine());
        System.out.print("Nuevo correo: ");
        cliente.setCorreo(scanner.nextLine());
        System.out.print("Nuevo teléfono: ");
        cliente.setTelefono(scanner.nextLine());

        if (clienteDao.actualizar(cliente)) {
            System.out.println("Cliente actualizado correctamente.");
        } else {
            System.out.println("No se encontró un cliente con ese ID.");
        }
    }

    private static void desactivarCliente() throws SQLException {
        System.out.print("ID del cliente a desactivar: ");
        int id = leerEntero();
        if (clienteDao.desactivar(id)) {
            System.out.println("Cliente desactivado correctamente.");
        } else {
            System.out.println("No se encontró un cliente con ese ID.");
        }
    }

        // ---------- MENÚ DE MEMBRESÍAS ----------
    private static void menuMembresias() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE MEMBRESÍAS ---");
            System.out.println("1. Registrar membresía");
            System.out.println("2. Listar membresías");
            System.out.println("3. Consultar membresías de un cliente");
            System.out.println("4. Actualizar membresía");
            System.out.println("5. Desactivar membresía");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");
            opcion = leerEntero();

            try {
                switch (opcion) {
                    case 1:
                        registrarMembresia();
                        break;
                    case 2:
                        membresiaDao.actualizarVencidas();
                        mostrarMembresias(membresiaDao.listar());
                        break;
                    case 3:
                        consultarMembresiasCliente();
                        break;
                    case 4:
                        actualizarMembresia();
                        break;
                    case 5:
                        desactivarMembresia();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (SQLException e) {
                System.out.println("Error en la base de datos: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void registrarMembresia() throws SQLException {
        Membresia membresia = new Membresia();
        System.out.print("ID del cliente: ");
        membresia.setIdCliente(leerEntero());
        if (!elegirPlan(membresia)) {
            return;
        }        
        System.out.print("Fecha de inicio (AAAA-MM-DD): ");
        LocalDate fechaInicio = leerFecha();
        if (fechaInicio == null) {
            System.out.println("Fecha no válida.");
            return;
        }
        membresia.setFechaInicio(fechaInicio);
        membresia.setEstado("Activa");

        if (membresiaDao.insertar(membresia)) {
            System.out.println("Membresía registrada. Vence el " + membresia.getFechaVencimiento());
        }
    }

    private static void consultarMembresiasCliente() throws SQLException {
        System.out.print("ID del cliente: ");
        int idCliente = leerEntero();
        membresiaDao.actualizarVencidas();
        mostrarMembresias(membresiaDao.buscarPorCliente(idCliente));
    }

    private static void actualizarMembresia() throws SQLException {
        Membresia membresia = new Membresia();
        System.out.print("ID de la membresía a actualizar: ");
        membresia.setIdMembresia(leerEntero());
        if (!elegirPlan(membresia)) {
            return;
        }       

        if (membresiaDao.actualizar(membresia)) {
            System.out.println("Membresía actualizada correctamente.");
        } else {
            System.out.println("No se encontró una membresía con ese ID.");
        }
    }

    private static void desactivarMembresia() throws SQLException {
        System.out.print("ID de la membresía a desactivar: ");
        int id = leerEntero();
        if (membresiaDao.desactivar(id)) {
            System.out.println("Membresía desactivada correctamente.");
        } else {
            System.out.println("No se encontró una membresía con ese ID.");
        }
    }

    private static boolean elegirPlan(Membresia membresia) {
        System.out.println("1. Plan semanal (7 días) - $40.000");
        System.out.println("2. Plan quincenal (15 días) - $55.000");
        System.out.println("3. Plan mensual (30 días) - $65.000");
        System.out.println("4. Rutina por sesión (1 día) - $15.000");
        System.out.print("Seleccione el plan: ");
        switch (leerEntero()) {
            case 1:
                membresia.setTipoMembresia("Plan semanal");
                membresia.setDuracion(7);
                membresia.setCosto(40000);
                return true;
            case 2:
                membresia.setTipoMembresia("Plan quincenal");
                membresia.setDuracion(15);
                membresia.setCosto(55000);
                return true;
            case 3:
                membresia.setTipoMembresia("Plan mensual");
                membresia.setDuracion(30);
                membresia.setCosto(65000);
                return true;
            case 4:
                membresia.setTipoMembresia("Rutina por sesión");
                membresia.setDuracion(1);
                membresia.setCosto(15000);
                return true;
            default:
                System.out.println("Plan no válido.");
                return false;
        }
    }

    private static void mostrarMembresias(List<Membresia> membresias) {
        if (membresias.isEmpty()) {
            System.out.println("No se encontraron membresías.");
            return;
        }
        for (Membresia m : membresias) {
            System.out.println("ID: " + m.getIdMembresia()
                    + " | Cliente: " + m.getIdCliente()
                    + " | " + m.getTipoMembresia()
                    + " | Duración: " + m.getDuracion() + " día(s)"
                    + " | Costo: " + m.getCosto()
                    + " | Inicio: " + m.getFechaInicio()
                    + " | Vence: " + m.getFechaVencimiento()
                    + " | Estado: " + m.getEstado());
        }
    }

    private static double leerDecimal() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static LocalDate leerFecha() {
        try {
            return LocalDate.parse(scanner.nextLine().trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // ---------- MÉTODOS DE APOYO ----------
    private static void mostrarClientes(List<Cliente> clientes) {
        if (clientes.isEmpty()) {
            System.out.println("No se encontraron clientes.");
            return;
        }
        for (Cliente c : clientes) {
            System.out.println("ID: " + c.getIdCliente()
                    + " | " + c.getNombreCompleto()
                    + " | Doc: " + c.getDocumento()
                    + " | Correo: " + c.getCorreo()
                    + " | Tel: " + c.getTelefono()
                    + " | Estado: " + c.getEstado());
        }
    }

    private static int leerEntero() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}