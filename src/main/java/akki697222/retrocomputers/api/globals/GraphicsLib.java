package akki697222.retrocomputers.api.globals;

import akki697222.retrocomputers.RetroComputers;
import akki697222.retrocomputers.client.gui.ComputerScreenScreen;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public class GraphicsLib extends TwoArgFunction {
    public GraphicsLib() {

    }
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaTable graphics = new LuaTable();

        graphics.set("drawText", new drawText());

        env.set("graphics", graphics);
        env.get("package").get("loaded").set("graphics", graphics);

        return graphics;
    }

    static final class drawText extends VarArgFunction {
        drawText() {

        }
        @Override
        public Varargs invoke(Varargs args) {
            try {
                String text = args.checkstring(1).tojstring();
                int x = args.checkinteger(2).toint();
                int y = args.checkinteger(3).toint();
                int color = args.checkinteger(4).toint();

                ComputerScreenScreen.instance().drawText(text, x, y, color);
                return TRUE;
            } catch (Exception e) {
                return FALSE;
            }
        }
    }
}