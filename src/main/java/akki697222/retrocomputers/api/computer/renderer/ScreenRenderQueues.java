package akki697222.retrocomputers.api.computer.renderer;

import java.util.ArrayList;
import java.util.List;

import static akki697222.retrocomputers.client.RetroComputersClient.clientLogger;

public class ScreenRenderQueues {
    public static final int MAX_RENDER_QUEUES = 2048;
    private List<IRenderQueue> renderQueues;
    public ScreenRenderQueues() {
        renderQueues = new ArrayList<>();
    }
    public void push(IRenderQueue queue) {
        if (renderQueues.size() > MAX_RENDER_QUEUES) {
            pop();
        } else {
            renderQueues.add(queue);
        }
    }

    public IRenderQueue pop() {
        return renderQueues.removeFirst();
    }

    public <T extends IRenderQueue> void clear(Class<T> clearQueue) {
        renderQueues.removeIf(clearQueue::isInstance);
    }

    public List<IRenderQueue> get() {
        return new ArrayList<>(renderQueues);
    }

    public void clear() {
        renderQueues = new ArrayList<>(MAX_RENDER_QUEUES);
    }
}
