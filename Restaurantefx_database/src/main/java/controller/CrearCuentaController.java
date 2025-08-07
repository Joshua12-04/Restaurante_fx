package controller;

import dataBase.dataBaseManager;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import application.App;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Usuario;

public class CrearCuentaController {


    private final String         RUTA_OJO_ABIERTO      = "/imagenes/practicos/ojo.png";
    private final String         RUTA_OJO_CERRADO      = "/imagenes/practicos/ojoCerrado.png";


    private String               strNombreTemporal;
    private String               strApellidoTemporal;
    private String               strUsuarioNuevoTemporal;
    private String               strContraseñaNuevaTemporal;

    // Image
    private Image                imagenOjoAbierto;
    private Image                imagenOjoCerrado;

    // Objetos Usuario
    private Usuario              usuario;

    @FXML private ResourceBundle resources;
    @FXML private URL            location;
    @FXML private Button         btnAbrirCrearCuenta;
    @FXML private Button         btnIniciarSesion;
    @FXML private Label          lblCuentaUsuario;
    @FXML private TextField      txtContraseñaVisible;
    @FXML private TextField      txtGuardarApellido;
    @FXML private TextField      txtCrearUsuario;
    @FXML private TextField      txtGuardarNombre;
    @FXML private PasswordField  txtCrearContraseña;
    @FXML private ImageView      imgContraseña;

    @FXML
    void CrearUsuario(ActionEvent event) {
        if (validarDatosCompletos()) {
            crearUsuarioNuevo();
            boolean bEntradaValida = existeEnBaseDEDatos();
            if (!bEntradaValida) {
                abrirPantallaPrincipal(usuario, event);
            } else {
                reiniciarTextosEnBlanco();
            }
        }
    }

