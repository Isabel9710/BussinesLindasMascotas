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
import co.com.bussineslm.entities.Perfiles;
import co.com.bussineslm.entities.Permisos;
import co.com.bussineslm.entities.SubModulos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class PermisosJpaController implements Serializable {

    public PermisosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Permisos permisos) throws IllegalOrphanException {
        List<String> illegalOrphanMessages = null;
        Perfiles idPerfilOrphanCheck = permisos.getIdPerfil();
        if (idPerfilOrphanCheck != null) {
            Permisos oldPermisosOfIdPerfil = idPerfilOrphanCheck.getPermisos();
            if (oldPermisosOfIdPerfil != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Perfiles " + idPerfilOrphanCheck + " already has an item of type Permisos whose idPerfil column cannot be null. Please make another selection for the idPerfil field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Perfiles idPerfil = permisos.getIdPerfil();
            if (idPerfil != null) {
                idPerfil = em.getReference(idPerfil.getClass(), idPerfil.getIdPerfil());
                permisos.setIdPerfil(idPerfil);
            }
            SubModulos idSubmodulo = permisos.getIdSubmodulo();
            if (idSubmodulo != null) {
                idSubmodulo = em.getReference(idSubmodulo.getClass(), idSubmodulo.getIdSubmodulo());
                permisos.setIdSubmodulo(idSubmodulo);
            }
            em.persist(permisos);
            if (idPerfil != null) {
                idPerfil.setPermisos(permisos);
                idPerfil = em.merge(idPerfil);
            }
            if (idSubmodulo != null) {
                idSubmodulo.getPermisosList().add(permisos);
                idSubmodulo = em.merge(idSubmodulo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Permisos permisos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Permisos persistentPermisos = em.find(Permisos.class, permisos.getIdPermiso());
            Perfiles idPerfilOld = persistentPermisos.getIdPerfil();
            Perfiles idPerfilNew = permisos.getIdPerfil();
            SubModulos idSubmoduloOld = persistentPermisos.getIdSubmodulo();
            SubModulos idSubmoduloNew = permisos.getIdSubmodulo();
            List<String> illegalOrphanMessages = null;
            if (idPerfilNew != null && !idPerfilNew.equals(idPerfilOld)) {
                Permisos oldPermisosOfIdPerfil = idPerfilNew.getPermisos();
                if (oldPermisosOfIdPerfil != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Perfiles " + idPerfilNew + " already has an item of type Permisos whose idPerfil column cannot be null. Please make another selection for the idPerfil field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPerfilNew != null) {
                idPerfilNew = em.getReference(idPerfilNew.getClass(), idPerfilNew.getIdPerfil());
                permisos.setIdPerfil(idPerfilNew);
            }
            if (idSubmoduloNew != null) {
                idSubmoduloNew = em.getReference(idSubmoduloNew.getClass(), idSubmoduloNew.getIdSubmodulo());
                permisos.setIdSubmodulo(idSubmoduloNew);
            }
            permisos = em.merge(permisos);
            if (idPerfilOld != null && !idPerfilOld.equals(idPerfilNew)) {
                idPerfilOld.setPermisos(null);
                idPerfilOld = em.merge(idPerfilOld);
            }
            if (idPerfilNew != null && !idPerfilNew.equals(idPerfilOld)) {
                idPerfilNew.setPermisos(permisos);
                idPerfilNew = em.merge(idPerfilNew);
            }
            if (idSubmoduloOld != null && !idSubmoduloOld.equals(idSubmoduloNew)) {
                idSubmoduloOld.getPermisosList().remove(permisos);
                idSubmoduloOld = em.merge(idSubmoduloOld);
            }
            if (idSubmoduloNew != null && !idSubmoduloNew.equals(idSubmoduloOld)) {
                idSubmoduloNew.getPermisosList().add(permisos);
                idSubmoduloNew = em.merge(idSubmoduloNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = permisos.getIdPermiso();
                if (findPermisos(id) == null) {
                    throw new NonexistentEntityException("The permisos with id " + id + " no longer exists.");
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
            Permisos permisos;
            try {
                permisos = em.getReference(Permisos.class, id);
                permisos.getIdPermiso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permisos with id " + id + " no longer exists.", enfe);
            }
            Perfiles idPerfil = permisos.getIdPerfil();
            if (idPerfil != null) {
                idPerfil.setPermisos(null);
                idPerfil = em.merge(idPerfil);
            }
            SubModulos idSubmodulo = permisos.getIdSubmodulo();
            if (idSubmodulo != null) {
                idSubmodulo.getPermisosList().remove(permisos);
                idSubmodulo = em.merge(idSubmodulo);
            }
            em.remove(permisos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Permisos> findPermisosEntities() {
        return findPermisosEntities(true, -1, -1);
    }

    public List<Permisos> findPermisosEntities(int maxResults, int firstResult) {
        return findPermisosEntities(false, maxResults, firstResult);
    }

    private List<Permisos> findPermisosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Permisos.class));
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

    public Permisos findPermisos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Permisos.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermisosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Permisos> rt = cq.from(Permisos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
