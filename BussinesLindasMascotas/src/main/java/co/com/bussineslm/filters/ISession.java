
package co.com.bussineslm.filters;

import co.com.bussineslm.entities.Permisos;
import co.com.bussineslm.entities.Personas;


public interface ISession {
    void setSecurityContext();
    Boolean isAuthorized();
    Boolean isAuthorized(String action);
    Personas getUsuario();
    Permisos getPermisos();    
}
