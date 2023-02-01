package lh.wordtree.entity;

public enum NumberFrequency {
    LESS0(0, "#e9ecef"),
    LESS1(100, "#0e4429"),
    MORE0(1000, "#006d32"),
    MORE1(3000, "#31ab4b"),
    MORE2(6000, "#39d353");
    public int value;
    public String color;

    NumberFrequency(int value, String color) {
        this.value = value;
        this.color = color;
    }
}
