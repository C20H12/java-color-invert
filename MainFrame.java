import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {
  HashMap<String, Integer> g_colorValuesMap = new HashMap<>();
  JPanel[] g_colorOutputDisplayPanels = new JPanel[2];

  public MainFrame() {
    initFrame();

    g_colorValuesMap.put("R", 0);
    g_colorValuesMap.put("G", 0);
    g_colorValuesMap.put("B", 0);

    JPanel redPanel = createSliderPanel("R", new Color(255, 0, 0, 125));
    add(redPanel);
  
    JPanel greenPanel = createSliderPanel("G", new Color(0, 255, 0, 125));
    add(greenPanel);

    JPanel bluePanel = createSliderPanel("B", new Color(0, 0, 255, 125));
    add(bluePanel);

    JPanel bottomPortionPanel = new JPanel(new GridLayout(1, 3, 10, 0));
    bottomPortionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JPanel origColorPanel = createColorPanel("Selected", 0);
    bottomPortionPanel.add(origColorPanel);

    JPanel invColorPanel = createColorPanel("Inverted", 1);
    bottomPortionPanel.add(invColorPanel);

    JPanel colorInfoSidePanel = createInfoSidePanel();
    bottomPortionPanel.add(colorInfoSidePanel);

    add(bottomPortionPanel);

    setVisible(true);
  }

  private void initFrame() { 
    // initlaizes this window
    setSize(900, 500);
    setResizable(false);
    setTitle("Color Invert");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    setFont(new Font("SansSerif", Font.BOLD, 25));

    ImageIcon icon = new ImageIcon("icon.png");
    setIconImage(icon.getImage());

    getContentPane().setBackground(new Color(238, 238, 240));
  }

  private JPanel createSliderPanel(String name, Color c) {
    // initializes a panel with a lable, slider, input, button
    JPanel pane = new JPanel() {
      @Override // this is needed to paint the panel after its child
      protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
      }
    };
    pane.setMaximumSize(new Dimension(900, 60));
    pane.setOpaque(false); // parent will paint first
    pane.setBackground(c);
    pane.setLayout(new FlowLayout()); // flexbox basically

    JLabel nameLbl = new JLabel(name);
    nameLbl.setFont(getFont());

    JSlider slider = new JSlider(0, 255, 0);
    slider.setPreferredSize(new Dimension(750, 50));
    slider.setPaintTicks(true);
    slider.setMinorTickSpacing(5);
    slider.setPaintTrack(true);
    slider.setMajorTickSpacing(15);
    slider.setPaintLabels(true);
    slider.setOpaque(false);
    slider.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

    JTextField valInputBox = new JTextField();
    valInputBox.setPreferredSize(new Dimension(50, 25));
    valInputBox.setText("0");

    JButton enterButton = new JButton();
    enterButton.setMargin(new Insets(2, 2, 2, 2));
    enterButton.setText("Set");
    enterButton.setFocusable(false);
    
    
    // event listeners
    slider.addChangeListener((e) -> {
      // set the number in the text box and update the global color vals
      valInputBox.setText(Integer.toString(slider.getValue()));
      g_colorValuesMap.put(name, slider.getValue());
    });

    ActionListener callback = e -> {
      // checks if text in the input box is valid
      String input = valInputBox.getText();
      if (input.length() == 0) {
        PopupWindow.show(
          "Empty input!!",
          "Error...", "error"
        );
        return;
      }
      int inputNumber;
      try {
        inputNumber = Integer.parseInt(input);
      } catch (NumberFormatException _except) {
        PopupWindow.show(
          "Invalid number, contains invalid characters!",
          "Error...", "error"
        );
        return;
      }
      if (inputNumber > 255) {
        PopupWindow.show(
          "Invalid number, should be in the range 0 - 255!",
          "Error...", "error"
        );
        return;
      }
      SwingUtilities.invokeLater(() -> {
        slider.setValue(inputNumber);
      });
    };

    valInputBox.addActionListener(callback); // triggers on enter press

    enterButton.addActionListener(callback);
    
    // adding them in order of left to right
    pane.add(nameLbl);
    pane.add(slider);
    pane.add(valInputBox);
    pane.add(enterButton);

    return pane;
  }

  private JPanel createColorPanel(String title, int idx) {
    // creates a color output panel
    JPanel pane = new JPanel();
    pane.setBorder(BorderFactory.createLineBorder(Color.black));
    pane.setLayout(null);
    
    JLabel label = new JLabel(title);
    label.setFont(getFont());
    label.setBounds(43, 10, 300, 30);
    pane.add(label);
    
    JPanel colorDisp = new JPanel();
    colorDisp.setBackground(getBackground());
    colorDisp.setBounds(43, 50, 200, 200);
    pane.add(colorDisp);

    // stores the actual display panel to the global array
    g_colorOutputDisplayPanels[idx] = colorDisp;
    
    return pane;
  }

  private JPanel createInfoSidePanel() {
    // creates a panel with a GO button, inverted color values, and a hex output
    JPanel pane = new JPanel();
    pane.setBorder(BorderFactory.createLineBorder(Color.black));
    pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    JButton btn = new JButton();
    btn.setText("GO");
    btn.setMaximumSize(new Dimension(300, 50));
    btn.setFont(getFont());
    btn.setFocusable(false);

    JLabel outputR = new JLabel();
    outputR.setFont(getFont());
    JLabel outputG = new JLabel();
    outputG.setFont(getFont());
    JLabel outputB = new JLabel();
    outputB.setFont(getFont());
    JLabel outputHex = new JLabel();
    outputHex.setFont(getFont());

    btn.addActionListener(e -> {
      int r = g_colorValuesMap.get("R");
      int g = g_colorValuesMap.get("G");
      int b = g_colorValuesMap.get("B");
      int invR = 255 - r;
      int invG = 255 - g;
      int invB = 255 - b;
      JPanel colorOrig = g_colorOutputDisplayPanels[0];
      JPanel colorChanged = g_colorOutputDisplayPanels[1];
      colorOrig.setBackground(new Color(r, g, b));
      colorChanged.setBackground(new Color(invR, invG, invB));
      outputR.setText("Inv R: " + Integer.toString(invR));
      outputG.setText("Inv G: " + Integer.toString(invG));
      outputB.setText("Inv B: " + Integer.toString(invB));
      outputHex.setText(
        toPaddedHexString(r) +
        toPaddedHexString(g) +
        toPaddedHexString(b) + " => " +
        toPaddedHexString(invR) +
        toPaddedHexString(invG) +
        toPaddedHexString(invB)
      );
    });

    pane.add(btn);
    pane.add(outputR);
    pane.add(outputG);
    pane.add(outputB);
    pane.add(outputHex);

    return pane;
  }

  private String toPaddedHexString(int in) {
    // pad the hex so 0 - 9 is like 00 - 09
    if (in < 16) {
      return "0" + Integer.toHexString(in);
    }
    return Integer.toHexString(in);
  }
}
