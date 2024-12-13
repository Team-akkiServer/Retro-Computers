package akki697222.retrocomputers.client.gui;

import akki697222.retrocomputers.RetroComputers;
import akki697222.retrocomputers.api.component.IBasicComponent;
import akki697222.retrocomputers.api.computer.renderer.TextRendererQueue;
import akki697222.retrocomputers.common.components.BasicLogicBoardComponent;
import akki697222.retrocomputers.common.components.expansions.CRTExpansion;
import akki697222.retrocomputers.common.items.AbstractComponentItem;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;

import static akki697222.retrocomputers.client.RetroComputersClient.clientLogger;

public class ComputerScreenScreen extends Screen {
    private static ComputerScreenScreen instance = null;
    private GuiGraphics guiGraphics;
    private List<TextRendererQueue> rendererQueues;
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
    private static int[] pixelBuffer;
    public static ComputerScreenScreen instance() {
        return instance;
    }
    public ComputerScreenScreen(List<ItemStack> installedComponents) {
        super(Component.empty());

        this.installedComponents = new ArrayList<>(installedComponents);
        logInstalledComponents();

        pixelBuffer = new int[NATIVE_WIDTH * NATIVE_HEIGHT];
        // Calculate actual render dimensions
        this.renderWidth = NATIVE_WIDTH * PIXEL_SCALE;
        this.renderHeight = NATIVE_HEIGHT * PIXEL_SCALE;
        this.rendererQueues = new ArrayList<>();
        instance = this;

        installedComponents.forEach(itemStack -> {
            if (itemStack.getItem() instanceof AbstractComponentItem componentItem) {
                if (componentItem.getComponent() instanceof BasicLogicBoardComponent component) {
                    component.runProgram("ayhjhyhyuahahtb('");
                }
            }
        });
    }

    private void renderScaledPixelBuffer(GuiGraphics guiGraphics) {
        // 仮想画面のスケール後のサイズ
        int scaledWidth = NATIVE_WIDTH * PIXEL_SCALE;
        int scaledHeight = NATIVE_HEIGHT * PIXEL_SCALE;

        // ウィンドウの中央に描画するためのオフセット
        int offsetX = (width - scaledWidth) / 2; // ウィンドウ全体の幅からスケール後の幅を引いた差分
        int offsetY = (height - scaledHeight) / 2; // 高さも同様

        // ピクセルデータをNativeImageに変換
        NativeImage nativeImage = new NativeImage(NATIVE_WIDTH, NATIVE_HEIGHT, true);
        for (int y = 0; y < NATIVE_HEIGHT; y++) {
            for (int x = 0; x < NATIVE_WIDTH; x++) {
                int color = pixelBuffer[y * NATIVE_WIDTH + x];
                nativeImage.setPixelRGBA(x, y, color);
            }
        }

        // NativeImageをテクスチャとして登録
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(RetroComputers.MODID, "pixel_texture");
        DynamicTexture dynamicTexture = new DynamicTexture(nativeImage);
        Minecraft.getInstance().getTextureManager().register(texture, dynamicTexture);

        guiGraphics.blit(
                texture,              // テクスチャのリソース
                offsetX, offsetY,     // スクリーン座標
                0.0f, 0.0f,           // テクスチャのUVオフセット
                scaledWidth, scaledHeight, // 描画するスクリーンサイズ
                scaledWidth, scaledHeight  // テクスチャ全体のサイズ
        );

        // 後始末
        dynamicTexture.close(); // 不要ならリソース解放
    }

    public void drawColorGradient(int[] pixelBuffer) {
        for (int x = 0; x < NATIVE_WIDTH; x++) {
            // x位置に応じて虹色の色相を設定
            float hue = (float) x / NATIVE_WIDTH; // 0から1の間で色相を設定
            int rgb = Color.HSBtoRGB(hue, 1.0f, 1.0f); // 色相、彩度、明度を指定してRGBに変換

            // ARGB形式で設定
            int color = 0xFF000000 | rgb;

            // 全てのy座標に同じ色を描画
            for (int y = 0; y < NATIVE_HEIGHT; y++) {
                drawPixelRectangle(pixelBuffer, x, y, 1, 1, color); // 各ピクセルに色を設定
            }
        }
    }

    public void drawCircularRainbow(int[] pixelBuffer) {
        int centerX = NATIVE_WIDTH / 2;
        int centerY = NATIVE_HEIGHT / 2;

        for (int x = 0; x < NATIVE_WIDTH; x++) {
            for (int y = 0; y < NATIVE_HEIGHT; y++) {
                // ピクセルの中心からの距離を計算
                double dx = x - centerX;
                double dy = y - centerY;
                double distance = Math.sqrt(dx * dx + dy * dy);

                // 距離に基づいて色相を決定
                float hue = (float)(Math.atan2(dy, dx) / (2 * Math.PI)) + 0.5f;  // -πからπを0から1に変換
                // 彩度と明度を設定
                int rgb = Color.HSBtoRGB(hue, 1.0f, 1.0f);
                int color = 0xFF000000 | rgb;

                // ピクセルに色を描画
                drawPixelRectangle(pixelBuffer, x, y, 1, 1, color);
            }
        }
    }

    public void drawPixelRectangle(int[] buffer, int x, int y, int width, int height, int color) {
        clientLogger.debug("Drawing Pixel Rectangle " + x + ":" + y + "to" + width + ":" + height);
        for (int dy = 0; dy < height; dy++) {
            for (int dx = 0; dx < width; dx++) {
                int px = x + dx;
                int py = y + dy;
                if (px >= 0 && px < NATIVE_WIDTH && py >= 0 && py < NATIVE_HEIGHT) {
                    buffer[py * NATIVE_WIDTH + px] = color;
                }
            }
        }
    }

    public void drawPixelRectangle(int x, int y, int width, int height, int color) {
        drawPixelRectangle(pixelBuffer, x, y, width, height, color);
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

        int offsetX = (width / 2) + scaledWidth;
        int offsetY = (height / 2) + scaledHeight;

        clientLogger.debug("Real Position " + offsetX + ":" + offsetY);

        rendererQueues.add(new TextRendererQueue(text, offsetX, offsetY, color));
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

    public void drawCRT(int[] pixelBuffer) {

    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.guiGraphics = guiGraphics;

        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        // Clear pixel buffer
        Arrays.fill(pixelBuffer, 0xFF000000);

        // Final Initialization
        installedComponents.forEach(itemStack -> {
            if (itemStack.getItem() instanceof AbstractComponentItem componentItem) {
                if (componentItem.getComponent() instanceof CRTExpansion) {
                    drawCRT(pixelBuffer);
                }
            }
        });

        // Render the scaled pixel buffer
        renderScaledPixelBuffer(guiGraphics);

        drawText(guiGraphics, "Hello, World!", mouseX, mouseY, 0xFFFFFFFF);
        for (TextRendererQueue queue : rendererQueues) {
            drawText(guiGraphics, queue.text(), queue.x(), queue.y(), queue.color());
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

    public static void pixelBuffer(int[] buffer) {
        pixelBuffer = buffer;
    }

    public static int[] pixelBuffer() {
        return pixelBuffer;
    }
}
