package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Usuario;

public class PantallaPrincipalController {

    // Objeto Usuario
    private Usuario              usuarioEnCuestion;


    private String               registrarImagenesRestaurante = "A";

    // Constantes
    private final String         COLOR_BLANCO                 = "-fx-border-width: 3; -fx-background-color:  transparent; -fx-border-color: white";
    private final String         COlOR_PREDETERMINADO         = "-fx-border-width: 3; -fx-background-color:  transparent; -fx-border-color: #883232";
    @FXML private Button         btnReservacion;
    @FXML private Label          lblNombreUsuario;
    @FXML private ResourceBundle resources;
    @FXML private URL            location;
    @FXML private Button         btnBoston;
    @FXML private Button         btnCerrarSesion;
    @FXML private Button         btnEspadas;
    @FXML private Button         btnSunroll;


    @FXML
    void acercaDe(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("JABRU");
                alerta.setHeaderText(null);
                alerta.setResizable(false);
                alerta.setContentText("Somos una empresa que busca lo mejor para nuestros clientes" +
                        ", buscamos que las personas puedan disfrutar de sus alimentos de una manera " +
                        " mas practica, no solo para ellos, sino para la misma empresa de comida." +
                        " Hacer una reservacion a la hora, dia y cuantas personas sera mucho mas facil que nunca");
                alerta.initModality(javafx.stage.Modality.APPLICATION_MODAL);
                alerta.showAndWait();
            } catch (Exception e) {
                System.err.println("Error al mostrar alerta: " + e.getMessage());
            }
        });
    }

    @FXML
    void cerrarSesionUsuario(ActionEvent event) {
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
    void listaResevaciones(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("ListaReservaciones.fxml"));
            Scene scene = new Scene(fxmlLoader.load(),700,500);

            ListaReservacionesController controller = fxmlLoader.getController();

            controller.setUsuarioEnCuestion(usuarioEnCuestion);

            controller.cargarReservacionesUsuario();

            Stage stage = new Stage();
            stage.setTitle("JABRU - RESERVACIONES");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        }catch (Exception e){
            System.err.println("Error al abrir reservacioens "+e.getMessage());
        }

    }

    @FXML
    void seleccionarBoston(ActionEvent event) {
        cerrarVentanaActual(event);
        registrarImagenesRestaurante = "B";
        abrirPantallaReservar(registrarImagenesRestaurante);
    }

    @FXML
    void seleccionarEspadas(ActionEvent event) {
        cerrarVentanaActual(event);
        registrarImagenesRestaurante = "E";
        abrirPantallaReservar(registrarImagenesRestaurante);
    }

    @FXML
    void seleccionarSunroll(ActionEvent event) {
        cerrarVentanaActual(event);
        registrarImagenesRestaurante = "S";
        abrirPantallaReservar(registrarImagenesRestaurante);
    }

    @FXML
    void colorVisualBoston(MouseEvent event) {
        restauranteVisualPredeterminado(btnBoston);
    }

    @FXML
    void colorVisualEspadas(MouseEvent event) {
        restauranteVisualPredeterminado(btnEspadas);
    }

    @FXML
    void colorVisualSunroll(MouseEvent event) {
        restauranteVisualPredeterminado(btnSunroll);
    }

    @FXML
    void seleccionVisualBoston(MouseEvent event) {
        restauranteSelccionadoVisual(btnBoston);
    }

    @FXML
    void seleccionVisualEspadas(MouseEvent event) {
        restauranteSelccionadoVisual(btnEspadas);
    }

    @FXML
    void seleccionVisualSunroll(MouseEvent event) {
        restauranteSelccionadoVisual(btnSunroll);

    }

    @FXML
    void initialize() {

    }

    // Metodos Practicos
    private void abrirPantallaReservar(String registrarImagenesRestaurante) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("reservarRestaurante.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);

            //  el controlador bien que JavaFX hizo
            ReservarRestauranteController controller = fxmlLoader.getController();

            // Ahora Si el setter
            controller.setRegistrarPropiedadesRestaurante(registrarImagenesRestaurante);
            controller.setUsuarioEnCuestion(usuarioEnCuestion);


            Stage stage = new Stage();
            stage.setTitle("JABRU");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cerrarVentanaActual(ActionEvent event) {
        final Node nodo = (Node) event.getSource();
        final Stage ventana = (Stage) nodo.getScene().getWindow();
        ventana.close();
    }

    private void restauranteSelccionadoVisual(Button botonSeleccionado) {
        botonSeleccionado.setStyle(COLOR_BLANCO);
    }

    private void restauranteVisualPredeterminado(Button botonSeleccionado) {
        botonSeleccionado.setStyle(COlOR_PREDETERMINADO);
    }

    private void bievenidadUsuario(Usuario usuarioEnCuestion) {
        lblNombreUsuario.setText(usuarioEnCuestion.getNombre());
    }


    // Getters y Setters
    public Usuario getUsuarioEnCuestion() {
        return usuarioEnCuestion;
    }

    public void setUsuarioEnCuestion(Usuario usuarioEnCuestion) {
        this.usuarioEnCuestion = usuarioEnCuestion;
        bievenidadUsuario(this.usuarioEnCuestion);
    }
}
