/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.JPAcontrollers;

import co.com.bussineslm.JPAcontrollers.exceptions.IllegalOrphanException;
import co.com.bussineslm.JPAcontrollers.exceptions.NonexistentEntityException;
import co.com.bussineslm.entities.Servicios;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.com.bussineslm.entities.ServiciosPorEmpleados;
import co.com.bussineslm.entities.TipoServicios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class ServiciosJpaController implements Serializable {

    public ServiciosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Servicios servicios) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ServiciosPorEmpleados serviciosPorEmpleados = servicios.getServiciosPorEmpleados();
            if (serviciosPorEmpleados != null) {
                serviciosPorEmpleados = em.getReference(serviciosPorEmpleados.getClass(), serviciosPorEmpleados.getIdServicioEmpl());
                servicios.setServiciosPorEmpleados(serviciosPorEmpleados);
            }
            TipoServicios idTipoServicio = servicios.getIdTipoServicio();
            if (idTipoServicio != null) {
                idTipoServicio = em.getReference(idTipoServicio.getClass(), idTipoServicio.getIdTipoServicio());
                servicios.setIdTipoServicio(idTipoServicio);
            }
            em.persist(servicios);
            if (serviciosPorEmpleados != null) {
                Servicios oldIdServicioOfServiciosPorEmpleados = serviciosPorEmpleados.getIdServicio();
                if (oldIdServicioOfServiciosPorEmpleados != null) {
                    oldIdServicioOfServiciosPorEmpleados.setServiciosPorEmpleados(null);
                    oldIdServicioOfServiciosPorEmpleados = em.merge(oldIdServicioOfServiciosPorEmpleados);
                }
                serviciosPorEmpleados.setIdServicio(servicios);
                serviciosPorEmpleados = em.merge(serviciosPorEmpleados);
            }
            if (idTipoServicio != null) {
                idTipoServicio.getServiciosList().add(servicios);
                idTipoServicio = em.merge(idTipoServicio);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Servicios servicios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Servicios persistentServicios = em.find(Servicios.class, servicios.getIdServicio());
            ServiciosPorEmpleados serviciosPorEmpleadosOld = persistentServicios.getServiciosPorEmpleados();
            ServiciosPorEmpleados serviciosPorEmpleadosNew = servicios.getServiciosPorEmpleados();
            TipoServicios idTipoServicioOld = persistentServicios.getIdTipoServicio();
            TipoServicios idTipoServicioNew = servicios.getIdTipoServicio();
            List<String> illegalOrphanMessages = null;
            if (serviciosPorEmpleadosOld != null && !serviciosPorEmpleadosOld.equals(serviciosPorEmpleadosNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain ServiciosPorEmpleados " + serviciosPorEmpleadosOld + " since its idServicio field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (serviciosPorEmpleadosNew != null) {
                serviciosPorEmpleadosNew = em.getReference(serviciosPorEmpleadosNew.getClass(), serviciosPorEmpleadosNew.getIdServicioEmpl());
                servicios.setServiciosPorEmpleados(serviciosPorEmpleadosNew);
            }
            if (idTipoServicioNew != null) {
                idTipoServicioNew = em.getReference(idTipoServicioNew.getClass(), idTipoServicioNew.getIdTipoServicio());
                servicios.setIdTipoServicio(idTipoServicioNew);
            }
            servicios = em.merge(servicios);
            if (serviciosPorEmpleadosNew != null && !serviciosPorEmpleadosNew.equals(serviciosPorEmpleadosOld)) {
                Servicios oldIdServicioOfServiciosPorEmpleados = serviciosPorEmpleadosNew.getIdServicio();
                if (oldIdServicioOfServiciosPorEmpleados != null) {
                    oldIdServicioOfServiciosPorEmpleados.setServiciosPorEmpleados(null);
                    oldIdServicioOfServiciosPorEmpleados = em.merge(oldIdServicioOfServiciosPorEmpleados);
                }
                serviciosPorEmpleadosNew.setIdServicio(servicios);
                serviciosPorEmpleadosNew = em.merge(serviciosPorEmpleadosNew);
            }
            if (idTipoServicioOld != null && !idTipoServicioOld.equals(idTipoServicioNew)) {
                idTipoServicioOld.getServiciosList().remove(servicios);
                idTipoServicioOld = em.merge(idTipoServicioOld);
            }
            if (idTipoServicioNew != null && !idTipoServicioNew.equals(idTipoServicioOld)) {
                idTipoServicioNew.getServiciosList().add(servicios);
                idTipoServicioNew = em.merge(idTipoServicioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = servicios.getIdServicio();
                if (findServicios(id) == null) {
                    throw new NonexistentEntityException("The servicios with id " + id + " no longer exists.");
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
            Servicios servicios;
            try {
                servicios = em.getReference(Servicios.class, id);
                servicios.getIdServicio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The servicios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            ServiciosPorEmpleados serviciosPorEmpleadosOrphanCheck = servicios.getServiciosPorEmpleados();
            if (serviciosPorEmpleadosOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Servicios (" + servicios + ") cannot be destroyed since the ServiciosPorEmpleados " + serviciosPorEmpleadosOrphanCheck + " in its serviciosPorEmpleados field has a non-nullable idServicio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoServicios idTipoServicio = servicios.getIdTipoServicio();
            if (idTipoServicio != null) {
                idTipoServicio.getServiciosList().remove(servicios);
                idTipoServicio = em.merge(idTipoServicio);
            }
            em.remove(servicios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Servicios> findServiciosEntities() {
        return findServiciosEntities(true, -1, -1);
    }

    public List<Servicios> findServiciosEntities(int maxResults, int firstResult) {
        return findServiciosEntities(false, maxResults, firstResult);
    }

    private List<Servicios> findServiciosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Servicios.class));
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

    public Servicios findServicios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Servicios.class, id);
        } finally {
            em.close();
        }
    }

    public int getServiciosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Servicios> rt = cq.from(Servicios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
