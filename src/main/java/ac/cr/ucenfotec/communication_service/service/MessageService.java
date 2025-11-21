package ac.cr.ucenfotec.communication_service.service;

import ac.cr.ucenfotec.communication_service.model.MessageRequest;
import ac.cr.ucenfotec.communication_service.model.MessageResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class MessageService {

    public MessageResponse processMessage(MessageRequest request) {
        validateSystemIds(request);
        validateMessage(request.getMensaje());

        MessageResponse response = new MessageResponse();
        response.setId(UUID.randomUUID().toString());
        response.setEmisor(request.getEmisor());
        response.setReceptor(request.getReceptor());
        response.setMensaje(request.getMensaje().toString());
        response.setTimestamp(Instant.now());

        return response;
    }

    private void validateSystemIds(@NotNull MessageRequest request) {

        if (request.getEmisor() == null) {
            throw new IllegalArgumentException("Emisor inválido");
        }

        if (request.getReceptor() == null) {
            throw new IllegalArgumentException("Receptor inválido");
        }
    }

    private void validateMessage(JsonNode msg) {

        if (msg == null || msg.isNull()) {
            throw new IllegalArgumentException("Mensaje nulo");
        }

        if (msg.isObject() && msg.isEmpty()) {
            throw new IllegalArgumentException("Mensaje vacío");
        }
    }
}
