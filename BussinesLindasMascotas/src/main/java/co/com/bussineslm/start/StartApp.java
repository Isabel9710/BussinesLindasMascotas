
package co.com.bussineslm.start;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class StartApp implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        try {
            SetDataToBD.setTipoDocumento();
            SetDataToBD.setGenero();
            SetDataToBD.setPais();
            SetDataToBD.setDepartamento();
            SetDataToBD.setCiudad();
            SetDataToBD.setBarrio();
            SetDataToBD.setPerfil();
            SetDataToBD.setPersona();
            SetDataToBD.setUsuario();
            SetDataToBD.setPermisos();
            SetDataToBD.setModulos();
            SetDataToBD.setSubModulos();
        } catch (Exception ex) {
            Logger.getLogger(StartApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Destruido.");
    }
       
}
