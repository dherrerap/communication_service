package ac.cr.ucenfotec.communication_service.service;

import ac.cr.ucenfotec.communication_service.dto.SystemId;
import ac.cr.ucenfotec.communication_service.model.MessageRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QueueServiceTest {

    private QueueService queueService;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        queueService = new QueueService(100);
        mapper = new ObjectMapper();
    }

    @Test
    void enqueueMessage_ShouldStoreMessage() throws Exception {
        JsonNode msg = mapper.readTree("""
            {
                "accion": "test"
            }
        """);

        MessageRequest req = new MessageRequest(SystemId.S01_COM, SystemId.S02_REC, msg);

        queueService.enqueue(req);

        List<?> result = queueService.getMessagesFor(SystemId.S02_REC);

        assertEquals(1, result.size());
    }

    @Test
    void getMessagesFor_ShouldReturnOnlyMatchingReceptor() throws Exception {
        JsonNode msg = mapper.readTree("""
            {
                "accion": "test"
            }
        """);

        MessageRequest r1 = new MessageRequest(SystemId.S01_COM, SystemId.S02_REC, msg);
        MessageRequest r2 = new MessageRequest(SystemId.S03_REI, SystemId.S04_ENT, msg);

        queueService.enqueue(r1);
        queueService.enqueue(r2);

        List<?> result = queueService.getMessagesFor(SystemId.S02_REC);

        assertEquals(1, result.size());
    }

    @Test
    void expiredMessages_ShouldNotBeReturned() throws Exception {
        JsonNode msg = mapper.readTree("""
        {
            "accion": "test"
        }
        """);

        MessageRequest req = new MessageRequest(SystemId.S01_COM, SystemId.S02_REC, msg);

        queueService.enqueue(req);

        Thread.sleep(300);

        List<?> result = queueService.getMessagesFor(SystemId.S02_REC);

        assertTrue(result.isEmpty(), "Los mensajes expirados no deberían ser retornados");
    }
}