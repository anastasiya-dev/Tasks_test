public class CountClass extends Thread {
    private StringBuffer text;
    private int countTo;
    public CountClass(StringBuffer s, int c){
        this.text = s;
        this.countTo = c;
    }

    @Override
    public void run(){
        synchronized (text){
            int sum = 0;
            for(int i = 0; i < countTo; i++){
                sum+=i;
                text.append("Next value: " + i);
            }
            text.append("\nsum=" + sum + "\n");
        }
    }
}
