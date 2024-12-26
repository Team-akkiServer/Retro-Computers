package akki697222.retrocomputers.api.computer;

import org.luaj.vm2.LuaValue;

public record LuaEvent(String eventName, LuaValue... args) {
}
