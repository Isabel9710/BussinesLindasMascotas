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
import co.com.bussineslm.entities.Mascotas;
import co.com.bussineslm.entities.Sexos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class SexosJpaController implements Serializable {

    public SexosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sexos sexos) {
        if (sexos.getMascotasList() == null) {
            sexos.setMascotasList(new ArrayList<Mascotas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Mascotas> attachedMascotasList = new ArrayList<Mascotas>();
            for (Mascotas mascotasListMascotasToAttach : sexos.getMascotasList()) {
                mascotasListMascotasToAttach = em.getReference(mascotasListMascotasToAttach.getClass(), mascotasListMascotasToAttach.getIdMascota());
                attachedMascotasList.add(mascotasListMascotasToAttach);
            }
            sexos.setMascotasList(attachedMascotasList);
            em.persist(sexos);
            for (Mascotas mascotasListMascotas : sexos.getMascotasList()) {
                Sexos oldIdSexoOfMascotasListMascotas = mascotasListMascotas.getIdSexo();
                mascotasListMascotas.setIdSexo(sexos);
                mascotasListMascotas = em.merge(mascotasListMascotas);
                if (oldIdSexoOfMascotasListMascotas != null) {
                    oldIdSexoOfMascotasListMascotas.getMascotasList().remove(mascotasListMascotas);
                    oldIdSexoOfMascotasListMascotas = em.merge(oldIdSexoOfMascotasListMascotas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sexos sexos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sexos persistentSexos = em.find(Sexos.class, sexos.getIdSexo());
            List<Mascotas> mascotasListOld = persistentSexos.getMascotasList();
            List<Mascotas> mascotasListNew = sexos.getMascotasList();
            List<String> illegalOrphanMessages = null;
            for (Mascotas mascotasListOldMascotas : mascotasListOld) {
                if (!mascotasListNew.contains(mascotasListOldMascotas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Mascotas " + mascotasListOldMascotas + " since its idSexo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Mascotas> attachedMascotasListNew = new ArrayList<Mascotas>();
            for (Mascotas mascotasListNewMascotasToAttach : mascotasListNew) {
                mascotasListNewMascotasToAttach = em.getReference(mascotasListNewMascotasToAttach.getClass(), mascotasListNewMascotasToAttach.getIdMascota());
                attachedMascotasListNew.add(mascotasListNewMascotasToAttach);
            }
            mascotasListNew = attachedMascotasListNew;
            sexos.setMascotasList(mascotasListNew);
            sexos = em.merge(sexos);
            for (Mascotas mascotasListNewMascotas : mascotasListNew) {
                if (!mascotasListOld.contains(mascotasListNewMascotas)) {
                    Sexos oldIdSexoOfMascotasListNewMascotas = mascotasListNewMascotas.getIdSexo();
                    mascotasListNewMascotas.setIdSexo(sexos);
                    mascotasListNewMascotas = em.merge(mascotasListNewMascotas);
                    if (oldIdSexoOfMascotasListNewMascotas != null && !oldIdSexoOfMascotasListNewMascotas.equals(sexos)) {
                        oldIdSexoOfMascotasListNewMascotas.getMascotasList().remove(mascotasListNewMascotas);
                        oldIdSexoOfMascotasListNewMascotas = em.merge(oldIdSexoOfMascotasListNewMascotas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sexos.getIdSexo();
                if (findSexos(id) == null) {
                    throw new NonexistentEntityException("The sexos with id " + id + " no longer exists.");
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
            Sexos sexos;
            try {
                sexos = em.getReference(Sexos.class, id);
                sexos.getIdSexo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sexos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Mascotas> mascotasListOrphanCheck = sexos.getMascotasList();
            for (Mascotas mascotasListOrphanCheckMascotas : mascotasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Sexos (" + sexos + ") cannot be destroyed since the Mascotas " + mascotasListOrphanCheckMascotas + " in its mascotasList field has a non-nullable idSexo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(sexos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sexos> findSexosEntities() {
        return findSexosEntities(true, -1, -1);
    }

    public List<Sexos> findSexosEntities(int maxResults, int firstResult) {
        return findSexosEntities(false, maxResults, firstResult);
    }

    private List<Sexos> findSexosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sexos.class));
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

    public Sexos findSexos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sexos.class, id);
        } finally {
            em.close();
        }
    }

    public int getSexosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sexos> rt = cq.from(Sexos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
