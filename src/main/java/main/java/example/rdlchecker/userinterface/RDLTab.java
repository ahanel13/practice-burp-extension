package main.java.example.rdlchecker.userinterface;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;
import main.java.example.loggerinterface.TableModel;
import main.java.example.loggerinterface.UiHttpHandler;
import main.java.example.rdlchecker.businesslogic.RDLUrlGenerator;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class RDLTab {

    private JLabel fileLabel;
    private JLabel domainLabel;
    private JTextField fileTextField;
    private JTextField domainTextField;
    private JButton browseButton;
    private JButton submitButton;
    private final Logging logging;
    public RDLTab(MontoyaApi api) {
        api.userInterface().registerSuiteTab("Process RDL", constructRDLTab());
        logging = api.logging();
    }

    private Component constructRDLTab() {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);

        fileLabel = new JLabel("File Location:");
        fileTextField = new JTextField(20);
        browseButton = new JButton("Browse");
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    fileTextField.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        domainLabel = new JLabel("Domain:");
        domainTextField = new JTextField("example.com", 20);
        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileLocation = fileTextField.getText();
                String domain = domainTextField.getText();

                // do something with fileLocation and domain strings here
                processRDL(fileLocation, domain);
            }
        });

        panel.setLayout(customizeLayout(layout));
        return panel;
    }

    private void processRDL(String fileLocation, String domain) {
        RDLUrlGenerator rdlUrlGenerator;
        try {
            rdlUrlGenerator = new RDLUrlGenerator(fileLocation, logging);

        } catch (IOException e) {
            logging.logToError("Failed to initialize RDL Processor");
            return;
        }
        rdlUrlGenerator.generateUrls();
    }

    private GroupLayout customizeLayout(GroupLayout layout) {
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // add checkboxes and description to layout
        JLabel checkBoxLabel = new JLabel("Select options:");
        JCheckBox checkBox1 = new JCheckBox("Option 1");
        JCheckBox checkBox2 = new JCheckBox("Option 2");
        JLabel descriptionLabel = new JLabel("<html><body>Select one or both options to enable some functionality.</body></html>");

        // Horizontal group
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(fileLabel)
                                        .addComponent(domainLabel)
                                        .addComponent(checkBoxLabel)
                                )
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(fileTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(domainTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        )
                                        .addComponent(checkBox1)
                                        .addComponent(checkBox2)
                                )
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(browseButton)
                                        .addComponent(submitButton)
                                )
                        )
                        .addComponent(descriptionLabel)
                        .addComponent(submitButton, GroupLayout.Alignment.TRAILING)
        );

        // Vertical group
        layout.setVerticalGroup(
                        layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(fileLabel)
                                        .addComponent(fileTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(browseButton))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(domainLabel)
                                        .addComponent(domainTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(submitButton))
                .addComponent(checkBoxLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(checkBox1)
                        .addComponent(descriptionLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(checkBox2)
                        .addComponent(descriptionLabel))
                .addGap(20)
                .addComponent(submitButton)
        );

        // Center everything in the panel
        layout.linkSize(SwingConstants.HORIZONTAL, fileLabel, domainLabel);
        layout.linkSize(SwingConstants.HORIZONTAL, fileTextField, domainTextField, browseButton);
        return layout;
    }
}
