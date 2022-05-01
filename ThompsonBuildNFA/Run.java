import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class Run{
    public void test(){
        File f = new File(this.getClass().getResource("").getPath());

        System.out.println(f);
    }

    public static void main(String[] args) {
        new Run().test();
    }
}