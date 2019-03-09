/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.JPAcontrollers;

import co.com.bussineslm.JPAcontrollers.exceptions.NonexistentEntityException;
import co.com.bussineslm.entities.Citas;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.com.bussineslm.entities.EstadosCitas;
import co.com.bussineslm.entities.Mascotas;
import co.com.bussineslm.entities.Personas;
import co.com.bussineslm.entities.ServiciosPorEmpleados;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Isabel Medina
 */
public class CitasJpaController implements Serializable {

    public CitasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Citas citas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstadosCitas idEstado = citas.getIdEstado();
            if (idEstado != null) {
                idEstado = em.getReference(idEstado.getClass(), idEstado.getIdEstado());
                citas.setIdEstado(idEstado);
            }
            Mascotas idMascota = citas.getIdMascota();
            if (idMascota != null) {
                idMascota = em.getReference(idMascota.getClass(), idMascota.getIdMascota());
                citas.setIdMascota(idMascota);
            }
            Personas idPropietario = citas.getIdPropietario();
            if (idPropietario != null) {
                idPropietario = em.getReference(idPropietario.getClass(), idPropietario.getIdentificacion());
                citas.setIdPropietario(idPropietario);
            }
            ServiciosPorEmpleados idServicioPorEmpl = citas.getIdServicioPorEmpl();
            if (idServicioPorEmpl != null) {
                idServicioPorEmpl = em.getReference(idServicioPorEmpl.getClass(), idServicioPorEmpl.getIdServicioEmpl());
                citas.setIdServicioPorEmpl(idServicioPorEmpl);
            }
            em.persist(citas);
            if (idEstado != null) {
                idEstado.getCitasList().add(citas);
                idEstado = em.merge(idEstado);
            }
            if (idMascota != null) {
                idMascota.getCitasList().add(citas);
                idMascota = em.merge(idMascota);
            }
            if (idPropietario != null) {
                idPropietario.getCitasList().add(citas);
                idPropietario = em.merge(idPropietario);
            }
            if (idServicioPorEmpl != null) {
                idServicioPorEmpl.getCitasList().add(citas);
                idServicioPorEmpl = em.merge(idServicioPorEmpl);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Citas citas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Citas persistentCitas = em.find(Citas.class, citas.getIdCita());
            EstadosCitas idEstadoOld = persistentCitas.getIdEstado();
            EstadosCitas idEstadoNew = citas.getIdEstado();
            Mascotas idMascotaOld = persistentCitas.getIdMascota();
            Mascotas idMascotaNew = citas.getIdMascota();
            Personas idPropietarioOld = persistentCitas.getIdPropietario();
            Personas idPropietarioNew = citas.getIdPropietario();
            ServiciosPorEmpleados idServicioPorEmplOld = persistentCitas.getIdServicioPorEmpl();
            ServiciosPorEmpleados idServicioPorEmplNew = citas.getIdServicioPorEmpl();
            if (idEstadoNew != null) {
                idEstadoNew = em.getReference(idEstadoNew.getClass(), idEstadoNew.getIdEstado());
                citas.setIdEstado(idEstadoNew);
            }
            if (idMascotaNew != null) {
                idMascotaNew = em.getReference(idMascotaNew.getClass(), idMascotaNew.getIdMascota());
                citas.setIdMascota(idMascotaNew);
            }
            if (idPropietarioNew != null) {
                idPropietarioNew = em.getReference(idPropietarioNew.getClass(), idPropietarioNew.getIdentificacion());
                citas.setIdPropietario(idPropietarioNew);
            }
            if (idServicioPorEmplNew != null) {
                idServicioPorEmplNew = em.getReference(idServicioPorEmplNew.getClass(), idServicioPorEmplNew.getIdServicioEmpl());
                citas.setIdServicioPorEmpl(idServicioPorEmplNew);
            }
            citas = em.merge(citas);
            if (idEstadoOld != null && !idEstadoOld.equals(idEstadoNew)) {
                idEstadoOld.getCitasList().remove(citas);
                idEstadoOld = em.merge(idEstadoOld);
            }
            if (idEstadoNew != null && !idEstadoNew.equals(idEstadoOld)) {
                idEstadoNew.getCitasList().add(citas);
                idEstadoNew = em.merge(idEstadoNew);
            }
            if (idMascotaOld != null && !idMascotaOld.equals(idMascotaNew)) {
                idMascotaOld.getCitasList().remove(citas);
                idMascotaOld = em.merge(idMascotaOld);
            }
            if (idMascotaNew != null && !idMascotaNew.equals(idMascotaOld)) {
                idMascotaNew.getCitasList().add(citas);
                idMascotaNew = em.merge(idMascotaNew);
            }
            if (idPropietarioOld != null && !idPropietarioOld.equals(idPropietarioNew)) {
                idPropietarioOld.getCitasList().remove(citas);
                idPropietarioOld = em.merge(idPropietarioOld);
            }
            if (idPropietarioNew != null && !idPropietarioNew.equals(idPropietarioOld)) {
                idPropietarioNew.getCitasList().add(citas);
                idPropietarioNew = em.merge(idPropietarioNew);
            }
            if (idServicioPorEmplOld != null && !idServicioPorEmplOld.equals(idServicioPorEmplNew)) {
                idServicioPorEmplOld.getCitasList().remove(citas);
                idServicioPorEmplOld = em.merge(idServicioPorEmplOld);
            }
            if (idServicioPorEmplNew != null && !idServicioPorEmplNew.equals(idServicioPorEmplOld)) {
                idServicioPorEmplNew.getCitasList().add(citas);
                idServicioPorEmplNew = em.merge(idServicioPorEmplNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = citas.getIdCita();
                if (findCitas(id) == null) {
                    throw new NonexistentEntityException("The citas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Citas citas;
            try {
                citas = em.getReference(Citas.class, id);
                citas.getIdCita();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The citas with id " + id + " no longer exists.", enfe);
            }
            EstadosCitas idEstado = citas.getIdEstado();
            if (idEstado != null) {
                idEstado.getCitasList().remove(citas);
                idEstado = em.merge(idEstado);
            }
            Mascotas idMascota = citas.getIdMascota();
            if (idMascota != null) {
                idMascota.getCitasList().remove(citas);
                idMascota = em.merge(idMascota);
            }
            Personas idPropietario = citas.getIdPropietario();
            if (idPropietario != null) {
                idPropietario.getCitasList().remove(citas);
                idPropietario = em.merge(idPropietario);
            }
            ServiciosPorEmpleados idServicioPorEmpl = citas.getIdServicioPorEmpl();
            if (idServicioPorEmpl != null) {
                idServicioPorEmpl.getCitasList().remove(citas);
                idServicioPorEmpl = em.merge(idServicioPorEmpl);
            }
            em.remove(citas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Citas> findCitasEntities() {
        return findCitasEntities(true, -1, -1);
    }

    public List<Citas> findCitasEntities(int maxResults, int firstResult) {
        return findCitasEntities(false, maxResults, firstResult);
    }

    private List<Citas> findCitasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Citas.class));
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

    public Citas findCitas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Citas.class, id);
        } finally {
            em.close();
        }
    }

    public int getCitasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Citas> rt = cq.from(Citas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
