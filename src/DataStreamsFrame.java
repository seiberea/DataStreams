import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class DataStreamsFrame  extends JFrame
{
    private JPanel mainPanel, inputPanel, displayPanel, buttonPanel;
    private JLabel titleLabel;
    private JTextField inputTextField;
    private JTextArea originalTextArea, filteredTextArea;
    private JScrollPane originalScroll, filteredScroll;
    private JButton loadButton, searchButton, quitButton;
    private JFileChooser chooser;
    private String displayFile;
    private Path originalFilePath;
    private ActionListener quit = new quitListener();
    private ActionListener load = new loadListener();
    private ActionListener search = new searchListener();

    DataStreamsFrame()
    {
        setTitle("Data Streams");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        inputPanel = new JPanel();
        displayPanel = new JPanel();
        buttonPanel = new JPanel();
        titleLabel = new JLabel("Data Streams");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 40));
        inputTextField = new JTextField();
        inputTextField.setFont(new Font("Times New Roman", Font.BOLD, 20));

        originalTextArea = new JTextArea(5,5);
        TitledBorder originalBorder = BorderFactory.createTitledBorder("Original Text");
        originalBorder.setTitleFont(new Font("Times New Roman", Font.BOLD, 20));
        originalTextArea.setBorder(originalBorder);
        originalTextArea.setFont(new Font("Times New Roman", Font.BOLD, 15));
        originalScroll = new JScrollPane(originalTextArea);

        filteredTextArea = new JTextArea(5,5);
        TitledBorder filteredBorder = BorderFactory.createTitledBorder("Filtered Text");
        filteredBorder.setTitleFont(new Font("Times New Roman", Font.BOLD, 20));
        filteredTextArea.setBorder(filteredBorder);
        filteredTextArea.setFont(new Font("Times New Roman", Font.BOLD, 15));
        filteredScroll = new JScrollPane(filteredTextArea);

        loadButton = new JButton("Load File");
        loadButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
        loadButton.addActionListener(load);

        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
        searchButton.addActionListener(search);

        quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
        quitButton.addActionListener(quit);

        chooser = new JFileChooser();

        add(mainPanel);
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        inputPanel.setLayout(new GridLayout(2,1));
        inputPanel.add(titleLabel);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        inputPanel.add(inputTextField);

        mainPanel.add(displayPanel, BorderLayout.CENTER);
        displayPanel.setLayout(new GridLayout(1,2));
        displayPanel.add(originalScroll);
        displayPanel.add(filteredScroll);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        buttonPanel.add(loadButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(quitButton);
    }
    private class loadListener implements ActionListener
    {
        public void actionPerformed(ActionEvent AE)
        {
            File workingDirectory = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                File selectedFile = chooser.getSelectedFile();
                originalFilePath = selectedFile.toPath();
                try (Stream lines = Files.lines(Paths.get(originalFilePath.toString())))
                {
                    displayFile = (String) lines.collect(Collectors.joining("\n"));
                    originalTextArea.append(displayFile);
                }
                catch (FileNotFoundException e)
                {
                    System.out.println("File not found!");
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    private class searchListener implements ActionListener
    {
        public void actionPerformed(ActionEvent AE)
        {
            String filter = inputTextField.getText();
            try (Stream lines = Files.lines(Paths.get(originalFilePath.toString()))) {
                displayFile = (String) lines
                        .filter(w -> w.toString().contains(filter))
                        .collect(Collectors.joining("\n"));
                filteredTextArea.setText(displayFile);
            }
            catch (FileNotFoundException e)
            {
                System.out.println("File not found!");
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private class quitListener implements ActionListener
    {
        public void actionPerformed(ActionEvent AE)
        {
            System.exit(0);
        }
    }
}
