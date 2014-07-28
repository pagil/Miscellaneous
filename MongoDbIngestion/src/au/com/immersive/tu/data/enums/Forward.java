package au.com.immersive.tu.data.enums;

public enum Forward implements Direction {
    A1(1), A2(2), A3(3), A5(4), B5(4), A6(5), A7(4), A9(3), A8(2);

    private int position;
    private String code;

    private Forward(int pos) {
        position = pos;
        code = new StringBuffer(this.name()).reverse().toString();
    }

    @Override
    public String getCode() {
        return new StringBuffer(this.name()).reverse().toString();
    }

    public static Direction getByCode(String code) {
        for (Forward value : values()) {
            if (value.code.equals(code))
                return value;
        }
        return null;
    }

    @Override
    public int getPosition() {
        return position;
    }

}
