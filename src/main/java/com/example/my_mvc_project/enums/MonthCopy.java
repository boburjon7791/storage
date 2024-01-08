package com.example.my_mvc_project.enums;

public enum MonthCopy {
    YANVAR(1),
    FEVRAL(2),
    MART(3),
    APREL(4),
    MAY(5),
    IYUN(6),
    IYUL(7),
    AVGUST(8),
    SENTYABR(9),
    OKTYABR(10),
    NOYABR(11),
    DEKABR(12);
    final int value;

    MonthCopy(int value) {
        this.value = value;
    }

    public static MonthCopy intValue(int monthValue) {
        return MonthCopy.values()[monthValue-1];
    }

    public int getValue() {
        return value;
    }
}
