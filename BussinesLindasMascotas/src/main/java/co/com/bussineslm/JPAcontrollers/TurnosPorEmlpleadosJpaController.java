/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.JPAcontrollers;

import co.com.bussineslm.JPAcontrollers.exceptions.NonexistentEntityException;
import co.com.bussineslm.entities.TurnosPorEmlpleados;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Isabel Medina
 */
public class TurnosPorEmlpleadosJpaController implements Serializable {

    public TurnosPorEmlpleadosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TurnosPorEmlpleados turnosPorEmlpleados) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(turnosPorEmlpleados);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TurnosPorEmlpleados turnosPorEmlpleados) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            turnosPorEmlpleados = em.merge(turnosPorEmlpleados);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = turnosPorEmlpleados.getIdTunosPorEmpl();
                if (findTurnosPorEmlpleados(id) == null) {
                    throw new NonexistentEntityException("The turnosPorEmlpleados with id " + id + " no longer exists.");
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
            TurnosPorEmlpleados turnosPorEmlpleados;
            try {
                turnosPorEmlpleados = em.getReference(TurnosPorEmlpleados.class, id);
                turnosPorEmlpleados.getIdTunosPorEmpl();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The turnosPorEmlpleados with id " + id + " no longer exists.", enfe);
            }
            em.remove(turnosPorEmlpleados);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TurnosPorEmlpleados> findTurnosPorEmlpleadosEntities() {
        return findTurnosPorEmlpleadosEntities(true, -1, -1);
    }

    public List<TurnosPorEmlpleados> findTurnosPorEmlpleadosEntities(int maxResults, int firstResult) {
        return findTurnosPorEmlpleadosEntities(false, maxResults, firstResult);
    }

    private List<TurnosPorEmlpleados> findTurnosPorEmlpleadosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TurnosPorEmlpleados.class));
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

    public TurnosPorEmlpleados findTurnosPorEmlpleados(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TurnosPorEmlpleados.class, id);
        } finally {
            em.close();
        }
    }

    public int getTurnosPorEmlpleadosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TurnosPorEmlpleados> rt = cq.from(TurnosPorEmlpleados.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
