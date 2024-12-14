package akki697222.retrocomputers.client.gui;

import akki697222.retrocomputers.RetroComputers;
import akki697222.retrocomputers.api.component.IBasicComponent;
import akki697222.retrocomputers.api.computer.renderer.*;
import akki697222.retrocomputers.common.components.BasicLogicBoardComponent;
import akki697222.retrocomputers.common.components.expansions.CRTExpansion;
import akki697222.retrocomputers.common.items.AbstractComponentItem;
import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.BigArrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;

import static akki697222.retrocomputers.client.RetroComputersClient.clientLogger;

public class ComputerScreenScreen extends Screen {
    private static ComputerScreenScreen instance = null;
    private GuiGraphics guiGraphics;
    private ScreenRenderQueues rendererQueues;
    private static final ResourceLocation BORDER_TEXTURE = ResourceLocation.fromNamespaceAndPath(RetroComputers.MODID, "textures/gui/border_normal.png");
    private final List<ItemStack> installedComponents;
    // Reduced native resolution
    public static final int NATIVE_WIDTH = 640;
    public static final int NATIVE_HEIGHT = 480;

    // Pixel scale factor (how many screen pixels represent one native pixel)
    private static final int PIXEL_SCALE = 1;

    // Actual rendering dimensions
    private final int renderWidth;
    private final int renderHeight;

    // Pixel buffer for native resolution
    private static int[][] pixelBuffer;
    public static ComputerScreenScreen instance() {
        return instance;
    }
    public ComputerScreenScreen(List<ItemStack> installedComponents) {
        super(Component.empty());

        this.installedComponents = new ArrayList<>(installedComponents);
        logInstalledComponents();

        pixelBuffer = new int[NATIVE_WIDTH][NATIVE_HEIGHT];
        // Calculate actual render dimensions
        this.renderWidth = NATIVE_WIDTH * PIXEL_SCALE;
        this.renderHeight = NATIVE_HEIGHT * PIXEL_SCALE;
        this.rendererQueues = new ScreenRenderQueues();
        instance = this;

        installedComponents.forEach(itemStack -> {
            if (itemStack.getItem() instanceof AbstractComponentItem componentItem) {
                if (componentItem.getComponent() instanceof BasicLogicBoardComponent component) {
                    component.runProgram("graphics.drawRectangle(0, 0, graphics.width, graphics.height, 0xFF0000FF)");
                    instance().drawPng(64, 64, Path.of("soren1.png"));
                }
            }
        });
    }

    private void renderScaledPixelBuffer(GuiGraphics guiGraphics) {
        int scaledWidth = NATIVE_WIDTH * PIXEL_SCALE;
        int scaledHeight = NATIVE_HEIGHT * PIXEL_SCALE;

        Minecraft mc = Minecraft.getInstance();

        int _width = mc.getWindow().getGuiScaledWidth();
        int _height = mc.getWindow().getGuiScaledHeight();

        int offsetX = (_width - scaledWidth) / 2;
        int offsetY = (_height - scaledHeight) / 2;

        NativeImage nativeImage = new NativeImage(NATIVE_WIDTH, NATIVE_HEIGHT, true);
        for (int y = 0; y < NATIVE_HEIGHT; y++) {
            for (int x = 0; x < NATIVE_WIDTH; x++) {
                nativeImage.setPixelRGBA(x, y, pixelBuffer[x][y]);
            }
        }

        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(RetroComputers.MODID, "pixel_texture");
        DynamicTexture dynamicTexture = new DynamicTexture(nativeImage);
        Minecraft.getInstance().getTextureManager().register(texture, dynamicTexture);

        guiGraphics.blit(
                texture,              // テクスチャのリソース
                offsetX, offsetY,     // スクリーン座標
                //0, 0,
                0.0f, 0.0f,           // テクスチャのUVオフセット
                scaledWidth, scaledHeight, // 描画するスクリーンサイズ
                scaledWidth, scaledHeight  // テクスチャ全体のサイズ
        );

        // 後始末
        dynamicTexture.close(); // 不要ならリソース解放
    }
    public void drawPixelRectangle(int x, int y, int width, int height, int color) {
        rendererQueues.push(new RectangleRenderQueue(x, y, width, height, color));
    }

    public void drawPixel(int x, int y, int color) {
        int scaledWidth = NATIVE_WIDTH * PIXEL_SCALE;
        int scaledHeight = NATIVE_HEIGHT * PIXEL_SCALE;

        Minecraft mc = Minecraft.getInstance();

        int _width = mc.getWindow().getGuiScaledWidth();
        int _height = mc.getWindow().getGuiScaledHeight();

        int offsetX = (_width - scaledWidth) / 2 + x;
        int offsetY = (_height - scaledHeight) / 2 + y;

        rendererQueues.push(new RectangleRenderQueue(offsetX, offsetY, 1, 1, color));
    }

