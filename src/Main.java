import javax.swing.*;

public class Main {
    // Main-metod för att starta programmet
    public static void main(String[] args) {
        Bokningsprogram bok = new Bokningsprogram();
        JFrame jf = new JFrame();
        jf.setTitle("Bokningsprogram");
        jf.setSize(800, 800);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
        jf.add(bok);
    }
}