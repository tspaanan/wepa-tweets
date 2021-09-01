package projekti;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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
public class WepaComment extends AbstractPersistable<Long> {
    
    @ManyToOne
    private WepaTweetter tweetter;
    private LocalDateTime timestamp;
    private String commentContent;
    @JsonIgnore
    @ManyToOne
    private WepaMessage message;
}