    public boolean drawPng(int ix, int iy, Path path) {
        clientLogger.debug("Parsing image on path: " + path.toString());
        try {
            BufferedImage image = ImageIO.read(path.toFile());

            int width = image.getWidth();
            int height = image.getHeight();

            int[][] buffer = new int[width][height];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    buffer[x][y] = getABGRfromRGB(image.getRGB(x, y));
                }
            }

            rendererQueues.push(new CustomBufferRenderQueue(buffer, ix, iy, width, height));

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static int getABGRfromRGB(int rgba) {
        int red = (rgba >> 16) & 0xFF;
        int green = (rgba >> 8) & 0xFF;
        int blue = rgba & 0xFF;

        //abgr

        return (0xFF << 24) | (blue << 16) | (green << 8) | red;
    }

    private void drawText(GuiGraphics guiGraphics, Font font, String text, int x, int y, int color) {
        guiGraphics.drawString(font, text, x, y, color);
    }

    private void drawText(GuiGraphics guiGraphics, String text, int x, int y, int color) {
        drawText(guiGraphics, Minecraft.getInstance().font, text, x, y, color);
    }

    public void drawText(String text, int x, int y, int color) {
        int scaledWidth = NATIVE_WIDTH * PIXEL_SCALE;
        int scaledHeight = NATIVE_HEIGHT * PIXEL_SCALE;

        Minecraft mc = Minecraft.getInstance();

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        int offsetX = (width - scaledWidth) / 2 + x;
        int offsetY = (height - scaledHeight) / 2 + y;

        clientLogger.debug("Real Position " + offsetX + ":" + offsetY + ",T:" + text);

        rendererQueues.push(new TextRenderQueue(text, offsetX, offsetY, color));
    }

    public void logInstalledComponents() {
        StringBuilder sb = new StringBuilder();
        installedComponents.forEach(itemStack -> {
            Item item = itemStack.getItem();
            if (item instanceof AbstractComponentItem componentItem) {
                IBasicComponent component = componentItem.getComponent();

                sb.append(component.getProperty().getName()).append("\n");
            }
        });
        RetroComputers.logger.info(sb.toString());
    }

    public void drawCRT(int[][] pixelBuffer) {

    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.guiGraphics = guiGraphics;

        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        // Clear pixel buffer
        BigArrays.fill(pixelBuffer, 0xFF000000);

        // Final Initialization
        installedComponents.forEach(itemStack -> {
            if (itemStack.getItem() instanceof AbstractComponentItem componentItem) {
                if (componentItem.getComponent() instanceof CRTExpansion) {
                    drawCRT(pixelBuffer);
                }
            }
        });

        List<IRenderQueue> currentQueues = rendererQueues.get();
        for (IRenderQueue renderQueue : currentQueues) {
            if (renderQueue instanceof TextRenderQueue textRenderQueue) {
                guiGraphics.drawString(
                        Minecraft.getInstance().font,
                        textRenderQueue.text(),
                        textRenderQueue.x(),
                        textRenderQueue.y(),
                        textRenderQueue.color(),
                        false
                );
            } else if (renderQueue instanceof RectangleRenderQueue rectangleRenderQueue) {
                int startX = rectangleRenderQueue.x();
                int startY = rectangleRenderQueue.y();
                int width = rectangleRenderQueue.width();
                int height = rectangleRenderQueue.height();
                int color = rectangleRenderQueue.color();

                int maxX = Math.min(startX + width, 640);
                int maxY = Math.min(startY + height, 480);

                for (int y = startY; y < maxY; y++) {
                    for (int x = startX; x < maxX; x++) {
                        pixelBuffer[x][y] = color;
                    }
                }
            } else if (renderQueue instanceof CustomBufferRenderQueue customBufferRenderQueue) {
                int imageW = customBufferRenderQueue.imageWidth();
                int imageH = customBufferRenderQueue.imageHeight();
                int startX = customBufferRenderQueue.x();
                int startY = customBufferRenderQueue.y();
                int[][] buffer = customBufferRenderQueue.buffer();

                int ix = 0;
                int iy = 0;
                for (int y = startY; y < startY + imageH; y++) {
                    for (int x = startX; x < startX + imageW; x++) {
                        pixelBuffer[x][y] = buffer[ix][iy];
                        ix++;
                    }
                    iy++;
                    ix = 0;


                }
            }

            // Render the scaled pixel buffer
            renderScaledPixelBuffer(guiGraphics);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        //super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        int guiWidth = 192;
        int guiHeight = 128;
        int centerX = this.width / 2 - guiWidth / 2;
        int centerY = this.height / 2 - guiHeight / 2;

        //guiGraphics.blitSprite(BORDER_TEXTURE, centerX, centerY, 0, guiWidth, guiHeight);
        guiGraphics.fillGradient(0, 0, this.width, this.height, 0x00000000, 0x00000000);
    }

    public static void pixelBuffer(int[][] buffer) {
        pixelBuffer = buffer;
    }

    public static int[][] pixelBuffer() {
        return pixelBuffer;
    }
}
