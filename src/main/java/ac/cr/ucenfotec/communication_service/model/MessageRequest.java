package ac.cr.ucenfotec.communication_service.model;

import ac.cr.ucenfotec.communication_service.dto.SystemId;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageRequest {
    private SystemId emisor;
    private SystemId receptor;
    private JsonNode mensaje;
}
