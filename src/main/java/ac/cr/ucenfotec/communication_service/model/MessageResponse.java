package ac.cr.ucenfotec.communication_service.model;

import ac.cr.ucenfotec.communication_service.dto.SystemId;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.time.Instant;

@Data
public class MessageResponse {
    private String id;
    private SystemId emisor;
    private SystemId receptor;
    private JsonNode mensaje;
    private Instant timestamp;
}
