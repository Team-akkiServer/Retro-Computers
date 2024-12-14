package akki697222.retrocomputers.api.computer.renderer;

import java.util.ArrayList;
import java.util.List;

public class ScreenRenderQueues {
    public static final int MAX_RENDER_QUEUES = 2048;
    private List<IRenderQueue> renderQueues;
    public ScreenRenderQueues() {
        renderQueues = new ArrayList<>(MAX_RENDER_QUEUES);
    }
    public void push(IRenderQueue queue) {
        renderQueues.add(queue);
    }

    public IRenderQueue pop() {
        return renderQueues.removeFirst();
    }

    public List<IRenderQueue> get() {
        return new ArrayList<>(renderQueues);
    }

    public void clear() {
        renderQueues = new ArrayList<>(MAX_RENDER_QUEUES);
    }
}
