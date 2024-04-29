import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Year;

// Skapar en klass som ärver JPanel för att bygga användargränssnittet
public class Bokningsprogram extends JPanel {
    // Skapar en GridBagConstraints-instans för att hantera layouten
    GridBagConstraints gbc = new GridBagConstraints();
    // Skapar en AtomicInteger för att hantera vinsten
    private AtomicInteger vinst = new AtomicInteger();
    // Skapar en instans av VIPGäster-klassen för att hantera gästinformation
    private VIPGäster gäster = new VIPGäster();
    // Skapar en JTextArea för custom-output stream
    private JTextArea textArea;

    // Konstruktor för Bokningsprogram_för_bussresa_med_20_personer-klassen
    public Bokningsprogram() {
        // Sätter layouten för panelen till GridBagLayout
        setLayout(new GridBagLayout());
        // Sätter marginaler för GridBagConstraints
        gbc.insets = new Insets(5, 5, 5, 5);

        // Skapar en AtomicInteger för att hantera antalet gäster
        AtomicInteger gästantal = new AtomicInteger(0);

        // Lägger till olika GUI-komponenter
        addLabel("Personnummer");
        JTextField tf1 = addTextField();
        addLabel("Namn");
        JTextField tf2 = addTextField();
        addLabel("Kön");
        JComboBox<String> cb1 = addComboBox(new String[]{"Man", "Kvinna", "Vill ej uppge"});
        addLabel("VIP-status");
        JComboBox<VIPGäster.VIPStatus> cb2 = addComboBox(VIPGäster.VIPStatus.values());

        // Lägger till knappar och deras ActionListener
        läggTillKnappar(tf1, tf2, cb1, cb2, gästantal);

        // Lägger till en JTextArea för utdata
        addCustomOutputStreamTextArea();

        // Skriver ut instruktion till användaren
        System.out.println("Använd dig av GUI:n");
    }

    // Metod för att lägga till knappar och deras ActionListener
    private void läggTillKnappar(JTextField tf1, JTextField tf2, JComboBox<String> cb1, JComboBox<VIPGäster.VIPStatus> cb2, AtomicInteger gästantal) {
        addButton("Lägg till bokning", e -> läggTillBokning(tf1, tf2, cb1, cb2, gästantal));
        addButton("Ta bort bokning", e -> taBortBokning(tf1, tf2, cb1, gästantal));
        addButton("Skriv ut bokningar", e -> skrivUtBokningar());
        addButton("Beräkna vinst", e -> skrivUtvinst());
        addButton("Avsluta", e -> System.exit(0));
    }

    // Metod för att lägga till en etikett
    private void addLabel(String labelText) {
        JLabel label = new JLabel(labelText);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy++;
        add(label, gbc);
    }

