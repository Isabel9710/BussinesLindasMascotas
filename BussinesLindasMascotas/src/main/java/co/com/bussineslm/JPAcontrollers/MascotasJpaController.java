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
import co.com.bussineslm.entities.Razas;
import co.com.bussineslm.entities.Sexos;
import co.com.bussineslm.entities.Citas;
import java.util.ArrayList;
import java.util.List;
import co.com.bussineslm.entities.HistoriasClinicas;
import co.com.bussineslm.entities.Mascotas;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class MascotasJpaController implements Serializable {

    public MascotasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Mascotas mascotas) {
        if (mascotas.getCitasList() == null) {
            mascotas.setCitasList(new ArrayList<Citas>());
        }
        if (mascotas.getHistoriasClinicasList() == null) {
            mascotas.setHistoriasClinicasList(new ArrayList<HistoriasClinicas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personas idPropietario = mascotas.getIdPropietario();
            if (idPropietario != null) {
                idPropietario = em.getReference(idPropietario.getClass(), idPropietario.getIdentificacion());
                mascotas.setIdPropietario(idPropietario);
            }
            Razas idRaza = mascotas.getIdRaza();
            if (idRaza != null) {
                idRaza = em.getReference(idRaza.getClass(), idRaza.getIdRaza());
                mascotas.setIdRaza(idRaza);
            }
            Sexos idSexo = mascotas.getIdSexo();
            if (idSexo != null) {
                idSexo = em.getReference(idSexo.getClass(), idSexo.getIdSexo());
                mascotas.setIdSexo(idSexo);
            }
            List<Citas> attachedCitasList = new ArrayList<Citas>();
            for (Citas citasListCitasToAttach : mascotas.getCitasList()) {
                citasListCitasToAttach = em.getReference(citasListCitasToAttach.getClass(), citasListCitasToAttach.getIdCita());
                attachedCitasList.add(citasListCitasToAttach);
            }
            mascotas.setCitasList(attachedCitasList);
            List<HistoriasClinicas> attachedHistoriasClinicasList = new ArrayList<HistoriasClinicas>();
            for (HistoriasClinicas historiasClinicasListHistoriasClinicasToAttach : mascotas.getHistoriasClinicasList()) {
                historiasClinicasListHistoriasClinicasToAttach = em.getReference(historiasClinicasListHistoriasClinicasToAttach.getClass(), historiasClinicasListHistoriasClinicasToAttach.getIdProcedimiento());
                attachedHistoriasClinicasList.add(historiasClinicasListHistoriasClinicasToAttach);
            }
            mascotas.setHistoriasClinicasList(attachedHistoriasClinicasList);
            em.persist(mascotas);
            if (idPropietario != null) {
                idPropietario.getMascotasList().add(mascotas);
                idPropietario = em.merge(idPropietario);
            }
            if (idRaza != null) {
                idRaza.getMascotasList().add(mascotas);
                idRaza = em.merge(idRaza);
            }
            if (idSexo != null) {
                idSexo.getMascotasList().add(mascotas);
                idSexo = em.merge(idSexo);
            }
            for (Citas citasListCitas : mascotas.getCitasList()) {
                Mascotas oldIdMascotaOfCitasListCitas = citasListCitas.getIdMascota();
                citasListCitas.setIdMascota(mascotas);
                citasListCitas = em.merge(citasListCitas);
                if (oldIdMascotaOfCitasListCitas != null) {
                    oldIdMascotaOfCitasListCitas.getCitasList().remove(citasListCitas);
                    oldIdMascotaOfCitasListCitas = em.merge(oldIdMascotaOfCitasListCitas);
                }
            }
            for (HistoriasClinicas historiasClinicasListHistoriasClinicas : mascotas.getHistoriasClinicasList()) {
                Mascotas oldIdMascotaOfHistoriasClinicasListHistoriasClinicas = historiasClinicasListHistoriasClinicas.getIdMascota();
                historiasClinicasListHistoriasClinicas.setIdMascota(mascotas);
                historiasClinicasListHistoriasClinicas = em.merge(historiasClinicasListHistoriasClinicas);
                if (oldIdMascotaOfHistoriasClinicasListHistoriasClinicas != null) {
                    oldIdMascotaOfHistoriasClinicasListHistoriasClinicas.getHistoriasClinicasList().remove(historiasClinicasListHistoriasClinicas);
                    oldIdMascotaOfHistoriasClinicasListHistoriasClinicas = em.merge(oldIdMascotaOfHistoriasClinicasListHistoriasClinicas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Mascotas mascotas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mascotas persistentMascotas = em.find(Mascotas.class, mascotas.getIdMascota());
            Personas idPropietarioOld = persistentMascotas.getIdPropietario();
            Personas idPropietarioNew = mascotas.getIdPropietario();
            Razas idRazaOld = persistentMascotas.getIdRaza();
            Razas idRazaNew = mascotas.getIdRaza();
            Sexos idSexoOld = persistentMascotas.getIdSexo();
            Sexos idSexoNew = mascotas.getIdSexo();
            List<Citas> citasListOld = persistentMascotas.getCitasList();
            List<Citas> citasListNew = mascotas.getCitasList();
            List<HistoriasClinicas> historiasClinicasListOld = persistentMascotas.getHistoriasClinicasList();
            List<HistoriasClinicas> historiasClinicasListNew = mascotas.getHistoriasClinicasList();
            List<String> illegalOrphanMessages = null;
            for (Citas citasListOldCitas : citasListOld) {
                if (!citasListNew.contains(citasListOldCitas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Citas " + citasListOldCitas + " since its idMascota field is not nullable.");
                }
            }
            for (HistoriasClinicas historiasClinicasListOldHistoriasClinicas : historiasClinicasListOld) {
                if (!historiasClinicasListNew.contains(historiasClinicasListOldHistoriasClinicas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HistoriasClinicas " + historiasClinicasListOldHistoriasClinicas + " since its idMascota field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPropietarioNew != null) {
                idPropietarioNew = em.getReference(idPropietarioNew.getClass(), idPropietarioNew.getIdentificacion());
                mascotas.setIdPropietario(idPropietarioNew);
            }
            if (idRazaNew != null) {
                idRazaNew = em.getReference(idRazaNew.getClass(), idRazaNew.getIdRaza());
                mascotas.setIdRaza(idRazaNew);
            }
            if (idSexoNew != null) {
                idSexoNew = em.getReference(idSexoNew.getClass(), idSexoNew.getIdSexo());
                mascotas.setIdSexo(idSexoNew);
            }
            List<Citas> attachedCitasListNew = new ArrayList<Citas>();
            for (Citas citasListNewCitasToAttach : citasListNew) {
                citasListNewCitasToAttach = em.getReference(citasListNewCitasToAttach.getClass(), citasListNewCitasToAttach.getIdCita());
                attachedCitasListNew.add(citasListNewCitasToAttach);
            }
            citasListNew = attachedCitasListNew;
            mascotas.setCitasList(citasListNew);
            List<HistoriasClinicas> attachedHistoriasClinicasListNew = new ArrayList<HistoriasClinicas>();
            for (HistoriasClinicas historiasClinicasListNewHistoriasClinicasToAttach : historiasClinicasListNew) {
                historiasClinicasListNewHistoriasClinicasToAttach = em.getReference(historiasClinicasListNewHistoriasClinicasToAttach.getClass(), historiasClinicasListNewHistoriasClinicasToAttach.getIdProcedimiento());
                attachedHistoriasClinicasListNew.add(historiasClinicasListNewHistoriasClinicasToAttach);
            }
            historiasClinicasListNew = attachedHistoriasClinicasListNew;
            mascotas.setHistoriasClinicasList(historiasClinicasListNew);
            mascotas = em.merge(mascotas);
            if (idPropietarioOld != null && !idPropietarioOld.equals(idPropietarioNew)) {
                idPropietarioOld.getMascotasList().remove(mascotas);
                idPropietarioOld = em.merge(idPropietarioOld);
            }
            if (idPropietarioNew != null && !idPropietarioNew.equals(idPropietarioOld)) {
                idPropietarioNew.getMascotasList().add(mascotas);
                idPropietarioNew = em.merge(idPropietarioNew);
            }
            if (idRazaOld != null && !idRazaOld.equals(idRazaNew)) {
                idRazaOld.getMascotasList().remove(mascotas);
                idRazaOld = em.merge(idRazaOld);
            }
            if (idRazaNew != null && !idRazaNew.equals(idRazaOld)) {
                idRazaNew.getMascotasList().add(mascotas);
                idRazaNew = em.merge(idRazaNew);
            }
            if (idSexoOld != null && !idSexoOld.equals(idSexoNew)) {
                idSexoOld.getMascotasList().remove(mascotas);
                idSexoOld = em.merge(idSexoOld);
            }
            if (idSexoNew != null && !idSexoNew.equals(idSexoOld)) {
                idSexoNew.getMascotasList().add(mascotas);
                idSexoNew = em.merge(idSexoNew);
            }
            for (Citas citasListNewCitas : citasListNew) {
                if (!citasListOld.contains(citasListNewCitas)) {
                    Mascotas oldIdMascotaOfCitasListNewCitas = citasListNewCitas.getIdMascota();
                    citasListNewCitas.setIdMascota(mascotas);
                    citasListNewCitas = em.merge(citasListNewCitas);
                    if (oldIdMascotaOfCitasListNewCitas != null && !oldIdMascotaOfCitasListNewCitas.equals(mascotas)) {
                        oldIdMascotaOfCitasListNewCitas.getCitasList().remove(citasListNewCitas);
                        oldIdMascotaOfCitasListNewCitas = em.merge(oldIdMascotaOfCitasListNewCitas);
                    }
                }
            }
            for (HistoriasClinicas historiasClinicasListNewHistoriasClinicas : historiasClinicasListNew) {
                if (!historiasClinicasListOld.contains(historiasClinicasListNewHistoriasClinicas)) {
                    Mascotas oldIdMascotaOfHistoriasClinicasListNewHistoriasClinicas = historiasClinicasListNewHistoriasClinicas.getIdMascota();
                    historiasClinicasListNewHistoriasClinicas.setIdMascota(mascotas);
                    historiasClinicasListNewHistoriasClinicas = em.merge(historiasClinicasListNewHistoriasClinicas);
                    if (oldIdMascotaOfHistoriasClinicasListNewHistoriasClinicas != null && !oldIdMascotaOfHistoriasClinicasListNewHistoriasClinicas.equals(mascotas)) {
                        oldIdMascotaOfHistoriasClinicasListNewHistoriasClinicas.getHistoriasClinicasList().remove(historiasClinicasListNewHistoriasClinicas);
                        oldIdMascotaOfHistoriasClinicasListNewHistoriasClinicas = em.merge(oldIdMascotaOfHistoriasClinicasListNewHistoriasClinicas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = mascotas.getIdMascota();
                if (findMascotas(id) == null) {
                    throw new NonexistentEntityException("The mascotas with id " + id + " no longer exists.");
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
            Mascotas mascotas;
            try {
                mascotas = em.getReference(Mascotas.class, id);
                mascotas.getIdMascota();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mascotas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Citas> citasListOrphanCheck = mascotas.getCitasList();
            for (Citas citasListOrphanCheckCitas : citasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Mascotas (" + mascotas + ") cannot be destroyed since the Citas " + citasListOrphanCheckCitas + " in its citasList field has a non-nullable idMascota field.");
            }
            List<HistoriasClinicas> historiasClinicasListOrphanCheck = mascotas.getHistoriasClinicasList();
            for (HistoriasClinicas historiasClinicasListOrphanCheckHistoriasClinicas : historiasClinicasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Mascotas (" + mascotas + ") cannot be destroyed since the HistoriasClinicas " + historiasClinicasListOrphanCheckHistoriasClinicas + " in its historiasClinicasList field has a non-nullable idMascota field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Personas idPropietario = mascotas.getIdPropietario();
            if (idPropietario != null) {
                idPropietario.getMascotasList().remove(mascotas);
                idPropietario = em.merge(idPropietario);
            }
            Razas idRaza = mascotas.getIdRaza();
            if (idRaza != null) {
                idRaza.getMascotasList().remove(mascotas);
                idRaza = em.merge(idRaza);
            }
            Sexos idSexo = mascotas.getIdSexo();
            if (idSexo != null) {
                idSexo.getMascotasList().remove(mascotas);
                idSexo = em.merge(idSexo);
            }
            em.remove(mascotas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Mascotas> findMascotasEntities() {
        return findMascotasEntities(true, -1, -1);
    }

    public List<Mascotas> findMascotasEntities(int maxResults, int firstResult) {
        return findMascotasEntities(false, maxResults, firstResult);
    }

    private List<Mascotas> findMascotasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Mascotas.class));
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

    public Mascotas findMascotas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Mascotas.class, id);
        } finally {
            em.close();
        }
    }

    public int getMascotasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Mascotas> rt = cq.from(Mascotas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
