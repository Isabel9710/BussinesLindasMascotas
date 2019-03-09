/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.JPAcontrollers;

import co.com.bussineslm.JPAcontrollers.exceptions.IllegalOrphanException;
import co.com.bussineslm.JPAcontrollers.exceptions.NonexistentEntityException;
import co.com.bussineslm.entities.Cargos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.com.bussineslm.entities.InformacionLaboral;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class CargosJpaController implements Serializable {

    public CargosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cargos cargos) {
        if (cargos.getInformacionLaboralList() == null) {
            cargos.setInformacionLaboralList(new ArrayList<InformacionLaboral>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<InformacionLaboral> attachedInformacionLaboralList = new ArrayList<InformacionLaboral>();
            for (InformacionLaboral informacionLaboralListInformacionLaboralToAttach : cargos.getInformacionLaboralList()) {
                informacionLaboralListInformacionLaboralToAttach = em.getReference(informacionLaboralListInformacionLaboralToAttach.getClass(), informacionLaboralListInformacionLaboralToAttach.getIdPersona());
                attachedInformacionLaboralList.add(informacionLaboralListInformacionLaboralToAttach);
            }
            cargos.setInformacionLaboralList(attachedInformacionLaboralList);
            em.persist(cargos);
            for (InformacionLaboral informacionLaboralListInformacionLaboral : cargos.getInformacionLaboralList()) {
                Cargos oldIdCargoOfInformacionLaboralListInformacionLaboral = informacionLaboralListInformacionLaboral.getIdCargo();
                informacionLaboralListInformacionLaboral.setIdCargo(cargos);
                informacionLaboralListInformacionLaboral = em.merge(informacionLaboralListInformacionLaboral);
                if (oldIdCargoOfInformacionLaboralListInformacionLaboral != null) {
                    oldIdCargoOfInformacionLaboralListInformacionLaboral.getInformacionLaboralList().remove(informacionLaboralListInformacionLaboral);
                    oldIdCargoOfInformacionLaboralListInformacionLaboral = em.merge(oldIdCargoOfInformacionLaboralListInformacionLaboral);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cargos cargos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cargos persistentCargos = em.find(Cargos.class, cargos.getIdCargo());
            List<InformacionLaboral> informacionLaboralListOld = persistentCargos.getInformacionLaboralList();
            List<InformacionLaboral> informacionLaboralListNew = cargos.getInformacionLaboralList();
            List<String> illegalOrphanMessages = null;
            for (InformacionLaboral informacionLaboralListOldInformacionLaboral : informacionLaboralListOld) {
                if (!informacionLaboralListNew.contains(informacionLaboralListOldInformacionLaboral)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain InformacionLaboral " + informacionLaboralListOldInformacionLaboral + " since its idCargo field is not nullable.");
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
            cargos.setInformacionLaboralList(informacionLaboralListNew);
            cargos = em.merge(cargos);
            for (InformacionLaboral informacionLaboralListNewInformacionLaboral : informacionLaboralListNew) {
                if (!informacionLaboralListOld.contains(informacionLaboralListNewInformacionLaboral)) {
                    Cargos oldIdCargoOfInformacionLaboralListNewInformacionLaboral = informacionLaboralListNewInformacionLaboral.getIdCargo();
                    informacionLaboralListNewInformacionLaboral.setIdCargo(cargos);
                    informacionLaboralListNewInformacionLaboral = em.merge(informacionLaboralListNewInformacionLaboral);
                    if (oldIdCargoOfInformacionLaboralListNewInformacionLaboral != null && !oldIdCargoOfInformacionLaboralListNewInformacionLaboral.equals(cargos)) {
                        oldIdCargoOfInformacionLaboralListNewInformacionLaboral.getInformacionLaboralList().remove(informacionLaboralListNewInformacionLaboral);
                        oldIdCargoOfInformacionLaboralListNewInformacionLaboral = em.merge(oldIdCargoOfInformacionLaboralListNewInformacionLaboral);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cargos.getIdCargo();
                if (findCargos(id) == null) {
                    throw new NonexistentEntityException("The cargos with id " + id + " no longer exists.");
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
            Cargos cargos;
            try {
                cargos = em.getReference(Cargos.class, id);
                cargos.getIdCargo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cargos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<InformacionLaboral> informacionLaboralListOrphanCheck = cargos.getInformacionLaboralList();
            for (InformacionLaboral informacionLaboralListOrphanCheckInformacionLaboral : informacionLaboralListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cargos (" + cargos + ") cannot be destroyed since the InformacionLaboral " + informacionLaboralListOrphanCheckInformacionLaboral + " in its informacionLaboralList field has a non-nullable idCargo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cargos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cargos> findCargosEntities() {
        return findCargosEntities(true, -1, -1);
    }

    public List<Cargos> findCargosEntities(int maxResults, int firstResult) {
        return findCargosEntities(false, maxResults, firstResult);
    }

    private List<Cargos> findCargosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cargos.class));
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

    public Cargos findCargos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cargos.class, id);
        } finally {
            em.close();
        }
    }

    public int getCargosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cargos> rt = cq.from(Cargos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
