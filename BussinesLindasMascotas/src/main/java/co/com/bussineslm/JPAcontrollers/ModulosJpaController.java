/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.JPAcontrollers;

import co.com.bussineslm.JPAcontrollers.exceptions.IllegalOrphanException;
import co.com.bussineslm.JPAcontrollers.exceptions.NonexistentEntityException;
import co.com.bussineslm.entities.Modulos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.com.bussineslm.entities.SubModulos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class ModulosJpaController implements Serializable {

    public ModulosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Modulos modulos) {
        if (modulos.getSubModulosList() == null) {
            modulos.setSubModulosList(new ArrayList<SubModulos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<SubModulos> attachedSubModulosList = new ArrayList<SubModulos>();
            for (SubModulos subModulosListSubModulosToAttach : modulos.getSubModulosList()) {
                subModulosListSubModulosToAttach = em.getReference(subModulosListSubModulosToAttach.getClass(), subModulosListSubModulosToAttach.getIdSubmodulo());
                attachedSubModulosList.add(subModulosListSubModulosToAttach);
            }
            modulos.setSubModulosList(attachedSubModulosList);
            em.persist(modulos);
            for (SubModulos subModulosListSubModulos : modulos.getSubModulosList()) {
                Modulos oldIdModuloOfSubModulosListSubModulos = subModulosListSubModulos.getIdModulo();
                subModulosListSubModulos.setIdModulo(modulos);
                subModulosListSubModulos = em.merge(subModulosListSubModulos);
                if (oldIdModuloOfSubModulosListSubModulos != null) {
                    oldIdModuloOfSubModulosListSubModulos.getSubModulosList().remove(subModulosListSubModulos);
                    oldIdModuloOfSubModulosListSubModulos = em.merge(oldIdModuloOfSubModulosListSubModulos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Modulos modulos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Modulos persistentModulos = em.find(Modulos.class, modulos.getIdModulo());
            List<SubModulos> subModulosListOld = persistentModulos.getSubModulosList();
            List<SubModulos> subModulosListNew = modulos.getSubModulosList();
            List<String> illegalOrphanMessages = null;
            for (SubModulos subModulosListOldSubModulos : subModulosListOld) {
                if (!subModulosListNew.contains(subModulosListOldSubModulos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SubModulos " + subModulosListOldSubModulos + " since its idModulo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<SubModulos> attachedSubModulosListNew = new ArrayList<SubModulos>();
            for (SubModulos subModulosListNewSubModulosToAttach : subModulosListNew) {
                subModulosListNewSubModulosToAttach = em.getReference(subModulosListNewSubModulosToAttach.getClass(), subModulosListNewSubModulosToAttach.getIdSubmodulo());
                attachedSubModulosListNew.add(subModulosListNewSubModulosToAttach);
            }
            subModulosListNew = attachedSubModulosListNew;
            modulos.setSubModulosList(subModulosListNew);
            modulos = em.merge(modulos);
            for (SubModulos subModulosListNewSubModulos : subModulosListNew) {
                if (!subModulosListOld.contains(subModulosListNewSubModulos)) {
                    Modulos oldIdModuloOfSubModulosListNewSubModulos = subModulosListNewSubModulos.getIdModulo();
                    subModulosListNewSubModulos.setIdModulo(modulos);
                    subModulosListNewSubModulos = em.merge(subModulosListNewSubModulos);
                    if (oldIdModuloOfSubModulosListNewSubModulos != null && !oldIdModuloOfSubModulosListNewSubModulos.equals(modulos)) {
                        oldIdModuloOfSubModulosListNewSubModulos.getSubModulosList().remove(subModulosListNewSubModulos);
                        oldIdModuloOfSubModulosListNewSubModulos = em.merge(oldIdModuloOfSubModulosListNewSubModulos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = modulos.getIdModulo();
                if (findModulos(id) == null) {
                    throw new NonexistentEntityException("The modulos with id " + id + " no longer exists.");
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
            Modulos modulos;
            try {
                modulos = em.getReference(Modulos.class, id);
                modulos.getIdModulo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The modulos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<SubModulos> subModulosListOrphanCheck = modulos.getSubModulosList();
            for (SubModulos subModulosListOrphanCheckSubModulos : subModulosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Modulos (" + modulos + ") cannot be destroyed since the SubModulos " + subModulosListOrphanCheckSubModulos + " in its subModulosList field has a non-nullable idModulo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(modulos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Modulos> findModulosEntities() {
        return findModulosEntities(true, -1, -1);
    }

    public List<Modulos> findModulosEntities(int maxResults, int firstResult) {
        return findModulosEntities(false, maxResults, firstResult);
    }

    private List<Modulos> findModulosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Modulos.class));
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

    public Modulos findModulos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Modulos.class, id);
        } finally {
            em.close();
        }
    }

    public int getModulosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Modulos> rt = cq.from(Modulos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
