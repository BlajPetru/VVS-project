import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import main.Server;

public class GUI implements Runnable{
    private JButton startButton;
    private JButton maintenanceButton;
    private JButton stopButton;
    private JPanel panelMain;
    private JTextField textField1;
    Server server = new Server();
    int restart=0;
    int curentPort=0,oldPort=0;

    public GUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());


        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                curentPort = Integer.parseInt(textField1.getText());
                if (curentPort==oldPort && restart==1){
                    server.setStateServer(1);
                    JOptionPane.showMessageDialog(null, "Server restarted at port:"+curentPort);
                }
                if ((curentPort!=oldPort && restart==1) || restart==0){
                    startSRV();
                    restart=0;
                }
            }
        });

        maintenanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Server is now in maintenance mode!");
                server.setStateServer(2);
                startButton.setText("Restart");
                startButton.setEnabled(true);
                restart=1;
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Server was stoped!");
                server.setStateServer(3);
                startButton.setEnabled(true);
                restart=1;
            }
        });
    }


    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        JFrame frame=new JFrame("GUI");

        frame.setContentPane(new GUI().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void startSRV(){
        if (server.setPort(curentPort) && server.acceptServerPort()) {
            JOptionPane.showMessageDialog(null, "Server started at port:"+curentPort);
            startButton.setEnabled(false);
            oldPort=curentPort;
            server.setStateServer(1);
            Thread interfaceThread = null;
            try {
                interfaceThread = new Thread(new GUI());
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            } catch (UnsupportedLookAndFeelException unsupportedLookAndFeelException) {
                unsupportedLookAndFeelException.printStackTrace();
            } catch (InstantiationException instantiationException) {
                instantiationException.printStackTrace();
            } catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            }
            interfaceThread.start();
        } else {
            JOptionPane.showMessageDialog(null, "Port is already opened!");

        }
    }

    @Override
    public void run() {
        server.listenForClients();
    }
}
