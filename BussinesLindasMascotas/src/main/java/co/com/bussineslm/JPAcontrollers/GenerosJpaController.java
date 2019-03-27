/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.JPAcontrollers;

import co.com.bussineslm.JPAcontrollers.exceptions.IllegalOrphanException;
import co.com.bussineslm.JPAcontrollers.exceptions.NonexistentEntityException;
import co.com.bussineslm.entities.Generos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.com.bussineslm.entities.Personas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class GenerosJpaController implements Serializable {

    public GenerosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Generos generos) {
        if (generos.getPersonasList() == null) {
            generos.setPersonasList(new ArrayList<Personas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Personas> attachedPersonasList = new ArrayList<Personas>();
            for (Personas personasListPersonasToAttach : generos.getPersonasList()) {
                personasListPersonasToAttach = em.getReference(personasListPersonasToAttach.getClass(), personasListPersonasToAttach.getIdentificacion());
                attachedPersonasList.add(personasListPersonasToAttach);
            }
            generos.setPersonasList(attachedPersonasList);
            em.persist(generos);
            for (Personas personasListPersonas : generos.getPersonasList()) {
                Generos oldIdGeneroOfPersonasListPersonas = personasListPersonas.getIdGenero();
                personasListPersonas.setIdGenero(generos);
                personasListPersonas = em.merge(personasListPersonas);
                if (oldIdGeneroOfPersonasListPersonas != null) {
                    oldIdGeneroOfPersonasListPersonas.getPersonasList().remove(personasListPersonas);
                    oldIdGeneroOfPersonasListPersonas = em.merge(oldIdGeneroOfPersonasListPersonas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Generos generos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Generos persistentGeneros = em.find(Generos.class, generos.getIdGenero());
            List<Personas> personasListOld = persistentGeneros.getPersonasList();
            List<Personas> personasListNew = generos.getPersonasList();
            List<String> illegalOrphanMessages = null;
            for (Personas personasListOldPersonas : personasListOld) {
                if (!personasListNew.contains(personasListOldPersonas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Personas " + personasListOldPersonas + " since its idGenero field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Personas> attachedPersonasListNew = new ArrayList<Personas>();
            for (Personas personasListNewPersonasToAttach : personasListNew) {
                personasListNewPersonasToAttach = em.getReference(personasListNewPersonasToAttach.getClass(), personasListNewPersonasToAttach.getIdentificacion());
                attachedPersonasListNew.add(personasListNewPersonasToAttach);
            }
            personasListNew = attachedPersonasListNew;
            generos.setPersonasList(personasListNew);
            generos = em.merge(generos);
            for (Personas personasListNewPersonas : personasListNew) {
                if (!personasListOld.contains(personasListNewPersonas)) {
                    Generos oldIdGeneroOfPersonasListNewPersonas = personasListNewPersonas.getIdGenero();
                    personasListNewPersonas.setIdGenero(generos);
                    personasListNewPersonas = em.merge(personasListNewPersonas);
                    if (oldIdGeneroOfPersonasListNewPersonas != null && !oldIdGeneroOfPersonasListNewPersonas.equals(generos)) {
                        oldIdGeneroOfPersonasListNewPersonas.getPersonasList().remove(personasListNewPersonas);
                        oldIdGeneroOfPersonasListNewPersonas = em.merge(oldIdGeneroOfPersonasListNewPersonas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = generos.getIdGenero();
                if (findGeneros(id) == null) {
                    throw new NonexistentEntityException("The generos with id " + id + " no longer exists.");
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
            Generos generos;
            try {
                generos = em.getReference(Generos.class, id);
                generos.getIdGenero();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The generos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Personas> personasListOrphanCheck = generos.getPersonasList();
            for (Personas personasListOrphanCheckPersonas : personasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Generos (" + generos + ") cannot be destroyed since the Personas " + personasListOrphanCheckPersonas + " in its personasList field has a non-nullable idGenero field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(generos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Generos> findGenerosEntities() {
        return findGenerosEntities(true, -1, -1);
    }

    public List<Generos> findGenerosEntities(int maxResults, int firstResult) {
        return findGenerosEntities(false, maxResults, firstResult);
    }

    private List<Generos> findGenerosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Generos.class));
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

    public Generos findGeneros(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Generos.class, id);
        } finally {
            em.close();
        }
    }

    public int getGenerosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Generos> rt = cq.from(Generos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Generos buscarGeneroNombre(String nombreGenero) {
        EntityManager em = getEntityManager();
        
        try {
            Query q = em.createNamedQuery("Generos.findByNombreGenero", Generos.class);
            q.setParameter("nombreGenero", nombreGenero);
            
            return (Generos)q.getSingleResult();
        } catch (Exception ex) {
            return null;
        }finally{
            em.close();
        }
    }
    
}
