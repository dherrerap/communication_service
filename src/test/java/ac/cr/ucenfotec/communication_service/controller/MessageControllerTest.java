package ac.cr.ucenfotec.communication_service.controller;

import ac.cr.ucenfotec.communication_service.dto.SystemId;
import ac.cr.ucenfotec.communication_service.model.MessageRequest;
import ac.cr.ucenfotec.communication_service.model.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class MessageControllerTest {

    private MessageController messageController;

    @BeforeEach
    void setUp() {
        messageController = new MessageController();
    }

    @Test
    void sendMessage() {
        MessageRequest messageRequest = new MessageRequest(SystemId.S02_REC, SystemId.S02_REC, "Test");

        ResponseEntity<MessageResponse> response = messageController.sendMessage(messageRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void sendEmptyMessage() {
        MessageRequest messageRequest = new MessageRequest(SystemId.S02_REC, SystemId.S02_REC, "");

        ResponseEntity<MessageResponse> response = messageController.sendMessage(messageRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void sendNullMessage() {
        MessageRequest messageRequest = new MessageRequest(SystemId.S02_REC, SystemId.S02_REC, null);

        ResponseEntity<MessageResponse> response = messageController.sendMessage(messageRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void sendInvalidFormatMessage() {
        InvalidMessageRequest invalidMessageRequest = new InvalidMessageRequest("Invalid message");

        ResponseEntity<MessageResponse> response = messageController.sendMessage(messageRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void sendInvalidSenderMessage() {
        MessageRequest messageRequest = new MessageRequest("INVALID_SENDER", SystemId.S02_REC, "Test");

        ResponseEntity<MessageResponse> response = messageController.sendMessage(messageRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void sendInvalidReceptorMessage() {
        MessageRequest messageRequest = new MessageRequest(SystemId.S01_COM, "INVALID_RECEPTOR", "Test");

        ResponseEntity<MessageResponse> response = messageController.sendMessage(messageRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testSendMessage() {
    }
}