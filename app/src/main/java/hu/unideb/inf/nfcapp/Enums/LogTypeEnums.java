package hu.unideb.inf.nfcapp.Enums;

public enum LogTypeEnums {

    LOGIN_CARD(200),
    LOGOUT_CARD(300),
    LOGIN_PASSWORD(201),
    LOGOUT_PASSWORD(301),
    ;

    private final int levelCode;


    LogTypeEnums(int i) {
        this.levelCode = i;
    }

    public int getLevelCode() {
        return levelCode;
    }
}
