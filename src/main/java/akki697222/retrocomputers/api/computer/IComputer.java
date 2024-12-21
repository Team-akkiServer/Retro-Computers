package akki697222.retrocomputers.api.computer;

public interface IComputer {
    void turnOn();
    void turnOff();
    boolean getPowerState();
    boolean runProgram(String program);
    void update();
}
