package ac.cr.ucenfotec.communication_service.controller;

import ac.cr.ucenfotec.communication_service.dto.SystemId;
import ac.cr.ucenfotec.communication_service.model.MessageResponse;
import ac.cr.ucenfotec.communication_service.service.MessageService;
import ac.cr.ucenfotec.communication_service.service.QueueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageService messageService;

    @Autowired
    private QueueService queueService;

    @TestConfiguration
    static class TestConfig {

        @Bean
        MessageService messageService() {
            return Mockito.mock(MessageService.class);
        }

        @Bean
        QueueService queueService() {
            return Mockito.mock(QueueService.class);
        }
    }

    @BeforeEach
    void setUp() {
    }

    private final String url = "/api/mensaje";
    private final String GET_MESSAGES_URL = "/api/mensaje/";

    @Test
    void sendMessageSuccessfully() throws Exception {
        String validJson = """
            {
                "emisor": "S02_REC",
                "receptor": "S04_ENT",
                "mensaje": {
                    "tipo": "PRUEBA",
                    "contenido": "Mensaje de prueba"
                }
            }
        """;

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isOk());
    }

    @Test
    void sendEmptyMessage() throws Exception {
        String json = """
            {
                "emisor": "S02_REC",
                "receptor": "S04_ENT",
                "mensaje": {}
            }
        """;

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendNullMessage() throws Exception {
        String json = """
            {
                "emisor": "S02_REC",
                "receptor": "S04_ENT",
                "mensaje": null
            }
        """;

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendInvalidJsonFormat() throws Exception {
        String invalidJson = """
            {
                "emisor": "S02_REC",
                "receptor": "S04_ENT",
                "mensaje": null
            """;

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendInvalidSender() throws Exception {
        String json = """
            {
                "emisor": "XXXX_123",
                "receptor": "S04_ENT",
                "mensaje": { "data": 1 }
            }
        """;

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendInvalidReceptor() throws Exception {
        String json = """
            {
                "emisor": "S02_REC",
                "receptor": "NOPE",
                "mensaje": { "data": 1 }
            }
        """;

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void missingFields() throws Exception {
        String json = """
            {
                "emisor": "S02_REC"
            }
        """;

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendEmptyBody() throws Exception {
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMessagesSuccessfully() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String receptor = "S02_REC";

        MessageResponse mockResponse = new MessageResponse();
        mockResponse.setId("123");
        mockResponse.setEmisor(SystemId.S01_COM);
        mockResponse.setReceptor(SystemId.S02_REC);
        mockResponse.setMensaje(mapper.readTree("{\"a\":1}"));
        mockResponse.setTimestamp(Instant.now());

        when(messageService.getMessagesByReceptor(receptor))
                .thenReturn(List.of(mockResponse));

        mockMvc.perform(get(GET_MESSAGES_URL + receptor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("123"))
                .andExpect(jsonPath("$[0].emisor").value("S01_COM"))
                .andExpect(jsonPath("$[0].receptor").value("S02_REC"))
                .andExpect(jsonPath("$[0].mensaje.a").value(1));
    }

    @Test
    void getMessagesEmptyList() throws Exception {

        Mockito.when(queueService.getMessagesFor(SystemId.S02_REC))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get(GET_MESSAGES_URL + "S02_REC"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getMessagesInvalidReceptor() throws Exception {
        when(messageService.getMessagesByReceptor("INVALID_ID"))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        mockMvc.perform(get(GET_MESSAGES_URL + "INVALID_ID"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMessagesMissingReceptor() throws Exception {
        mockMvc.perform(get(GET_MESSAGES_URL))
                .andExpect(status().isNotFound());
    }

}