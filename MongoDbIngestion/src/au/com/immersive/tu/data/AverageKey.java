package au.com.immersive.tu.data;

import au.com.immersive.tu.data.enums.Backward;
import au.com.immersive.tu.data.enums.Forward;

public class AverageKey {
    private int timeSlot;
    private String tollPoint;
    private int dayOfWeek;

    public AverageKey(int timeSlot, String tollPoint, int dayOfWeek) {
        super();
        this.timeSlot = timeSlot;
        this.tollPoint = tollPoint;
        this.dayOfWeek = dayOfWeek;
        validateValues();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dayOfWeek;
        result = prime * result + timeSlot;
        result = prime * result + ((tollPoint == null) ? 0 : tollPoint.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AverageKey other = (AverageKey) obj;
        if (dayOfWeek != other.dayOfWeek)
            return false;
        if (timeSlot != other.timeSlot)
            return false;
        if (tollPoint == null) {
            if (other.tollPoint != null)
                return false;
        } else if (!tollPoint.equals(other.tollPoint))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AverageKey [timeSlot=" + timeSlot + ", tollPoint=" + tollPoint + ", dayOfWeek=" + dayOfWeek + "]";
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public String getTollPoint() {
        return tollPoint;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    private void validateValues() {
        if ((timeSlot < 0) || (timeSlot > 95)) {
            throw new IllegalArgumentException("Incorrect value for timeSlot: " + timeSlot);
        }
        if ((dayOfWeek < 0) || (dayOfWeek > 6)) {
            throw new IllegalArgumentException("Incorrect value for dayOfWeek: " + timeSlot);
        }
        if ((Forward.getByCode(tollPoint) == null) && (Backward.getByCode(tollPoint) == null)) {
            throw new IllegalArgumentException("Incorrect value for dayOfWeek: " + timeSlot);

        }
    }
}
