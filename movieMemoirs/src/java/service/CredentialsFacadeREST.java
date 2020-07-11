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
import restMem.Credentials;
import restMem.Person;

/**
 *
 * @author xnxxxnxx
 */
@Stateless
@Path("restmem.credentials")
public class CredentialsFacadeREST extends AbstractFacade<Credentials> {

    @PersistenceContext(unitName = "movieMemoirsPU")
    private EntityManager em;

    public CredentialsFacadeREST() {
        super(Credentials.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Credentials entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, Credentials entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Credentials find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credentials> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credentials> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
     * create new user credential information
     * @param entity
     * @return 
     */
    @Path("/newCredential")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Credentials createPerson(Credentials entity){
        em.persist(entity);
        em.flush();
        return entity;
    }
    
    /**
     * Select by using user name.
     * @param username
     * @return 
     */
    @GET
    @Path ("findByUsername/{username}")
    @Produces({"application/json"})
        public List<Credentials> findByUsername(@PathParam("username") String username){
            Query query = em.createNamedQuery("Credentials.findByUsername");
            query.setParameter("username", username);
            return query.getResultList();
        }
    /**
     * Select by using password.
     * @param paswd
     * @return 
     */
    @GET
    @Path ("findByPaswd/{paswd}")
    @Produces({"application/json"})
        public List<Credentials> findByPaswd(@PathParam("paswd") String paswd){
            Query query = em.createNamedQuery("Credentials.findByPaswd");
            query.setParameter("paswd", paswd);
            return query.getResultList();
        }
    /**
     * Select by using sign up date.
     * @param signupDate
     * @return 
     */ 
    @GET
    @Path ("findBySignupDate/{signupDate}")
    @Produces({"application/json"})
        public List<Credentials> findBySignupDate(@PathParam("signupDate") Date signupDate){
            Query query = em.createNamedQuery("Credentials.findBySignupDate");
            query.setParameter("signupDate", signupDate);
            return query.getResultList();
        }
      
    @GET
    @Path("findByUsernameAndPassword/{username}/{paswd}")
    @Produces({"application/json"})
    public Person findByUsernameAndPassword(@PathParam("username") String username, @PathParam("paswd") String paswd) {
        /**
         * Select by combining address, postcode and state.
         */
        TypedQuery<Credentials> q = em.createQuery("SELECT c FROM Credentials c WHERE c.username = :username AND c.paswd=:paswd", Credentials.class);
        q.setParameter("username", username);
        q.setParameter("paswd", paswd);
        List<Credentials> result = q.getResultList();
        if(result.isEmpty())
        {
            return null;
        }
        return result.get(0).getPersonId();
    }
        

}
