/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.JPAcontrollers;

import co.com.bussineslm.JPAcontrollers.exceptions.IllegalOrphanException;
import co.com.bussineslm.JPAcontrollers.exceptions.NonexistentEntityException;
import co.com.bussineslm.entities.Perfiles;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.com.bussineslm.entities.Permisos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class PerfilesJpaController implements Serializable {

    public PerfilesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Perfiles perfiles) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Permisos permisos = perfiles.getPermisos();
            if (permisos != null) {
                permisos = em.getReference(permisos.getClass(), permisos.getIdPermiso());
                perfiles.setPermisos(permisos);
            }
            em.persist(perfiles);
            if (permisos != null) {
                Perfiles oldIdPerfilOfPermisos = permisos.getIdPerfil();
                if (oldIdPerfilOfPermisos != null) {
                    oldIdPerfilOfPermisos.setPermisos(null);
                    oldIdPerfilOfPermisos = em.merge(oldIdPerfilOfPermisos);
                }
                permisos.setIdPerfil(perfiles);
                permisos = em.merge(permisos);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Perfiles perfiles) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Perfiles persistentPerfiles = em.find(Perfiles.class, perfiles.getIdPerfil());
            Permisos permisosOld = persistentPerfiles.getPermisos();
            Permisos permisosNew = perfiles.getPermisos();
            List<String> illegalOrphanMessages = null;
            if (permisosOld != null && !permisosOld.equals(permisosNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Permisos " + permisosOld + " since its idPerfil field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (permisosNew != null) {
                permisosNew = em.getReference(permisosNew.getClass(), permisosNew.getIdPermiso());
                perfiles.setPermisos(permisosNew);
            }
            perfiles = em.merge(perfiles);
            if (permisosNew != null && !permisosNew.equals(permisosOld)) {
                Perfiles oldIdPerfilOfPermisos = permisosNew.getIdPerfil();
                if (oldIdPerfilOfPermisos != null) {
                    oldIdPerfilOfPermisos.setPermisos(null);
                    oldIdPerfilOfPermisos = em.merge(oldIdPerfilOfPermisos);
                }
                permisosNew.setIdPerfil(perfiles);
                permisosNew = em.merge(permisosNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = perfiles.getIdPerfil();
                if (findPerfiles(id) == null) {
                    throw new NonexistentEntityException("The perfiles with id " + id + " no longer exists.");
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
            Perfiles perfiles;
            try {
                perfiles = em.getReference(Perfiles.class, id);
                perfiles.getIdPerfil();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The perfiles with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Permisos permisosOrphanCheck = perfiles.getPermisos();
            if (permisosOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Perfiles (" + perfiles + ") cannot be destroyed since the Permisos " + permisosOrphanCheck + " in its permisos field has a non-nullable idPerfil field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(perfiles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Perfiles> findPerfilesEntities() {
        return findPerfilesEntities(true, -1, -1);
    }

    public List<Perfiles> findPerfilesEntities(int maxResults, int firstResult) {
        return findPerfilesEntities(false, maxResults, firstResult);
    }

    private List<Perfiles> findPerfilesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Perfiles.class));
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

    public Perfiles findPerfiles(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Perfiles.class, id);
        } finally {
            em.close();
        }
    }

    public int getPerfilesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Perfiles> rt = cq.from(Perfiles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Perfiles buscarPerfilNombre(String nombrePerfil) {
        EntityManager em = getEntityManager();
        
        try{
            Query q = em.createNamedQuery("Perfiles.findByNombrePerfil");
            q.setParameter("nombrePerfil", nombrePerfil);
            
            return (Perfiles) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }finally{
            em.close();
}
    }
}
