/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.JPAcontrollers;

import co.com.bussineslm.JPAcontrollers.exceptions.IllegalOrphanException;
import co.com.bussineslm.JPAcontrollers.exceptions.NonexistentEntityException;
import co.com.bussineslm.JPAcontrollers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.com.bussineslm.entities.Departamentos;
import co.com.bussineslm.entities.Paises;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class PaisesJpaController implements Serializable {

    public PaisesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Paises paises) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamentos departamentos = paises.getDepartamentos();
            if (departamentos != null) {
                departamentos = em.getReference(departamentos.getClass(), departamentos.getIdDepartamento());
                paises.setDepartamentos(departamentos);
            }
            em.persist(paises);
            if (departamentos != null) {
                Paises oldIdPaisOfDepartamentos = departamentos.getIdPais();
                if (oldIdPaisOfDepartamentos != null) {
                    oldIdPaisOfDepartamentos.setDepartamentos(null);
                    oldIdPaisOfDepartamentos = em.merge(oldIdPaisOfDepartamentos);
                }
                departamentos.setIdPais(paises);
                departamentos = em.merge(departamentos);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPaises(paises.getIdPais()) != null) {
                throw new PreexistingEntityException("Paises " + paises + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Paises paises) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paises persistentPaises = em.find(Paises.class, paises.getIdPais());
            Departamentos departamentosOld = persistentPaises.getDepartamentos();
            Departamentos departamentosNew = paises.getDepartamentos();
            List<String> illegalOrphanMessages = null;
            if (departamentosOld != null && !departamentosOld.equals(departamentosNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Departamentos " + departamentosOld + " since its idPais field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (departamentosNew != null) {
                departamentosNew = em.getReference(departamentosNew.getClass(), departamentosNew.getIdDepartamento());
                paises.setDepartamentos(departamentosNew);
            }
            paises = em.merge(paises);
            if (departamentosNew != null && !departamentosNew.equals(departamentosOld)) {
                Paises oldIdPaisOfDepartamentos = departamentosNew.getIdPais();
                if (oldIdPaisOfDepartamentos != null) {
                    oldIdPaisOfDepartamentos.setDepartamentos(null);
                    oldIdPaisOfDepartamentos = em.merge(oldIdPaisOfDepartamentos);
                }
                departamentosNew.setIdPais(paises);
                departamentosNew = em.merge(departamentosNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = paises.getIdPais();
                if (findPaises(id) == null) {
                    throw new NonexistentEntityException("The paises with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paises paises;
            try {
                paises = em.getReference(Paises.class, id);
                paises.getIdPais();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The paises with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Departamentos departamentosOrphanCheck = paises.getDepartamentos();
            if (departamentosOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paises (" + paises + ") cannot be destroyed since the Departamentos " + departamentosOrphanCheck + " in its departamentos field has a non-nullable idPais field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(paises);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Paises> findPaisesEntities() {
        return findPaisesEntities(true, -1, -1);
    }

    public List<Paises> findPaisesEntities(int maxResults, int firstResult) {
        return findPaisesEntities(false, maxResults, firstResult);
    }

    private List<Paises> findPaisesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Paises.class));
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

    public Paises findPaises(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Paises.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaisesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Paises> rt = cq.from(Paises.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
