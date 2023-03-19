import java.awt.Toolkit;

import javax.swing.JOptionPane;

public class PopupWindow {
  public static void show(String message, String title, String type) {
    int actualType;
    if (type.equals("plain")) {
      actualType = JOptionPane.PLAIN_MESSAGE;
    }
    else if (type.equals("info")) {
      actualType = JOptionPane.INFORMATION_MESSAGE;
    }
    else if (type.equals("question")) {
      actualType = JOptionPane.QUESTION_MESSAGE;
    }
    else if (type.equals("warning")) {
      actualType = JOptionPane.WARNING_MESSAGE;
    }
    else if (type.equals("error")) {
      actualType = JOptionPane.ERROR_MESSAGE;
    }
    else {
      actualType = JOptionPane.PLAIN_MESSAGE;
    }
    Toolkit.getDefaultToolkit().beep();
    JOptionPane.showMessageDialog(
      null, message, title, actualType
    );
  }
  public static void show(String message) {
    Toolkit.getDefaultToolkit().beep();
    JOptionPane.showMessageDialog(null, message);
  }
}
