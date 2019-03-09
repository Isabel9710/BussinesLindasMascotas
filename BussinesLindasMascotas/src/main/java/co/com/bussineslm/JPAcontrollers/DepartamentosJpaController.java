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
import co.com.bussineslm.entities.Paises;
import co.com.bussineslm.entities.Ciudades;
import co.com.bussineslm.entities.Departamentos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class DepartamentosJpaController implements Serializable {

    public DepartamentosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Departamentos departamentos) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Paises idPaisOrphanCheck = departamentos.getIdPais();
        if (idPaisOrphanCheck != null) {
            Departamentos oldDepartamentosOfIdPais = idPaisOrphanCheck.getDepartamentos();
            if (oldDepartamentosOfIdPais != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Paises " + idPaisOrphanCheck + " already has an item of type Departamentos whose idPais column cannot be null. Please make another selection for the idPais field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paises idPais = departamentos.getIdPais();
            if (idPais != null) {
                idPais = em.getReference(idPais.getClass(), idPais.getIdPais());
                departamentos.setIdPais(idPais);
            }
            Ciudades ciudades = departamentos.getCiudades();
            if (ciudades != null) {
                ciudades = em.getReference(ciudades.getClass(), ciudades.getIdCiudad());
                departamentos.setCiudades(ciudades);
            }
            em.persist(departamentos);
            if (idPais != null) {
                idPais.setDepartamentos(departamentos);
                idPais = em.merge(idPais);
            }
            if (ciudades != null) {
                Departamentos oldIdDepartamentoOfCiudades = ciudades.getIdDepartamento();
                if (oldIdDepartamentoOfCiudades != null) {
                    oldIdDepartamentoOfCiudades.setCiudades(null);
                    oldIdDepartamentoOfCiudades = em.merge(oldIdDepartamentoOfCiudades);
                }
                ciudades.setIdDepartamento(departamentos);
                ciudades = em.merge(ciudades);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDepartamentos(departamentos.getIdDepartamento()) != null) {
                throw new PreexistingEntityException("Departamentos " + departamentos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Departamentos departamentos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamentos persistentDepartamentos = em.find(Departamentos.class, departamentos.getIdDepartamento());
            Paises idPaisOld = persistentDepartamentos.getIdPais();
            Paises idPaisNew = departamentos.getIdPais();
            Ciudades ciudadesOld = persistentDepartamentos.getCiudades();
            Ciudades ciudadesNew = departamentos.getCiudades();
            List<String> illegalOrphanMessages = null;
            if (idPaisNew != null && !idPaisNew.equals(idPaisOld)) {
                Departamentos oldDepartamentosOfIdPais = idPaisNew.getDepartamentos();
                if (oldDepartamentosOfIdPais != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Paises " + idPaisNew + " already has an item of type Departamentos whose idPais column cannot be null. Please make another selection for the idPais field.");
                }
            }
            if (ciudadesOld != null && !ciudadesOld.equals(ciudadesNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Ciudades " + ciudadesOld + " since its idDepartamento field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPaisNew != null) {
                idPaisNew = em.getReference(idPaisNew.getClass(), idPaisNew.getIdPais());
                departamentos.setIdPais(idPaisNew);
            }
            if (ciudadesNew != null) {
                ciudadesNew = em.getReference(ciudadesNew.getClass(), ciudadesNew.getIdCiudad());
                departamentos.setCiudades(ciudadesNew);
            }
            departamentos = em.merge(departamentos);
            if (idPaisOld != null && !idPaisOld.equals(idPaisNew)) {
                idPaisOld.setDepartamentos(null);
                idPaisOld = em.merge(idPaisOld);
            }
            if (idPaisNew != null && !idPaisNew.equals(idPaisOld)) {
                idPaisNew.setDepartamentos(departamentos);
                idPaisNew = em.merge(idPaisNew);
            }
            if (ciudadesNew != null && !ciudadesNew.equals(ciudadesOld)) {
                Departamentos oldIdDepartamentoOfCiudades = ciudadesNew.getIdDepartamento();
                if (oldIdDepartamentoOfCiudades != null) {
                    oldIdDepartamentoOfCiudades.setCiudades(null);
                    oldIdDepartamentoOfCiudades = em.merge(oldIdDepartamentoOfCiudades);
                }
                ciudadesNew.setIdDepartamento(departamentos);
                ciudadesNew = em.merge(ciudadesNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = departamentos.getIdDepartamento();
                if (findDepartamentos(id) == null) {
                    throw new NonexistentEntityException("The departamentos with id " + id + " no longer exists.");
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
            Departamentos departamentos;
            try {
                departamentos = em.getReference(Departamentos.class, id);
                departamentos.getIdDepartamento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departamentos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Ciudades ciudadesOrphanCheck = departamentos.getCiudades();
            if (ciudadesOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamentos (" + departamentos + ") cannot be destroyed since the Ciudades " + ciudadesOrphanCheck + " in its ciudades field has a non-nullable idDepartamento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Paises idPais = departamentos.getIdPais();
            if (idPais != null) {
                idPais.setDepartamentos(null);
                idPais = em.merge(idPais);
            }
            em.remove(departamentos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Departamentos> findDepartamentosEntities() {
        return findDepartamentosEntities(true, -1, -1);
    }

    public List<Departamentos> findDepartamentosEntities(int maxResults, int firstResult) {
        return findDepartamentosEntities(false, maxResults, firstResult);
    }

    private List<Departamentos> findDepartamentosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Departamentos.class));
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

    public Departamentos findDepartamentos(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Departamentos.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartamentosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Departamentos> rt = cq.from(Departamentos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
