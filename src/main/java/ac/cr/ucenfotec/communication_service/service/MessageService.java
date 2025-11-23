package ac.cr.ucenfotec.communication_service.service;

import ac.cr.ucenfotec.communication_service.dto.SystemId;
import ac.cr.ucenfotec.communication_service.model.MessageRequest;
import ac.cr.ucenfotec.communication_service.model.MessageResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final QueueService queueService;

    public MessageService(QueueService queueService) {
        this.queueService = queueService;
    }

    public MessageResponse processMessage(MessageRequest request) {
        validateSystemIds(request);
        validateMessage(request.getMensaje());

        String id = UUID.randomUUID().toString();
        request.setId(id);

        queueService.enqueue(request);

        MessageResponse response = new MessageResponse();
        response.setId(id);
        response.setEmisor(request.getEmisor());
        response.setReceptor(request.getReceptor());
        response.setMensaje(request.getMensaje());
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

    public List<MessageResponse> getMessagesByReceptor(String receptor) {
        SystemId receptorEnum;

        try {
            receptorEnum = SystemId.valueOf(receptor);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid receptor");
        }

        return queueService.getMessagesFor(receptorEnum);
    }
}