    // Metod för att lägga till en textfält
    private JTextField addTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(150, 30));
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy++;
        add(textField, gbc);
        return textField;
    }

    // Metod för att lägga till en ComboBox
    private <T> JComboBox<T> addComboBox(T[] items) {
        JComboBox<T> comboBox = new JComboBox<>(items);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy++;
        add(comboBox, gbc);
        return comboBox;
    }

    // Metod för att lägga till en knapp
    private void addButton(String buttonText, ActionListener actionListener) {
        JButton button = new JButton(buttonText);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        button.addActionListener(actionListener);
        add(button, gbc);
    }

    // Metod för att skriva ut vinsten
    private void skrivUtvinst() {
        System.out.println("Total vinst: " + vinst);
    }

    // Metod för att skriva ut bokningarna
    private void skrivUtBokningar() {
        for (int i = 0; i < 20; i++) {
            if(gäster.namn[i] != null) {
                String vipInfo = "";
                if (gäster.hämtaVIPStatus(i) != null && gäster.hämtaVIPStatus(i) != VIPGäster.VIPStatus.EJ_VIP) {
                    vipInfo = " VIP-status: " + gäster.hämtaVIPStatus(i);
                }
                System.out.println(gäster.namn[i] + " / Gäst " + (1 + i) + "med personnummret("+ gäster.persnum[i] + ") är " + gäster.ålder[i] + " år gammal" + vipInfo);
            }
        }
    }

    // Metod för att lägga till en JTextArea för custom-outputstream
    private void addCustomOutputStreamTextArea() {
        textArea = new JTextArea(35, 30);
        textArea.setPreferredSize(new Dimension(700, 3000));

        JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
        System.setOut(printStream);
        System.setErr(printStream);

        gbc.gridheight = 0;
        gbc.gridy = 0;
        gbc.gridx = 3;
        add(scroll, gbc);
    }

    // Metod för att ta bort en bokning
    private void taBortBokning(JTextField tf1, JTextField tf2, JComboBox cb1, AtomicInteger gästantal) {
        for (int i = 0; i < 20; i++) {
            if(tf1.getText().equals(gäster.persnum[i]) && tf2.getText().equalsIgnoreCase(gäster.namn[i]) && cb1.getSelectedItem().equals(gäster.kön[i])){
                gäster.persnum[i] = "";
                gäster.namn[i] = "";
                gäster.kön[i] = "";
                VIPGäster.VIPStatus status = gäster.hämtaVIPStatus(i); // Hämta VIP-status
                if (status != null && status != VIPGäster.VIPStatus.EJ_VIP) {
                    int kostnad = 300 + ((status.ordinal() + 1) * 100);
                    vinst.addAndGet(-kostnad);
                } else {
                    vinst.addAndGet(-300);
                }
                gäster.sättVIPStatus(i, null); // Rensa VIP-status
                gästantal.addAndGet(-1) ;
                System.out.println("Bokning "+ (i+1) +" borttagen");
                break;
            }
            else if (i == 19){
                System.out.println("Det finns ingen bokning med denna information. Var vänlig och dubbelkolla att all information stämmer");
            }
        }
    }

    // Metod för att lägga till en bokning
    private void läggTillBokning(JTextField tf1, JTextField tf2, JComboBox cb1, JComboBox cb2, AtomicInteger gästantal) {
        for (int i = 0; i < 20; i++) {
            if (tf1.getText().equalsIgnoreCase("") || tf2.getText().equalsIgnoreCase("")) {
                System.out.println("Ett eller flera fält är tomma");
                break;
            }
            if (tf1.getText().length() != 12 || Integer.valueOf(tf1.getText().substring(0, 4)) < 1900 || Integer.valueOf(tf1.getText().substring(0, 4)) > 2022) {
                System.out.println("Ogiltigt personnummer");
                break;
            }
            if (tf1.getText().equalsIgnoreCase(gäster.persnum[i])) {
                System.out.println("Detta personnummer används redan");
                break;
            }
            if (gäster.persnum[i] == null && gäster.namn[i] == null) {
                gäster.persnum[gästantal.intValue()] = tf1.getText();
                gäster.namn[gästantal.intValue()] = tf2.getText();
                gäster.kön[gästantal.intValue()] = cb1.getSelectedItem().toString();
                gäster.ålder[gästantal.intValue()] = Year.now().getValue() - Integer.valueOf(tf1.getText().substring(0, 4));
                if (cb2.getSelectedItem() != VIPGäster.VIPStatus.EJ_VIP) {
                    gäster.sättVIPStatus(gästantal.intValue(), (VIPGäster.VIPStatus) cb2.getSelectedItem());
                    int kostnad = 300 + ((cb2.getSelectedIndex() + 1) * 100);
                    vinst.addAndGet(kostnad);
                } else {
                    vinst.addAndGet(300);
                }
                System.out.println("Bokningsplats " + (gästantal.intValue() + 1) + " reserveras för " + gäster.namn[gästantal.intValue()]);
                gästantal.addAndGet(1);
                break;
            }
        }
    }
}
