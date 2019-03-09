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
import co.com.bussineslm.entities.Barrios;
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
public class CiudadesJpaController implements Serializable {

    public CiudadesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ciudades ciudades) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Departamentos idDepartamentoOrphanCheck = ciudades.getIdDepartamento();
        if (idDepartamentoOrphanCheck != null) {
            Ciudades oldCiudadesOfIdDepartamento = idDepartamentoOrphanCheck.getCiudades();
            if (oldCiudadesOfIdDepartamento != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Departamentos " + idDepartamentoOrphanCheck + " already has an item of type Ciudades whose idDepartamento column cannot be null. Please make another selection for the idDepartamento field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Barrios barrios = ciudades.getBarrios();
            if (barrios != null) {
                barrios = em.getReference(barrios.getClass(), barrios.getIdBarrio());
                ciudades.setBarrios(barrios);
            }
            Departamentos idDepartamento = ciudades.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento = em.getReference(idDepartamento.getClass(), idDepartamento.getIdDepartamento());
                ciudades.setIdDepartamento(idDepartamento);
            }
            em.persist(ciudades);
            if (barrios != null) {
                Ciudades oldIdCiudadOfBarrios = barrios.getIdCiudad();
                if (oldIdCiudadOfBarrios != null) {
                    oldIdCiudadOfBarrios.setBarrios(null);
                    oldIdCiudadOfBarrios = em.merge(oldIdCiudadOfBarrios);
                }
                barrios.setIdCiudad(ciudades);
                barrios = em.merge(barrios);
            }
            if (idDepartamento != null) {
                idDepartamento.setCiudades(ciudades);
                idDepartamento = em.merge(idDepartamento);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCiudades(ciudades.getIdCiudad()) != null) {
                throw new PreexistingEntityException("Ciudades " + ciudades + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ciudades ciudades) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciudades persistentCiudades = em.find(Ciudades.class, ciudades.getIdCiudad());
            Barrios barriosOld = persistentCiudades.getBarrios();
            Barrios barriosNew = ciudades.getBarrios();
            Departamentos idDepartamentoOld = persistentCiudades.getIdDepartamento();
            Departamentos idDepartamentoNew = ciudades.getIdDepartamento();
            List<String> illegalOrphanMessages = null;
            if (barriosOld != null && !barriosOld.equals(barriosNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Barrios " + barriosOld + " since its idCiudad field is not nullable.");
            }
            if (idDepartamentoNew != null && !idDepartamentoNew.equals(idDepartamentoOld)) {
                Ciudades oldCiudadesOfIdDepartamento = idDepartamentoNew.getCiudades();
                if (oldCiudadesOfIdDepartamento != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Departamentos " + idDepartamentoNew + " already has an item of type Ciudades whose idDepartamento column cannot be null. Please make another selection for the idDepartamento field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (barriosNew != null) {
                barriosNew = em.getReference(barriosNew.getClass(), barriosNew.getIdBarrio());
                ciudades.setBarrios(barriosNew);
            }
            if (idDepartamentoNew != null) {
                idDepartamentoNew = em.getReference(idDepartamentoNew.getClass(), idDepartamentoNew.getIdDepartamento());
                ciudades.setIdDepartamento(idDepartamentoNew);
            }
            ciudades = em.merge(ciudades);
            if (barriosNew != null && !barriosNew.equals(barriosOld)) {
                Ciudades oldIdCiudadOfBarrios = barriosNew.getIdCiudad();
                if (oldIdCiudadOfBarrios != null) {
                    oldIdCiudadOfBarrios.setBarrios(null);
                    oldIdCiudadOfBarrios = em.merge(oldIdCiudadOfBarrios);
                }
                barriosNew.setIdCiudad(ciudades);
                barriosNew = em.merge(barriosNew);
            }
            if (idDepartamentoOld != null && !idDepartamentoOld.equals(idDepartamentoNew)) {
                idDepartamentoOld.setCiudades(null);
                idDepartamentoOld = em.merge(idDepartamentoOld);
            }
            if (idDepartamentoNew != null && !idDepartamentoNew.equals(idDepartamentoOld)) {
                idDepartamentoNew.setCiudades(ciudades);
                idDepartamentoNew = em.merge(idDepartamentoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = ciudades.getIdCiudad();
                if (findCiudades(id) == null) {
                    throw new NonexistentEntityException("The ciudades with id " + id + " no longer exists.");
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
            Ciudades ciudades;
            try {
                ciudades = em.getReference(Ciudades.class, id);
                ciudades.getIdCiudad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ciudades with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Barrios barriosOrphanCheck = ciudades.getBarrios();
            if (barriosOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ciudades (" + ciudades + ") cannot be destroyed since the Barrios " + barriosOrphanCheck + " in its barrios field has a non-nullable idCiudad field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Departamentos idDepartamento = ciudades.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento.setCiudades(null);
                idDepartamento = em.merge(idDepartamento);
            }
            em.remove(ciudades);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ciudades> findCiudadesEntities() {
        return findCiudadesEntities(true, -1, -1);
    }

    public List<Ciudades> findCiudadesEntities(int maxResults, int firstResult) {
        return findCiudadesEntities(false, maxResults, firstResult);
    }

    private List<Ciudades> findCiudadesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ciudades.class));
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

    public Ciudades findCiudades(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ciudades.class, id);
        } finally {
            em.close();
        }
    }

    public int getCiudadesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ciudades> rt = cq.from(Ciudades.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
