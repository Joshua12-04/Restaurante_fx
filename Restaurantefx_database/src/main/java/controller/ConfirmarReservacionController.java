package controller;

import dataBase.dataBaseManager;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Usuario;

public class ConfirmarReservacionController {

    // Imagenes
    private Image                   imagenLogoBoston;
    private Image                   imagenLogoEspadas;
    private Image                   imagenLogoSunroll;

    // Constantes
    private final String             RUTA_SUNROLL_LOGO                  = "/imagenes/sunroll/sunrollpng.png";
    private final String             RUTA_ESPADAS_LOGO                  = "/imagenes/espadas/espadaslogo.png";
    private final String             RUTA_BOSTON_lOGO                   = "/imagenes/boston/bostonpng.png";
    private final String             BOSTON_NOMBRE                      = "Boston";
    private final String             ESPADAS_NOMBRE                     = "Espadas";
    private final String             SUNROLL_NOMBRE                     = "Sunroll";

    // Variables Practicas
    private String                   strRegistrarPropiedadesrestaurante = "A";
    private String                   strCantidadPersonas;
    private String                   strDiaSelccionado;
    private String                   strMesSeleccionado;
    private String                   strHoraSeleccionada;


    // Objeto Usuario
    private Usuario                  usuarioEnCuestion;

    // Arreglos
    private String[]                 cantidadPersonas                   = {"2","4","6","8","10","12"};
    private String[]                 dia                                = {"1","2","3","4","5","6","7","8","9","10", "11","12","13","14","15","16","17","18","19","20", "21","22","23","24","25","26","27","28","29","30"};
    private String[]                 mes                                = {"1","2","3","4","5","6","7","8","9","10", "11","12"};
    private String[]                 hora                               = {"8","9","10", "11","12","13","14","15","16","17","18","19","20", "21"};

    @FXML private ResourceBundle    resources;
    @FXML private URL location;
    @FXML private ChoiceBox<String> boxCDia;
    @FXML private ChoiceBox<String> boxCHora;
    @FXML private ChoiceBox<String> boxCMes;
    @FXML private ChoiceBox<String> boxCPersonas;
    @FXML private AnchorPane        btnCancelar;
    @FXML private Button            btnConfirmarReservar;
    @FXML private ImageView         imgRestaurante;
    @FXML private Label             lblNombreRestaurante;
    @FXML private Label             lblNombreUsuario;

    // FXML event
    @FXML
    void cancelarREservacion(ActionEvent event) {
        cerrarVentanaActual(event);
    }

    @FXML
    void reservacionConfirmada(ActionEvent event) {
        if (!validarSelecciones()){
            mostrarErrorSeleccion();
            return;
        }
            guardarValoresReservacion();

        if (guardarReservacionDB()){
            mostrarInformacionReserva();
            cerrarVentanaActual(event);
        }else {
            mostrarErrorReservacion();
        }
    }

    private boolean validarSelecciones() {
        return boxCPersonas.getSelectionModel().getSelectedItem() != null &&
                boxCDia.getSelectionModel().getSelectedItem() != null &&
                boxCMes.getSelectionModel().getSelectedItem() != null &&
                boxCHora.getSelectionModel().getSelectedItem() != null;
    }
    private boolean guardarReservacionDB(){
        try {
            String nombreRestaurante = obtenerNombreRestaurante();

            String fechaReserva = strDiaSelccionado + "/" +strMesSeleccionado;
            int numeroPersonas = Integer.parseInt(strCantidadPersonas);

            //base de datos chula guapa y preciosa
            dataBase.dataBaseManager dbManager = dataBase.dataBaseManager.getInstance();
            return dbManager.crearReservacion(
                    usuarioEnCuestion.getNombreUsuario(),
                    nombreRestaurante,
                    fechaReserva,
                    strHoraSeleccionada + "00",
                    numeroPersonas
            );
        }catch (NumberFormatException e){
            System.err.println("error al convertir el numero de personas "+e.getMessage());
            return false;
        }catch (Exception e){
            System.err.println("error al guardar la reservacion "+e.getMessage());
            return false;
        }
    }

