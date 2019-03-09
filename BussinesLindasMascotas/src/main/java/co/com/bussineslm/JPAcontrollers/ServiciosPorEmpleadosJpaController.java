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
import co.com.bussineslm.entities.Servicios;
import co.com.bussineslm.entities.Citas;
import java.util.ArrayList;
import java.util.List;
import co.com.bussineslm.entities.HistoriasClinicas;
import co.com.bussineslm.entities.ServiciosPorEmpleados;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class ServiciosPorEmpleadosJpaController implements Serializable {

    public ServiciosPorEmpleadosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ServiciosPorEmpleados serviciosPorEmpleados) throws IllegalOrphanException {
        if (serviciosPorEmpleados.getCitasList() == null) {
            serviciosPorEmpleados.setCitasList(new ArrayList<Citas>());
        }
        if (serviciosPorEmpleados.getHistoriasClinicasList() == null) {
            serviciosPorEmpleados.setHistoriasClinicasList(new ArrayList<HistoriasClinicas>());
        }
        List<String> illegalOrphanMessages = null;
        InformacionLaboral idPersonaLabOrphanCheck = serviciosPorEmpleados.getIdPersonaLab();
        if (idPersonaLabOrphanCheck != null) {
            ServiciosPorEmpleados oldServiciosPorEmpleadosOfIdPersonaLab = idPersonaLabOrphanCheck.getServiciosPorEmpleados();
            if (oldServiciosPorEmpleadosOfIdPersonaLab != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The InformacionLaboral " + idPersonaLabOrphanCheck + " already has an item of type ServiciosPorEmpleados whose idPersonaLab column cannot be null. Please make another selection for the idPersonaLab field.");
            }
        }
        Servicios idServicioOrphanCheck = serviciosPorEmpleados.getIdServicio();
        if (idServicioOrphanCheck != null) {
            ServiciosPorEmpleados oldServiciosPorEmpleadosOfIdServicio = idServicioOrphanCheck.getServiciosPorEmpleados();
            if (oldServiciosPorEmpleadosOfIdServicio != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Servicios " + idServicioOrphanCheck + " already has an item of type ServiciosPorEmpleados whose idServicio column cannot be null. Please make another selection for the idServicio field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InformacionLaboral idPersonaLab = serviciosPorEmpleados.getIdPersonaLab();
            if (idPersonaLab != null) {
                idPersonaLab = em.getReference(idPersonaLab.getClass(), idPersonaLab.getIdPersona());
                serviciosPorEmpleados.setIdPersonaLab(idPersonaLab);
            }
            Servicios idServicio = serviciosPorEmpleados.getIdServicio();
            if (idServicio != null) {
                idServicio = em.getReference(idServicio.getClass(), idServicio.getIdServicio());
                serviciosPorEmpleados.setIdServicio(idServicio);
            }
            List<Citas> attachedCitasList = new ArrayList<Citas>();
            for (Citas citasListCitasToAttach : serviciosPorEmpleados.getCitasList()) {
                citasListCitasToAttach = em.getReference(citasListCitasToAttach.getClass(), citasListCitasToAttach.getIdCita());
                attachedCitasList.add(citasListCitasToAttach);
            }
            serviciosPorEmpleados.setCitasList(attachedCitasList);
            List<HistoriasClinicas> attachedHistoriasClinicasList = new ArrayList<HistoriasClinicas>();
            for (HistoriasClinicas historiasClinicasListHistoriasClinicasToAttach : serviciosPorEmpleados.getHistoriasClinicasList()) {
                historiasClinicasListHistoriasClinicasToAttach = em.getReference(historiasClinicasListHistoriasClinicasToAttach.getClass(), historiasClinicasListHistoriasClinicasToAttach.getIdProcedimiento());
                attachedHistoriasClinicasList.add(historiasClinicasListHistoriasClinicasToAttach);
            }
            serviciosPorEmpleados.setHistoriasClinicasList(attachedHistoriasClinicasList);
            em.persist(serviciosPorEmpleados);
            if (idPersonaLab != null) {
                idPersonaLab.setServiciosPorEmpleados(serviciosPorEmpleados);
                idPersonaLab = em.merge(idPersonaLab);
            }
            if (idServicio != null) {
                idServicio.setServiciosPorEmpleados(serviciosPorEmpleados);
                idServicio = em.merge(idServicio);
            }
            for (Citas citasListCitas : serviciosPorEmpleados.getCitasList()) {
                ServiciosPorEmpleados oldIdServicioPorEmplOfCitasListCitas = citasListCitas.getIdServicioPorEmpl();
                citasListCitas.setIdServicioPorEmpl(serviciosPorEmpleados);
                citasListCitas = em.merge(citasListCitas);
                if (oldIdServicioPorEmplOfCitasListCitas != null) {
                    oldIdServicioPorEmplOfCitasListCitas.getCitasList().remove(citasListCitas);
                    oldIdServicioPorEmplOfCitasListCitas = em.merge(oldIdServicioPorEmplOfCitasListCitas);
                }
            }
            for (HistoriasClinicas historiasClinicasListHistoriasClinicas : serviciosPorEmpleados.getHistoriasClinicasList()) {
                ServiciosPorEmpleados oldIdServicioPorEmplOfHistoriasClinicasListHistoriasClinicas = historiasClinicasListHistoriasClinicas.getIdServicioPorEmpl();
                historiasClinicasListHistoriasClinicas.setIdServicioPorEmpl(serviciosPorEmpleados);
                historiasClinicasListHistoriasClinicas = em.merge(historiasClinicasListHistoriasClinicas);
                if (oldIdServicioPorEmplOfHistoriasClinicasListHistoriasClinicas != null) {
                    oldIdServicioPorEmplOfHistoriasClinicasListHistoriasClinicas.getHistoriasClinicasList().remove(historiasClinicasListHistoriasClinicas);
                    oldIdServicioPorEmplOfHistoriasClinicasListHistoriasClinicas = em.merge(oldIdServicioPorEmplOfHistoriasClinicasListHistoriasClinicas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ServiciosPorEmpleados serviciosPorEmpleados) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ServiciosPorEmpleados persistentServiciosPorEmpleados = em.find(ServiciosPorEmpleados.class, serviciosPorEmpleados.getIdServicioEmpl());
            InformacionLaboral idPersonaLabOld = persistentServiciosPorEmpleados.getIdPersonaLab();
            InformacionLaboral idPersonaLabNew = serviciosPorEmpleados.getIdPersonaLab();
            Servicios idServicioOld = persistentServiciosPorEmpleados.getIdServicio();
            Servicios idServicioNew = serviciosPorEmpleados.getIdServicio();
            List<Citas> citasListOld = persistentServiciosPorEmpleados.getCitasList();
            List<Citas> citasListNew = serviciosPorEmpleados.getCitasList();
            List<HistoriasClinicas> historiasClinicasListOld = persistentServiciosPorEmpleados.getHistoriasClinicasList();
            List<HistoriasClinicas> historiasClinicasListNew = serviciosPorEmpleados.getHistoriasClinicasList();
            List<String> illegalOrphanMessages = null;
            if (idPersonaLabNew != null && !idPersonaLabNew.equals(idPersonaLabOld)) {
                ServiciosPorEmpleados oldServiciosPorEmpleadosOfIdPersonaLab = idPersonaLabNew.getServiciosPorEmpleados();
                if (oldServiciosPorEmpleadosOfIdPersonaLab != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The InformacionLaboral " + idPersonaLabNew + " already has an item of type ServiciosPorEmpleados whose idPersonaLab column cannot be null. Please make another selection for the idPersonaLab field.");
                }
            }
            if (idServicioNew != null && !idServicioNew.equals(idServicioOld)) {
                ServiciosPorEmpleados oldServiciosPorEmpleadosOfIdServicio = idServicioNew.getServiciosPorEmpleados();
                if (oldServiciosPorEmpleadosOfIdServicio != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Servicios " + idServicioNew + " already has an item of type ServiciosPorEmpleados whose idServicio column cannot be null. Please make another selection for the idServicio field.");
                }
            }
            for (Citas citasListOldCitas : citasListOld) {
                if (!citasListNew.contains(citasListOldCitas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Citas " + citasListOldCitas + " since its idServicioPorEmpl field is not nullable.");
                }
            }
            for (HistoriasClinicas historiasClinicasListOldHistoriasClinicas : historiasClinicasListOld) {
                if (!historiasClinicasListNew.contains(historiasClinicasListOldHistoriasClinicas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HistoriasClinicas " + historiasClinicasListOldHistoriasClinicas + " since its idServicioPorEmpl field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPersonaLabNew != null) {
                idPersonaLabNew = em.getReference(idPersonaLabNew.getClass(), idPersonaLabNew.getIdPersona());
                serviciosPorEmpleados.setIdPersonaLab(idPersonaLabNew);
            }
            if (idServicioNew != null) {
                idServicioNew = em.getReference(idServicioNew.getClass(), idServicioNew.getIdServicio());
                serviciosPorEmpleados.setIdServicio(idServicioNew);
            }
            List<Citas> attachedCitasListNew = new ArrayList<Citas>();
            for (Citas citasListNewCitasToAttach : citasListNew) {
                citasListNewCitasToAttach = em.getReference(citasListNewCitasToAttach.getClass(), citasListNewCitasToAttach.getIdCita());
                attachedCitasListNew.add(citasListNewCitasToAttach);
            }
            citasListNew = attachedCitasListNew;
            serviciosPorEmpleados.setCitasList(citasListNew);
            List<HistoriasClinicas> attachedHistoriasClinicasListNew = new ArrayList<HistoriasClinicas>();
            for (HistoriasClinicas historiasClinicasListNewHistoriasClinicasToAttach : historiasClinicasListNew) {
                historiasClinicasListNewHistoriasClinicasToAttach = em.getReference(historiasClinicasListNewHistoriasClinicasToAttach.getClass(), historiasClinicasListNewHistoriasClinicasToAttach.getIdProcedimiento());
                attachedHistoriasClinicasListNew.add(historiasClinicasListNewHistoriasClinicasToAttach);
            }
            historiasClinicasListNew = attachedHistoriasClinicasListNew;
            serviciosPorEmpleados.setHistoriasClinicasList(historiasClinicasListNew);
            serviciosPorEmpleados = em.merge(serviciosPorEmpleados);
            if (idPersonaLabOld != null && !idPersonaLabOld.equals(idPersonaLabNew)) {
                idPersonaLabOld.setServiciosPorEmpleados(null);
                idPersonaLabOld = em.merge(idPersonaLabOld);
            }
            if (idPersonaLabNew != null && !idPersonaLabNew.equals(idPersonaLabOld)) {
                idPersonaLabNew.setServiciosPorEmpleados(serviciosPorEmpleados);
                idPersonaLabNew = em.merge(idPersonaLabNew);
            }
            if (idServicioOld != null && !idServicioOld.equals(idServicioNew)) {
                idServicioOld.setServiciosPorEmpleados(null);
                idServicioOld = em.merge(idServicioOld);
            }
            if (idServicioNew != null && !idServicioNew.equals(idServicioOld)) {
                idServicioNew.setServiciosPorEmpleados(serviciosPorEmpleados);
                idServicioNew = em.merge(idServicioNew);
            }
            for (Citas citasListNewCitas : citasListNew) {
                if (!citasListOld.contains(citasListNewCitas)) {
                    ServiciosPorEmpleados oldIdServicioPorEmplOfCitasListNewCitas = citasListNewCitas.getIdServicioPorEmpl();
                    citasListNewCitas.setIdServicioPorEmpl(serviciosPorEmpleados);
                    citasListNewCitas = em.merge(citasListNewCitas);
                    if (oldIdServicioPorEmplOfCitasListNewCitas != null && !oldIdServicioPorEmplOfCitasListNewCitas.equals(serviciosPorEmpleados)) {
                        oldIdServicioPorEmplOfCitasListNewCitas.getCitasList().remove(citasListNewCitas);
                        oldIdServicioPorEmplOfCitasListNewCitas = em.merge(oldIdServicioPorEmplOfCitasListNewCitas);
                    }
                }
            }
            for (HistoriasClinicas historiasClinicasListNewHistoriasClinicas : historiasClinicasListNew) {
                if (!historiasClinicasListOld.contains(historiasClinicasListNewHistoriasClinicas)) {
                    ServiciosPorEmpleados oldIdServicioPorEmplOfHistoriasClinicasListNewHistoriasClinicas = historiasClinicasListNewHistoriasClinicas.getIdServicioPorEmpl();
                    historiasClinicasListNewHistoriasClinicas.setIdServicioPorEmpl(serviciosPorEmpleados);
                    historiasClinicasListNewHistoriasClinicas = em.merge(historiasClinicasListNewHistoriasClinicas);
                    if (oldIdServicioPorEmplOfHistoriasClinicasListNewHistoriasClinicas != null && !oldIdServicioPorEmplOfHistoriasClinicasListNewHistoriasClinicas.equals(serviciosPorEmpleados)) {
                        oldIdServicioPorEmplOfHistoriasClinicasListNewHistoriasClinicas.getHistoriasClinicasList().remove(historiasClinicasListNewHistoriasClinicas);
                        oldIdServicioPorEmplOfHistoriasClinicasListNewHistoriasClinicas = em.merge(oldIdServicioPorEmplOfHistoriasClinicasListNewHistoriasClinicas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = serviciosPorEmpleados.getIdServicioEmpl();
                if (findServiciosPorEmpleados(id) == null) {
                    throw new NonexistentEntityException("The serviciosPorEmpleados with id " + id + " no longer exists.");
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
            ServiciosPorEmpleados serviciosPorEmpleados;
            try {
                serviciosPorEmpleados = em.getReference(ServiciosPorEmpleados.class, id);
                serviciosPorEmpleados.getIdServicioEmpl();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The serviciosPorEmpleados with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Citas> citasListOrphanCheck = serviciosPorEmpleados.getCitasList();
            for (Citas citasListOrphanCheckCitas : citasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ServiciosPorEmpleados (" + serviciosPorEmpleados + ") cannot be destroyed since the Citas " + citasListOrphanCheckCitas + " in its citasList field has a non-nullable idServicioPorEmpl field.");
            }
            List<HistoriasClinicas> historiasClinicasListOrphanCheck = serviciosPorEmpleados.getHistoriasClinicasList();
            for (HistoriasClinicas historiasClinicasListOrphanCheckHistoriasClinicas : historiasClinicasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ServiciosPorEmpleados (" + serviciosPorEmpleados + ") cannot be destroyed since the HistoriasClinicas " + historiasClinicasListOrphanCheckHistoriasClinicas + " in its historiasClinicasList field has a non-nullable idServicioPorEmpl field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            InformacionLaboral idPersonaLab = serviciosPorEmpleados.getIdPersonaLab();
            if (idPersonaLab != null) {
                idPersonaLab.setServiciosPorEmpleados(null);
                idPersonaLab = em.merge(idPersonaLab);
            }
            Servicios idServicio = serviciosPorEmpleados.getIdServicio();
            if (idServicio != null) {
                idServicio.setServiciosPorEmpleados(null);
                idServicio = em.merge(idServicio);
            }
            em.remove(serviciosPorEmpleados);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ServiciosPorEmpleados> findServiciosPorEmpleadosEntities() {
        return findServiciosPorEmpleadosEntities(true, -1, -1);
    }

    public List<ServiciosPorEmpleados> findServiciosPorEmpleadosEntities(int maxResults, int firstResult) {
        return findServiciosPorEmpleadosEntities(false, maxResults, firstResult);
    }

    private List<ServiciosPorEmpleados> findServiciosPorEmpleadosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ServiciosPorEmpleados.class));
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

    public ServiciosPorEmpleados findServiciosPorEmpleados(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ServiciosPorEmpleados.class, id);
        } finally {
            em.close();
        }
    }

    public int getServiciosPorEmpleadosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ServiciosPorEmpleados> rt = cq.from(ServiciosPorEmpleados.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
