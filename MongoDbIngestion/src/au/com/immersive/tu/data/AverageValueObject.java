package au.com.immersive.tu.data;

public class AverageValueObject {
    double price;
    double speed;
    long totalVolume;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public long getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(long totalVolume) {
        this.totalVolume = totalVolume;
    }

    @Override
    public String toString() {
        return "AverageValueObject [price=" + price + ", speed=" + speed + ", totalVolume=" + totalVolume + "]";
    }

}
