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
import co.com.bussineslm.entities.Especies;
import co.com.bussineslm.entities.Mascotas;
import co.com.bussineslm.entities.Razas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class RazasJpaController implements Serializable {

    public RazasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Razas razas) {
        if (razas.getMascotasList() == null) {
            razas.setMascotasList(new ArrayList<Mascotas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Especies idEspecie = razas.getIdEspecie();
            if (idEspecie != null) {
                idEspecie = em.getReference(idEspecie.getClass(), idEspecie.getIdEspecie());
                razas.setIdEspecie(idEspecie);
            }
            List<Mascotas> attachedMascotasList = new ArrayList<Mascotas>();
            for (Mascotas mascotasListMascotasToAttach : razas.getMascotasList()) {
                mascotasListMascotasToAttach = em.getReference(mascotasListMascotasToAttach.getClass(), mascotasListMascotasToAttach.getIdMascota());
                attachedMascotasList.add(mascotasListMascotasToAttach);
            }
            razas.setMascotasList(attachedMascotasList);
            em.persist(razas);
            if (idEspecie != null) {
                idEspecie.getRazasList().add(razas);
                idEspecie = em.merge(idEspecie);
            }
            for (Mascotas mascotasListMascotas : razas.getMascotasList()) {
                Razas oldIdRazaOfMascotasListMascotas = mascotasListMascotas.getIdRaza();
                mascotasListMascotas.setIdRaza(razas);
                mascotasListMascotas = em.merge(mascotasListMascotas);
                if (oldIdRazaOfMascotasListMascotas != null) {
                    oldIdRazaOfMascotasListMascotas.getMascotasList().remove(mascotasListMascotas);
                    oldIdRazaOfMascotasListMascotas = em.merge(oldIdRazaOfMascotasListMascotas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Razas razas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Razas persistentRazas = em.find(Razas.class, razas.getIdRaza());
            Especies idEspecieOld = persistentRazas.getIdEspecie();
            Especies idEspecieNew = razas.getIdEspecie();
            List<Mascotas> mascotasListOld = persistentRazas.getMascotasList();
            List<Mascotas> mascotasListNew = razas.getMascotasList();
            List<String> illegalOrphanMessages = null;
            for (Mascotas mascotasListOldMascotas : mascotasListOld) {
                if (!mascotasListNew.contains(mascotasListOldMascotas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Mascotas " + mascotasListOldMascotas + " since its idRaza field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idEspecieNew != null) {
                idEspecieNew = em.getReference(idEspecieNew.getClass(), idEspecieNew.getIdEspecie());
                razas.setIdEspecie(idEspecieNew);
            }
            List<Mascotas> attachedMascotasListNew = new ArrayList<Mascotas>();
            for (Mascotas mascotasListNewMascotasToAttach : mascotasListNew) {
                mascotasListNewMascotasToAttach = em.getReference(mascotasListNewMascotasToAttach.getClass(), mascotasListNewMascotasToAttach.getIdMascota());
                attachedMascotasListNew.add(mascotasListNewMascotasToAttach);
            }
            mascotasListNew = attachedMascotasListNew;
            razas.setMascotasList(mascotasListNew);
            razas = em.merge(razas);
            if (idEspecieOld != null && !idEspecieOld.equals(idEspecieNew)) {
                idEspecieOld.getRazasList().remove(razas);
                idEspecieOld = em.merge(idEspecieOld);
            }
            if (idEspecieNew != null && !idEspecieNew.equals(idEspecieOld)) {
                idEspecieNew.getRazasList().add(razas);
                idEspecieNew = em.merge(idEspecieNew);
            }
            for (Mascotas mascotasListNewMascotas : mascotasListNew) {
                if (!mascotasListOld.contains(mascotasListNewMascotas)) {
                    Razas oldIdRazaOfMascotasListNewMascotas = mascotasListNewMascotas.getIdRaza();
                    mascotasListNewMascotas.setIdRaza(razas);
                    mascotasListNewMascotas = em.merge(mascotasListNewMascotas);
                    if (oldIdRazaOfMascotasListNewMascotas != null && !oldIdRazaOfMascotasListNewMascotas.equals(razas)) {
                        oldIdRazaOfMascotasListNewMascotas.getMascotasList().remove(mascotasListNewMascotas);
                        oldIdRazaOfMascotasListNewMascotas = em.merge(oldIdRazaOfMascotasListNewMascotas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = razas.getIdRaza();
                if (findRazas(id) == null) {
                    throw new NonexistentEntityException("The razas with id " + id + " no longer exists.");
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
            Razas razas;
            try {
                razas = em.getReference(Razas.class, id);
                razas.getIdRaza();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The razas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Mascotas> mascotasListOrphanCheck = razas.getMascotasList();
            for (Mascotas mascotasListOrphanCheckMascotas : mascotasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Razas (" + razas + ") cannot be destroyed since the Mascotas " + mascotasListOrphanCheckMascotas + " in its mascotasList field has a non-nullable idRaza field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Especies idEspecie = razas.getIdEspecie();
            if (idEspecie != null) {
                idEspecie.getRazasList().remove(razas);
                idEspecie = em.merge(idEspecie);
            }
            em.remove(razas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Razas> findRazasEntities() {
        return findRazasEntities(true, -1, -1);
    }

    public List<Razas> findRazasEntities(int maxResults, int firstResult) {
        return findRazasEntities(false, maxResults, firstResult);
    }

    private List<Razas> findRazasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Razas.class));
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

    public Razas findRazas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Razas.class, id);
        } finally {
            em.close();
        }
    }

    public int getRazasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Razas> rt = cq.from(Razas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
