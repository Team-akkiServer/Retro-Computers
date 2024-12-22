package akki697222.retrocomputers.api.globals;

import akki697222.retrocomputers.RetroComputers;
import akki697222.retrocomputers.api.computer.renderer.TextRenderQueue;
import akki697222.retrocomputers.client.gui.ComputerScreenScreen;
import com.google.errorprone.annotations.Var;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

import static akki697222.retrocomputers.client.RetroComputersClient.clientLogger;

public class GraphicsLib extends TwoArgFunction {
    public ComputerScreenScreen screenInstance;
    public GraphicsLib(ComputerScreenScreen screenInstance) {
        this.screenInstance = screenInstance;
    }
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaTable graphics = new LuaTable();

        graphics.set("drawText", new drawText());
        graphics.set("drawRectangle", new drawRectangle());
        graphics.set("clear", new clear());
        graphics.set("clearText", new clearText());
        graphics.set("width", LuaInteger.valueOf(ComputerScreenScreen.NATIVE_WIDTH));
        graphics.set("height", LuaInteger.valueOf(ComputerScreenScreen.NATIVE_HEIGHT));

        env.set("graphics", graphics);
        env.get("package").get("loaded").set("graphics", graphics);

        return graphics;
    }

    final class clear extends VarArgFunction {
        clear() {}

        @Override
        public Varargs invoke(Varargs args) {
            screenInstance.getRendererQueues().clear();
            return NIL;
        }
    }

    final class clearText extends VarArgFunction {
        clearText() {}

        @Override
        public Varargs invoke(Varargs args) {
            screenInstance.getRendererQueues().clear(TextRenderQueue.class);
            return NIL;
        }
    }

     final class drawText extends VarArgFunction {
        drawText() {

        }
        @Override
        public Varargs invoke(Varargs args) {
            try {
                String text = args.checkstring(1).tojstring();
                int x = args.checkinteger(2).toint();
                int y = args.checkinteger(3).toint();
                int color = args.checkinteger(4).toint();

                screenInstance.drawText(text, x, y, color);
                return TRUE;
            } catch (Exception e) {
                return FALSE;
            }
        }
    }

     final class drawRectangle extends VarArgFunction {
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

                screenInstance.drawPixelRectangle(x, y, w, h, color);
                return TRUE;
            } catch (Exception e) {
                return FALSE;
            }
        }
    }
}