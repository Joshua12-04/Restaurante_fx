package controller;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Usuario;

public class ReservarRestauranteController {

    // Objeto Usuario
    private Usuario              usuarioEnCuestion;


    private String               registrarPropiedadesRestaurante = "A";

    // Constantes
    private final String         RUTA_BOSTON_lOGO             = "/imagenes/boston/bostonpng.png";
    private final String         RUTA_BOSTON_MENU_UNO         = "/imagenes/boston/menu1.jpg";
    private final String         RUTA_BOSTON_MENU_DOS         = "/imagenes/boston/menu2.jpg";
    private final String         RUTA_BOSTON_MENU_TRES        = "/imagenes/boston/menu3.jpg";
    private final String         RUTA_ESPADAS_LOGO            = "/imagenes/espadas/espadaslogo.png";
    private final String         RUTA_ESPADAS_MENU_UNO        = "/imagenes/espadas/menu1.jpeg";
    private final String         RUTA_ESPADAS_MENU_DOS        = "/imagenes/espadas/menu2.jpeg";
    private final String         RUTA_ESPADAS_MENU_TRES       = "/imagenes/espadas/menu3.jpeg";
    private final String         RUTA_SUNROLL_LOGO            = "/imagenes/sunroll/sunrollpng.png";
    private final String         RUTA_SUNROLL_MENU_UNO        = "/imagenes/sunroll/menu1.jpeg";
    private final String         RUTA_SUNROLL_MENU_DOS        = "/imagenes/sunroll/menu2.jpeg";
    private final String         RUTA_SUNROLL_MENU_TRES       = "/imagenes/sunroll/menu3.jpeg";

    // Imagenes
    private Image                imagenLogoBoston;
    private Image                imagenMenuUnoBoston;
    private Image                imagenMenuDosBoston;
    private Image                imagenMenuTresBoston;
    private Image                imagenLogoEspadas;
    private Image                imagenMenuUnoEspadas;
    private Image                imagenMenuDosEspadas;
    private Image                imagenMenuTresEspadas;
    private Image                imagenLogoSunroll;
    private Image                imagenMenuUnoSunroll;
    private Image                imagenMenuDosSunroll;
    private Image                imagenMenuTresSunroll;



    @FXML private ResourceBundle resources;
    @FXML private URL            location;
    @FXML private Button         btnReservar;
    @FXML private Button         btnVolver;
    @FXML private ImageView      imgComidaDos;
    @FXML private ImageView      imgComidaTres;
    @FXML private ImageView      imgComidaUno;
    @FXML private ImageView      imgLogoRestaurante;

    // FXML event
    @FXML
    void ReservarMesa(ActionEvent event) {
        try {
            FXMLLoader cargador = new FXMLLoader(App.class.getResource("confirmaReservacion.fxml"));
            Scene escena = new Scene(cargador.load(), 300,400);
            Stage escenario =  new Stage();
            ConfirmarReservacionController controller = cargador.getController();
            controller.setUsuarioEnCuestion(this.usuarioEnCuestion);
            controller.setStrRegistrarPropiedadesrestaurante(this.registrarPropiedadesRestaurante);
            escenario.setTitle("JABRU");
            escenario.setResizable(true);
            escenario.setScene(escena);
            escenario.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void volverPrincipal(ActionEvent event) {
        cerrarVentanaActual(event);
        try {
            FXMLLoader cargador = new FXMLLoader(App.class.getResource("pantallaPrincipal.fxml"));
            Scene escena = new Scene(cargador.load(), 600,400);
            Stage escenario =  new Stage();
            PantallaPrincipalController usuarioCuenta =  cargador.getController();
            usuarioCuenta.setUsuarioEnCuestion(usuarioEnCuestion);
            escenario.setTitle("JABRU");
            escenario.setResizable(true);
            escenario.setScene(escena);
            escenario.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        cargarImagenes();
    }

    // Metodos Practicos
    private void inicializarImaganesCorrespondientes(String imagenesCargar)  {
        if (Objects.equals(imagenesCargar, "B")) {
            ajustarImagenesBoston();
        } else if (Objects.equals(imagenesCargar, "E")) {
            ajustarImagenesEspadas();
        } else if (Objects.equals(imagenesCargar, "S")) {
            ajustarImagenesSunroll();
        } else {
            System.out.println("No se lograron registrar las imagenes");
        }
    }

    private void ajustarImagenesSunroll() {
        imgLogoRestaurante.setImage(imagenLogoSunroll);
        imgLogoRestaurante.setFitHeight(65);
        imgLogoRestaurante.setX(22);
        imgLogoRestaurante.setY(9);
        imgComidaUno.setImage(imagenMenuUnoSunroll);
        imgComidaDos.setImage(imagenMenuDosSunroll);
        imgComidaDos.setY(0);
        imgComidaDos.setX(22);
        imgComidaTres.setImage(imagenMenuTresSunroll);
    }

    private void ajustarImagenesEspadas() {
        imgLogoRestaurante.setImage(imagenLogoEspadas);
        imgComidaUno.setImage(imagenMenuUnoEspadas);
        imgComidaDos.setImage(imagenMenuDosEspadas);
        imgComidaTres.setImage(imagenMenuTresEspadas);
    }

    private void ajustarImagenesBoston() {
        imgLogoRestaurante.setImage(imagenLogoBoston);
        imgComidaUno.setImage(imagenMenuUnoBoston);
        imgComidaDos.setImage(imagenMenuDosBoston);
        imgComidaTres.setImage(imagenMenuTresBoston);
    }

    private void cargarImagenes() {
        try {
            imagenLogoBoston = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(RUTA_BOSTON_lOGO)));
            imagenMenuUnoBoston = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(RUTA_BOSTON_MENU_UNO)));
            imagenMenuDosBoston = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(RUTA_BOSTON_MENU_DOS)));
            imagenMenuTresBoston = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(RUTA_BOSTON_MENU_TRES)));
            imagenLogoEspadas = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(RUTA_ESPADAS_LOGO)));
            imagenMenuUnoEspadas = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(RUTA_ESPADAS_MENU_UNO)));
            imagenMenuDosEspadas = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(RUTA_ESPADAS_MENU_DOS)));
            imagenMenuTresEspadas = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(RUTA_ESPADAS_MENU_TRES)));
            imagenLogoSunroll = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(RUTA_SUNROLL_LOGO)));
            imagenMenuUnoSunroll = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(RUTA_SUNROLL_MENU_UNO)));
            imagenMenuDosSunroll = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(RUTA_SUNROLL_MENU_DOS)));
            imagenMenuTresSunroll = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(RUTA_SUNROLL_MENU_TRES)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void cerrarVentanaActual(ActionEvent event) {
        final Node nodo = (Node) event.getSource();
        final Stage ventana = (Stage) nodo.getScene().getWindow();
        ventana.close();
    }

    // Getters y Setters

    public String getRegistrarPropiedadesRestaurante() {
        return registrarPropiedadesRestaurante;
    }

    public void setRegistrarPropiedadesRestaurante(String registrarPropiedadesRestaurante) {
        this.registrarPropiedadesRestaurante = registrarPropiedadesRestaurante;
        inicializarImaganesCorrespondientes(this.registrarPropiedadesRestaurante);
    }

    public Usuario getUsuarioEnCuestion() {
        return usuarioEnCuestion;
    }

    public void setUsuarioEnCuestion(Usuario usuarioEnCuestion) {
        this.usuarioEnCuestion = usuarioEnCuestion;
        System.out.println("Usuario: " + this.usuarioEnCuestion.getNombre());
    }


}

