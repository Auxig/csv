package dominio;

/**
 * Created by auxi on 31/05/17.
 */
public class Usuario {

    private String codigo;
    private String nombres;
    private String apellidos;
    private String correo;

    public Usuario(String codigo, String nombres, String apellidos, String correo) {
        setCodigo(codigo);
        setNombres(nombres);
        setApellidos(apellidos);
        setCorreo(correo);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;

        if (codigo != null ? !codigo.equals(usuario.codigo) : usuario.codigo != null) return false;
        if (nombres != null ? !nombres.equals(usuario.nombres) : usuario.nombres != null) return false;
        if (apellidos != null ? !apellidos.equals(usuario.apellidos) : usuario.apellidos != null) return false;
        return correo != null ? correo.equals(usuario.correo) : usuario.correo == null;
    }

    @Override
    public int hashCode() {
        int result = codigo != null ? codigo.hashCode() : 0;
        result = 31 * result + (nombres != null ? nombres.hashCode() : 0);
        result = 31 * result + (apellidos != null ? apellidos.hashCode() : 0);
        result = 31 * result + (correo != null ? correo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "codigo='" + codigo + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", correo='" + correo + '\'' +
                '}';
    }
}
