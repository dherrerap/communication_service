package ac.cr.ucenfotec.communication_service.model;

import ac.cr.ucenfotec.communication_service.dto.SystemId;
import lombok.Data;

import java.time.Instant;

@Data
public class MessageResponse {
    private String id;
    private SystemId emisor;
    private SystemId receptor;
    private String mensaje;
    private Instant timestamp;
}
