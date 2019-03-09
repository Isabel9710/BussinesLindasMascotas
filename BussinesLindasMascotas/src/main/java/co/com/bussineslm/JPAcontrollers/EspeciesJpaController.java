/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.JPAcontrollers;

import co.com.bussineslm.JPAcontrollers.exceptions.IllegalOrphanException;
import co.com.bussineslm.JPAcontrollers.exceptions.NonexistentEntityException;
import co.com.bussineslm.entities.Especies;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.com.bussineslm.entities.Razas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class EspeciesJpaController implements Serializable {

    public EspeciesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Especies especies) {
        if (especies.getRazasList() == null) {
            especies.setRazasList(new ArrayList<Razas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Razas> attachedRazasList = new ArrayList<Razas>();
            for (Razas razasListRazasToAttach : especies.getRazasList()) {
                razasListRazasToAttach = em.getReference(razasListRazasToAttach.getClass(), razasListRazasToAttach.getIdRaza());
                attachedRazasList.add(razasListRazasToAttach);
            }
            especies.setRazasList(attachedRazasList);
            em.persist(especies);
            for (Razas razasListRazas : especies.getRazasList()) {
                Especies oldIdEspecieOfRazasListRazas = razasListRazas.getIdEspecie();
                razasListRazas.setIdEspecie(especies);
                razasListRazas = em.merge(razasListRazas);
                if (oldIdEspecieOfRazasListRazas != null) {
                    oldIdEspecieOfRazasListRazas.getRazasList().remove(razasListRazas);
                    oldIdEspecieOfRazasListRazas = em.merge(oldIdEspecieOfRazasListRazas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Especies especies) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Especies persistentEspecies = em.find(Especies.class, especies.getIdEspecie());
            List<Razas> razasListOld = persistentEspecies.getRazasList();
            List<Razas> razasListNew = especies.getRazasList();
            List<String> illegalOrphanMessages = null;
            for (Razas razasListOldRazas : razasListOld) {
                if (!razasListNew.contains(razasListOldRazas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Razas " + razasListOldRazas + " since its idEspecie field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Razas> attachedRazasListNew = new ArrayList<Razas>();
            for (Razas razasListNewRazasToAttach : razasListNew) {
                razasListNewRazasToAttach = em.getReference(razasListNewRazasToAttach.getClass(), razasListNewRazasToAttach.getIdRaza());
                attachedRazasListNew.add(razasListNewRazasToAttach);
            }
            razasListNew = attachedRazasListNew;
            especies.setRazasList(razasListNew);
            especies = em.merge(especies);
            for (Razas razasListNewRazas : razasListNew) {
                if (!razasListOld.contains(razasListNewRazas)) {
                    Especies oldIdEspecieOfRazasListNewRazas = razasListNewRazas.getIdEspecie();
                    razasListNewRazas.setIdEspecie(especies);
                    razasListNewRazas = em.merge(razasListNewRazas);
                    if (oldIdEspecieOfRazasListNewRazas != null && !oldIdEspecieOfRazasListNewRazas.equals(especies)) {
                        oldIdEspecieOfRazasListNewRazas.getRazasList().remove(razasListNewRazas);
                        oldIdEspecieOfRazasListNewRazas = em.merge(oldIdEspecieOfRazasListNewRazas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = especies.getIdEspecie();
                if (findEspecies(id) == null) {
                    throw new NonexistentEntityException("The especies with id " + id + " no longer exists.");
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
            Especies especies;
            try {
                especies = em.getReference(Especies.class, id);
                especies.getIdEspecie();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The especies with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Razas> razasListOrphanCheck = especies.getRazasList();
            for (Razas razasListOrphanCheckRazas : razasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Especies (" + especies + ") cannot be destroyed since the Razas " + razasListOrphanCheckRazas + " in its razasList field has a non-nullable idEspecie field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(especies);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Especies> findEspeciesEntities() {
        return findEspeciesEntities(true, -1, -1);
    }

    public List<Especies> findEspeciesEntities(int maxResults, int firstResult) {
        return findEspeciesEntities(false, maxResults, firstResult);
    }

    private List<Especies> findEspeciesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Especies.class));
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

    public Especies findEspecies(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Especies.class, id);
        } finally {
            em.close();
        }
    }

    public int getEspeciesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Especies> rt = cq.from(Especies.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
