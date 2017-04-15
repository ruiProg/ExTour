package models;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;

@Entity
public class Estate extends Model {

    @Id
    @Column(length = 10)
    public String id;

    @Basic(optional = false)
    public String title;

    @ManyToOne()
    @Basic(optional = false)
    public Category category;

    @ManyToOne()
    @Basic(optional = false)
    public Parish parish;

    @Column(columnDefinition = "TEXT")
    public String details;

    public String imageURL;
    public String linkURL;

    @Column(length=40)
    public String coords;

    public static Finder<String,Estate> find = new Finder<>(Estate.class);

    public Estate(String pID, String pTitle, String pCategory, String parishID, String pDetails, String pImageURL, String pLinkURL, String pCoords){
        id = pID;
        title = pTitle;
        category = Category.find.byId(pCategory);
        parish = Parish.find.byId(parishID);
        details = pDetails;
        imageURL = pImageURL;
        linkURL = pLinkURL;
        coords = pCoords;
    }
}
