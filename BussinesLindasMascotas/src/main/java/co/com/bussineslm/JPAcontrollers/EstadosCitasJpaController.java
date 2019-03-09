/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.JPAcontrollers;

import co.com.bussineslm.JPAcontrollers.exceptions.IllegalOrphanException;
import co.com.bussineslm.JPAcontrollers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.com.bussineslm.entities.Citas;
import co.com.bussineslm.entities.EstadosCitas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class EstadosCitasJpaController implements Serializable {

    public EstadosCitasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EstadosCitas estadosCitas) {
        if (estadosCitas.getCitasList() == null) {
            estadosCitas.setCitasList(new ArrayList<Citas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Citas> attachedCitasList = new ArrayList<Citas>();
            for (Citas citasListCitasToAttach : estadosCitas.getCitasList()) {
                citasListCitasToAttach = em.getReference(citasListCitasToAttach.getClass(), citasListCitasToAttach.getIdCita());
                attachedCitasList.add(citasListCitasToAttach);
            }
            estadosCitas.setCitasList(attachedCitasList);
            em.persist(estadosCitas);
            for (Citas citasListCitas : estadosCitas.getCitasList()) {
                EstadosCitas oldIdEstadoOfCitasListCitas = citasListCitas.getIdEstado();
                citasListCitas.setIdEstado(estadosCitas);
                citasListCitas = em.merge(citasListCitas);
                if (oldIdEstadoOfCitasListCitas != null) {
                    oldIdEstadoOfCitasListCitas.getCitasList().remove(citasListCitas);
                    oldIdEstadoOfCitasListCitas = em.merge(oldIdEstadoOfCitasListCitas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EstadosCitas estadosCitas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstadosCitas persistentEstadosCitas = em.find(EstadosCitas.class, estadosCitas.getIdEstado());
            List<Citas> citasListOld = persistentEstadosCitas.getCitasList();
            List<Citas> citasListNew = estadosCitas.getCitasList();
            List<String> illegalOrphanMessages = null;
            for (Citas citasListOldCitas : citasListOld) {
                if (!citasListNew.contains(citasListOldCitas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Citas " + citasListOldCitas + " since its idEstado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Citas> attachedCitasListNew = new ArrayList<Citas>();
            for (Citas citasListNewCitasToAttach : citasListNew) {
                citasListNewCitasToAttach = em.getReference(citasListNewCitasToAttach.getClass(), citasListNewCitasToAttach.getIdCita());
                attachedCitasListNew.add(citasListNewCitasToAttach);
            }
            citasListNew = attachedCitasListNew;
            estadosCitas.setCitasList(citasListNew);
            estadosCitas = em.merge(estadosCitas);
            for (Citas citasListNewCitas : citasListNew) {
                if (!citasListOld.contains(citasListNewCitas)) {
                    EstadosCitas oldIdEstadoOfCitasListNewCitas = citasListNewCitas.getIdEstado();
                    citasListNewCitas.setIdEstado(estadosCitas);
                    citasListNewCitas = em.merge(citasListNewCitas);
                    if (oldIdEstadoOfCitasListNewCitas != null && !oldIdEstadoOfCitasListNewCitas.equals(estadosCitas)) {
                        oldIdEstadoOfCitasListNewCitas.getCitasList().remove(citasListNewCitas);
                        oldIdEstadoOfCitasListNewCitas = em.merge(oldIdEstadoOfCitasListNewCitas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estadosCitas.getIdEstado();
                if (findEstadosCitas(id) == null) {
                    throw new NonexistentEntityException("The estadosCitas with id " + id + " no longer exists.");
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
            EstadosCitas estadosCitas;
            try {
                estadosCitas = em.getReference(EstadosCitas.class, id);
                estadosCitas.getIdEstado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadosCitas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Citas> citasListOrphanCheck = estadosCitas.getCitasList();
            for (Citas citasListOrphanCheckCitas : citasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EstadosCitas (" + estadosCitas + ") cannot be destroyed since the Citas " + citasListOrphanCheckCitas + " in its citasList field has a non-nullable idEstado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estadosCitas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EstadosCitas> findEstadosCitasEntities() {
        return findEstadosCitasEntities(true, -1, -1);
    }

    public List<EstadosCitas> findEstadosCitasEntities(int maxResults, int firstResult) {
        return findEstadosCitasEntities(false, maxResults, firstResult);
    }

    private List<EstadosCitas> findEstadosCitasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstadosCitas.class));
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

    public EstadosCitas findEstadosCitas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstadosCitas.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadosCitasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstadosCitas> rt = cq.from(EstadosCitas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
