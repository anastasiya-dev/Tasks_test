package sensors.support;

public enum SensorUnit {
    BAR("bar"), VOLTAGE("voltage"), DEGREE((char) 176 + "C"), PERCENT("%");

    private String value;

    SensorUnit(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}
