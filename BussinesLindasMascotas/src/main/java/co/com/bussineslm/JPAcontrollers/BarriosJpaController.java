/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.JPAcontrollers;

import co.com.bussineslm.JPAcontrollers.exceptions.IllegalOrphanException;
import co.com.bussineslm.JPAcontrollers.exceptions.NonexistentEntityException;
import co.com.bussineslm.entities.Barrios;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.com.bussineslm.entities.Ciudades;
import co.com.bussineslm.entities.Personas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class BarriosJpaController implements Serializable {

    public BarriosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Barrios barrios) throws IllegalOrphanException {
        if (barrios.getPersonasList() == null) {
            barrios.setPersonasList(new ArrayList<Personas>());
        }
        List<String> illegalOrphanMessages = null;
        Ciudades idCiudadOrphanCheck = barrios.getIdCiudad();
        if (idCiudadOrphanCheck != null) {
            Barrios oldBarriosOfIdCiudad = idCiudadOrphanCheck.getBarrios();
            if (oldBarriosOfIdCiudad != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Ciudades " + idCiudadOrphanCheck + " already has an item of type Barrios whose idCiudad column cannot be null. Please make another selection for the idCiudad field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciudades idCiudad = barrios.getIdCiudad();
            if (idCiudad != null) {
                idCiudad = em.getReference(idCiudad.getClass(), idCiudad.getIdCiudad());
                barrios.setIdCiudad(idCiudad);
            }
            List<Personas> attachedPersonasList = new ArrayList<Personas>();
            for (Personas personasListPersonasToAttach : barrios.getPersonasList()) {
                personasListPersonasToAttach = em.getReference(personasListPersonasToAttach.getClass(), personasListPersonasToAttach.getIdentificacion());
                attachedPersonasList.add(personasListPersonasToAttach);
            }
            barrios.setPersonasList(attachedPersonasList);
            em.persist(barrios);
            if (idCiudad != null) {
                idCiudad.setBarrios(barrios);
                idCiudad = em.merge(idCiudad);
            }
            for (Personas personasListPersonas : barrios.getPersonasList()) {
                Barrios oldIdBarrioOfPersonasListPersonas = personasListPersonas.getIdBarrio();
                personasListPersonas.setIdBarrio(barrios);
                personasListPersonas = em.merge(personasListPersonas);
                if (oldIdBarrioOfPersonasListPersonas != null) {
                    oldIdBarrioOfPersonasListPersonas.getPersonasList().remove(personasListPersonas);
                    oldIdBarrioOfPersonasListPersonas = em.merge(oldIdBarrioOfPersonasListPersonas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Barrios barrios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Barrios persistentBarrios = em.find(Barrios.class, barrios.getIdBarrio());
            Ciudades idCiudadOld = persistentBarrios.getIdCiudad();
            Ciudades idCiudadNew = barrios.getIdCiudad();
            List<Personas> personasListOld = persistentBarrios.getPersonasList();
            List<Personas> personasListNew = barrios.getPersonasList();
            List<String> illegalOrphanMessages = null;
            if (idCiudadNew != null && !idCiudadNew.equals(idCiudadOld)) {
                Barrios oldBarriosOfIdCiudad = idCiudadNew.getBarrios();
                if (oldBarriosOfIdCiudad != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Ciudades " + idCiudadNew + " already has an item of type Barrios whose idCiudad column cannot be null. Please make another selection for the idCiudad field.");
                }
            }
            for (Personas personasListOldPersonas : personasListOld) {
                if (!personasListNew.contains(personasListOldPersonas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Personas " + personasListOldPersonas + " since its idBarrio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCiudadNew != null) {
                idCiudadNew = em.getReference(idCiudadNew.getClass(), idCiudadNew.getIdCiudad());
                barrios.setIdCiudad(idCiudadNew);
            }
            List<Personas> attachedPersonasListNew = new ArrayList<Personas>();
            for (Personas personasListNewPersonasToAttach : personasListNew) {
                personasListNewPersonasToAttach = em.getReference(personasListNewPersonasToAttach.getClass(), personasListNewPersonasToAttach.getIdentificacion());
                attachedPersonasListNew.add(personasListNewPersonasToAttach);
            }
            personasListNew = attachedPersonasListNew;
            barrios.setPersonasList(personasListNew);
            barrios = em.merge(barrios);
            if (idCiudadOld != null && !idCiudadOld.equals(idCiudadNew)) {
                idCiudadOld.setBarrios(null);
                idCiudadOld = em.merge(idCiudadOld);
            }
            if (idCiudadNew != null && !idCiudadNew.equals(idCiudadOld)) {
                idCiudadNew.setBarrios(barrios);
                idCiudadNew = em.merge(idCiudadNew);
            }
            for (Personas personasListNewPersonas : personasListNew) {
                if (!personasListOld.contains(personasListNewPersonas)) {
                    Barrios oldIdBarrioOfPersonasListNewPersonas = personasListNewPersonas.getIdBarrio();
                    personasListNewPersonas.setIdBarrio(barrios);
                    personasListNewPersonas = em.merge(personasListNewPersonas);
                    if (oldIdBarrioOfPersonasListNewPersonas != null && !oldIdBarrioOfPersonasListNewPersonas.equals(barrios)) {
                        oldIdBarrioOfPersonasListNewPersonas.getPersonasList().remove(personasListNewPersonas);
                        oldIdBarrioOfPersonasListNewPersonas = em.merge(oldIdBarrioOfPersonasListNewPersonas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = barrios.getIdBarrio();
                if (findBarrios(id) == null) {
                    throw new NonexistentEntityException("The barrios with id " + id + " no longer exists.");
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
            Barrios barrios;
            try {
                barrios = em.getReference(Barrios.class, id);
                barrios.getIdBarrio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The barrios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Personas> personasListOrphanCheck = barrios.getPersonasList();
            for (Personas personasListOrphanCheckPersonas : personasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Barrios (" + barrios + ") cannot be destroyed since the Personas " + personasListOrphanCheckPersonas + " in its personasList field has a non-nullable idBarrio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Ciudades idCiudad = barrios.getIdCiudad();
            if (idCiudad != null) {
                idCiudad.setBarrios(null);
                idCiudad = em.merge(idCiudad);
            }
            em.remove(barrios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Barrios> findBarriosEntities() {
        return findBarriosEntities(true, -1, -1);
    }

    public List<Barrios> findBarriosEntities(int maxResults, int firstResult) {
        return findBarriosEntities(false, maxResults, firstResult);
    }

    private List<Barrios> findBarriosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Barrios.class));
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

    public Barrios findBarrios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Barrios.class, id);
        } finally {
            em.close();
        }
    }

    public int getBarriosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Barrios> rt = cq.from(Barrios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Barrios buscarBarrioNombre(String nombreBarrio) {
        EntityManager em = getEntityManager();
        
        try {
            Query q = em.createNamedQuery("Barrios.findByNombreBarrio");
            q.setParameter("nombreBarrio", nombreBarrio);
            
            return (Barrios) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }finally{
            em.close();
        }
    }
    
}
