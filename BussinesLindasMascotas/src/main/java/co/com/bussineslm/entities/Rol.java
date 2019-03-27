
package co.com.bussineslm.entities;


public class Rol {
    
    private String rolId;
    private String nombre;

    public Rol(String rolId, String nombre) {
        this.rolId = rolId;
        this.nombre = nombre;
    }

    public String getRolId() {
        return rolId;
    }

    public void setRolId(String rolId) {
        this.rolId = rolId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