    @FXML
    void pantallaIniciarSesion(ActionEvent event) {
        cerrarVentanaActual(event);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("IniciarSesion.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 390, 287);
            Stage stage = new Stage();
            stage.setTitle("JABRU");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void mostrarContraseña(MouseEvent event) {
        imgContraseña.setImage(imagenOjoAbierto);
        txtCrearContraseña.setVisible(false);
        txtCrearContraseña.setManaged(false);
        txtContraseñaVisible.setVisible(true);
        txtContraseñaVisible.setManaged(true);
    }

    @FXML
    void ocultarContraseña(MouseEvent event) {
        imgContraseña.setImage(imagenOjoCerrado);
        txtContraseñaVisible.setVisible(false);
        txtContraseñaVisible.setManaged(false);
        txtCrearContraseña.setVisible(true);
        txtCrearContraseña.setManaged(true);
    }

    @FXML
    void initialize() {
        inicializarTextos();
        cargarImagenes();
    }

    // Metodos Practicos
    private void inicializarTextos() {
        usuarioYContraseñaVisible();
        reiniciarTextosEnBlanco();
    }

    private void cerrarVentanaActual(ActionEvent event) {
        final Node nodo = (Node) event.getSource();
        final Stage ventana = (Stage) nodo.getScene().getWindow();
        ventana.close();
    }


    private void abrirPantallaPrincipal(Usuario usuario, ActionEvent event) {
        cerrarVentanaActual(event);
        try {
            FXMLLoader cargador = new FXMLLoader(App.class.getResource("pantallaPrincipal.fxml"));
            Scene escena = new Scene(cargador.load(), 600,400);
            Stage escenario =  new Stage();
            PantallaPrincipalController usuarioCuenta = cargador.getController();
            usuarioCuenta.setUsuarioEnCuestion(usuario);
            escenario.setTitle("JABRU");
            escenario.setResizable(true);
            escenario.setScene(escena);
            escenario.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reiniciarTextosEnBlanco() {
        txtGuardarNombre.setText("");
        txtGuardarApellido.setText("");
        txtCrearUsuario.setText("");
        txtCrearContraseña.setText("");
    }

    private void usuarioYContraseñaVisible() {
        txtGuardarNombre.setFocusTraversable(false);
        txtGuardarApellido.setFocusTraversable(false);
        txtCrearUsuario.setFocusTraversable(false);
        txtCrearContraseña.setFocusTraversable(false);
        txtContraseñaVisible.setManaged(false);
        txtContraseñaVisible.setVisible(false);
        txtContraseñaVisible.textProperty().bindBidirectional(txtCrearContraseña.textProperty());
    }

    private boolean validarDatosCompletos() {
        if (comprobarQueIngresoNombre() && comprobarQueIngresoApellido()
                && comprobarQueIngresoUsuarioNuevo() && comprobarQueIngresoContraseñaNueva()) {
            return true;
        } else {
            return false;
        }
    }

    public void crearUsuarioNuevo() {
        usuario = new Usuario(strNombreTemporal,strApellidoTemporal
                ,strUsuarioNuevoTemporal, strContraseñaNuevaTemporal);
    }

    private boolean existeEnBaseDEDatos() {
        dataBaseManager db = dataBaseManager.getInstance();

        boolean bExiste = revisarExistenciaDB(db);

        crearUsuarioEnDB(bExiste, db);
        return bExiste;
    }

    private void crearUsuarioEnDB(boolean bExiste, dataBaseManager db) {
        if (bExiste) {
            if (db.crearUsuario(usuario)){
                System.out.println("usuario creado correctamente en la base de datos");
            }else {
                mostrarAlerta("error al crear el usuario, intenta de nuevo");
            }
        }
    }

    private boolean revisarExistenciaDB(dataBaseManager db) {
        if (db.existeUsuario(strUsuarioNuevoTemporal)){
            mostrarAlerta("El nombre de usuario ya existe, elige otro");
            return true;
        } else {
            return false;
        }
    }

    private boolean comprobarQueIngresoContraseñaNueva() {
        if (txtCrearContraseña.getText().isBlank()
                || txtCrearContraseña.getText().isEmpty()) {
            mostrarAlerta("No has ingresado la contraseña");
            reiniciarTextosEnBlanco();
            return false;
        } else {
            strContraseñaNuevaTemporal = txtCrearContraseña.getText().trim();
            return true;
        }

    }

    private boolean comprobarQueIngresoUsuarioNuevo() {
        if (txtCrearUsuario.getText().isBlank()
                || txtCrearUsuario.getText().isEmpty()) {
            mostrarAlerta("No has ingresado el Usuario");
            reiniciarTextosEnBlanco();
            return false;
        } else {
            strUsuarioNuevoTemporal = txtCrearUsuario.getText().trim();
            return true;
        }
    }

    private boolean comprobarQueIngresoApellido() {
        if (txtGuardarApellido.getText().isBlank()
                || txtGuardarApellido.getText().isEmpty()) {
            mostrarAlerta("No has ingresado el Apellido");
            reiniciarTextosEnBlanco();
            return false;
        } else {
            strApellidoTemporal = txtGuardarApellido.getText().trim();
            return true;
        }
    }

    private boolean comprobarQueIngresoNombre() {
        if (txtGuardarNombre.getText().isBlank()
                || txtGuardarNombre.getText().isEmpty()) {
            mostrarAlerta("No has ingresado el Nombre");
            reiniciarTextosEnBlanco();
            return false;
        } else {
            strNombreTemporal = txtGuardarNombre.getText().trim();
            return true;
        }
    }

    private void cargarImagenes() {
        try {
            imagenOjoAbierto = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(RUTA_OJO_ABIERTO)));
            imagenOjoCerrado =  new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(RUTA_OJO_CERRADO)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String mensaje) {
        Platform.runLater(() -> {
            try {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("JABRU");
                alerta.setHeaderText(null);
                alerta.setContentText(mensaje);
                alerta.initModality(javafx.stage.Modality.APPLICATION_MODAL);
                alerta.showAndWait();
            } catch (Exception e) {
                System.err.println("Error al mostrar alerta: " + e.getMessage());
            }
        });
    }

}
