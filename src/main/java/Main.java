import fr.litarvan.openauth.Authenticator;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import me.h12z.LauncherLib.Launcher;
import me.marnic.jdl.Downloader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;

public class Main extends JFrame {
    public static void main(String[] args) {

        //Set the Jframe Properties

        JFrame frame = new JFrame("Launcher");
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        frame.setSize(500, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 2));

        //Add the Login Button

        JButton loginButton = new JButton("Login");

        //Add Login Button Listener
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Define Email and Password Field Values

                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                //Check if Email or Password is empty

                if (!email.isEmpty() && !password.isEmpty()) {

                    //Create the Authenticator

                    MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                    MicrosoftAuthResult result = null;

                    try {

                        //Try to log in with Credentials from Dialog

                        result = authenticator.loginWithCredentials(email, password);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return;
                    }

                    try {

                        //Windows Folder Path for Jar

                        String sep = File.separator;
                        String jarPath = System.getProperty("user.home") + sep + "AppData" + sep + "Roaming" + sep + ".minecraft" + sep + "versions" + sep + "BenClient";
                        String jarfileName = "BenClient.jar";

                        //Windows Folder Path for Launcher Library

                        String folderpath = System.getProperty("user.home") + sep + "AppData" + sep + "Roaming" + sep + ".minecraft";


                        //Check if JarFile Exists

                        File jarFile = new File(jarPath, jarfileName);

                        if (!jarFile.exists()) {

                            //Show Download Message

                            JOptionPane.showMessageDialog(frame, "Der Client wird im hintergrund herruntergeladen bitte gedulde dich etwas.");

                            //Create the Downloader

                            Downloader downloader = new Downloader(true);

                            //Get the Download Path

                            String downloadPath = System.getProperty("user.home") + sep + "AppData" + sep + "Roaming" + sep + ".minecraft" + sep + "versions" + sep + "BenClient";

                            // Convert String to Path

                            File mkdir = new File(downloadPath);

                            // Check for Path if not Exists

                            if (!mkdir.exists()) {

                                //Making the Path if not Existing

                                mkdir.mkdirs();
                            }

                            // Define DownloadPaths

                            File jar = new File(downloadPath + sep + "BenClient.jar");
                            File json = new File( downloadPath + sep + "BenClient.json");

                            //Download the Files from Github

                            downloader.downloadFileToLocation("https://github.com/Bensonheimer992/BenClient-Installer/raw/main/BenClient.jar", jar.getAbsolutePath());
                            downloader.downloadFileToLocation("https://github.com/Bensonheimer992/BenClient-Installer/raw/main/BenClient.json", json.getAbsolutePath());

                            JOptionPane.showMessageDialog(frame, "Der Client wurde herruntergeladen. Drücke nochmal um ihn zu starten.");
                        } else {

                            //Send Starting Message

                            JOptionPane.showMessageDialog(frame, "Der Client startet.");

                            //Create the Launcher

                            Launcher launcher = new Launcher(folderpath, "BenClient", result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken());

                            //Launch the Game

                            launcher.launch();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Bitte gib Email und Passwot ein.");
                }
            }
        });

        frame.add(emailLabel);
        frame.add(emailField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(new JLabel()); // Placeholder
        frame.add(loginButton);

        frame.setVisible(true);
    }
}
