package cz.sindema;

public class Tank {

    private static final double TOLERANCE = 1e-6;

    private double remainingCapacity;
    private int flowRate;
    private double fillTime;

    public Tank(int remainingCapacity, int flowRate) {
        this.remainingCapacity = remainingCapacity;
        this.flowRate = flowRate;
        setFillTime();
    }

    public double getFillTime() {
        return fillTime;
    }

    public boolean fill(double fillingTime) {
        remainingCapacity = remainingCapacity - flowRate * fillingTime;
        setFillTime();
        return remainingCapacity < TOLERANCE;
    }

    private void setFillTime() {
        fillTime = remainingCapacity / flowRate;
    }

    public int getFlowRate() {
        return flowRate;
    }

    public void increaseFlowRate(int increase) {
        flowRate = flowRate + increase;
        setFillTime();
    }
}