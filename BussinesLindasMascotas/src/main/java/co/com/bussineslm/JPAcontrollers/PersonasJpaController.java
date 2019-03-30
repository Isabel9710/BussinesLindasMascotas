/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.JPAcontrollers;

import co.com.bussineslm.JPAcontrollers.exceptions.IllegalOrphanException;
import co.com.bussineslm.JPAcontrollers.exceptions.NonexistentEntityException;
import co.com.bussineslm.JPAcontrollers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.com.bussineslm.entities.InformacionLaboral;
import co.com.bussineslm.entities.Barrios;
import co.com.bussineslm.entities.Generos;
import co.com.bussineslm.entities.TiposDocumentos;
import co.com.bussineslm.entities.Usuarios;
import java.util.ArrayList;
import java.util.List;
import co.com.bussineslm.entities.Citas;
import co.com.bussineslm.entities.Mascotas;
import co.com.bussineslm.entities.Personas;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class PersonasJpaController implements Serializable {

    public PersonasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Personas personas) throws PreexistingEntityException, Exception {
        if (personas.getUsuariosList() == null) {
            personas.setUsuariosList(new ArrayList<Usuarios>());
        }
        if (personas.getCitasList() == null) {
            personas.setCitasList(new ArrayList<Citas>());
        }
        if (personas.getMascotasList() == null) {
            personas.setMascotasList(new ArrayList<Mascotas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InformacionLaboral informacionLaboral = personas.getInformacionLaboral();
            if (informacionLaboral != null) {
                informacionLaboral = em.getReference(informacionLaboral.getClass(), informacionLaboral.getIdPersona());
                personas.setInformacionLaboral(informacionLaboral);
            }
            Barrios idBarrio = personas.getIdBarrio();
            if (idBarrio != null) {
                idBarrio = em.getReference(idBarrio.getClass(), idBarrio.getIdBarrio());
                personas.setIdBarrio(idBarrio);
            }
            Generos idGenero = personas.getIdGenero();
            if (idGenero != null) {
                idGenero = em.getReference(idGenero.getClass(), idGenero.getIdGenero());
                personas.setIdGenero(idGenero);
            }
            TiposDocumentos tipoDocumento = personas.getTipoDocumento();
            if (tipoDocumento != null) {
                tipoDocumento = em.getReference(tipoDocumento.getClass(), tipoDocumento.getIdTipoDoc());
                personas.setTipoDocumento(tipoDocumento);
            }
            List<Usuarios> attachedUsuariosList = new ArrayList<Usuarios>();
            for (Usuarios usuariosListUsuariosToAttach : personas.getUsuariosList()) {
                usuariosListUsuariosToAttach = em.getReference(usuariosListUsuariosToAttach.getClass(), usuariosListUsuariosToAttach.getCorreoElectronico());
                attachedUsuariosList.add(usuariosListUsuariosToAttach);
            }
            personas.setUsuariosList(attachedUsuariosList);
            List<Citas> attachedCitasList = new ArrayList<Citas>();
            for (Citas citasListCitasToAttach : personas.getCitasList()) {
                citasListCitasToAttach = em.getReference(citasListCitasToAttach.getClass(), citasListCitasToAttach.getIdCita());
                attachedCitasList.add(citasListCitasToAttach);
            }
            personas.setCitasList(attachedCitasList);
            List<Mascotas> attachedMascotasList = new ArrayList<Mascotas>();
            for (Mascotas mascotasListMascotasToAttach : personas.getMascotasList()) {
                mascotasListMascotasToAttach = em.getReference(mascotasListMascotasToAttach.getClass(), mascotasListMascotasToAttach.getIdMascota());
                attachedMascotasList.add(mascotasListMascotasToAttach);
            }
            personas.setMascotasList(attachedMascotasList);
            em.persist(personas);
            if (informacionLaboral != null) {
                Personas oldPersonasOfInformacionLaboral = informacionLaboral.getPersonas();
                if (oldPersonasOfInformacionLaboral != null) {
                    oldPersonasOfInformacionLaboral.setInformacionLaboral(null);
                    oldPersonasOfInformacionLaboral = em.merge(oldPersonasOfInformacionLaboral);
                }
                informacionLaboral.setPersonas(personas);
                informacionLaboral = em.merge(informacionLaboral);
            }
            if (idBarrio != null) {
                idBarrio.getPersonasList().add(personas);
                idBarrio = em.merge(idBarrio);
            }
            if (idGenero != null) {
                idGenero.getPersonasList().add(personas);
                idGenero = em.merge(idGenero);
            }
            if (tipoDocumento != null) {
                tipoDocumento.getPersonasList().add(personas);
                tipoDocumento = em.merge(tipoDocumento);
            }
            for (Usuarios usuariosListUsuarios : personas.getUsuariosList()) {
                Personas oldIdUsuarioOfUsuariosListUsuarios = usuariosListUsuarios.getIdUsuario();
                usuariosListUsuarios.setIdUsuario(personas);
                usuariosListUsuarios = em.merge(usuariosListUsuarios);
                if (oldIdUsuarioOfUsuariosListUsuarios != null) {
                    oldIdUsuarioOfUsuariosListUsuarios.getUsuariosList().remove(usuariosListUsuarios);
                    oldIdUsuarioOfUsuariosListUsuarios = em.merge(oldIdUsuarioOfUsuariosListUsuarios);
                }
            }
            for (Citas citasListCitas : personas.getCitasList()) {
                Personas oldIdPropietarioOfCitasListCitas = citasListCitas.getIdPropietario();
                citasListCitas.setIdPropietario(personas);
                citasListCitas = em.merge(citasListCitas);
                if (oldIdPropietarioOfCitasListCitas != null) {
                    oldIdPropietarioOfCitasListCitas.getCitasList().remove(citasListCitas);
                    oldIdPropietarioOfCitasListCitas = em.merge(oldIdPropietarioOfCitasListCitas);
                }
            }
            for (Mascotas mascotasListMascotas : personas.getMascotasList()) {
                Personas oldIdPropietarioOfMascotasListMascotas = mascotasListMascotas.getIdPropietario();
                mascotasListMascotas.setIdPropietario(personas);
                mascotasListMascotas = em.merge(mascotasListMascotas);
                if (oldIdPropietarioOfMascotasListMascotas != null) {
                    oldIdPropietarioOfMascotasListMascotas.getMascotasList().remove(mascotasListMascotas);
                    oldIdPropietarioOfMascotasListMascotas = em.merge(oldIdPropietarioOfMascotasListMascotas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPersonas(personas.getIdentificacion()) != null) {
                throw new PreexistingEntityException("Personas " + personas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Personas personas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personas persistentPersonas = em.find(Personas.class, personas.getIdentificacion());
            InformacionLaboral informacionLaboralOld = persistentPersonas.getInformacionLaboral();
            InformacionLaboral informacionLaboralNew = personas.getInformacionLaboral();
            Barrios idBarrioOld = persistentPersonas.getIdBarrio();
            Barrios idBarrioNew = personas.getIdBarrio();
            Generos idGeneroOld = persistentPersonas.getIdGenero();
            Generos idGeneroNew = personas.getIdGenero();
            TiposDocumentos tipoDocumentoOld = persistentPersonas.getTipoDocumento();
            TiposDocumentos tipoDocumentoNew = personas.getTipoDocumento();
            List<Usuarios> usuariosListOld = persistentPersonas.getUsuariosList();
            List<Usuarios> usuariosListNew = personas.getUsuariosList();
            List<Citas> citasListOld = persistentPersonas.getCitasList();
            List<Citas> citasListNew = personas.getCitasList();
            List<Mascotas> mascotasListOld = persistentPersonas.getMascotasList();
            List<Mascotas> mascotasListNew = personas.getMascotasList();
            List<String> illegalOrphanMessages = null;
            if (informacionLaboralOld != null && !informacionLaboralOld.equals(informacionLaboralNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain InformacionLaboral " + informacionLaboralOld + " since its personas field is not nullable.");
            }
            for (Usuarios usuariosListOldUsuarios : usuariosListOld) {
                if (!usuariosListNew.contains(usuariosListOldUsuarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuarios " + usuariosListOldUsuarios + " since its idUsuario field is not nullable.");
                }
            }
            for (Citas citasListOldCitas : citasListOld) {
                if (!citasListNew.contains(citasListOldCitas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Citas " + citasListOldCitas + " since its idPropietario field is not nullable.");
                }
            }
            for (Mascotas mascotasListOldMascotas : mascotasListOld) {
                if (!mascotasListNew.contains(mascotasListOldMascotas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Mascotas " + mascotasListOldMascotas + " since its idPropietario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (informacionLaboralNew != null) {
                informacionLaboralNew = em.getReference(informacionLaboralNew.getClass(), informacionLaboralNew.getIdPersona());
                personas.setInformacionLaboral(informacionLaboralNew);
            }
            if (idBarrioNew != null) {
                idBarrioNew = em.getReference(idBarrioNew.getClass(), idBarrioNew.getIdBarrio());
                personas.setIdBarrio(idBarrioNew);
            }
            if (idGeneroNew != null) {
                idGeneroNew = em.getReference(idGeneroNew.getClass(), idGeneroNew.getIdGenero());
                personas.setIdGenero(idGeneroNew);
            }
            if (tipoDocumentoNew != null) {
                tipoDocumentoNew = em.getReference(tipoDocumentoNew.getClass(), tipoDocumentoNew.getIdTipoDoc());
                personas.setTipoDocumento(tipoDocumentoNew);
            }
            List<Usuarios> attachedUsuariosListNew = new ArrayList<Usuarios>();
            for (Usuarios usuariosListNewUsuariosToAttach : usuariosListNew) {
                usuariosListNewUsuariosToAttach = em.getReference(usuariosListNewUsuariosToAttach.getClass(), usuariosListNewUsuariosToAttach.getCorreoElectronico());
                attachedUsuariosListNew.add(usuariosListNewUsuariosToAttach);
            }
            usuariosListNew = attachedUsuariosListNew;
            personas.setUsuariosList(usuariosListNew);
            List<Citas> attachedCitasListNew = new ArrayList<Citas>();
            for (Citas citasListNewCitasToAttach : citasListNew) {
                citasListNewCitasToAttach = em.getReference(citasListNewCitasToAttach.getClass(), citasListNewCitasToAttach.getIdCita());
                attachedCitasListNew.add(citasListNewCitasToAttach);
            }
            citasListNew = attachedCitasListNew;
            personas.setCitasList(citasListNew);
            List<Mascotas> attachedMascotasListNew = new ArrayList<Mascotas>();
            for (Mascotas mascotasListNewMascotasToAttach : mascotasListNew) {
                mascotasListNewMascotasToAttach = em.getReference(mascotasListNewMascotasToAttach.getClass(), mascotasListNewMascotasToAttach.getIdMascota());
                attachedMascotasListNew.add(mascotasListNewMascotasToAttach);
            }
            mascotasListNew = attachedMascotasListNew;
            personas.setMascotasList(mascotasListNew);
            personas = em.merge(personas);
            if (informacionLaboralNew != null && !informacionLaboralNew.equals(informacionLaboralOld)) {
                Personas oldPersonasOfInformacionLaboral = informacionLaboralNew.getPersonas();
                if (oldPersonasOfInformacionLaboral != null) {
                    oldPersonasOfInformacionLaboral.setInformacionLaboral(null);
                    oldPersonasOfInformacionLaboral = em.merge(oldPersonasOfInformacionLaboral);
                }
                informacionLaboralNew.setPersonas(personas);
                informacionLaboralNew = em.merge(informacionLaboralNew);
            }
            if (idBarrioOld != null && !idBarrioOld.equals(idBarrioNew)) {
                idBarrioOld.getPersonasList().remove(personas);
                idBarrioOld = em.merge(idBarrioOld);
            }
            if (idBarrioNew != null && !idBarrioNew.equals(idBarrioOld)) {
                idBarrioNew.getPersonasList().add(personas);
                idBarrioNew = em.merge(idBarrioNew);
            }
            if (idGeneroOld != null && !idGeneroOld.equals(idGeneroNew)) {
                idGeneroOld.getPersonasList().remove(personas);
                idGeneroOld = em.merge(idGeneroOld);
            }
            if (idGeneroNew != null && !idGeneroNew.equals(idGeneroOld)) {
                idGeneroNew.getPersonasList().add(personas);
                idGeneroNew = em.merge(idGeneroNew);
            }
            if (tipoDocumentoOld != null && !tipoDocumentoOld.equals(tipoDocumentoNew)) {
                tipoDocumentoOld.getPersonasList().remove(personas);
                tipoDocumentoOld = em.merge(tipoDocumentoOld);
            }
            if (tipoDocumentoNew != null && !tipoDocumentoNew.equals(tipoDocumentoOld)) {
                tipoDocumentoNew.getPersonasList().add(personas);
                tipoDocumentoNew = em.merge(tipoDocumentoNew);
            }
            for (Usuarios usuariosListNewUsuarios : usuariosListNew) {
                if (!usuariosListOld.contains(usuariosListNewUsuarios)) {
                    Personas oldIdUsuarioOfUsuariosListNewUsuarios = usuariosListNewUsuarios.getIdUsuario();
                    usuariosListNewUsuarios.setIdUsuario(personas);
                    usuariosListNewUsuarios = em.merge(usuariosListNewUsuarios);
                    if (oldIdUsuarioOfUsuariosListNewUsuarios != null && !oldIdUsuarioOfUsuariosListNewUsuarios.equals(personas)) {
                        oldIdUsuarioOfUsuariosListNewUsuarios.getUsuariosList().remove(usuariosListNewUsuarios);
                        oldIdUsuarioOfUsuariosListNewUsuarios = em.merge(oldIdUsuarioOfUsuariosListNewUsuarios);
                    }
                }
            }
            for (Citas citasListNewCitas : citasListNew) {
                if (!citasListOld.contains(citasListNewCitas)) {
                    Personas oldIdPropietarioOfCitasListNewCitas = citasListNewCitas.getIdPropietario();
                    citasListNewCitas.setIdPropietario(personas);
                    citasListNewCitas = em.merge(citasListNewCitas);
                    if (oldIdPropietarioOfCitasListNewCitas != null && !oldIdPropietarioOfCitasListNewCitas.equals(personas)) {
                        oldIdPropietarioOfCitasListNewCitas.getCitasList().remove(citasListNewCitas);
                        oldIdPropietarioOfCitasListNewCitas = em.merge(oldIdPropietarioOfCitasListNewCitas);
                    }
                }
            }
            for (Mascotas mascotasListNewMascotas : mascotasListNew) {
                if (!mascotasListOld.contains(mascotasListNewMascotas)) {
                    Personas oldIdPropietarioOfMascotasListNewMascotas = mascotasListNewMascotas.getIdPropietario();
                    mascotasListNewMascotas.setIdPropietario(personas);
                    mascotasListNewMascotas = em.merge(mascotasListNewMascotas);
                    if (oldIdPropietarioOfMascotasListNewMascotas != null && !oldIdPropietarioOfMascotasListNewMascotas.equals(personas)) {
                        oldIdPropietarioOfMascotasListNewMascotas.getMascotasList().remove(mascotasListNewMascotas);
                        oldIdPropietarioOfMascotasListNewMascotas = em.merge(oldIdPropietarioOfMascotasListNewMascotas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = personas.getIdentificacion();
                if (findPersonas(id) == null) {
                    throw new NonexistentEntityException("The personas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personas personas;
            try {
                personas = em.getReference(Personas.class, id);
                personas.getIdentificacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The personas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            InformacionLaboral informacionLaboralOrphanCheck = personas.getInformacionLaboral();
            if (informacionLaboralOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Personas (" + personas + ") cannot be destroyed since the InformacionLaboral " + informacionLaboralOrphanCheck + " in its informacionLaboral field has a non-nullable personas field.");
            }
            List<Usuarios> usuariosListOrphanCheck = personas.getUsuariosList();
            for (Usuarios usuariosListOrphanCheckUsuarios : usuariosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Personas (" + personas + ") cannot be destroyed since the Usuarios " + usuariosListOrphanCheckUsuarios + " in its usuariosList field has a non-nullable idUsuario field.");
            }
            List<Citas> citasListOrphanCheck = personas.getCitasList();
            for (Citas citasListOrphanCheckCitas : citasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Personas (" + personas + ") cannot be destroyed since the Citas " + citasListOrphanCheckCitas + " in its citasList field has a non-nullable idPropietario field.");
            }
            List<Mascotas> mascotasListOrphanCheck = personas.getMascotasList();
            for (Mascotas mascotasListOrphanCheckMascotas : mascotasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Personas (" + personas + ") cannot be destroyed since the Mascotas " + mascotasListOrphanCheckMascotas + " in its mascotasList field has a non-nullable idPropietario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Barrios idBarrio = personas.getIdBarrio();
            if (idBarrio != null) {
                idBarrio.getPersonasList().remove(personas);
                idBarrio = em.merge(idBarrio);
            }
            Generos idGenero = personas.getIdGenero();
            if (idGenero != null) {
                idGenero.getPersonasList().remove(personas);
                idGenero = em.merge(idGenero);
            }
            TiposDocumentos tipoDocumento = personas.getTipoDocumento();
            if (tipoDocumento != null) {
                tipoDocumento.getPersonasList().remove(personas);
                tipoDocumento = em.merge(tipoDocumento);
            }
            em.remove(personas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Personas> findPersonasEntities() {
        return findPersonasEntities(true, -1, -1);
    }

    public List<Personas> findPersonasEntities(int maxResults, int firstResult) {
        return findPersonasEntities(false, maxResults, firstResult);
    }

    private List<Personas> findPersonasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Personas.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Personas findPersonas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Personas.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Personas> rt = cq.from(Personas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
