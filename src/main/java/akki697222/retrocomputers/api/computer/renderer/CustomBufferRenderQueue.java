package akki697222.retrocomputers.api.computer.renderer;

public record CustomBufferRenderQueue(int[][] buffer, int x, int y, int imageWidth, int imageHeight) implements IRenderQueue {
}
