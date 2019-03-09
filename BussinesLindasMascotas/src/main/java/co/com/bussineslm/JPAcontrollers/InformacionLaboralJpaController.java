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
import co.com.bussineslm.entities.ServiciosPorEmpleados;
import co.com.bussineslm.entities.Cargos;
import co.com.bussineslm.entities.InformacionLaboral;
import co.com.bussineslm.entities.Personas;
import co.com.bussineslm.entities.TiposContratos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class InformacionLaboralJpaController implements Serializable {

    public InformacionLaboralJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(InformacionLaboral informacionLaboral) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Personas personasOrphanCheck = informacionLaboral.getPersonas();
        if (personasOrphanCheck != null) {
            InformacionLaboral oldInformacionLaboralOfPersonas = personasOrphanCheck.getInformacionLaboral();
            if (oldInformacionLaboralOfPersonas != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Personas " + personasOrphanCheck + " already has an item of type InformacionLaboral whose personas column cannot be null. Please make another selection for the personas field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ServiciosPorEmpleados serviciosPorEmpleados = informacionLaboral.getServiciosPorEmpleados();
            if (serviciosPorEmpleados != null) {
                serviciosPorEmpleados = em.getReference(serviciosPorEmpleados.getClass(), serviciosPorEmpleados.getIdServicioEmpl());
                informacionLaboral.setServiciosPorEmpleados(serviciosPorEmpleados);
            }
            Cargos idCargo = informacionLaboral.getIdCargo();
            if (idCargo != null) {
                idCargo = em.getReference(idCargo.getClass(), idCargo.getIdCargo());
                informacionLaboral.setIdCargo(idCargo);
            }
            Personas personas = informacionLaboral.getPersonas();
            if (personas != null) {
                personas = em.getReference(personas.getClass(), personas.getIdentificacion());
                informacionLaboral.setPersonas(personas);
            }
            TiposContratos idTipoContrato = informacionLaboral.getIdTipoContrato();
            if (idTipoContrato != null) {
                idTipoContrato = em.getReference(idTipoContrato.getClass(), idTipoContrato.getIdTipoContrato());
                informacionLaboral.setIdTipoContrato(idTipoContrato);
            }
            em.persist(informacionLaboral);
            if (serviciosPorEmpleados != null) {
                InformacionLaboral oldIdPersonaLabOfServiciosPorEmpleados = serviciosPorEmpleados.getIdPersonaLab();
                if (oldIdPersonaLabOfServiciosPorEmpleados != null) {
                    oldIdPersonaLabOfServiciosPorEmpleados.setServiciosPorEmpleados(null);
                    oldIdPersonaLabOfServiciosPorEmpleados = em.merge(oldIdPersonaLabOfServiciosPorEmpleados);
                }
                serviciosPorEmpleados.setIdPersonaLab(informacionLaboral);
                serviciosPorEmpleados = em.merge(serviciosPorEmpleados);
            }
            if (idCargo != null) {
                idCargo.getInformacionLaboralList().add(informacionLaboral);
                idCargo = em.merge(idCargo);
            }
            if (personas != null) {
                personas.setInformacionLaboral(informacionLaboral);
                personas = em.merge(personas);
            }
            if (idTipoContrato != null) {
                idTipoContrato.getInformacionLaboralList().add(informacionLaboral);
                idTipoContrato = em.merge(idTipoContrato);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findInformacionLaboral(informacionLaboral.getIdPersona()) != null) {
                throw new PreexistingEntityException("InformacionLaboral " + informacionLaboral + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(InformacionLaboral informacionLaboral) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InformacionLaboral persistentInformacionLaboral = em.find(InformacionLaboral.class, informacionLaboral.getIdPersona());
            ServiciosPorEmpleados serviciosPorEmpleadosOld = persistentInformacionLaboral.getServiciosPorEmpleados();
            ServiciosPorEmpleados serviciosPorEmpleadosNew = informacionLaboral.getServiciosPorEmpleados();
            Cargos idCargoOld = persistentInformacionLaboral.getIdCargo();
            Cargos idCargoNew = informacionLaboral.getIdCargo();
            Personas personasOld = persistentInformacionLaboral.getPersonas();
            Personas personasNew = informacionLaboral.getPersonas();
            TiposContratos idTipoContratoOld = persistentInformacionLaboral.getIdTipoContrato();
            TiposContratos idTipoContratoNew = informacionLaboral.getIdTipoContrato();
            List<String> illegalOrphanMessages = null;
            if (serviciosPorEmpleadosOld != null && !serviciosPorEmpleadosOld.equals(serviciosPorEmpleadosNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain ServiciosPorEmpleados " + serviciosPorEmpleadosOld + " since its idPersonaLab field is not nullable.");
            }
            if (personasNew != null && !personasNew.equals(personasOld)) {
                InformacionLaboral oldInformacionLaboralOfPersonas = personasNew.getInformacionLaboral();
                if (oldInformacionLaboralOfPersonas != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Personas " + personasNew + " already has an item of type InformacionLaboral whose personas column cannot be null. Please make another selection for the personas field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (serviciosPorEmpleadosNew != null) {
                serviciosPorEmpleadosNew = em.getReference(serviciosPorEmpleadosNew.getClass(), serviciosPorEmpleadosNew.getIdServicioEmpl());
                informacionLaboral.setServiciosPorEmpleados(serviciosPorEmpleadosNew);
            }
            if (idCargoNew != null) {
                idCargoNew = em.getReference(idCargoNew.getClass(), idCargoNew.getIdCargo());
                informacionLaboral.setIdCargo(idCargoNew);
            }
            if (personasNew != null) {
                personasNew = em.getReference(personasNew.getClass(), personasNew.getIdentificacion());
                informacionLaboral.setPersonas(personasNew);
            }
            if (idTipoContratoNew != null) {
                idTipoContratoNew = em.getReference(idTipoContratoNew.getClass(), idTipoContratoNew.getIdTipoContrato());
                informacionLaboral.setIdTipoContrato(idTipoContratoNew);
            }
            informacionLaboral = em.merge(informacionLaboral);
            if (serviciosPorEmpleadosNew != null && !serviciosPorEmpleadosNew.equals(serviciosPorEmpleadosOld)) {
                InformacionLaboral oldIdPersonaLabOfServiciosPorEmpleados = serviciosPorEmpleadosNew.getIdPersonaLab();
                if (oldIdPersonaLabOfServiciosPorEmpleados != null) {
                    oldIdPersonaLabOfServiciosPorEmpleados.setServiciosPorEmpleados(null);
                    oldIdPersonaLabOfServiciosPorEmpleados = em.merge(oldIdPersonaLabOfServiciosPorEmpleados);
                }
                serviciosPorEmpleadosNew.setIdPersonaLab(informacionLaboral);
                serviciosPorEmpleadosNew = em.merge(serviciosPorEmpleadosNew);
            }
            if (idCargoOld != null && !idCargoOld.equals(idCargoNew)) {
                idCargoOld.getInformacionLaboralList().remove(informacionLaboral);
                idCargoOld = em.merge(idCargoOld);
            }
            if (idCargoNew != null && !idCargoNew.equals(idCargoOld)) {
                idCargoNew.getInformacionLaboralList().add(informacionLaboral);
                idCargoNew = em.merge(idCargoNew);
            }
            if (personasOld != null && !personasOld.equals(personasNew)) {
                personasOld.setInformacionLaboral(null);
                personasOld = em.merge(personasOld);
            }
            if (personasNew != null && !personasNew.equals(personasOld)) {
                personasNew.setInformacionLaboral(informacionLaboral);
                personasNew = em.merge(personasNew);
            }
            if (idTipoContratoOld != null && !idTipoContratoOld.equals(idTipoContratoNew)) {
                idTipoContratoOld.getInformacionLaboralList().remove(informacionLaboral);
                idTipoContratoOld = em.merge(idTipoContratoOld);
            }
            if (idTipoContratoNew != null && !idTipoContratoNew.equals(idTipoContratoOld)) {
                idTipoContratoNew.getInformacionLaboralList().add(informacionLaboral);
                idTipoContratoNew = em.merge(idTipoContratoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = informacionLaboral.getIdPersona();
                if (findInformacionLaboral(id) == null) {
                    throw new NonexistentEntityException("The informacionLaboral with id " + id + " no longer exists.");
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
            InformacionLaboral informacionLaboral;
            try {
                informacionLaboral = em.getReference(InformacionLaboral.class, id);
                informacionLaboral.getIdPersona();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The informacionLaboral with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            ServiciosPorEmpleados serviciosPorEmpleadosOrphanCheck = informacionLaboral.getServiciosPorEmpleados();
            if (serviciosPorEmpleadosOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This InformacionLaboral (" + informacionLaboral + ") cannot be destroyed since the ServiciosPorEmpleados " + serviciosPorEmpleadosOrphanCheck + " in its serviciosPorEmpleados field has a non-nullable idPersonaLab field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cargos idCargo = informacionLaboral.getIdCargo();
            if (idCargo != null) {
                idCargo.getInformacionLaboralList().remove(informacionLaboral);
                idCargo = em.merge(idCargo);
            }
            Personas personas = informacionLaboral.getPersonas();
            if (personas != null) {
                personas.setInformacionLaboral(null);
                personas = em.merge(personas);
            }
            TiposContratos idTipoContrato = informacionLaboral.getIdTipoContrato();
            if (idTipoContrato != null) {
                idTipoContrato.getInformacionLaboralList().remove(informacionLaboral);
                idTipoContrato = em.merge(idTipoContrato);
            }
            em.remove(informacionLaboral);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<InformacionLaboral> findInformacionLaboralEntities() {
        return findInformacionLaboralEntities(true, -1, -1);
    }

    public List<InformacionLaboral> findInformacionLaboralEntities(int maxResults, int firstResult) {
        return findInformacionLaboralEntities(false, maxResults, firstResult);
    }

    private List<InformacionLaboral> findInformacionLaboralEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InformacionLaboral.class));
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

    public InformacionLaboral findInformacionLaboral(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(InformacionLaboral.class, id);
        } finally {
            em.close();
        }
    }

    public int getInformacionLaboralCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InformacionLaboral> rt = cq.from(InformacionLaboral.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
