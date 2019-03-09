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
import co.com.bussineslm.entities.Modulos;
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
public class SubModulosJpaController implements Serializable {

    public SubModulosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SubModulos subModulos) {
        if (subModulos.getPermisosList() == null) {
            subModulos.setPermisosList(new ArrayList<Permisos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Modulos idModulo = subModulos.getIdModulo();
            if (idModulo != null) {
                idModulo = em.getReference(idModulo.getClass(), idModulo.getIdModulo());
                subModulos.setIdModulo(idModulo);
            }
            List<Permisos> attachedPermisosList = new ArrayList<Permisos>();
            for (Permisos permisosListPermisosToAttach : subModulos.getPermisosList()) {
                permisosListPermisosToAttach = em.getReference(permisosListPermisosToAttach.getClass(), permisosListPermisosToAttach.getIdPermiso());
                attachedPermisosList.add(permisosListPermisosToAttach);
            }
            subModulos.setPermisosList(attachedPermisosList);
            em.persist(subModulos);
            if (idModulo != null) {
                idModulo.getSubModulosList().add(subModulos);
                idModulo = em.merge(idModulo);
            }
            for (Permisos permisosListPermisos : subModulos.getPermisosList()) {
                SubModulos oldIdSubmoduloOfPermisosListPermisos = permisosListPermisos.getIdSubmodulo();
                permisosListPermisos.setIdSubmodulo(subModulos);
                permisosListPermisos = em.merge(permisosListPermisos);
                if (oldIdSubmoduloOfPermisosListPermisos != null) {
                    oldIdSubmoduloOfPermisosListPermisos.getPermisosList().remove(permisosListPermisos);
                    oldIdSubmoduloOfPermisosListPermisos = em.merge(oldIdSubmoduloOfPermisosListPermisos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SubModulos subModulos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SubModulos persistentSubModulos = em.find(SubModulos.class, subModulos.getIdSubmodulo());
            Modulos idModuloOld = persistentSubModulos.getIdModulo();
            Modulos idModuloNew = subModulos.getIdModulo();
            List<Permisos> permisosListOld = persistentSubModulos.getPermisosList();
            List<Permisos> permisosListNew = subModulos.getPermisosList();
            List<String> illegalOrphanMessages = null;
            for (Permisos permisosListOldPermisos : permisosListOld) {
                if (!permisosListNew.contains(permisosListOldPermisos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Permisos " + permisosListOldPermisos + " since its idSubmodulo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idModuloNew != null) {
                idModuloNew = em.getReference(idModuloNew.getClass(), idModuloNew.getIdModulo());
                subModulos.setIdModulo(idModuloNew);
            }
            List<Permisos> attachedPermisosListNew = new ArrayList<Permisos>();
            for (Permisos permisosListNewPermisosToAttach : permisosListNew) {
                permisosListNewPermisosToAttach = em.getReference(permisosListNewPermisosToAttach.getClass(), permisosListNewPermisosToAttach.getIdPermiso());
                attachedPermisosListNew.add(permisosListNewPermisosToAttach);
            }
            permisosListNew = attachedPermisosListNew;
            subModulos.setPermisosList(permisosListNew);
            subModulos = em.merge(subModulos);
            if (idModuloOld != null && !idModuloOld.equals(idModuloNew)) {
                idModuloOld.getSubModulosList().remove(subModulos);
                idModuloOld = em.merge(idModuloOld);
            }
            if (idModuloNew != null && !idModuloNew.equals(idModuloOld)) {
                idModuloNew.getSubModulosList().add(subModulos);
                idModuloNew = em.merge(idModuloNew);
            }
            for (Permisos permisosListNewPermisos : permisosListNew) {
                if (!permisosListOld.contains(permisosListNewPermisos)) {
                    SubModulos oldIdSubmoduloOfPermisosListNewPermisos = permisosListNewPermisos.getIdSubmodulo();
                    permisosListNewPermisos.setIdSubmodulo(subModulos);
                    permisosListNewPermisos = em.merge(permisosListNewPermisos);
                    if (oldIdSubmoduloOfPermisosListNewPermisos != null && !oldIdSubmoduloOfPermisosListNewPermisos.equals(subModulos)) {
                        oldIdSubmoduloOfPermisosListNewPermisos.getPermisosList().remove(permisosListNewPermisos);
                        oldIdSubmoduloOfPermisosListNewPermisos = em.merge(oldIdSubmoduloOfPermisosListNewPermisos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = subModulos.getIdSubmodulo();
                if (findSubModulos(id) == null) {
                    throw new NonexistentEntityException("The subModulos with id " + id + " no longer exists.");
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
            SubModulos subModulos;
            try {
                subModulos = em.getReference(SubModulos.class, id);
                subModulos.getIdSubmodulo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subModulos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Permisos> permisosListOrphanCheck = subModulos.getPermisosList();
            for (Permisos permisosListOrphanCheckPermisos : permisosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SubModulos (" + subModulos + ") cannot be destroyed since the Permisos " + permisosListOrphanCheckPermisos + " in its permisosList field has a non-nullable idSubmodulo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Modulos idModulo = subModulos.getIdModulo();
            if (idModulo != null) {
                idModulo.getSubModulosList().remove(subModulos);
                idModulo = em.merge(idModulo);
            }
            em.remove(subModulos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SubModulos> findSubModulosEntities() {
        return findSubModulosEntities(true, -1, -1);
    }

    public List<SubModulos> findSubModulosEntities(int maxResults, int firstResult) {
        return findSubModulosEntities(false, maxResults, firstResult);
    }

    private List<SubModulos> findSubModulosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SubModulos.class));
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

    public SubModulos findSubModulos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SubModulos.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubModulosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SubModulos> rt = cq.from(SubModulos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
