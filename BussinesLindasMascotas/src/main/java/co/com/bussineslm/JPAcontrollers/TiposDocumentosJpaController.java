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
import co.com.bussineslm.entities.Personas;
import co.com.bussineslm.entities.TiposDocumentos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class TiposDocumentosJpaController implements Serializable {

    public TiposDocumentosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TiposDocumentos tiposDocumentos) {
        if (tiposDocumentos.getPersonasList() == null) {
            tiposDocumentos.setPersonasList(new ArrayList<Personas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Personas> attachedPersonasList = new ArrayList<Personas>();
            for (Personas personasListPersonasToAttach : tiposDocumentos.getPersonasList()) {
                personasListPersonasToAttach = em.getReference(personasListPersonasToAttach.getClass(), personasListPersonasToAttach.getIdentificacion());
                attachedPersonasList.add(personasListPersonasToAttach);
            }
            tiposDocumentos.setPersonasList(attachedPersonasList);
            em.persist(tiposDocumentos);
            for (Personas personasListPersonas : tiposDocumentos.getPersonasList()) {
                TiposDocumentos oldTipoDocumentoOfPersonasListPersonas = personasListPersonas.getTipoDocumento();
                personasListPersonas.setTipoDocumento(tiposDocumentos);
                personasListPersonas = em.merge(personasListPersonas);
                if (oldTipoDocumentoOfPersonasListPersonas != null) {
                    oldTipoDocumentoOfPersonasListPersonas.getPersonasList().remove(personasListPersonas);
                    oldTipoDocumentoOfPersonasListPersonas = em.merge(oldTipoDocumentoOfPersonasListPersonas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TiposDocumentos tiposDocumentos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TiposDocumentos persistentTiposDocumentos = em.find(TiposDocumentos.class, tiposDocumentos.getIdTipoDoc());
            List<Personas> personasListOld = persistentTiposDocumentos.getPersonasList();
            List<Personas> personasListNew = tiposDocumentos.getPersonasList();
            List<String> illegalOrphanMessages = null;
            for (Personas personasListOldPersonas : personasListOld) {
                if (!personasListNew.contains(personasListOldPersonas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Personas " + personasListOldPersonas + " since its tipoDocumento field is not nullable.");
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
            tiposDocumentos.setPersonasList(personasListNew);
            tiposDocumentos = em.merge(tiposDocumentos);
            for (Personas personasListNewPersonas : personasListNew) {
                if (!personasListOld.contains(personasListNewPersonas)) {
                    TiposDocumentos oldTipoDocumentoOfPersonasListNewPersonas = personasListNewPersonas.getTipoDocumento();
                    personasListNewPersonas.setTipoDocumento(tiposDocumentos);
                    personasListNewPersonas = em.merge(personasListNewPersonas);
                    if (oldTipoDocumentoOfPersonasListNewPersonas != null && !oldTipoDocumentoOfPersonasListNewPersonas.equals(tiposDocumentos)) {
                        oldTipoDocumentoOfPersonasListNewPersonas.getPersonasList().remove(personasListNewPersonas);
                        oldTipoDocumentoOfPersonasListNewPersonas = em.merge(oldTipoDocumentoOfPersonasListNewPersonas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tiposDocumentos.getIdTipoDoc();
                if (findTiposDocumentos(id) == null) {
                    throw new NonexistentEntityException("The tiposDocumentos with id " + id + " no longer exists.");
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
            TiposDocumentos tiposDocumentos;
            try {
                tiposDocumentos = em.getReference(TiposDocumentos.class, id);
                tiposDocumentos.getIdTipoDoc();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tiposDocumentos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Personas> personasListOrphanCheck = tiposDocumentos.getPersonasList();
            for (Personas personasListOrphanCheckPersonas : personasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TiposDocumentos (" + tiposDocumentos + ") cannot be destroyed since the Personas " + personasListOrphanCheckPersonas + " in its personasList field has a non-nullable tipoDocumento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tiposDocumentos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TiposDocumentos> findTiposDocumentosEntities() {
        return findTiposDocumentosEntities(true, -1, -1);
    }

    public List<TiposDocumentos> findTiposDocumentosEntities(int maxResults, int firstResult) {
        return findTiposDocumentosEntities(false, maxResults, firstResult);
    }

    private List<TiposDocumentos> findTiposDocumentosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TiposDocumentos.class));
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

    public TiposDocumentos findTiposDocumentos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TiposDocumentos.class, id);
        } finally {
            em.close();
        }
    }

    public int getTiposDocumentosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TiposDocumentos> rt = cq.from(TiposDocumentos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public TiposDocumentos buscarTipoDocNombre(String nombreTipoDoc) {
        EntityManager em = getEntityManager();
        
        try {
            Query q = em.createNamedQuery("TiposDocumentos.findByNombreTipoDoc");
            q.setParameter("nombreTipoDoc", nombreTipoDoc);
            
            return (TiposDocumentos) q.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally{
            em.close();
}
    }
    
}
