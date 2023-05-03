import javax.swing.*;

public class frmSpecialistDashboard extends JFrame {
  private static frmSpecialistDashboard instance;
  private JButton logOutButton;
  private JTextArea recentArea;
  private JButton postButton;
  private JLabel titleLabel;
  private JPanel panel;
  private JLabel insLabel;
  private JLabel upcomingLabel;
  private JScrollPane recentPane;
  private JTextField placeField;
  private JTextField feeField;
  private JTextField dateField;
  private JTextField descField;
  private JTextField extraField;
  private JLabel fillLabel;
  private JLabel placeLabel;
  private JLabel dateLabel;
  private JLabel feeLabel;
  private JLabel descLabel;
  private JLabel noteLabel;
  private JLabel copyrightLabel;
  private JButton resetPwdButton;
  private JButton clearAllButton;
  private JButton delistHealingButton;

  private frmSpecialistDashboard() {
    setContentPane(panel);
    setTitle("mindfulNESS - Specialist Dashboard");
    setSize(1200, 800);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    logOutButton.addActionListener(
        e -> {
          int option =
              JOptionPane.showConfirmDialog(
                  null,
                  "Are you sure you want to log out?",
                  "Confirmation",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.QUESTION_MESSAGE);
          if (option == JOptionPane.YES_OPTION) {
            logOutButton.setEnabled(false);
            SwingWorker<Void, Void> worker =
                new SwingWorker<>() {
                  @Override
                  protected Void doInBackground() {
                    try {
                      setVisible(false);
                      JOptionPane.showMessageDialog(
                          null,
                          "Logged out! See you again.",
                          "Success",
                          JOptionPane.WARNING_MESSAGE);
                      Thread.sleep(1000);
                      System.exit(0);
                    } catch (InterruptedException ex) {
                      throw new RuntimeException(ex);
                    }
                    return null;
                  }

                  @Override
                  protected void done() {
                    logOutButton.setEnabled(true);
                  }
                };
            worker.execute();
          }
        });
    resetPwdButton.addActionListener(
        e -> {
          JPasswordField oldPwd = new JPasswordField();
          JPasswordField newPwd = new JPasswordField();
          Object[] message = {"Old password: ", oldPwd, "New password: ", newPwd};
          int option =
              JOptionPane.showConfirmDialog(
                  null,
                  message,
                  "Changing Password",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.QUESTION_MESSAGE);
          boolean conditionCheck =
              !String.valueOf(oldPwd.getPassword()).equals(String.valueOf(newPwd.getPassword()));
          if ((option == JOptionPane.YES_OPTION) && conditionCheck) {
            resetPwdButton.setEnabled(false);
            SwingWorker<Void, Void> worker =
                new SwingWorker<>() {
                  @Override
                  protected Void doInBackground() {
                    if (ConnectSQL.submitPasswordUpdate(
                        frmIndex.getInstance().getCredentials()[0],
                        String.valueOf(oldPwd.getPassword()),
                        String.valueOf(newPwd.getPassword()))) {
                      JOptionPane.showMessageDialog(
                          null,
                          "Password changed successfully!",
                          "Success",
                          JOptionPane.INFORMATION_MESSAGE);
                      oldPwd.setText("");
                      newPwd.setText("");

                    } else {
                      JOptionPane.showMessageDialog(
                          null,
                          "The old password is incorrect!",
                          "Warning",
                          JOptionPane.WARNING_MESSAGE);
                      oldPwd.setText("");
                      newPwd.setText("");
                      resetPwdButton.setEnabled(true);
                    }
                    return null;
                  }

                  @Override
                  protected void done() {
                    resetPwdButton.setEnabled(true);
                  }
                };
            worker.execute();
          } else {
            JOptionPane.showMessageDialog(
                null,
                "The new password must different from old one!",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
          }
        });
    clearAllButton.addActionListener(
        e -> {
          int option =
              JOptionPane.showConfirmDialog(
                  null,
                  "Are you sure you want to clear all field(s)?",
                  "Confirmation",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.WARNING_MESSAGE);
          if (option == JOptionPane.YES_OPTION) {
            clearAllButton.setEnabled(false);
            SwingWorker<Void, Void> worker =
                new SwingWorker<>() {
                  @Override
                  protected Void doInBackground() {
                    placeField.setText("");
                    dateField.setText("");
                    feeField.setText("");
                    descField.setText("");
                    extraField.setText("");
                    return null;
                  }

                  @Override
                  protected void done() {
                    clearAllButton.setEnabled(true);
                  }
                };
            worker.execute();
          }
        });
    postButton.addActionListener(
        e -> {
          postButton.setEnabled(false);
          if (placeField.getText().isEmpty()
              || dateField.getText().isEmpty()
              || feeField.getText().isEmpty()
              || descField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(
                null, "Field(s) are empty!", "Warning", JOptionPane.WARNING_MESSAGE);
            postButton.setEnabled(true);
            return;
          }
          int option =
              JOptionPane.showConfirmDialog(
                  null,
                  "Confirm posting healing information?",
                  "Confirmation",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.QUESTION_MESSAGE);
          if (option == JOptionPane.YES_OPTION) {
            SwingWorker<Void, Void> worker =
                new SwingWorker<>() {
                  @Override
                  protected Void doInBackground() {
                    if (ConnectSQL.submitHealingUpdate(
                        frmIndex.getInstance().getID()[0],
                        placeField.getText(),
                        dateField.getText(),
                        feeField.getText(),
                        descField.getText(),
                        extraField.getText())) {
                      JOptionPane.showMessageDialog(
                          null,
                          "Healing information posted! Please check the nearby box.",
                          "Success",
                          JOptionPane.INFORMATION_MESSAGE);
                      placeField.setText("");
                      dateField.setText("");
                      feeField.setText("");
                      descField.setText("");
                      extraField.setText("");
                      recentArea.selectAll();
                      recentArea.replaceSelection("");
                      recentArea.setText(
                          ConnectSQL.showSpecialistBookingQuery(frmIndex.getInstance().getID()[0]));
                    } else {
                      JOptionPane.showMessageDialog(
                          null,
                          "Healing information posting error. Please try again!",
                          "Warning",
                          JOptionPane.WARNING_MESSAGE);
                    }
                    return null;
                  }

                  @Override
                  protected void done() {
                    postButton.setEnabled(true);
                  }
                };
            worker.execute();
          }
        });
    delistHealingButton.addActionListener(
        e -> {
          String idHeal =
              JOptionPane.showInputDialog(
                  null,
                  "Enter the healing ID you want to delist: ",
                  "Cancellation",
                  JOptionPane.INFORMATION_MESSAGE);
          if (idHeal != null && !idHeal.isBlank()) {
            int option =
                JOptionPane.showConfirmDialog(
                    null,
                    "Confirm delist healing with ID: " + idHeal + "?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
              delistHealingButton.setEnabled(false);
              SwingWorker<Void, Void> worker =
                  new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() {
                      if (ConnectSQL.delistHealingUpdate(
                          frmIndex.getInstance().getID()[0], idHeal)) {
                        JOptionPane.showMessageDialog(
                            null,
                            "Healing with ID: "
                                + idHeal
                                + " delisted. Check the nearby box for confirmation!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                        recentArea.selectAll();
                        recentArea.replaceSelection("");
                        recentArea.setText(
                            ConnectSQL.showSpecialistBookingQuery(
                                frmIndex.getInstance().getID()[0]));
                      } else {
                        JOptionPane.showMessageDialog(
                            null,
                            "Cannot delist healing with ID: "
                                + idHeal
                                + """
                                . Please only input "vacant" ID!""",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                      }
                      return null;
                    }

                    @Override
                    protected void done() {
                      delistHealingButton.setEnabled(true);
                    }
                  };
              worker.execute();
            }
          } else {
            JOptionPane.showMessageDialog(
                null, "Nothing to delisted!", "Warning", JOptionPane.WARNING_MESSAGE);
          }
        });
  }

  public static synchronized frmSpecialistDashboard getInstance() {
    if (instance == null) {
      instance = new frmSpecialistDashboard();
    }
    return instance;
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
    if (isVisible()) {
      insLabel.setText(
          "Welcome back, "
              + ConnectSQL.showNameQuery(
                  frmIndex.getInstance().getID()[0], frmIndex.getInstance().getID()[1])
              + "!");
      recentArea.selectAll();
      recentArea.replaceSelection("");
      recentArea.setText(ConnectSQL.showSpecialistBookingQuery(frmIndex.getInstance().getID()[0]));
      recentArea.setEditable(false);
    }
  }
}
