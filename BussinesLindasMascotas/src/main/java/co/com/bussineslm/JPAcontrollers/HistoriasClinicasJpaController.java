/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.JPAcontrollers;

import co.com.bussineslm.JPAcontrollers.exceptions.NonexistentEntityException;
import co.com.bussineslm.entities.HistoriasClinicas;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.com.bussineslm.entities.Mascotas;
import co.com.bussineslm.entities.ServiciosPorEmpleados;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class HistoriasClinicasJpaController implements Serializable {

    public HistoriasClinicasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HistoriasClinicas historiasClinicas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mascotas idMascota = historiasClinicas.getIdMascota();
            if (idMascota != null) {
                idMascota = em.getReference(idMascota.getClass(), idMascota.getIdMascota());
                historiasClinicas.setIdMascota(idMascota);
            }
            ServiciosPorEmpleados idServicioPorEmpl = historiasClinicas.getIdServicioPorEmpl();
            if (idServicioPorEmpl != null) {
                idServicioPorEmpl = em.getReference(idServicioPorEmpl.getClass(), idServicioPorEmpl.getIdServicioEmpl());
                historiasClinicas.setIdServicioPorEmpl(idServicioPorEmpl);
            }
            em.persist(historiasClinicas);
            if (idMascota != null) {
                idMascota.getHistoriasClinicasList().add(historiasClinicas);
                idMascota = em.merge(idMascota);
            }
            if (idServicioPorEmpl != null) {
                idServicioPorEmpl.getHistoriasClinicasList().add(historiasClinicas);
                idServicioPorEmpl = em.merge(idServicioPorEmpl);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HistoriasClinicas historiasClinicas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HistoriasClinicas persistentHistoriasClinicas = em.find(HistoriasClinicas.class, historiasClinicas.getIdProcedimiento());
            Mascotas idMascotaOld = persistentHistoriasClinicas.getIdMascota();
            Mascotas idMascotaNew = historiasClinicas.getIdMascota();
            ServiciosPorEmpleados idServicioPorEmplOld = persistentHistoriasClinicas.getIdServicioPorEmpl();
            ServiciosPorEmpleados idServicioPorEmplNew = historiasClinicas.getIdServicioPorEmpl();
            if (idMascotaNew != null) {
                idMascotaNew = em.getReference(idMascotaNew.getClass(), idMascotaNew.getIdMascota());
                historiasClinicas.setIdMascota(idMascotaNew);
            }
            if (idServicioPorEmplNew != null) {
                idServicioPorEmplNew = em.getReference(idServicioPorEmplNew.getClass(), idServicioPorEmplNew.getIdServicioEmpl());
                historiasClinicas.setIdServicioPorEmpl(idServicioPorEmplNew);
            }
            historiasClinicas = em.merge(historiasClinicas);
            if (idMascotaOld != null && !idMascotaOld.equals(idMascotaNew)) {
                idMascotaOld.getHistoriasClinicasList().remove(historiasClinicas);
                idMascotaOld = em.merge(idMascotaOld);
            }
            if (idMascotaNew != null && !idMascotaNew.equals(idMascotaOld)) {
                idMascotaNew.getHistoriasClinicasList().add(historiasClinicas);
                idMascotaNew = em.merge(idMascotaNew);
            }
            if (idServicioPorEmplOld != null && !idServicioPorEmplOld.equals(idServicioPorEmplNew)) {
                idServicioPorEmplOld.getHistoriasClinicasList().remove(historiasClinicas);
                idServicioPorEmplOld = em.merge(idServicioPorEmplOld);
            }
            if (idServicioPorEmplNew != null && !idServicioPorEmplNew.equals(idServicioPorEmplOld)) {
                idServicioPorEmplNew.getHistoriasClinicasList().add(historiasClinicas);
                idServicioPorEmplNew = em.merge(idServicioPorEmplNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = historiasClinicas.getIdProcedimiento();
                if (findHistoriasClinicas(id) == null) {
                    throw new NonexistentEntityException("The historiasClinicas with id " + id + " no longer exists.");
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
            HistoriasClinicas historiasClinicas;
            try {
                historiasClinicas = em.getReference(HistoriasClinicas.class, id);
                historiasClinicas.getIdProcedimiento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historiasClinicas with id " + id + " no longer exists.", enfe);
            }
            Mascotas idMascota = historiasClinicas.getIdMascota();
            if (idMascota != null) {
                idMascota.getHistoriasClinicasList().remove(historiasClinicas);
                idMascota = em.merge(idMascota);
            }
            ServiciosPorEmpleados idServicioPorEmpl = historiasClinicas.getIdServicioPorEmpl();
            if (idServicioPorEmpl != null) {
                idServicioPorEmpl.getHistoriasClinicasList().remove(historiasClinicas);
                idServicioPorEmpl = em.merge(idServicioPorEmpl);
            }
            em.remove(historiasClinicas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HistoriasClinicas> findHistoriasClinicasEntities() {
        return findHistoriasClinicasEntities(true, -1, -1);
    }

    public List<HistoriasClinicas> findHistoriasClinicasEntities(int maxResults, int firstResult) {
        return findHistoriasClinicasEntities(false, maxResults, firstResult);
    }

    private List<HistoriasClinicas> findHistoriasClinicasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HistoriasClinicas.class));
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

    public HistoriasClinicas findHistoriasClinicas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HistoriasClinicas.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistoriasClinicasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HistoriasClinicas> rt = cq.from(HistoriasClinicas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
