package projekti;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
    private byte[] imageContent;
    @ManyToOne
    private WepaTweetter owner;
    private String description;
    private String mediaType;
}
