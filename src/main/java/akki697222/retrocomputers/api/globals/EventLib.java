package akki697222.retrocomputers.api.globals;

import akki697222.retrocomputers.api.computer.Computer;
import akki697222.retrocomputers.api.computer.LuaEvent;
import akki697222.retrocomputers.client.gui.ComputerScreenScreen;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

import java.util.UUID;

public class EventLib extends TwoArgFunction {
    private final Computer computerInstance;
    public EventLib(Computer computerInstance) {
        this.computerInstance = computerInstance;
    }

    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaTable event = new LuaTable();

        event.set("queue", new queue());

        env.set("event", event);
        env.get("package").get("loaded").set("event", event);

        return event;
    }

    final class queue extends VarArgFunction {
        queue() {}

        @Override
        public Varargs invoke(Varargs args) {
            String eventName = args.checkjstring(1);
            LuaValue[] eventArgs = new LuaValue[args.narg() - 1];
            for (int i = 2; i <= args.narg(); i++) {
                eventArgs[i - 2] = args.arg(i);
            }
            computerInstance.queueEvent(new LuaEvent(eventName, eventArgs));
            return NIL;
        }
    }
}
