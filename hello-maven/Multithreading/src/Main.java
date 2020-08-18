public class Main {
    public static void main(String[] args){
        StringBuffer text = new StringBuffer();
        CountClass c1 = new CountClass(text, 100);
        CountClass c2 = new CountClass(text, 200);
        c1.start();
        c2.start();
        try {
            c1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            c1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Result: " + text);
    }
}
