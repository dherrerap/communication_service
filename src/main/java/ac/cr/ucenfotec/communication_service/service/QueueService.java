package ac.cr.ucenfotec.communication_service.service;

import ac.cr.ucenfotec.communication_service.dto.MessageQueueItem;
import ac.cr.ucenfotec.communication_service.dto.SystemId;
import ac.cr.ucenfotec.communication_service.model.MessageRequest;
import ac.cr.ucenfotec.communication_service.model.MessageResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.DelayQueue;

@Service
public class QueueService {

    private final long ttlMillis;

    public QueueService() {
        this.ttlMillis = 60000;
    }

    public QueueService(long ttlMillis) {
        this.ttlMillis = ttlMillis;
    }

    private final DelayQueue<MessageQueueItem> queue = new DelayQueue<>();

    public void enqueue(MessageRequest request) {
        queue.put(new MessageQueueItem(request, ttlMillis));
    }

    public List<MessageResponse> getMessagesFor(SystemId receptor) {
        cleanExpired();
        return queue.stream()
                .filter(item -> item.getReceptor().equals(receptor))
                .map(MessageQueueItem::toResponse)
                .toList();
    }

    private void cleanExpired() {
        MessageQueueItem item;
        while ((item = queue.poll()) != null) {
        }
    }
}