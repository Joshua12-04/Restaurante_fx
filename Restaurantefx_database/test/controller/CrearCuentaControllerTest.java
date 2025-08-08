package controller;

import dataBase.dataBaseManager;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)

class CrearCuentaControllerTest {
    @Test
    @DisplayName("Debería crear un usuario nuevo correctamente con el método crearUsuarioNuevo")
    void testCrearUsuarioNuevo() throws Exception {
        // Arrange - Preparar
        CrearCuentaController controller = new CrearCuentaController();

        // Configurar los campos temporales usando reflexión
        setPrivateField(controller, "strNombreTemporal", "Gael");
        setPrivateField(controller, "strApellidoTemporal", "Rodriguez");
        setPrivateField(controller, "strUsuarioNuevoTemporal", "69Gaelo");
        setPrivateField(controller, "strContraseñaNuevaTemporal", "02/03/2006");

        // Act - Actuar (ejecutar el método que queremos probar)
        controller.crearUsuarioNuevo(); // Este método es público, no necesitamos reflexión

        // Assert - Verificar
        // Obtenemos el usuario creado usando reflexión
        Usuario usuarioCreado = (Usuario) getPrivateField(controller, "usuario");

        // Verificaciones
        assertNotNull(usuarioCreado, "El usuario no debería ser null");
        assertEquals("Gael", usuarioCreado.getNombre(), "El nombre debería ser 'Gael'");
        assertEquals("Rodriguez", usuarioCreado.getApellido(), "El apellido debería ser 'Rodriguez'");
        assertEquals("69Gaelo", usuarioCreado.getNombreUsuario(), "El usuario debería ser '69Gaelo'");
        assertEquals("02/03/2006", usuarioCreado.getContraseñaUsuario(), "La contraseña debería ser '02/03/2006'");
    }

    // Métodos auxiliares para usar reflexión
    private void setPrivateField(Object obj, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    private Object getPrivateField(Object obj, String fieldName) throws Exception {
        java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }
}