package ac.cr.ucenfotec.communication_service.controller;

import ac.cr.ucenfotec.communication_service.dto.SystemId;
import ac.cr.ucenfotec.communication_service.model.MessageRequest;
import ac.cr.ucenfotec.communication_service.model.MessageResponse;
import ac.cr.ucenfotec.communication_service.service.MessageService;
import ac.cr.ucenfotec.communication_service.service.QueueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;
    private final QueueService queueService;

    public MessageController(MessageService messageService, QueueService queueService) {
        this.messageService = messageService;
        this.queueService = queueService;
    }

    @PostMapping("/mensaje")
    public ResponseEntity<MessageResponse> sendMessage(@Valid @RequestBody MessageRequest request) {
        MessageResponse response = messageService.processMessage(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mensaje/{receptor}")
    public ResponseEntity<List<MessageResponse>> getMessagesByReceptor(@PathVariable String receptor) {

        SystemId receptorEnum;
        try {
            receptorEnum = SystemId.valueOf(receptor);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(queueService.getMessagesFor(receptorEnum));
    }

}
