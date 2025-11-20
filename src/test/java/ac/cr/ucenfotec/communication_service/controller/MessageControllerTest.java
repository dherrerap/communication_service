package ac.cr.ucenfotec.communication_service.controller;

import ac.cr.ucenfotec.communication_service.dto.SystemId;
import ac.cr.ucenfotec.communication_service.model.InvalidMessageRequest;
import ac.cr.ucenfotec.communication_service.model.MessageRequest;
import ac.cr.ucenfotec.communication_service.model.MessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class MessageControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {

    }

    @Test
    void sendMessage() throws JsonProcessingException {
        JsonNode mensajeJson = mapper.readTree("""
            {
                "tipo": "PRUEBA",
                "contenido": "Mensaje de prueba"
            }
        """);
        MessageRequest messageRequest = new MessageRequest(SystemId.S02_REC, SystemId.S04_ENT, mensajeJson);

        ResponseEntity<MessageResponse> response = messageController.sendMessage(messageRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void sendEmptyMessage() throws JsonProcessingException {
        JsonNode mensajeJson = mapper.readTree("""
            {
            }
        """);
        MessageRequest messageRequest = new MessageRequest(SystemId.S02_REC, SystemId.S04_ENT, mensajeJson);

        ResponseEntity<MessageResponse> response = messageController.sendMessage(messageRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void sendNullMessage() {
        MessageRequest messageRequest = new MessageRequest(SystemId.S02_REC, SystemId.S04_ENT, null);

        ResponseEntity<MessageResponse> response = messageController.sendMessage(messageRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void sendInvalidFormatMessage() {
        InvalidMessageRequest invalidMessageRequest = new InvalidMessageRequest("invalidSender",
                "invalidReceptor", "Invalid message");

        ResponseEntity<MessageResponse> response = messageController.sendMessage(invalidMessageRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void sendInvalidSenderMessage() throws JsonProcessingException {
        JsonNode mensajeJson = mapper.readTree("""
            {
                "tipo": "PRUEBA",
                "contenido": "Mensaje de prueba"
            }
        """);
        MessageRequest messageRequest = new MessageRequest(null, SystemId.S02_REC, mensajeJson);

        ResponseEntity<MessageResponse> response = messageController.sendMessage(messageRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void sendInvalidReceptorMessage() throws JsonProcessingException {
        JsonNode mensajeJson = mapper.readTree("""
            {
                "tipo": "PRUEBA",
                "contenido": "Mensaje de prueba"
            }
        """);
        MessageRequest messageRequest = new MessageRequest(SystemId.S01_COM, null, mensajeJson);

        ResponseEntity<MessageResponse> response = messageController.sendMessage(messageRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testSendMessage() {
    }
}