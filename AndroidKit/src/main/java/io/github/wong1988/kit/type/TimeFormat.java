package io.github.wong1988.kit.type;

public enum TimeFormat {
    SHORTER1("yyyy-MM-dd", "2000-01-01"),
    SHORTER2("yyyy/MM/dd", "2000/01/01"),
    SHORTER3("yyyy年MM月dd日", "2000年01月01日"),
    SHORTER4("yyyy-M-d", "2000-1-1"),
    SHORTER5("yyyy/M/d", "2000/1/1"),
    SHORTER6("yyyy年M月d日", "2000年1月1日"),

    SHORT1("yyyy-MM-dd E", "2000-01-01 周六"),
    SHORT2("yyyy/MM/dd E", "2000/01/01 周六"),
    SHORT3("yyyy年MM月dd日 E", "2000年01月01日 周六"),
    SHORT4("yyyy-M-d E", "2000-1-1 周六"),
    SHORT5("yyyy/M/d E", "2000/1/1 周六"),
    SHORT6("yyyy年M月d日 E", "2000年1月1日 周六"),


    NORMAL1("yyyy-MM-dd HH:mm", "2000-01-01 00:00"),
    NORMAL2("yyyy/MM/dd HH:mm", "2000/01/01 00:00"),
    NORMAL3("yyyy年MM月dd日 HH:mm", "2000年01月01日 00:00"),
    NORMAL4("yyyy-MM-dd a hh:mm", "2000-01-01 上午 00:00"),
    NORMAL5("yyyy/MM/dd a hh:mm", "2000/01/01 上午 00:00"),
    NORMAL6("yyyy年MM月dd日 a hh:mm", "2000年01月01日 上午 00:00"),

    ;

    public String format;
    public String example;

    TimeFormat(String format, String example) {
        this.format = format;
        this.example = example;
    }


}
