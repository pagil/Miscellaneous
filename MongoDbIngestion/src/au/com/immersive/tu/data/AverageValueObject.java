package au.com.immersive.tu.data;

public class AverageValueObject {
    double price;
    long priceCounter;
    double speed;
    long speedCounter;
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

    public long getPriceCounter() {
        return priceCounter;
    }

    public void setPriceCounter(long priceCounter) {
        this.priceCounter = priceCounter;
    }

    public long getSpeedCounter() {
        return speedCounter;
    }

    public void setSpeedCounter(long speedCounter) {
        this.speedCounter = speedCounter;
    }

    @Override
    public String toString() {
        return "AverageValueObject [price=" + price + ", priceCounter=" + priceCounter + ", speed=" + speed
                + ", speedCounter=" + speedCounter + ", totalVolume=" + totalVolume + "]";
    }

}
