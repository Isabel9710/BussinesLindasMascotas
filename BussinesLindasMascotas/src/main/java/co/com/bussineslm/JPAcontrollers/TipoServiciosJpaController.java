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
import co.com.bussineslm.entities.Servicios;
import co.com.bussineslm.entities.TipoServicios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class TipoServiciosJpaController implements Serializable {

    public TipoServiciosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoServicios tipoServicios) {
        if (tipoServicios.getServiciosList() == null) {
            tipoServicios.setServiciosList(new ArrayList<Servicios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Servicios> attachedServiciosList = new ArrayList<Servicios>();
            for (Servicios serviciosListServiciosToAttach : tipoServicios.getServiciosList()) {
                serviciosListServiciosToAttach = em.getReference(serviciosListServiciosToAttach.getClass(), serviciosListServiciosToAttach.getIdServicio());
                attachedServiciosList.add(serviciosListServiciosToAttach);
            }
            tipoServicios.setServiciosList(attachedServiciosList);
            em.persist(tipoServicios);
            for (Servicios serviciosListServicios : tipoServicios.getServiciosList()) {
                TipoServicios oldIdTipoServicioOfServiciosListServicios = serviciosListServicios.getIdTipoServicio();
                serviciosListServicios.setIdTipoServicio(tipoServicios);
                serviciosListServicios = em.merge(serviciosListServicios);
                if (oldIdTipoServicioOfServiciosListServicios != null) {
                    oldIdTipoServicioOfServiciosListServicios.getServiciosList().remove(serviciosListServicios);
                    oldIdTipoServicioOfServiciosListServicios = em.merge(oldIdTipoServicioOfServiciosListServicios);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoServicios tipoServicios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoServicios persistentTipoServicios = em.find(TipoServicios.class, tipoServicios.getIdTipoServicio());
            List<Servicios> serviciosListOld = persistentTipoServicios.getServiciosList();
            List<Servicios> serviciosListNew = tipoServicios.getServiciosList();
            List<String> illegalOrphanMessages = null;
            for (Servicios serviciosListOldServicios : serviciosListOld) {
                if (!serviciosListNew.contains(serviciosListOldServicios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Servicios " + serviciosListOldServicios + " since its idTipoServicio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Servicios> attachedServiciosListNew = new ArrayList<Servicios>();
            for (Servicios serviciosListNewServiciosToAttach : serviciosListNew) {
                serviciosListNewServiciosToAttach = em.getReference(serviciosListNewServiciosToAttach.getClass(), serviciosListNewServiciosToAttach.getIdServicio());
                attachedServiciosListNew.add(serviciosListNewServiciosToAttach);
            }
            serviciosListNew = attachedServiciosListNew;
            tipoServicios.setServiciosList(serviciosListNew);
            tipoServicios = em.merge(tipoServicios);
            for (Servicios serviciosListNewServicios : serviciosListNew) {
                if (!serviciosListOld.contains(serviciosListNewServicios)) {
                    TipoServicios oldIdTipoServicioOfServiciosListNewServicios = serviciosListNewServicios.getIdTipoServicio();
                    serviciosListNewServicios.setIdTipoServicio(tipoServicios);
                    serviciosListNewServicios = em.merge(serviciosListNewServicios);
                    if (oldIdTipoServicioOfServiciosListNewServicios != null && !oldIdTipoServicioOfServiciosListNewServicios.equals(tipoServicios)) {
                        oldIdTipoServicioOfServiciosListNewServicios.getServiciosList().remove(serviciosListNewServicios);
                        oldIdTipoServicioOfServiciosListNewServicios = em.merge(oldIdTipoServicioOfServiciosListNewServicios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoServicios.getIdTipoServicio();
                if (findTipoServicios(id) == null) {
                    throw new NonexistentEntityException("The tipoServicios with id " + id + " no longer exists.");
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
            TipoServicios tipoServicios;
            try {
                tipoServicios = em.getReference(TipoServicios.class, id);
                tipoServicios.getIdTipoServicio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoServicios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Servicios> serviciosListOrphanCheck = tipoServicios.getServiciosList();
            for (Servicios serviciosListOrphanCheckServicios : serviciosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoServicios (" + tipoServicios + ") cannot be destroyed since the Servicios " + serviciosListOrphanCheckServicios + " in its serviciosList field has a non-nullable idTipoServicio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoServicios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoServicios> findTipoServiciosEntities() {
        return findTipoServiciosEntities(true, -1, -1);
    }

    public List<TipoServicios> findTipoServiciosEntities(int maxResults, int firstResult) {
        return findTipoServiciosEntities(false, maxResults, firstResult);
    }

    private List<TipoServicios> findTipoServiciosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoServicios.class));
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

    public TipoServicios findTipoServicios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoServicios.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoServiciosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoServicios> rt = cq.from(TipoServicios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
