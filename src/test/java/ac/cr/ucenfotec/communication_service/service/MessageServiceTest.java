package ac.cr.ucenfotec.communication_service.service;

import ac.cr.ucenfotec.communication_service.dto.SystemId;
import ac.cr.ucenfotec.communication_service.model.MessageRequest;
import ac.cr.ucenfotec.communication_service.model.MessageResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    private QueueService queueService;
    private MessageService messageService;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        queueService = mock(QueueService.class);
        messageService = new MessageService(queueService);
    }

    @Test
    void processMessage_ShouldEnqueueAndReturnValidResponse() throws Exception {
        JsonNode msg = mapper.readTree("""
            {
                "accion": "registrar",
                "valor": 123
            }
        """);

        MessageRequest request = new MessageRequest(null, SystemId.S01_COM, SystemId.S02_REC, msg);

        MessageResponse response = messageService.processMessage(request);

        verify(queueService, times(1)).enqueue(request);

        assertNotNull(response.getId());
        assertEquals(SystemId.S01_COM, response.getEmisor());
        assertEquals(SystemId.S02_REC, response.getReceptor());
        assertEquals(msg, response.getMensaje());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void processMessage_ShouldThrow_WhenEmisorIsNull() throws Exception {
        JsonNode msg = mapper.readTree("""
            {"k":"v"}
        """);

        MessageRequest request = new MessageRequest(null, null, SystemId.S02_REC, msg);

        assertThrows(IllegalArgumentException.class, () -> messageService.processMessage(request));
        verify(queueService, never()).enqueue(any());
    }

    @Test
    void processMessage_ShouldThrow_WhenReceptorIsNull() throws Exception {
        JsonNode msg = mapper.readTree("""
            {"k":"v"}
        """);

        MessageRequest request = new MessageRequest(null, SystemId.S01_COM, null, msg);

        assertThrows(IllegalArgumentException.class, () -> messageService.processMessage(request));
        verify(queueService, never()).enqueue(any());
    }

    @Test
    void processMessage_ShouldThrow_WhenMessageIsNull() {
        MessageRequest request = new MessageRequest(null, SystemId.S01_COM, SystemId.S02_REC, null);

        assertThrows(IllegalArgumentException.class, () -> messageService.processMessage(request));
        verify(queueService, never()).enqueue(any());
    }

    @Test
    void processMessage_ShouldThrow_WhenMessageIsEmptyObject() throws Exception {
        JsonNode emptyMsg = mapper.readTree("{}");

        MessageRequest request = new MessageRequest(null, SystemId.S01_COM, SystemId.S02_REC, emptyMsg);

        assertThrows(IllegalArgumentException.class, () -> messageService.processMessage(request));
        verify(queueService, never()).enqueue(any());
    }
}