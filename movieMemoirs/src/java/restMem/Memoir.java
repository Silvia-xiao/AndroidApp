/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restMem;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author xnxxxnxx
 */
@Entity
@Table(name = "MEMOIR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Memoir.findAll", query = "SELECT m FROM Memoir m")
    , @NamedQuery(name = "Memoir.findByMemoirId", query = "SELECT m FROM Memoir m WHERE m.memoirId = :memoirId")
    , @NamedQuery(name = "Memoir.findByMovieName", query = "SELECT m FROM Memoir m WHERE m.movieName = :movieName")
    , @NamedQuery(name = "Memoir.findByMovieReleaseDate", query = "SELECT m FROM Memoir m WHERE m.movieReleaseDate = :movieReleaseDate")
    , @NamedQuery(name = "Memoir.findByUserWatchDateTime", query = "SELECT m FROM Memoir m WHERE m.userWatchDateTime = :userWatchDateTime")
    , @NamedQuery(name = "Memoir.findByUserComment", query = "SELECT m FROM Memoir m WHERE m.userComment = :userComment")
    , @NamedQuery(name = "Memoir.findByScore", query = "SELECT m FROM Memoir m WHERE m.score = :score")
    , @NamedQuery(name = "Memoir.findByMovieNameAndCinemaName", query = "SELECT m FROM Memoir m WHERE m.cinemaId.cinemaName=:cinemaName and m.movieName=:movieName")})
public class Memoir implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PUBLIC_RATING")
    private double publicRating;

    @Size(max = 500)
    @Column(name = "IMAGEURL")
    private String imageurl;

    @Size(max = 50)
    @Column(name = "OMDB")
    private String omdb;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MEMOIR_ID")
    private Integer memoirId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "MOVIE_NAME")
    private String movieName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MOVIE_RELEASE_DATE")
    @Temporal(TemporalType.DATE)
    private Date movieReleaseDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "USER_WATCH_DATE_TIME")
    @Temporal(TemporalType.DATE)
    private Date userWatchDateTime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "USER_COMMENT")
    private String userComment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SCORE")
    private double score;
    @JoinColumn(name = "CINEMA_ID", referencedColumnName = "CINEMA_ID")
    @ManyToOne(optional = false)
    private Cinema cinemaId;
    @JoinColumn(name = "PERSON_ID", referencedColumnName = "PERSON_ID")
    @ManyToOne(optional = false)
    private Person personId;

    public Memoir() {
    }

    public Memoir(Integer memoirId) {
        this.memoirId = memoirId;
    }

    public Memoir(Integer memoirId, String movieName, Date movieReleaseDate, Date userWatchDateTime, String userComment, double score) {
        this.memoirId = memoirId;
        this.movieName = movieName;
        this.movieReleaseDate = movieReleaseDate;
        this.userWatchDateTime = userWatchDateTime;
        this.userComment = userComment;
        this.score = score;
    }

    public Integer getMemoirId() {
        return memoirId;
    }

    public void setMemoirId(Integer memoirId) {
        this.memoirId = memoirId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Date getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(Date movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public Date getUserWatchDateTime() {
        return userWatchDateTime;
    }

    public void setUserWatchDateTime(Date userWatchDateTime) {
        this.userWatchDateTime = userWatchDateTime;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Cinema getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Cinema cinemaId) {
        this.cinemaId = cinemaId;
    }

    public Person getPersonId() {
        return personId;
    }

    public void setPersonId(Person personId) {
        this.personId = personId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (memoirId != null ? memoirId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Memoir)) {
            return false;
        }
        Memoir other = (Memoir) object;
        if ((this.memoirId == null && other.memoirId != null) || (this.memoirId != null && !this.memoirId.equals(other.memoirId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "restMem.Memoir[ memoirId=" + memoirId + " ]";
    }

    public String getOmdb() {
        return omdb;
    }

    public void setOmdb(String omdb) {
        this.omdb = omdb;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public double getPublicRating() {
        return publicRating;
    }

    public void setPublicRating(double publicRating) {
        this.publicRating = publicRating;
    }
    
}
