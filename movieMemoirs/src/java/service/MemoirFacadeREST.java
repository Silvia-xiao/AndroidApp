/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
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
import restMem.Memoir;

/**
 *
 * @author xnxxxnxx
 */
@Stateless
@Path("restmem.memoir")
public class MemoirFacadeREST extends AbstractFacade<Memoir> {

    @PersistenceContext(unitName = "movieMemoirsPU")
    private EntityManager em;

    public MemoirFacadeREST() {
        super(Memoir.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Memoir entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Memoir entity) {
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
    public Memoir find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Memoir> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Memoir> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
     * Select by using movie name.
     * @param movieName
     * @return 
     */
    @GET
    @Path ("findByMovieName/{movieName}")
        @Produces({"application/json"})
        public List<Memoir> findByMovieName(@PathParam("movieName") String movieName){
            Query query = em.createNamedQuery("Memoir.findByMovieName");
            query.setParameter("movieName", movieName);
            return query.getResultList();
        }
    /**
     * Select by using movie release date.
     * @param movieReleaseDate
     * @return 
     */    
    @GET
    @Path ("findByMovieReleaseDate/{movieReleaseDate}")
        @Produces({"application/json"})
        public List<Memoir> findByMovieReleaseDate(@PathParam("movieReleaseDate") Date movieReleaseDate){
            Query query = em.createNamedQuery("Memoir.findByMovieReleaseDate");
            query.setParameter("movieReleaseDate", movieReleaseDate);
            return query.getResultList();
        }
    /**
     * Select by using user watched date and time.
     * @param userWatchDateTime
     * @return 
     */    
    @GET
    @Path ("findByUserWatchDateTime/{userWatchDateTime}")
        @Produces({"application/json"})
        public List<Memoir> findByUserWatchDateTime(@PathParam("userWatchDateTime") Timestamp userWatchDateTime){
            Query query = em.createNamedQuery("Memoir.findByUserWatchDateTime");
            query.setParameter("userWatchDateTime", userWatchDateTime);
            return query.getResultList();
        }
        
    /**
     * Select by using user watched date and time.
     * @param userWatchDateTime
     * @return 
     */      
    @GET
    @Path ("findByUserComment/{userComment}")
        @Produces({"application/json"})
        public List<Memoir> findByUserComment(@PathParam("userComment") String userComment){
            Query query = em.createNamedQuery("Memoir.findByUserComment");
            query.setParameter("userComment", userComment);
            return query.getResultList();
        }
    /**
     * Select by movie rating score.
     * @param score
     * @return 
     */  
    @GET
    @Path ("findByScore/{score}")
        @Produces({"application/json"})
        public List<Memoir> findByScore(@PathParam("score") double score){
            Query query = em.createNamedQuery("Memoir.findByScore");
            query.setParameter("score", score);
            return query.getResultList();
        }
    
    ///3.(c)
    /**
     * Select by combining memoir id and cinema id.
     * @param movieName
     * @param cinemaId
     * @return 
     */
    @GET
    @Path("findByMemoirAndCinemaId/{movieName}/{cinemaId}")
    @Produces({"application/json"})
    public List<Memoir> findByMemoirAndCinemaId(@PathParam("movieName") String movieName, @PathParam("cinemaId") int cinemaId) {
        TypedQuery<Memoir> q = em.createQuery("SELECT m FROM Memoir m WHERE m.cinemaId.cinemaId=:cinemaId AND m.movieName=:movieName", Memoir.class);
        q.setParameter("movieName", movieName);
        q.setParameter("cinemaId", cinemaId);
        return q.getResultList();
    }
   
    ///3.(d)
    /**
     * Select by combining movie name and cinema name.
     * @param movieName
     * @param cinemaName
     * @return 
     */
    @GET
    @Path ("findByMovieNameAndCinemaName/{movieName}/{cinemaName}")
        @Produces({"application/json"})
        public List<Memoir> findByMovieNameAndCinemaName(@PathParam("movieName") String movieName, @PathParam("cinemaName") String cinemaName){
            Query query = em.createNamedQuery("Memoir.findByMovieNameAndCinemaName");
            query.setParameter("movieName", movieName);
            query.setParameter("cinemaName", cinemaName);
            return query.getResultList();
        }

    //Task 4
    ///a
    /**
     * Select movies watched between a period.
     * @param personId
     * @param startDate
     * @param endDate
     * @return 
     */
    @GET
    @Path("movieWatchedInPeriod/{personId}/{startDate}/{endDate}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object movieWatchedInPeriod(@PathParam("personId") int personId, @PathParam("startDate") Timestamp startDate
            , @PathParam("endDate") Timestamp endDate) 
    {
        Query query = em.createQuery("SELECT m.cinemaId.location,count(m.cinemaId.location) FROM Memoir m " 
                + "WHERE m.personId.personId=:personId AND m.userWatchDateTime BETWEEN :startDate AND :endDate GROUP BY m.cinemaId.location",Object[].class);
        query.setParameter("personId", personId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        List<Object[]> queryList = query.getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) 
        {
            JsonObject memoirObject = Json.createObjectBuilder().
            add("location", (String)row[0])
            .add("count", (long)row[1]).build();
            arrayBuilder.add(memoirObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
   
    //b
        /**
     * Shows in each months, how many movies have been watched by user.
     * @param personId
     * @param year
     * @return 
     */
    @GET
    @Path("MovieWatchedPerMonth/{personId}/{year}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object MovieWatchedPerMonth(@PathParam("personId") int personId, @PathParam("year") int year) 
    {
        Query query = em.createQuery("SELECT EXTRACT(MONTH FROM m.userWatchDateTime),count(EXTRACT(MONTH FROM m.userWatchDateTime)) FROM Memoir m" 
                +" WHERE m.personId.personId = :personId AND EXTRACT(YEAR FROM m.userWatchDateTime)=:year " +"GROUP BY EXTRACT(MONTH FROM m.userWatchDateTime)",Object[].class);
        query.setParameter("personId", personId);
        query.setParameter("year", year);
        List<Object[]> queryList = query.getResultList();
        /**
         * Use DateFormatSymbols to transfer the number of month to the English month short name.
         */
        DateFormatSymbols dfs = new DateFormatSymbols();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) 
        {
            JsonObject memoirObject = Json.createObjectBuilder().
            add("Month Name", (Integer)row[0])
            .add("count", (long)row[1]).build();
            arrayBuilder.add(memoirObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
 
    //c
        /**
     * Find the highest score of the movies marked by user.
     * @param personId
     * @return 
     */    
    @GET
    @Path("FindHighestScoreOfPerson/{personId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object FindHighestScoreOfPerson(@PathParam("personId") int personId) 
    {
        Query query = em.createQuery("SELECT m.movieName,m.score,m.movieReleaseDate FROM Memoir m "
                +"WHERE m.personId.personId = :personId AND m.score = (SELECT max(m.score) FROM Memoir m WHERE m.personId.personId = :personId)",Object[].class);
        query.setParameter("personId", personId);
        //Create SimpleDateFormat object
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Object[]> queryList = query.getResultList(); 
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) 
        {
            JsonObject memoirObject = Json.createObjectBuilder().
            add("movieName", (String)row[0])
            .add("score", (double)row[1])
            .add("movieReleaseDate", (String) format.format(row[2])).build();
            arrayBuilder.add(memoirObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
    //d
        /**
     * Find the movie that the release year is same with user watch year.
     * @param personId
     * @return 
     */
    @GET
    @Path("findSameYearReleaseWatch/{personId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object findSameYearReleaseWatch(@PathParam("personId") int personId) 
    {
        /**
         * Only select the year of the release date not the whole datetime.
         */
        Query query = em.createQuery("SELECT m.movieName,EXTRACT(YEAR FROM m.movieReleaseDate) FROM Memoir m "
                +"WHERE m.personId.personId = :personId AND EXTRACT(YEAR FROM m.movieReleaseDate) = EXTRACT(YEAR FROM m.userWatchDateTime)",Object[].class);
        query.setParameter("personId", personId);
        List<Object[]> queryList = query.getResultList(); 
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) 
        {
            JsonObject memoirObject = Json.createObjectBuilder().
            add("movieName", (String)row[0])
            .add("ReleaseYear", (String)row[1].toString()).build();
            arrayBuilder.add(memoirObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;

    }
   
    //e
    /**
     * Find the remake movies that the user watched.
     * @param personId
     * @return 
     */
    @GET
    @Path("findWatchedRemakeMovies/{personId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object findWatchedRemakeMovies(@PathParam("personId") int personId) 
    {
        /**
         * Only select the year of the release date not the whole datetime.
         */
        Query query = em.createQuery("SELECT m.movieName,EXTRACT(YEAR FROM m.movieReleaseDate) FROM Memoir m WHERE m.movieName IN "
                +"(SELECT m.movieName FROM Memoir m GROUP BY m.movieName HAVING count(distinct(m.movieReleaseDate))>1) "
                + "AND m.personId.personId = :personId GROUP BY m.movieName, m.movieReleaseDate",Object[].class);
        query.setParameter("personId", personId);
        List<Object[]> queryList = query.getResultList(); 
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) 
        {
            JsonObject memoirObject = Json.createObjectBuilder().
            add("movieName", (String)row[0])
            .add("ReleaseYear", (String)row[1].toString()).build();
            arrayBuilder.add(memoirObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;

    }
   
    //f
    /**
     * Find the top 5 movies in user heart in the recent year.
     * @param personId
     * @return 
     */    
    @GET
    @Path("top5Movies/{personId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object top5Movies(@PathParam("personId") int personId )
    {
        //Create SimpleDateFormat object
        Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) -1);
        //Create SimpleDateFormat object
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Query q = em.createQuery("SELECT m.movieName,m.movieReleaseDate,m.score FROM Memoir m" 
                + " WHERE m.personId.personId=:personId AND EXTRACT(YEAR FROM m.movieReleaseDate) = :currentYear"
                + " ORDER BY m.score DESC",Object[].class);
        q.setParameter("personId", personId);
        /**
         * Get the year now.
         */
        q.setParameter("currentYear", calendar.get(Calendar.YEAR));
        q.setMaxResults(5);
        List<Object[]> queryList = q.getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) 
        {
            JsonObject personObject = Json.createObjectBuilder().
            add("movieName", (String) row[0])
            .add("movieReleaseDate", (String) format.format(row[1]))
            .add("score",(double) row[2]).build();
            arrayBuilder.add(personObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
    
    /**
     * Select by combining memoir id and cinema id.
     * @param personId
     * @return 
     */
    @GET
    @Path("findByPersonId/{personId}")
    @Produces({"application/json"})
    public List<Memoir> findByPersonId(@PathParam("personId") int personId) {
        TypedQuery<Memoir> q = em.createQuery("SELECT m FROM Memoir m WHERE m.personId.personId=:personId", Memoir.class);
        q.setParameter("personId", personId);
        return q.getResultList();
    }

    
}
