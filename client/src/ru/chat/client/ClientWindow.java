package ru.chat.client;

import ru.network.TCPConnection;
import ru.network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {
    private static final String IP_ADDR = "127.0.0.1";
    private static final int PORT = 8189;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }

    private final JTextArea log = new JTextArea();
    private final JTextField fieldNickName = new JTextField("noName");
    private final JTextField fieldInput = new JTextField();
    private TCPConnection connection;

    private ClientWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        log.setEditable(false);
        log.setLineWrap(true);
        fieldInput.addActionListener(this);
        add(log, BorderLayout.CENTER);
        add(fieldInput, BorderLayout.SOUTH);
        add(fieldNickName, BorderLayout.NORTH);
        setVisible(true);
        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
        } catch (IOException e) {
            printMessage("Connection exception: " + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText();
         if (msg.equals("")) return;
         else {
             fieldInput.setText(null);
             connection.sendString(fieldNickName.getText()+": "+msg);
         }
    }


    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMessage("Connection ready...");

    }

    @Override
    public void onRecieveString(TCPConnection tcpConnection, String value) {
        printMessage(value);

    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMessage("Connection closed... ");

    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMessage("Connection exception: " + e);
    }

    private synchronized void printMessage(String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
