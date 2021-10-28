package projekti;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author tspaanan
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class PublicImageObject extends AbstractPersistable<Long> {
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Type(type="org.hibernate.type.BinaryType") //tämä varmistaa toiminnan Herokussa, hajottaa H2-tietokantamoottorin!
    private byte[] imageContent;
    @ManyToOne
    private WepaTweetter owner;
    private String description;
    private String mediaType;
    @ManyToMany
    private List<WepaTweetter> likes;
    @OneToMany(mappedBy = "image")
    private List<WepaComment> comments;
}
