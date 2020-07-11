/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.sql.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import restMem.Person;

/**
 *
 * @author xnxxxnxx
 */
@Stateless
@Path("restmem.person")
public class PersonFacadeREST extends AbstractFacade<Person> {

    @PersistenceContext(unitName = "movieMemoirsPU")
    private EntityManager em;

    public PersonFacadeREST() {
        super(Person.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Person entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Person entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Person find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Person> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Person> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    /**
     * Create new user
     * @param entity
     * @return 
     */
    @Path ("/newPerson")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Person createPerson(Person entity){
        em.persist(entity);
        em.flush();
        return entity;
    }
    
     /**
     * Select by using first name.
     * @param firstname
     * @return 
     */
    @GET
    @Path ("findByFirstname/{firstname}")
        @Produces({"application/json"})
        public List<Person> findByFirstname(@PathParam("firstname") String firstname){
            Query query = em.createNamedQuery("Person.findByFirstname");
            query.setParameter("firstname", firstname);
            return query.getResultList();
        }

    /**
     * Select by using surname.
     * @param surname
     * @return 
     */    
    @GET
    @Path ("findBySurname/{surname}")
        @Produces({"application/json"})
        public List<Person> findBySurname(@PathParam("surname") String surname){
            Query query = em.createNamedQuery("Person.findBySurname");
            query.setParameter("surname", surname);
            return query.getResultList();
        }
    /**
     * Select by using gender.
     * @param gender
     * @return 
     */
    @GET
    @Path ("findByGender/{gender}")
        @Produces({"application/json"})
        public List<Person> findByGender(@PathParam("gender") String gender){
            Query query = em.createNamedQuery("Person.findByGender");
            query.setParameter("gender", gender);
            return query.getResultList();
        }
/**
     * Select by using date of birth.
     * @param dob
     * @return 
     */
    @GET
    @Path ("findByDob/{dob}")
        @Produces({"application/json"})
        public List<Person> findByDob(@PathParam("dob") Date dob){
            Query query = em.createNamedQuery("Person.findByDob");
            query.setParameter("dob", dob);
            return query.getResultList();
        }
    /**
     * Select by using address.
     * @param address
     * @return 
     */
    @GET
    @Path ("findByAddress/{address}")
        @Produces({"application/json"})
        public List<Person> findByAddress(@PathParam("address") String address){
            Query query = em.createNamedQuery("Person.findByAddress");
            query.setParameter("address", address);
            return query.getResultList();
        }
    /**
     * Select by using state.
     * @param state
     * @return 
     */
    @GET
    @Path ("findByState/{state}")
        @Produces({"application/json"})
        public List<Person> findByState(@PathParam("state") String state){
            Query query = em.createNamedQuery("Person.findByState");
            query.setParameter("state", state);
            return query.getResultList();
        }
/**
     * Select by using postcode.
     * @param postcode
     * @return 
     */
    @GET
    @Path ("findByPostcode/{postcode}")
        @Produces({"application/json"})
        public List<Person> findByPostcode(@PathParam("postcode") int postcode){
            Query query = em.createNamedQuery("Person.findByPostcode");
            query.setParameter("postcode", postcode);
            return query.getResultList();
        }
    ///3.(b)
    /**
     * Select by combining address, postcode and state.
     * @param address
     * @param state
     * @param postcode
     * @return 
     */
    @GET
    @Path("findByAddressAndStateAndPostcode/{address}/{state}/{postcode}")
    @Produces({"application/json"})
    public List<Person> findByAddressAndStateAndPostcode(@PathParam("address") String address, @PathParam("state") String state, @PathParam("postcode") int postcode) {
        /**
         * Select by combining address, postcode and state.
         */
        TypedQuery<Person> q = em.createQuery("SELECT p FROM Person p WHERE p.address = :address AND p.state=:state AND p.postcode=:postcode", Person.class);
        q.setParameter("address", address);
        q.setParameter("state", state);
        q.setParameter("postcode", postcode);
        return q.getResultList();
    }

}
