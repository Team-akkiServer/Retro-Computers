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
        graphics.set("drawRectangle", new drawRectangle());
        graphics.set("width", LuaInteger.valueOf(ComputerScreenScreen.NATIVE_WIDTH));
        graphics.set("height", LuaInteger.valueOf(ComputerScreenScreen.NATIVE_HEIGHT));

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

    static final class drawRectangle extends VarArgFunction {
        drawRectangle() {

        }

        @Override
        public Varargs invoke(Varargs args) {
            try {
                int x = args.checkint(1);
                int y = args.checkint(2);
                int w = args.checkint(3);
                int h = args.checkint(4);
                int color = args.checkint(5);

                ComputerScreenScreen.instance().drawPixelRectangle(x, y, w, h, color);
                return TRUE;
            } catch (Exception e) {
                return FALSE;
            }
        }
    }
}