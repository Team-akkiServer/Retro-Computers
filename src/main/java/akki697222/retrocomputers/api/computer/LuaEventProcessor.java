package akki697222.retrocomputers.api.computer;

import org.luaj.vm2.LuaThread;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.Queue;

public class LuaEventProcessor {
    private final Queue<LuaEvent> eventQueues;
    private final LuaThread currentLuaThread;

    public LuaEventProcessor(Queue<LuaEvent> eventQueues, LuaThread currentLuaThread) {
        this.eventQueues = eventQueues;
        this.currentLuaThread = currentLuaThread;
    }

    public void processNextEvent() {
        if (!eventQueues.isEmpty()) {
            LuaEvent event = eventQueues.poll();
            if (event != null) {
                LuaValue[] eventArgs = new LuaValue[event.args().length + 1];
                eventArgs[0] = LuaValue.valueOf(event.eventName());
                System.arraycopy(event.args(), 0, eventArgs, 1, event.args().length);

                Varargs args = LuaValue.varargsOf(eventArgs);

                Varargs result = currentLuaThread.resume(args);

                handleResult(result);
            }
        }
    }

    private void handleResult(Varargs result) {

    }
}