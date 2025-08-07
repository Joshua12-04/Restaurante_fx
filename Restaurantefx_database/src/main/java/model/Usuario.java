package model;

public class Usuario {
    private String strNombre;
    private String strApellido;
    private String strNombreUsuario;
    private String strContraseñaUsuario;

    // Contructor por defecto y parametrizado
    public Usuario(String nombre, String apellido, String nombreUsuario, String contraseñaUsuario) {
        this.strNombre            = nombre;
        this.strApellido          = apellido;
        this.strNombreUsuario     = nombreUsuario;
        this.strContraseñaUsuario = contraseñaUsuario;
    }

    public Usuario() {
        this("Joshua","Maynez","root","1234");
    }

    // Getters y Setters
    public String getNombre() {
        return strNombre;
    }

    public void setNombre(String nombre) {
        this.strNombre = nombre;
    }

    public String getApellido() {
        return strApellido;
    }

    public void setApellido(String apellido) {
        this.strApellido = apellido;
    }

    public String getNombreUsuario() {
        return strNombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.strNombreUsuario = nombreUsuario;
    }

    public String getContraseñaUsuario() {
        return strContraseñaUsuario;
    }

    public void setContraseñaUsuario(String contraseñaUsuario) {
        this.strContraseñaUsuario = contraseñaUsuario;
    }
}
