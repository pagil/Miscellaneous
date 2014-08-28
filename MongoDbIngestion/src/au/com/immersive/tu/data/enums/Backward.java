package au.com.immersive.tu.data.enums;

public enum Backward implements Direction {
    B6(1), B4(2), B9(2), A4(2), B8(3), B3(3), B2(4), B1(5);

    private int position;
    private String code;

    private Backward(int pos) {
        position = pos;
        code = new StringBuffer(this.name()).reverse().toString();
    }

    @Override
    public String getCode() {
        return new StringBuffer(this.name()).reverse().toString();
    }

    public static Direction getByCode(String code) {
        for (Backward value : values()) {
            if (value.code.equals(code))
                return value;
        }
        return null;
    }

    @Override
    public int getPosition() {
        return this.position;
    }

}
