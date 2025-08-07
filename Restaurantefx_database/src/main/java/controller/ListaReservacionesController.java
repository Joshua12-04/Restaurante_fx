package controller;

import java.net.URL;
import java.security.PrivateKey;
import java.util.List;
import java.util.ResourceBundle;

import dataBase.dataBaseManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Usuario;
import javafx.stage.Modality;

public class ListaReservacionesController {

    // Objeto Usuario
    private Usuario                usuarioEnCuestion;

    @FXML private ResourceBundle   resources;
    @FXML private URL              location;
    @FXML private Button           btnActualizar;
    @FXML private Button           btnCerrar;
    @FXML private Label            lblNombreUsuario;
    @FXML private Label            lblTotalReservaciones;
    @FXML private Label            lblNombreUsuarioCuenta;
    @FXML private ListView<String> listViewReservaciones;

    // FXML event
    @FXML
    void actualizarTabla(ActionEvent event) {
        cargarReservacionesUsuario();

        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("JABRU");
                alert.setHeaderText(null);
                alert.setContentText("LISTA DE RESERVACIONES ACTUALIZADA");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            }catch (Exception e){
                System.err.println("Error al mostrar la alerta: "+e.getMessage());
            }
        });
    }

    @FXML
    void cerrarVentana(ActionEvent event) {
        final Node nodo = (Node) event.getSource();
        final Stage ventana = (Stage) nodo.getScene().getWindow();
        ventana.close();
    }

    @FXML
    void initialize() {

        listViewReservaciones.setStyle("-fx-font-size: 12px; -fx-font-family: 'Arial';");

    }
    // Metodos Practicos

    public void cargarReservacionesUsuario() {
        if (usuarioEnCuestion == null){
            mostrarErrorUsuario();
            return;
        }

        try {
            dataBaseManager dbManager = dataBaseManager.getInstance();
            List<String > reservaciones = dbManager.obtenerReservacionesUsuario(usuarioEnCuestion.getNombreUsuario());

            ObservableList<String> observableListReservaciones = FXCollections.observableArrayList(reservaciones);

            listViewReservaciones.setItems(observableListReservaciones);

            lblTotalReservaciones.setText("Total de reservaciones activas: "+reservaciones.size());

            if (reservaciones.isEmpty()){
                observableListReservaciones.add("No tienes reservaciones activas: ");
                listViewReservaciones.setItems(observableListReservaciones);
            }

        }catch (Exception e){
            System.err.println("Error al cargar reservaciones "+e.getMessage());
            mostrarErrorCarga();
        }
    }

    private void mostrarErrorUsuario() {
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("JABRU - ERROR");
                alert.setHeaderText(null);
                alert.setContentText("ERROR: no se pudo identificar el usuario. \n" +
                        "Por favor inicia sesion nuevamente");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            }catch (Exception e){
                System.err.println("Error al mostrar alerta de usuario "+e.getMessage());
            }
        });
    }

    private void listaReservacionesUsuario(Usuario usuarioEnCuestion) {
        lblNombreUsuarioCuenta.setText(usuarioEnCuestion.getNombre());
    }

    private void mostrarErrorCarga() {
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("JABRU - ERROR");
                alert.setHeaderText(null);
                alert.setContentText("ERROR al cargar las reservaciones. \n" +
                        "Por favor revisar la base de datos");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            }catch (Exception e){
                System.err.println("Error al mostrar alerta de carga "+e.getMessage());
            }
        });
    }

    // Getters y Setters
    public Usuario getUsuarioEnCuestion() {
        return usuarioEnCuestion;
    }

    public void setUsuarioEnCuestion(Usuario usuarioEnCuestion) {
        this.usuarioEnCuestion = usuarioEnCuestion;
        listaReservacionesUsuario(this.usuarioEnCuestion);

    }
}
