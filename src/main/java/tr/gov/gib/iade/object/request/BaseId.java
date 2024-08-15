package tr.gov.gib.iade.object.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class BaseId implements Serializable {
    private Integer id;

    public BaseId() {
    }

    public BaseId(Integer id) {
        this.id = id;
    }

}
