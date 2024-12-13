package akki697222.retrocomputers.api.component;

public class ComponentProperty {
    private final String name;
    private final ComponentTier tier;
    private final int powerConsumption;
    private final int powerRequirement;
    public ComponentProperty(String name, ComponentTier tier, int powerConsumption, int powerRequirement) {
        this.name = name;
        this.tier = tier;
        this.powerConsumption = powerConsumption;
        this.powerRequirement = powerRequirement;
    }

    public String getName() {
        return name;
    }

    public ComponentTier getTier() {
        return tier;
    }

    public int getPowerConsumption() {
        return powerConsumption;
    }

    public int getPowerRequirement() {
        return powerRequirement;
    }
}