    private String obtenerNombreRestaurante(){
        switch (getStrRegistrarPropiedadesrestaurante()){
            case "B":
                return BOSTON_NOMBRE;
            case "E":
                return ESPADAS_NOMBRE;
            case "S":
                return SUNROLL_NOMBRE;
            default:
                return "Restaurante desconocido";
        }
    }
    private void mostrarErrorSeleccion(){
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("JABRU - ERROR");
                alert.setHeaderText(null);
                alert.setContentText("por favor, selecciona TODOS LOS CAMPOS: \n" +
                        "Numero de personas\n" +
                        "Dia\n" +
                        "Mes\n" +
                        "Hora\n");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            }catch (Exception e){
                System.err.println("Error al mostrar alerta de error "+e.getMessage());
            }
        });
    }

    private void mostrarErrorReservacion(){
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("JABRU - ERROR ");
                alert.setHeaderText(null);
                alert.setContentText("Error al crear la reservacion \n" +
                        "por favor intenta nuevamente");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            }catch (Exception e){
                System.err.println("Error al mostrar alerta de eror de reservacion");
            }
        });
    }

    @FXML
    void initialize() {
        establecerPropiedadesChoicebox();
        cargarImagenes();
    }

    // Metodos Practicos
    private void cerrarVentanaActual(ActionEvent event) {
        final Node nodo = (Node) event.getSource();
        final Stage ventana = (Stage) nodo.getScene().getWindow();
        ventana.close();
    }

    private void establecerPropiedadesChoicebox() {
        // Llenar los ChoiceBox con los arreglos
        boxCPersonas.getItems().addAll(cantidadPersonas);
        boxCDia.getItems().addAll(dia);
        boxCMes.getItems().addAll(mes);
        boxCHora.getItems().addAll(hora);

    }

    private void nombreUsuarioReservacion(Usuario usuarioEnCuestion) {
        lblNombreUsuario.setText(usuarioEnCuestion.getNombre());
    }

    private void inicializarPropiedadesRestaurante(String propiedades) {
        if (Objects.equals(propiedades,"B")) {
            propiedadesBoston();
        } else if (Objects.equals(propiedades,"E")) {
            propiedadesEspadas();
        } else if (Objects.equals(propiedades,"S")) {
            propiedadesSunroll();
        } else {
            System.out.println("No se lograron cargar las propiedades");
        }
    }

    private void cargarImagenes() {
        try {
            imagenLogoBoston = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(RUTA_BOSTON_lOGO)));
            imagenLogoEspadas = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(RUTA_ESPADAS_LOGO)));
            imagenLogoSunroll = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(RUTA_SUNROLL_LOGO)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void propiedadesSunroll() {
        lblNombreRestaurante.setText(SUNROLL_NOMBRE);
        definirImagenesSunroll();
    }

    private void definirImagenesSunroll() {
        imgRestaurante.setImage(imagenLogoSunroll);
        imgRestaurante.setFitWidth(50);
        imgRestaurante.setX(10);
        imgRestaurante.setY(10);
    }

    private void propiedadesEspadas() {
        lblNombreRestaurante.setText(ESPADAS_NOMBRE);
        definirImagenesEspadas();
    }

    private void definirImagenesEspadas() {
        imgRestaurante.setImage(imagenLogoEspadas);
        imgRestaurante.setFitWidth(50);
        imgRestaurante.setX(10);
        imgRestaurante.setY(10);
    }

    private void propiedadesBoston() {
        lblNombreRestaurante.setText(BOSTON_NOMBRE);
        definirImagenesBoston();
    }

    private void definirImagenesBoston() {
        imgRestaurante.setImage(imagenLogoBoston);
    }


    private void guardarValoresReservacion() {
        strCantidadPersonas = boxCPersonas.getSelectionModel().getSelectedItem().toString();
        strDiaSelccionado   = boxCDia.getSelectionModel().getSelectedItem().toString();
        strMesSeleccionado  = boxCMes.getSelectionModel().getSelectedItem().toString();
        strHoraSeleccionada = boxCHora.getSelectionModel().getSelectedItem().toString();
    }

    private void mostrarInformacionReserva() {
        Platform.runLater(() -> {
            try {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("JABRU");
                alerta.setHeaderText(null);
                alerta.setContentText("Reservacion hecha: \n"
                        + "Personas: " + strCantidadPersonas + "\n"
                        + "Dia: " + strDiaSelccionado + "\n"
                        + "Mes: " + strMesSeleccionado + "\n"
                        + "Hora: " + strHoraSeleccionada + "\n"
                        + "Los esperamos");
                alerta.initModality(javafx.stage.Modality.APPLICATION_MODAL);
                alerta.showAndWait();
            } catch (Exception e) {
                System.err.println("Error al mostrar alerta: " + e.getMessage());
            }
        });
    }

    // Getters y Setters
    public Usuario getUsuarioEnCuestion() {
        return usuarioEnCuestion;
    }

    public void setUsuarioEnCuestion(Usuario usuarioEnCuestion) {
        this.usuarioEnCuestion = usuarioEnCuestion;
        nombreUsuarioReservacion(this.usuarioEnCuestion);
    }

    public String getStrRegistrarPropiedadesrestaurante() {
        return strRegistrarPropiedadesrestaurante;
    }

    public void setStrRegistrarPropiedadesrestaurante(String strRegistrarPropiedadesrestaurante) {
        this.strRegistrarPropiedadesrestaurante = strRegistrarPropiedadesrestaurante;
        inicializarPropiedadesRestaurante(this.strRegistrarPropiedadesrestaurante);
    }
}

