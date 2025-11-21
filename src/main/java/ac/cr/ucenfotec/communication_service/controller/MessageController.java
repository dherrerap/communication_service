package ac.cr.ucenfotec.communication_service.controller;

import ac.cr.ucenfotec.communication_service.dto.SystemId;
import ac.cr.ucenfotec.communication_service.model.MessageRequest;
import ac.cr.ucenfotec.communication_service.model.MessageResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/mensaje")
public class MessageController {

    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody MessageRequest request) {

        // Emisor o receptor nulos
        if (request.getEmisor() == null || request.getReceptor() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        JsonNode msg = request.getMensaje();

        // Mensaje nulo
        if (msg == null || msg.isNull()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        // Mensaje vacío
        if (msg.isObject() && msg.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        MessageResponse response = new MessageResponse();
        response.setId(UUID.randomUUID().toString());
        response.setEmisor(SystemId.valueOf(request.getEmisor().name()));
        response.setReceptor(SystemId.valueOf(request.getReceptor().name()));
        response.setMensaje(msg.toString());
        response.setTimestamp(Instant.now());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
