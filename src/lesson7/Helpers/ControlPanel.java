package lesson7.Helpers;


import lesson7.Client.ClientApp;
import lesson7.Server.ServerApp;
import lesson7.Server.ServerHandler;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.*;

public class ControlPanel extends JFrame {
    private JCheckBox autoAuthChcBox;
    private JButton createClientBtn;
    private JButton openServerBtn;
    private JTextField serverPortTField;

    public static ServerHandler getCurrentServer() {
        return currentServer;
    }

    public static void setCurrentServer(ServerHandler currentServer) {
        ControlPanel.currentServer = currentServer;
    }

    private static ServerHandler currentServer;
    private int port;



    public ControlPanel() {
        prepareGUI();
    }

    private boolean getAutoAuthChcBoxStatus() {
        return autoAuthChcBox.isSelected();
    }

    //region GUI methods
    private void prepareGUI() {
        setTitle("Панель управления");
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setWindowSize();
        addComponents();
        setVisible(true);
    }

    private void setWindowSize() {
        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = sSize.height;
        int width = sSize.width;
        setSize(width / 4, height / 2);
        setLocation((width / 2) - (getWidth() / 2), (height / 2) - (getHeight() / 2));
    }

    private void addComponents() {
        addServerComponents();
        addClientComponents();
        setComponentsEnabled(false);
    }

    //region Adding server components
    private void addServerComponents() {
        addEmptyRow(0);
        placeComponent(getContentPane(), new JLabel("Порт сервера"), 0, 1, 1, 50, NONE, CENTER);
        addServerPortTField();
        addOpenServerButton();
        addEmptyRow(3);
    }

    private void addServerPortTField() {
        serverPortTField = new JTextField();
        serverPortTField.setText("8834");
        serverPortTField.setSize(100, 25);
        placeComponent(getContentPane(), serverPortTField, 0, 2, 1, 50, HORIZONTAL, CENTER);
    }

    private void addOpenServerButton() {
        openServerBtn = new JButton("Открыть сервер");
        openServerBtn.addActionListener(e -> openServerAction());
        placeComponent(getContentPane(), openServerBtn, 1, 1, 2, 10, NONE, WEST);
    }
    //endregion

    //region Adding client components
    private void addClientComponents() {
        addAutoAuthPanel();
        addCreateClientButton();
        addEmptyRow(6);
    }

    private void addCreateClientButton() {
        createClientBtn = new JButton("Добавить клиента");
        createClientBtn.addActionListener(e -> createClientAction());
        placeComponent(getContentPane(), createClientBtn, 5, 2, NONE, NORTH);
    }

    private void addAutoAuthPanel() {
        autoAuthChcBox = new JCheckBox();
        var autoAuthPanel = new JPanel();
        autoAuthPanel.setLayout(new FlowLayout());
        autoAuthPanel.add(autoAuthChcBox);
        autoAuthPanel.add(new JLabel("Авто аутентификация клиентов (кроме первого)"));
        placeComponent(getContentPane(), autoAuthPanel, 4, 2, HORIZONTAL, SOUTH);
    }
    //endregion

    //region Buttons actions
    private void openServerAction() {
        try {
            port = Integer.parseInt(serverPortTField.getText());
        } catch (NumberFormatException ex) {
            return;
        }
        new Thread(() -> {
            new ServerApp(this, port);
            setComponentsEnabled(true);
        }).start();
    }

    private synchronized void createClientAction() {
        createClientBtn.setEnabled(false);
        new Thread(() -> new ClientApp("localhost", port, getAutoAuthChcBoxStatus())).start();
        createClientBtn.setEnabled(true);
    }

    public void setComponentsEnabled(boolean isEnabled) {
        createClientBtn.setEnabled(isEnabled);
        autoAuthChcBox.setEnabled(isEnabled);
        serverPortTField.setEnabled(!isEnabled);
        openServerBtn.setEnabled(!isEnabled);
    }

    //endregion

    //endregion

    //region grid methods
    private void placeComponent(Container container, Component component, int gridX, int gridY, int gridHeight, int leftInsets, int fill, int anchor) {
        var gbc = new GridBagConstraints();
        gbc.gridx = gridX;
        gbc.gridy = gridY;
        gbc.gridheight = gridHeight;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, leftInsets, 0, 0);
        gbc.fill = fill;
        gbc.anchor = anchor;
        gbc.weightx = 0.1f;
        container.add(component, gbc);
    }

    private void placeComponent(Container container, Component component, int gridY, int gridWidth, int fill, int anchor) {
        var gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.gridheight = 1;
        gbc.gridwidth = gridWidth;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = fill;
        gbc.anchor = anchor;
        gbc.weightx = 0;
        container.add(component, gbc);
    }

    private void addEmptyRow(int y) {
        var gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.fill = BOTH;
        gbc.weighty = 1f;
        getContentPane().add(Box.createGlue(), gbc);
    }
    //endregion
}
