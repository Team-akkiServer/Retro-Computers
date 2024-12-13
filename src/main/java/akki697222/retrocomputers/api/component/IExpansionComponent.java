package akki697222.retrocomputers.api.component;

public interface IExpansionComponent extends IBasicComponent {
    void setUsing(boolean state);
    boolean getUsing();
    void update();
    void init();
}
