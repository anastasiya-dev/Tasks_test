package by.academy;

import java.io.Serializable;

public class DtoReceiver implements Serializable {
    int num;
    String name;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DtoReceiver{" +
                "num=" + num +
                ", name='" + name + '\'' +
                '}';
    }
}
