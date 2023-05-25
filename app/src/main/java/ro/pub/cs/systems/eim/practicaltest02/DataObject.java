package ro.pub.cs.systems.eim.practicaltest02;

public class DataObject {
    private String value;
    //save time in seconds
    private Integer time;
    public DataObject(String value, Integer time) {
        this.value = value;
        this.time = time;
    }
    public String getValue() {
        return value;
    }
    public Integer getTime() {
        return time;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public void setTime(Integer time) {
        this.time = time;
    }
    @Override
    public String toString() {
        return "DataObject{" +
                "value='" + value + '\'' +
                ", time=" + time +
                '}';
    }
}
