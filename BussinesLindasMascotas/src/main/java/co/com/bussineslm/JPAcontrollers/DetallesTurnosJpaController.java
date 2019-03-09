/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.JPAcontrollers;

import co.com.bussineslm.JPAcontrollers.exceptions.NonexistentEntityException;
import co.com.bussineslm.entities.DetallesTurnos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.com.bussineslm.entities.Turnos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class DetallesTurnosJpaController implements Serializable {

    public DetallesTurnosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetallesTurnos detallesTurnos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Turnos idTurno = detallesTurnos.getIdTurno();
            if (idTurno != null) {
                idTurno = em.getReference(idTurno.getClass(), idTurno.getIdTurno());
                detallesTurnos.setIdTurno(idTurno);
            }
            em.persist(detallesTurnos);
            if (idTurno != null) {
                idTurno.getDetallesTurnosList().add(detallesTurnos);
                idTurno = em.merge(idTurno);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetallesTurnos detallesTurnos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetallesTurnos persistentDetallesTurnos = em.find(DetallesTurnos.class, detallesTurnos.getIdDetalleTurno());
            Turnos idTurnoOld = persistentDetallesTurnos.getIdTurno();
            Turnos idTurnoNew = detallesTurnos.getIdTurno();
            if (idTurnoNew != null) {
                idTurnoNew = em.getReference(idTurnoNew.getClass(), idTurnoNew.getIdTurno());
                detallesTurnos.setIdTurno(idTurnoNew);
            }
            detallesTurnos = em.merge(detallesTurnos);
            if (idTurnoOld != null && !idTurnoOld.equals(idTurnoNew)) {
                idTurnoOld.getDetallesTurnosList().remove(detallesTurnos);
                idTurnoOld = em.merge(idTurnoOld);
            }
            if (idTurnoNew != null && !idTurnoNew.equals(idTurnoOld)) {
                idTurnoNew.getDetallesTurnosList().add(detallesTurnos);
                idTurnoNew = em.merge(idTurnoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detallesTurnos.getIdDetalleTurno();
                if (findDetallesTurnos(id) == null) {
                    throw new NonexistentEntityException("The detallesTurnos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetallesTurnos detallesTurnos;
            try {
                detallesTurnos = em.getReference(DetallesTurnos.class, id);
                detallesTurnos.getIdDetalleTurno();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallesTurnos with id " + id + " no longer exists.", enfe);
            }
            Turnos idTurno = detallesTurnos.getIdTurno();
            if (idTurno != null) {
                idTurno.getDetallesTurnosList().remove(detallesTurnos);
                idTurno = em.merge(idTurno);
            }
            em.remove(detallesTurnos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetallesTurnos> findDetallesTurnosEntities() {
        return findDetallesTurnosEntities(true, -1, -1);
    }

    public List<DetallesTurnos> findDetallesTurnosEntities(int maxResults, int firstResult) {
        return findDetallesTurnosEntities(false, maxResults, firstResult);
    }

    private List<DetallesTurnos> findDetallesTurnosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetallesTurnos.class));
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

    public DetallesTurnos findDetallesTurnos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetallesTurnos.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallesTurnosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetallesTurnos> rt = cq.from(DetallesTurnos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
