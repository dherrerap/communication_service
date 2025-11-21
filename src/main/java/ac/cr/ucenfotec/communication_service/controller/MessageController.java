package ac.cr.ucenfotec.communication_service.controller;

import ac.cr.ucenfotec.communication_service.model.MessageRequest;
import ac.cr.ucenfotec.communication_service.model.MessageResponse;
import ac.cr.ucenfotec.communication_service.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/mensaje")
    public ResponseEntity<MessageResponse> sendMessage(@Valid @RequestBody MessageRequest request) {
        MessageResponse response = messageService.processMessage(request);
        return ResponseEntity.ok(response);
    }

}
