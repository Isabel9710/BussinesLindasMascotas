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
import co.com.bussineslm.entities.DetallesTurnos;
import co.com.bussineslm.entities.Turnos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class TurnosJpaController implements Serializable {

    public TurnosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Turnos turnos) {
        if (turnos.getDetallesTurnosList() == null) {
            turnos.setDetallesTurnosList(new ArrayList<DetallesTurnos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<DetallesTurnos> attachedDetallesTurnosList = new ArrayList<DetallesTurnos>();
            for (DetallesTurnos detallesTurnosListDetallesTurnosToAttach : turnos.getDetallesTurnosList()) {
                detallesTurnosListDetallesTurnosToAttach = em.getReference(detallesTurnosListDetallesTurnosToAttach.getClass(), detallesTurnosListDetallesTurnosToAttach.getIdDetalleTurno());
                attachedDetallesTurnosList.add(detallesTurnosListDetallesTurnosToAttach);
            }
            turnos.setDetallesTurnosList(attachedDetallesTurnosList);
            em.persist(turnos);
            for (DetallesTurnos detallesTurnosListDetallesTurnos : turnos.getDetallesTurnosList()) {
                Turnos oldIdTurnoOfDetallesTurnosListDetallesTurnos = detallesTurnosListDetallesTurnos.getIdTurno();
                detallesTurnosListDetallesTurnos.setIdTurno(turnos);
                detallesTurnosListDetallesTurnos = em.merge(detallesTurnosListDetallesTurnos);
                if (oldIdTurnoOfDetallesTurnosListDetallesTurnos != null) {
                    oldIdTurnoOfDetallesTurnosListDetallesTurnos.getDetallesTurnosList().remove(detallesTurnosListDetallesTurnos);
                    oldIdTurnoOfDetallesTurnosListDetallesTurnos = em.merge(oldIdTurnoOfDetallesTurnosListDetallesTurnos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Turnos turnos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Turnos persistentTurnos = em.find(Turnos.class, turnos.getIdTurno());
            List<DetallesTurnos> detallesTurnosListOld = persistentTurnos.getDetallesTurnosList();
            List<DetallesTurnos> detallesTurnosListNew = turnos.getDetallesTurnosList();
            List<String> illegalOrphanMessages = null;
            for (DetallesTurnos detallesTurnosListOldDetallesTurnos : detallesTurnosListOld) {
                if (!detallesTurnosListNew.contains(detallesTurnosListOldDetallesTurnos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetallesTurnos " + detallesTurnosListOldDetallesTurnos + " since its idTurno field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<DetallesTurnos> attachedDetallesTurnosListNew = new ArrayList<DetallesTurnos>();
            for (DetallesTurnos detallesTurnosListNewDetallesTurnosToAttach : detallesTurnosListNew) {
                detallesTurnosListNewDetallesTurnosToAttach = em.getReference(detallesTurnosListNewDetallesTurnosToAttach.getClass(), detallesTurnosListNewDetallesTurnosToAttach.getIdDetalleTurno());
                attachedDetallesTurnosListNew.add(detallesTurnosListNewDetallesTurnosToAttach);
            }
            detallesTurnosListNew = attachedDetallesTurnosListNew;
            turnos.setDetallesTurnosList(detallesTurnosListNew);
            turnos = em.merge(turnos);
            for (DetallesTurnos detallesTurnosListNewDetallesTurnos : detallesTurnosListNew) {
                if (!detallesTurnosListOld.contains(detallesTurnosListNewDetallesTurnos)) {
                    Turnos oldIdTurnoOfDetallesTurnosListNewDetallesTurnos = detallesTurnosListNewDetallesTurnos.getIdTurno();
                    detallesTurnosListNewDetallesTurnos.setIdTurno(turnos);
                    detallesTurnosListNewDetallesTurnos = em.merge(detallesTurnosListNewDetallesTurnos);
                    if (oldIdTurnoOfDetallesTurnosListNewDetallesTurnos != null && !oldIdTurnoOfDetallesTurnosListNewDetallesTurnos.equals(turnos)) {
                        oldIdTurnoOfDetallesTurnosListNewDetallesTurnos.getDetallesTurnosList().remove(detallesTurnosListNewDetallesTurnos);
                        oldIdTurnoOfDetallesTurnosListNewDetallesTurnos = em.merge(oldIdTurnoOfDetallesTurnosListNewDetallesTurnos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = turnos.getIdTurno();
                if (findTurnos(id) == null) {
                    throw new NonexistentEntityException("The turnos with id " + id + " no longer exists.");
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
            Turnos turnos;
            try {
                turnos = em.getReference(Turnos.class, id);
                turnos.getIdTurno();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The turnos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetallesTurnos> detallesTurnosListOrphanCheck = turnos.getDetallesTurnosList();
            for (DetallesTurnos detallesTurnosListOrphanCheckDetallesTurnos : detallesTurnosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Turnos (" + turnos + ") cannot be destroyed since the DetallesTurnos " + detallesTurnosListOrphanCheckDetallesTurnos + " in its detallesTurnosList field has a non-nullable idTurno field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(turnos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Turnos> findTurnosEntities() {
        return findTurnosEntities(true, -1, -1);
    }

    public List<Turnos> findTurnosEntities(int maxResults, int firstResult) {
        return findTurnosEntities(false, maxResults, firstResult);
    }

    private List<Turnos> findTurnosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Turnos.class));
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

    public Turnos findTurnos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Turnos.class, id);
        } finally {
            em.close();
        }
    }

    public int getTurnosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Turnos> rt = cq.from(Turnos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
