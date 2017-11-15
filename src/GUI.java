import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.SwingConstants.TOP;

public class GUI extends JFrame implements ActionListener, RobotEventListener {

    private JPanel panel;
    private JLabel greetingLabel;
    private JLabel distanceLabel;
    private JLabel legsLabel;
    private JTextField textField;
    private JTextArea textArea;
    private JSpinner spinner;
    private JButton button;
    private JScrollPane scroll;
    private Image image;

    private int legsCount;
    private double distance;
    private final int LOWER_BOUND = 0;
    private final int INCREMENT = 1;
    private boolean isValuesSet;

    public GUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(750, 620);
        setResizable(false);
        panel = new JPanel();
        panel.setLayout(null);

        image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("rob.png"));
        this.setIconImage(image);

        greetingLabel = new JLabel("Welcome!");
        greetingLabel.setSize(150, 21);
        greetingLabel.setFont(new Font("Verdana", Font.BOLD, 22));
        greetingLabel.setVerticalAlignment(TOP);
        greetingLabel.setLocation(290, 17);
        panel.add(greetingLabel);

        distanceLabel = new JLabel("Set distance (meter):");
        distanceLabel.setSize(150, 15);
        distanceLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        distanceLabel.setVerticalAlignment(TOP);
        distanceLabel.setLocation(10, 52);
        panel.add(distanceLabel);

        legsLabel = new JLabel("Set a number of legs:");
        legsLabel.setSize(150, 17);
        legsLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        legsLabel.setVerticalAlignment(TOP);
        legsLabel.setLocation(538, 107);
        panel.add(legsLabel);

        textField = new JTextField();
        textField.setSize(250, 30);
        textField.setLocation(10, 70);
        panel.add(textField);

        textArea = new JTextArea();
        textArea.setEditable(false);

        scroll = new JScrollPane(textArea);
        scroll.setSize(520,450);
        scroll.setLocation(10,107);
        scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll);

        spinner = new JSpinner(new SpinnerNumberModel(LOWER_BOUND, LOWER_BOUND, null, INCREMENT));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setEditable(false);
        editor.getTextField().setBackground(Color.white);
        spinner.setSize(200, 30);
        spinner.setLocation(538, 125);
        panel.add(spinner);

        button = new JButton("Ok");
        button.setSize(120, 30);
        button.setLocation(538, 161);
        panel.add(button);
        button.addActionListener(this);

        setContentPane(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        legsCount = 0;
        isValuesSet = true;
        button.setEnabled(true);
        textArea.setText("");
        valuesSet();
        if(isValuesSet) {
            new Robot(this, legsCount, distance);
        }
        refreshForm();
    }

    public void valuesSet() {
        legsCount = (Integer) spinner.getValue();
        if (legsCount == 0) {
            textArea.append("Please, set the number of legs. \n");
            isValuesSet = false;
        }
        try {
            distance = Integer.parseInt(textField.getText());
        } catch (NumberFormatException ex) {
            textArea.append("Please, set distance. \n");
            isValuesSet = false;
        }
    }

    private void refreshForm() {
        textField.setText("");
        spinner.setValue(0);
    }

    @Override
    public void stepDone(int leg) {
        appendResult("The robot moved with ".concat(Integer.toString(leg).concat(" leg.\n")));
    }

    @Override
    public void robotStopped(int steps) {
        appendResult(Integer.toString(steps).concat(" steps have been done.\n")
                    .concat("Distance has been passed.\n"));
    }

    private void appendResult(String str) {
        textArea.append(str);
    }
}