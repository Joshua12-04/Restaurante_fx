package controller;

import dataBase.dataBaseManager;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import application.App;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Usuario;

public class IniciarSesionController {
    //Constantes
    private final Usuario       CUENTA_PREDETERMINADA = new Usuario();
    private final String        RUTA_OJO_ABIERTO      = "/imagenes/practicos/ojo.png";
    private final String        RUTA_OJO_CERRADO      = "/imagenes/practicos/ojoCerrado.png";

    // Imagnes
    private Image               imagenOjoAbierto;
    private Image               imagenOjoCerrado;

    // Objeto Usuario
    private ArrayList <Usuario> usuarios              = new ArrayList<>();

    // Variables Usuarios
    private String               strUsuarioTemporal;
    private String               strContraseñaTemporal;

    @FXML private ImageView      imgContraseña;
    @FXML private TextField      txtContraseñaVisible;
    @FXML private ResourceBundle resources;
    @FXML private URL            location;
    @FXML private Button         btnAbrirCrearCuenta;
    @FXML private Button         btnIniciarSesion;
    @FXML private TextField      txtContraseña;
    @FXML private TextField      txtUsuario;

    // FXML event
    @FXML
    void pantallaCrearCuenta(ActionEvent event) {
        cerrarVentanaActual(event);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("CrearCuenta.fxml"));
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
    void existenciaCuenta(ActionEvent event) {
        if (validarDatosCompletos()) {
            comprobarExistenciaUsuario(event);
        }
    }

    @FXML
    void mostrarContraseña(MouseEvent event) {
        imgContraseña.setImage(imagenOjoAbierto);
        txtContraseña.setVisible(false);
        txtContraseña.setManaged(false);
        txtContraseñaVisible.setVisible(true);
        txtContraseñaVisible.setManaged(true);
    }

    @FXML
    void ocultarContraseña(MouseEvent event) {
        imgContraseña.setImage(imagenOjoCerrado);
        txtContraseñaVisible.setVisible(false);
        txtContraseñaVisible.setManaged(false);
        txtContraseña.setVisible(true);
        txtContraseña.setManaged(true);
    }

    @FXML
    void initialize() {
        inicializarTextos();
        cargarImagenes();
        //guardarCuentasDeVerificacion();
    }

    // Metodos Practicos
    private void guardarCuentasDeVerificacion() {
        usuarios.add(CUENTA_PREDETERMINADA);
    }

    private void inicializarTextos() {
        usuarioYContraseñaVisible();
        reiniciarTextosEnBlanco();
    }

    private void reiniciarTextosEnBlanco() {
        txtUsuario.setText("");
        txtContraseña.setText("");
    }

    private void usuarioYContraseñaVisible() {
        txtUsuario.setFocusTraversable(false);
        txtContraseña.setFocusTraversable(false);
        txtContraseñaVisible.setManaged(false);
        txtContraseñaVisible.setVisible(false);
        txtContraseñaVisible.textProperty().bindBidirectional(txtContraseña.textProperty());
    }

    private boolean validarDatosCompletos() {
        if (comprobarQueIngresoUsuario() && comprobarQueIngresoContraseña()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean comprobarQueIngresoContraseña() {
        if (txtContraseña.getText().isBlank()
                || txtContraseña.getText().isEmpty()) {
            mostrarAlerta("No has ingresado la Contraseña");
            reiniciarTextosEnBlanco();
            return false;
        } else {
            strContraseñaTemporal = txtContraseña.getText();
            return true;
        }
    }

    private boolean comprobarQueIngresoUsuario() {
        if (txtUsuario.getText().isBlank()
                || txtUsuario.getText().isEmpty()) {
            mostrarAlerta("No has ingresado el Usuario");
            reiniciarTextosEnBlanco();
            return false;
        } else {
            strUsuarioTemporal = txtUsuario.getText();
            return true;
        }
    }

    private void comprobarExistenciaUsuario(ActionEvent event) {

        dataBaseManager db = dataBaseManager.getInstance();
        Usuario usuarioValidado = db.validarUsuario(strUsuarioTemporal,strContraseñaTemporal);
        if (usuarioValidado != null){
            reiniciarTextosEnBlanco();
            abrirPantallaDeInicio(usuarioValidado,event);
        }else {
            mostrarAlerta("usuario o contraseña incorrectos, intenta de nuevo o crea una cuenta.");
            reiniciarTextosEnBlanco();
        }
        /*for (Usuario ingresar : usuarios) {
            if (Objects.equals(ingresar.getNombreUsuario(), strUsuarioTemporal)
                    && Objects.equals(ingresar.getContraseñaUsuario(), strContraseñaTemporal)) {
                reiniciarTextosEnBlanco();
                abrirPantallaDeInicio(ingresar, event);
            } else {
                mostrarAlerta("No hay registro de tu cuenta, intenta de nuevo o crea una cuenta");
                reiniciarTextosEnBlanco();
            }
        }*/

    }

    private void abrirPantallaDeInicio(Usuario ingresar, ActionEvent event) {
        cerrarVentanaActual(event);
        try {
            FXMLLoader cargar = new FXMLLoader(App.class.getResource("pantallaPrincipal.fxml"));
            Scene escena = new Scene(cargar.load(), 600,400);
            Stage escenario =  new Stage();
            PantallaPrincipalController usuarioCuenta =  cargar.getController();
            usuarioCuenta.setUsuarioEnCuestion(ingresar);
            escenario.setTitle("JABRU");
            escenario.setResizable(false);
            escenario.setScene(escena);
            escenario.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cerrarVentanaActual(ActionEvent event) {
        final Node nodo = (Node) event.getSource();
        final Stage ventana = (Stage) nodo.getScene().getWindow();
        ventana.close();
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
