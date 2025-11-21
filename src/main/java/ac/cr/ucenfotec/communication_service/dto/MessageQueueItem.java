package ac.cr.ucenfotec.communication_service.dto;

import ac.cr.ucenfotec.communication_service.model.MessageRequest;
import ac.cr.ucenfotec.communication_service.model.MessageResponse;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class MessageQueueItem implements Delayed {

    private final MessageRequest request;
    private final long expireAt;

    public MessageQueueItem(MessageRequest request, long ttlMillis) {
        this.request = request;
        this.expireAt = System.currentTimeMillis() + ttlMillis;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = expireAt - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
    }

    public SystemId getReceptor() {
        return request.getReceptor();
    }

    public MessageResponse toResponse() {
        MessageResponse response = new MessageResponse();
        response.setId(UUID.randomUUID().toString());
        response.setEmisor(SystemId.valueOf(request.getEmisor().name()));
        response.setReceptor(SystemId.valueOf(request.getReceptor().name()));
        response.setMensaje(request.getMensaje());
        response.setTimestamp(Instant.now());
        return response;
    }
}