package projekti;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author tspaanan
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class WepaMessage extends AbstractPersistable<Long> {

    @ManyToOne
    private WepaTweetter tweetter;
    private LocalDateTime timestamp;
    private String messageContent;
    @ManyToMany
    private List<WepaTweetter> likes;
    @OneToMany(mappedBy = "message")
    private List<WepaComment> comments;
}
