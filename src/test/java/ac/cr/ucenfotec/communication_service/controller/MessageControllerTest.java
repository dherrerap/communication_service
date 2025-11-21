package ac.cr.ucenfotec.communication_service.controller;

import ac.cr.ucenfotec.communication_service.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageService messageService;

    @TestConfiguration
    static class TestConfig {

        @Bean
        MessageService messageService() {
            return Mockito.mock(MessageService.class);
        }
    }

    @BeforeEach
    void setUp() {
    }

    private final String url = "/api/mensaje";

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
}