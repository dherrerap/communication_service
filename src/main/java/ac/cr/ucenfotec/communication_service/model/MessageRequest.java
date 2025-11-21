package ac.cr.ucenfotec.communication_service.model;

import ac.cr.ucenfotec.communication_service.dto.SystemId;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    @NotNull(message = "El emisor es obligatorio")
    @JsonProperty("emisor")
    private SystemId emisor;

    @NotNull(message = "El receptor es obligatorio")
    @JsonProperty("receptor")
    private SystemId receptor;

    @NotNull(message = "El mensaje es obligatorio")
    @JsonProperty("mensaje")
    private JsonNode mensaje;

    @AssertTrue(message = "El mensaje no puede estar vacío")
    public boolean isMensajeNotEmpty() {
        return mensaje != null && !mensaje.isEmpty();
    }

}
