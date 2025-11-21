package ac.cr.ucenfotec.communication_service.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.time.Instant;

@Data
public class Message {
    private String emisor;
    private String receptor;
    private JsonNode mensaje;
    private Instant timestamp;
}
