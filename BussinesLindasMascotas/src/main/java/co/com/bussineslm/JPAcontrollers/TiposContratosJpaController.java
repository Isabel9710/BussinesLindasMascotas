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
import co.com.bussineslm.entities.InformacionLaboral;
import co.com.bussineslm.entities.TiposContratos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class TiposContratosJpaController implements Serializable {

    public TiposContratosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TiposContratos tiposContratos) {
        if (tiposContratos.getInformacionLaboralList() == null) {
            tiposContratos.setInformacionLaboralList(new ArrayList<InformacionLaboral>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<InformacionLaboral> attachedInformacionLaboralList = new ArrayList<InformacionLaboral>();
            for (InformacionLaboral informacionLaboralListInformacionLaboralToAttach : tiposContratos.getInformacionLaboralList()) {
                informacionLaboralListInformacionLaboralToAttach = em.getReference(informacionLaboralListInformacionLaboralToAttach.getClass(), informacionLaboralListInformacionLaboralToAttach.getIdPersona());
                attachedInformacionLaboralList.add(informacionLaboralListInformacionLaboralToAttach);
            }
            tiposContratos.setInformacionLaboralList(attachedInformacionLaboralList);
            em.persist(tiposContratos);
            for (InformacionLaboral informacionLaboralListInformacionLaboral : tiposContratos.getInformacionLaboralList()) {
                TiposContratos oldIdTipoContratoOfInformacionLaboralListInformacionLaboral = informacionLaboralListInformacionLaboral.getIdTipoContrato();
                informacionLaboralListInformacionLaboral.setIdTipoContrato(tiposContratos);
                informacionLaboralListInformacionLaboral = em.merge(informacionLaboralListInformacionLaboral);
                if (oldIdTipoContratoOfInformacionLaboralListInformacionLaboral != null) {
                    oldIdTipoContratoOfInformacionLaboralListInformacionLaboral.getInformacionLaboralList().remove(informacionLaboralListInformacionLaboral);
                    oldIdTipoContratoOfInformacionLaboralListInformacionLaboral = em.merge(oldIdTipoContratoOfInformacionLaboralListInformacionLaboral);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TiposContratos tiposContratos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TiposContratos persistentTiposContratos = em.find(TiposContratos.class, tiposContratos.getIdTipoContrato());
            List<InformacionLaboral> informacionLaboralListOld = persistentTiposContratos.getInformacionLaboralList();
            List<InformacionLaboral> informacionLaboralListNew = tiposContratos.getInformacionLaboralList();
            List<String> illegalOrphanMessages = null;
            for (InformacionLaboral informacionLaboralListOldInformacionLaboral : informacionLaboralListOld) {
                if (!informacionLaboralListNew.contains(informacionLaboralListOldInformacionLaboral)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain InformacionLaboral " + informacionLaboralListOldInformacionLaboral + " since its idTipoContrato field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<InformacionLaboral> attachedInformacionLaboralListNew = new ArrayList<InformacionLaboral>();
            for (InformacionLaboral informacionLaboralListNewInformacionLaboralToAttach : informacionLaboralListNew) {
                informacionLaboralListNewInformacionLaboralToAttach = em.getReference(informacionLaboralListNewInformacionLaboralToAttach.getClass(), informacionLaboralListNewInformacionLaboralToAttach.getIdPersona());
                attachedInformacionLaboralListNew.add(informacionLaboralListNewInformacionLaboralToAttach);
            }
            informacionLaboralListNew = attachedInformacionLaboralListNew;
            tiposContratos.setInformacionLaboralList(informacionLaboralListNew);
            tiposContratos = em.merge(tiposContratos);
            for (InformacionLaboral informacionLaboralListNewInformacionLaboral : informacionLaboralListNew) {
                if (!informacionLaboralListOld.contains(informacionLaboralListNewInformacionLaboral)) {
                    TiposContratos oldIdTipoContratoOfInformacionLaboralListNewInformacionLaboral = informacionLaboralListNewInformacionLaboral.getIdTipoContrato();
                    informacionLaboralListNewInformacionLaboral.setIdTipoContrato(tiposContratos);
                    informacionLaboralListNewInformacionLaboral = em.merge(informacionLaboralListNewInformacionLaboral);
                    if (oldIdTipoContratoOfInformacionLaboralListNewInformacionLaboral != null && !oldIdTipoContratoOfInformacionLaboralListNewInformacionLaboral.equals(tiposContratos)) {
                        oldIdTipoContratoOfInformacionLaboralListNewInformacionLaboral.getInformacionLaboralList().remove(informacionLaboralListNewInformacionLaboral);
                        oldIdTipoContratoOfInformacionLaboralListNewInformacionLaboral = em.merge(oldIdTipoContratoOfInformacionLaboralListNewInformacionLaboral);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tiposContratos.getIdTipoContrato();
                if (findTiposContratos(id) == null) {
                    throw new NonexistentEntityException("The tiposContratos with id " + id + " no longer exists.");
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
            TiposContratos tiposContratos;
            try {
                tiposContratos = em.getReference(TiposContratos.class, id);
                tiposContratos.getIdTipoContrato();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tiposContratos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<InformacionLaboral> informacionLaboralListOrphanCheck = tiposContratos.getInformacionLaboralList();
            for (InformacionLaboral informacionLaboralListOrphanCheckInformacionLaboral : informacionLaboralListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TiposContratos (" + tiposContratos + ") cannot be destroyed since the InformacionLaboral " + informacionLaboralListOrphanCheckInformacionLaboral + " in its informacionLaboralList field has a non-nullable idTipoContrato field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tiposContratos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TiposContratos> findTiposContratosEntities() {
        return findTiposContratosEntities(true, -1, -1);
    }

    public List<TiposContratos> findTiposContratosEntities(int maxResults, int firstResult) {
        return findTiposContratosEntities(false, maxResults, firstResult);
    }

    private List<TiposContratos> findTiposContratosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TiposContratos.class));
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

    public TiposContratos findTiposContratos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TiposContratos.class, id);
        } finally {
            em.close();
        }
    }

    public int getTiposContratosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TiposContratos> rt = cq.from(TiposContratos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
