package by.academy;

public class CountClass extends Thread{
    private StringBuffer text;
    private int countTo;
    public Integer sum;
    public CountClass(StringBuffer s, int c, Integer sum){
        this.text = s;
        this.countTo = c;
        this.sum = sum;
    }

    @Override
    public void run(){
        synchronized (sum){

            for(int i = 0; i < countTo; i++){
                sum+=i;
                text.append("Next value: " + i);
            }
            text.append("\nsum=" + sum + "\n");
        }
    }
}
