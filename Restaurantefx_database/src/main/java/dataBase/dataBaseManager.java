package dataBase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.SnapshotResult;
import javafx.scene.control.TableRow;
import model.Usuario;

public class dataBaseManager {
    private static final String database_URL = "jdbc:sqlite:jabru_reservas.db";
    private static dataBaseManager instance;

    private dataBaseManager() {
        iniciarDatabase();
    }

    public static dataBaseManager getInstance() {
        if (instance == null) {               //verificamos si ya existe
            instance = new dataBaseManager(); //creamos si no existe
        }
        return instance;                      //devolvemos la instancia ya sea nueva o que ya exista
    }

    private void iniciarDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            crearTablas();
            insertarUsuarioDefault();
            System.out.println("DB inicializada exitosamente");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: driver SQLite no encontrado");
            e.printStackTrace();
        }
    }


    private Connection getConnection () throws SQLException{
        return DriverManager.getConnection(database_URL);
    }

    private void crearTablas() {
        String crearTablaUsuario = """
                CREATE TABLE IF NOT EXISTS usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                nombre_usuario TEXT UNIQUE NOT NULL,
                contraseña TEXT NOT NULL,
                fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
        String crearTablaReservaciones = """
                 CREATE TABLE IF NOT EXISTS reservaciones (
                 id INTEGER PRIMARY KEY AUTOINCREMENT,
                 usuario_id INTEGER NOT NULL,
                 restaurante TEXT NOT NULL,
                 fecha_reserva TEXT NOT NULL,
                 hora_reserva TEXT NOT NULL,
                 numero_personas INTEGER NOT NULL,
                 estado TEXT DEFAULT 'ACTIVA',
                 fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                 
                 FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
                 )
                """;

        try (Connection conn = getConnection();
        Statement stmt = conn.createStatement()) {
            stmt.execute(crearTablaUsuario);
            stmt.execute(crearTablaReservaciones);
        }catch (SQLException e){
            System.err.println("Error al crear las tablaaaaaas" +e.getMessage() );
            e.printStackTrace();
        }
    }

    private void insertarUsuarioDefault() {
        String checarUsuario = "SELECT COUNT(*) FROM usuarios WHERE nombre_usuario = ?";
        String insertarUsuario = "INSERT INTO usuarios (nombre,apellido,nombre_usuario,contraseña) VALUES (?,?,?,?)";

        try (Connection conn = getConnection()){
            try (PreparedStatement checkStmt = conn.prepareStatement(checarUsuario)) {
                checkStmt.setString(1,"root");
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1)==0){
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertarUsuario)) {
                        insertStmt.setString(1, "Joshua");
                        insertStmt.setString(2, "Maynez");
                        insertStmt.setString(3, "root");
                        insertStmt.setString(4, "1234");
                        insertStmt.executeUpdate();
                        System.out.println("Usuario por defecto creado: root/1234");
                    }
                }
            }
        }catch (SQLException e){
            System.err.println("ERROR al insertar al usuario por defecto "+e.getMessage());
        }
    }

    public boolean crearUsuario (Usuario usuario){
        String sql = "INSERT INTO usuarios (nombre, apellido, nombre_usuario, contraseña) VALUES (?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getApellido());
            pstmt.setString(3, usuario.getNombreUsuario());
            pstmt.setString(4, usuario.getContraseñaUsuario());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        }catch (SQLException e){
            System.err.println("error al crear el usuario "+e.getMessage());
            return false;
        }
    }

    public Usuario validarUsuario (String nombreUsuario, String contraseña){
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ? AND contraseña = ?";
        try (Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
        pstmt.setString(1,nombreUsuario);
        pstmt.setString(2,contraseña);

        ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("nombre_usuario"),
                        rs.getString("contraseña")
                );
                return usuario;
            }
        }catch (SQLException e){
            System.err.println("error al validar usuario" + e.getMessage());
        }
        return null;
    }

    public boolean existeUsuario (String nombreUsuario){
        String sql = "SELECT COUNT(*) FROM usuarios WHERE nombre_usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,nombreUsuario);
            ResultSet rs = pstmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;
        }catch (SQLException e){
            System.err.println("error al verificar la existencia de un usuario: "+e.getMessage());
            return false;
        }
    }


    //metodos para las reservaciones
    public boolean crearReservacion (String nombreUsuario, String restaurante, String fechaReserva, String horaReserva, int numeroPersonas){
        String sql = """
                INSERT INTO reservaciones (usuario_id,restaurante,fecha_reserva,hora_reserva,numero_personas)
                SELECT id, ?,?,?,? FROM usuarios WHERE nombre_usuario = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,restaurante);
            pstmt.setString(2,fechaReserva);
            pstmt.setString(3,horaReserva);
            pstmt.setInt(4,numeroPersonas);
            pstmt.setString(5,nombreUsuario);

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        }catch (SQLException e){
            System.err.println("Error al crear reservacion: "+e.getMessage());
            return false;
        }
    }

    public List<String> obtenerReservacionesUsuario(String nombreUsuario){
        String sql = """
                SELECT r.restaurante, r.fecha_reserva, r.hora_reserva, r.numero_personas, r.estado
                            FROM reservaciones r
                            JOIN usuarios u ON r.usuario_id = u.id
                            WHERE u.nombre_usuario = ? AND r.estado = 'ACTIVA'
                            ORDER BY r.fecha_reserva, r.hora_reserva
                """;

        List<String> reservaciones = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,nombreUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                String reservacion = String.format(
                       "Restaurante: %s | Fecha: %s | Hora %s | Personas: %d | Estado %s",
                       rs.getString("restaurante"),
                        rs.getString("fecha_reserva"),
                        rs.getString("hora_reserva"),
                        rs.getInt("numero_personas"),
                        rs.getString("estado")
                );
                reservaciones.add(reservacion);
            }
        }catch (SQLException e){
            System.err.println("Error al obtener reservaciones "+e.getMessage());
        }
        return reservaciones;
    }
    public boolean cancelarReservacion(int reservacionId) {
        String sql = "UPDATE reservaciones SET estado = 'CANCELADA' WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reservacionId);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al cancelar reservación: " + e.getMessage());
            return false;
        }
    }

}

