package akki697222.retrocomputers.api.globals;

import akki697222.retrocomputers.RetroComputers;
import akki697222.retrocomputers.api.computer.Computer;
import akki697222.retrocomputers.api.computer.renderer.TextRenderQueue;
import akki697222.retrocomputers.client.gui.ComputerScreenScreen;
import com.google.errorprone.annotations.Var;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

import java.nio.file.Path;

import static akki697222.retrocomputers.client.RetroComputersClient.clientLogger;

public class GraphicsLib extends TwoArgFunction {
    private final Computer computerInstance;
    public GraphicsLib(Computer computerInstance) {
        this.computerInstance = computerInstance;
    }
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaTable graphics = new LuaTable();

        graphics.set("drawText", new drawText());
        graphics.set("drawRectangle", new drawRectangle());
        graphics.set("drawImage", new drawImage());
        graphics.set("clear", new clear());
        graphics.set("clearText", new clearText());
        graphics.set("cursorPosition", new cursorPosition());
        graphics.set("width", LuaInteger.valueOf(ComputerScreenScreen.NATIVE_WIDTH));
        graphics.set("height", LuaInteger.valueOf(ComputerScreenScreen.NATIVE_HEIGHT));

        env.set("graphics", graphics);
        env.get("package").get("loaded").set("graphics", graphics);

        return graphics;
    }

    final class cursorPosition extends VarArgFunction {
        cursorPosition() {}

        @Override
        public Varargs invoke(Varargs args) {
            int x = computerInstance.getComputerScreen().getCursorX();
            int y = computerInstance.getComputerScreen().getCursorY();
            return LuaValue.varargsOf(LuaInteger.valueOf(x), LuaInteger.valueOf(y));
        }
    }

    final class clear extends VarArgFunction {
        clear() {}

        @Override
        public Varargs invoke(Varargs args) {
            computerInstance.getComputerScreen().getRendererQueues().clear();
            return NIL;
        }
    }

    final class clearText extends VarArgFunction {
        clearText() {}

        @Override
        public Varargs invoke(Varargs args) {
            computerInstance.getComputerScreen().getRendererQueues().clear(TextRenderQueue.class);
            return NIL;
        }
    }

    final class drawText extends VarArgFunction {
        drawText() {

        }
        @Override
        public Varargs invoke(Varargs args) {
            try {
                String text = args.checkjstring(1);
                int x = args.checkinteger(2).toint();
                int y = args.checkinteger(3).toint();
                int color = args.checkinteger(4).toint();

                computerInstance.getComputerScreen().drawText(text, x, y, color);
                return TRUE;
            } catch (Exception e) {
                return FALSE;
            }
        }
    }

    final class drawImage extends VarArgFunction {
        drawImage() {}

        @Override
        public Varargs invoke(Varargs args) {
            String path = args.checkjstring(1);
            int x = args.checkint(2);
            int y = args.checkint(3);
            computerInstance.getComputerScreen().drawPng(x, y, Path.of(computerInstance.getRomPath().toString(), path));
            return NIL;
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

                computerInstance.getComputerScreen().drawPixelRectangle(x, y, w, h, color);
                return TRUE;
            } catch (Exception e) {
                return FALSE;
            }
        }
    }
}