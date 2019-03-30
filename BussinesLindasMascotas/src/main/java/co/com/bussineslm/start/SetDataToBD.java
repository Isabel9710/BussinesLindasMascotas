
package co.com.bussineslm.start;

import co.com.bussineslm.JPAcontrollers.BarriosJpaController;
import co.com.bussineslm.JPAcontrollers.CiudadesJpaController;
import co.com.bussineslm.JPAcontrollers.DepartamentosJpaController;
import co.com.bussineslm.JPAcontrollers.GenerosJpaController;
import co.com.bussineslm.JPAcontrollers.ModulosJpaController;
import co.com.bussineslm.JPAcontrollers.PaisesJpaController;
import co.com.bussineslm.JPAcontrollers.PerfilesJpaController;
import co.com.bussineslm.JPAcontrollers.PermisosJpaController;
import co.com.bussineslm.JPAcontrollers.PersonasJpaController;
import co.com.bussineslm.JPAcontrollers.SubModulosJpaController;
import co.com.bussineslm.JPAcontrollers.TiposDocumentosJpaController;
import co.com.bussineslm.JPAcontrollers.UsuariosJpaController;
import co.com.bussineslm.JPAcontrollers.exceptions.IllegalOrphanException;
import co.com.bussineslm.JPAcontrollers.exceptions.PreexistingEntityException;
import co.com.bussineslm.entities.Barrios;
import co.com.bussineslm.entities.Ciudades;
import co.com.bussineslm.entities.Departamentos;
import co.com.bussineslm.entities.Generos;
import co.com.bussineslm.entities.Modulos;
import co.com.bussineslm.entities.Paises;
import co.com.bussineslm.entities.Perfiles;
import co.com.bussineslm.entities.Permisos;
import co.com.bussineslm.entities.Personas;
import co.com.bussineslm.entities.Rol;
import co.com.bussineslm.entities.SubModulos;
import co.com.bussineslm.entities.TiposDocumentos;
import co.com.bussineslm.entities.Usuarios;
import co.com.bussineslm.utilities.JPAFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SetDataToBD {
    
    public static List<Rol> getRoles(){
        return new ArrayList<Rol>(){
            {
                add(new Rol("USER", "USUARIO"));
                add(new Rol("PROP", "PROPIETARIO"));
            }  
        };
    }
    
    public static void setUsuario() {
        UsuariosJpaController ctrl = new UsuariosJpaController(JPAFactory.getFACTORY());
        
        try {
                 
            Usuarios usuario = ctrl.findUsuarios("lindasmascotasmed@gmail.com");
            if (usuario == null){
                
              usuario = new Usuarios(); 
              usuario.setCorreoElectronico("lindasmascotas@gmail.com");
              usuario.setIdUsuario(new Personas(1020));
              usuario.setContraseña("12345MVI");
              usuario.setEstado(true);
              usuario.setRol("SU");
              
              ctrl.create(usuario);
            }
        } catch (Exception ex) {
            Logger.getLogger(SetDataToBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void setModulos() {
        ModulosJpaController ctrl = new ModulosJpaController(JPAFactory.getFACTORY());
        
        Modulos mod = ctrl.buscarModuloNombre("");
        
        try {
            if(mod == null){
                mod = new Modulos();
//                mod.setNombreModulo("PERSONAS");
//                mod.setNombreModulo("MASCOTAS");
//                mod.setNombreModulo("HISTORIA CLINICA");
//                mod.setNombreModulo("CITAS");
//                mod.setNombreModulo("SERVICIOS");
                
                ctrl.create(mod);
            }
        } catch (Exception ex) {
            Logger.getLogger(SetDataToBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void setPermisos() {
        PermisosJpaController ctrl = new PermisosJpaController(JPAFactory.getFACTORY());
        
        try {
            
            
        } catch (Exception e) {
        }
    }
    
    

    public static void setTipoDocumento() {
        TiposDocumentosJpaController ctrl = new TiposDocumentosJpaController(JPAFactory.getFACTORY());
        
        TiposDocumentos td = ctrl.buscarTipoDocNombre("CÉDULA CIUDADANÍA");
        
        if(td == null){
            td = new TiposDocumentos();
            td.setNombreTipoDoc("CÉDULA CIUDADANÍA");

            ctrl.create(td);
        }
                
    }

    public static void setGenero() {
        GenerosJpaController ctrl = new GenerosJpaController(JPAFactory.getFACTORY());
        
        Generos genero = ctrl.buscarGeneroNombre("MASCULINO");
        
        if(genero == null){
            genero =  new Generos();
            genero.setNombreGenero("MASCULINO");

            ctrl.create(genero);
        }    
    }

    public static void setPais() {
        try {
            PaisesJpaController ctrl = new PaisesJpaController(JPAFactory.getFACTORY());
            
            Paises pais = ctrl.findPaises("169");
            
            if(pais == null){
                pais = new Paises();
                pais.setIdPais("169");
                pais.setNombrePais("COLOMBIA");

                ctrl.create(pais);
            }
                  
        } catch (Exception ex) {
            Logger.getLogger(SetDataToBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setDepartamento() {
        try {
            DepartamentosJpaController ctrl = new DepartamentosJpaController(JPAFactory.getFACTORY());
            
            Departamentos depart = ctrl.findDepartamentos("05");
            
            if(depart == null){
                depart = new Departamentos();
                depart.setIdDepartamento("05");
                depart.setNombreDepart("ANTIOQUIA");
                depart.setIdPais(new Paises("169"));

                ctrl.create(depart);
            }
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(SetDataToBD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SetDataToBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setCiudad() {
        try {
            CiudadesJpaController ctrl = new CiudadesJpaController(JPAFactory.getFACTORY());
            
            Ciudades ciudad = ctrl.findCiudades("05001");
            
            if(ciudad == null){
                ciudad = new Ciudades();
                ciudad.setIdCiudad("05001");
                ciudad.setNombreCiudad("MEDELLIN");
                ciudad.setIdDepartamento(new Departamentos("05"));

                ctrl.create(ciudad);
            }
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(SetDataToBD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SetDataToBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setBarrio() {
        BarriosJpaController ctrl = new BarriosJpaController(JPAFactory.getFACTORY());
        
        Barrios b = ctrl.buscarBarrioNombre("BELEN");
        
        try {
            if(b == null){
                b = new Barrios();
                b.setNombreBarrio("BELEN");
                b.setIdCiudad(new Ciudades("05001"));

                ctrl.create(b);
            }
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(SetDataToBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setPerfil() {
        PerfilesJpaController ctrl = new PerfilesJpaController(JPAFactory.getFACTORY());
        
        Perfiles perf = ctrl.buscarPerfilNombre("SU");
        
        try {
            if(perf == null){
                perf = new Perfiles();
                perf.setNombrePerfil("SU");
                
                ctrl.create(perf);
            }
        } catch (Exception ex) {
            Logger.getLogger(SetDataToBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setPersona() {
        PersonasJpaController ctrl = new PersonasJpaController(JPAFactory.getFACTORY());
        GenerosJpaController ctrlGenero = new GenerosJpaController(JPAFactory.getFACTORY());
        BarriosJpaController ctrlBarrio = new BarriosJpaController(JPAFactory.getFACTORY());
        PerfilesJpaController ctrlPerfil = new PerfilesJpaController(JPAFactory.getFACTORY());
        
        Personas persona = ctrl.findPersonas(1020);
        
        try {
            if(persona == null){
                Generos g = ctrlGenero.buscarGeneroNombre("MASCULINO");
                Barrios b = ctrlBarrio.buscarBarrioNombre("BELEN");
                Perfiles perf = ctrlPerfil.buscarPerfilNombre("SU");
                
                persona = new Personas();
                persona.setIdentificacion(1020);
                persona.setNombres("EDWAR YESID");
                persona.setApellidos("ORTIZ ARANGO");
                persona.setFechaNacimiento(new Date());
                persona.setIdGenero(g);
                persona.setIdBarrio(b);
                persona.setDireccion("RODEO ALTO");
                persona.setTelefonoFijo("5776165");
                persona.setTelefonoMovil("3013958651");
                persona.setIdPerfil(perf);
                persona.setEstado(true);
                
                ctrl.create(persona);
            }
        } catch (Exception e) {
        }     
    }
    
    private List<Modulos> listaModulos() {
        List<Modulos> modulos = new ArrayList<>();
        
        
     
        return modulos;
    }  
}
