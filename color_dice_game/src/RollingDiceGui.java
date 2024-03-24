import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RollingDiceGui extends JFrame {
    private JButton rollButton;
    private JButton[] numberButtons;
    private int selectedNumber = -1; // Default value when no button is selected
    private Color[] buttonColors = {Color.YELLOW, Color.WHITE, Color.PINK, Color.BLUE, Color.RED, Color.GREEN};

    public RollingDiceGui() {
        super("Rolling Random Dice");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 800));
        setResizable(false);
        setLocationRelativeTo(null);

        addGuiComponents();
    }

    

    private void addGuiComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());


        

        // Panel for the title and dice image
        JPanel topPanel = new JPanel(new BorderLayout());
        

        
        // Adding label above the title
        JLabel titleLabel = new JLabel("Guess the Number", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.NORTH);




        // Help Button
        JButton helpButton = new JButton("Help");
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String instructions = "Instructions:\n\nGuess a number from 1 to 6.\nClick 'Roll the Dice' to roll the dice.\nThe game will display whether you win or lose based on your guess.\nYou can only make one guess per roll.";
                JOptionPane.showMessageDialog(null, instructions, "Instructions", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Use FlowLayout for top-left placement
        JPanel buttonHelpPannel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonHelpPannel.add(helpButton);

        mainPanel.add(buttonHelpPannel, BorderLayout.NORTH);

        
        

  

        // Dice Image
        JLabel diceImg = ImgService.loadImage("resources/dice1.png");
        topPanel.add(diceImg, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.CENTER);

        // Panel for buttons (two rows)
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3));

        // Initialize array for number buttons
        numberButtons = new JButton[6];
        for (int i = 0; i < 6; i++) {
            numberButtons[i] = createNumberButton("" + (i + 1), i + 1, diceImg);
            buttonPanel.add(numberButtons[i]);
        }

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Roll Button
        rollButton = new JButton("Roll the Dice");
        rollButton.setEnabled(false); // Initially disable the button
        rollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rollButton.setEnabled(false);

                // Roll the dice
                Thread rollThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Roll animation
                            for (int i = 0; i < 10; i++) {
                                int diceValue = (int) (Math.random() * 6) + 1;
                                ImgService.updateImage(diceImg, "resources/dice" + diceValue + ".png");
                                Thread.sleep(100);
                            }

                            // Final roll result
                            int finalDiceValue = (int) (Math.random() * 6) + 1;
                            ImgService.updateImage(diceImg, "resources/dice" + finalDiceValue + ".png");

                            // Display result
                            if (finalDiceValue == selectedNumber) {
                                JOptionPane.showMessageDialog(null, "You win!");
                            } else {
                                JOptionPane.showMessageDialog(null, "You lose!");
                            }

                            // Reset selected number
                            selectedNumber = -1;

                            // Enable number buttons and disable rollButton
                            for (JButton button : numberButtons) {
                                button.setEnabled(true);
                                button.setBackground(buttonColors[Integer.parseInt(button.getActionCommand()) - 1]);
                            }
                            rollButton.setEnabled(false);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                rollThread.start();
            }
        });
        topPanel.add(rollButton, BorderLayout.SOUTH);

        this.getContentPane().add(mainPanel);
        pack();
    }

    private JButton createNumberButton(String buttonText, int number, JLabel diceImgLabel) {
        JButton button = new JButton(buttonText);
        button.setActionCommand(String.valueOf(number));
        button.setPreferredSize(new Dimension(200, 100)); // Increase button height
        button.setBackground(buttonColors[number - 1]);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set selected number
                selectedNumber = number;

                // Disable other buttons
                for (JButton btn : numberButtons) {
                    if (!btn.equals(button)) {
                        btn.setEnabled(false);
                        btn.setBackground(Color.GRAY); // Change background color of disabled buttons
                    }
                }
                rollButton.setEnabled(true); // Enable rollButton
            }
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RollingDiceGui().setVisible(true);
            }
        });
    }
}
