import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


/**
 * Created by cataldo.
 */
public class MyUI extends JFrame {


    private JPanel buttonPanel;
    private JScrollPane textPanel;
    private JButton jbtSELECT;
    private JTextArea jtaNote;

    public MyUI() {
        this.setLayout(new GridLayout(2, 1));
        this.setTitle("Metriche");


        jbtSELECT = new JButton("Load Java File");
        jbtSELECT.setToolTipText("Seleziona i file java");

        buttonPanel = new JPanel();
        buttonPanel.add(jbtSELECT);
        buttonPanel.setBorder(new LineBorder(Color.BLUE));
        this.add(buttonPanel);

        jtaNote = new JTextArea();
        jtaNote.setLineWrap(true);
        jtaNote.setWrapStyleWord(true);
        textPanel = new JScrollPane(jtaNote);
        textPanel.setBorder(new TitledBorder("Data"));
        this.add(textPanel);


        jbtSELECT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                FileFilter javaFilter = new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        if (file.getName().toLowerCase().endsWith(".java")) {
                            return true;
                        }
                        if (file.isDirectory())
                            return true;
                        return false;
                    }

                    @Override
                    public String getDescription() {
                        return "Java Files (.java)";
                    }
                };
                fileChooser.addChoosableFileFilter(javaFilter);
                fileChooser.setFileFilter(javaFilter);

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    java.io.File[] files = fileChooser.getSelectedFiles();
                    jtaNote.setText("");
                    for (File file : files) {
                        try {
                            Metrics metrics = new Metrics(file);
                            jtaNote.append(file.toString() + ":\n");
                            jtaNote.append("numberOfRows: " + metrics.getNumberOfRows() + "\n");
                            jtaNote.append("numberOfChars: " + metrics.getNumberOfChars() + "\n");
                            jtaNote.append("NumberOfMethods: " + metrics.getNumberOfMethods() + "\n");
                            jtaNote.append("AverageRowPerMethods: " + metrics.getAverageRowPerMethods() + "\n");
                        } catch (ArithmeticException e) {

                            jtaNote.setText("non ci sono metodi");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        this.pack();
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
