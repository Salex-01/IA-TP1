import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Environment e = new Environment();
        if (args.length != 0 && args.length % 2 == 0) {
           e.parseArgs(args);
        }
        e.start();
        while(System.in.available()==0){
            Thread.sleep(100);
        }
        e.sStop();
    }
}
