import java.sql.*;
import java.util.Scanner;

public class Start {
    private static final String URL = "jdbc:mysql://localhost:3306/productos";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            do {
                System.out.println("******************************");
                System.out.println("GESTOR DE PRODUCTOS");
                System.out.println("******************************");
                System.out.println("1) Crear un producto");
                System.out.println("2) Listar todos los productos");
                System.out.println("3) Modificar un producto");
                System.out.println("4) Borrar un producto");
                System.out.println("0) Salir del programa");
                System.out.print("Escriba una opción: ");
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1 -> crearProducto(connection, scanner);
                    case 2 -> listarProductos(connection);
                    case 3 -> modificarProducto(connection, scanner);
                    case 4 -> borrarProducto(connection, scanner);
                    case 0 -> System.out.println("Saliendo del programa...");
                    default -> System.out.println("Opción no válida, intente nuevamente.");
                }
            } while (opcion != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void crearProducto(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Introduzca el nombre del producto: ");
        String nombre = scanner.nextLine();
        System.out.print("Introduzca el precio del producto: ");
        float precio = scanner.nextFloat();
        System.out.print("Introduzca la cantidad del producto: ");
        int cantidad = scanner.nextInt();

        String query = "INSERT INTO producto (nombre, precio, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nombre);
            statement.setFloat(2, precio);
            statement.setInt(3, cantidad);
            statement.executeUpdate();
            System.out.println("Producto creado exitosamente.");
        }
    }

    private static void listarProductos(Connection connection) throws SQLException {
        String query = "SELECT * FROM producto";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            System.out.println("Lista de productos:");
            while (resultSet.next()) {
                System.out.printf("ID: %d, Nombre: %s, Precio: %.2f, Cantidad: %d%n",
                        resultSet.getInt("id"),
                        resultSet.getString("nombre"),
                        resultSet.getFloat("precio"),
                        resultSet.getInt("cantidad"));
            }
        }
    }

    private static void modificarProducto(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Introduzca el ID del producto a modificar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Seleccione el campo a modificar:");
        System.out.println("1) Nombre");
        System.out.println("2) Precio");
        System.out.println("3) Cantidad");
        System.out.print("Escriba una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();

        String query = null;
        switch (opcion) {
            case 1 -> {
                System.out.print("Introduzca el nuevo nombre: ");
                String nuevoNombre = scanner.nextLine();
                query = "UPDATE producto SET nombre = ? WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, nuevoNombre);
                    statement.setInt(2, id);
                    statement.executeUpdate();
                }
            }
            case 2 -> {
                System.out.print("Introduzca el nuevo precio: ");
                float nuevoPrecio = scanner.nextFloat();
                query = "UPDATE producto SET precio = ? WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setFloat(1, nuevoPrecio);
                    statement.setInt(2, id);
                    statement.executeUpdate();
                }
            }
            case 3 -> {
                System.out.print("Introduzca la nueva cantidad: ");
                int nuevaCantidad = scanner.nextInt();
                query = "UPDATE producto SET cantidad = ? WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, nuevaCantidad);
                    statement.setInt(2, id);
                    statement.executeUpdate();
                }
            }
            default -> System.out.println("Opción no válida.");
        }
        if (query != null) {
            System.out.println("Producto modificado exitosamente.");
        }
    }

    private static void borrarProducto(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Introduzca el ID del producto a borrar: ");
        int id = scanner.nextInt();

        String query = "DELETE FROM producto WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int filasAfectadas = statement.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Producto borrado exitosamente.");
            } else {
                System.out.println("Producto no encontrado.");
            }
        }
    }
}
