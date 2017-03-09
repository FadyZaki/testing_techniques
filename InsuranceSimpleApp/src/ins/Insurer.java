package ins;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Insurer extends JFrame {

	String genderNames[] = {"Male","Female"};
	JFrame window;
	static final long serialVersionUID=0;
	JTextField age = new JTextField();
	JCheckBox married = new JCheckBox();
	JComboBox gender = new JComboBox();
	JTextField result = new JTextField("xxxxxxxxxxxxxxxx");
	JLabel config = new JLabel("<html><body>Enter age (16-65)<br>Select gender and whether married.</body></html>"), ageP, marriedP, genderP;
	JButton checkButton, continueButton, infoButton, exitButton;
	
	Insurer() {
		window = this;
		setTitle("Car Insurance");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		ageP = new JLabel("Age of customer");
		getContentPane().add( ageP );
		ageP.setBounds( 25, 25, 100, ageP.getPreferredSize().height );
		age.setName("age");
		getContentPane().add( age );
		age.setBounds( 150, 25, 100, age.getPreferredSize().height );
		marriedP = new JLabel("Married?");
		getContentPane().add( marriedP );
		marriedP.setBounds( 25, 55, 100, marriedP.getPreferredSize().height );
		married.setName("married");
		married.setBackground(Color.LIGHT_GRAY);
		getContentPane().add( married );
		married.setBounds( 147, 52, married.getPreferredSize().width, married.getPreferredSize().height );
		genderP = new JLabel("Gender");
		getContentPane().add( genderP );
		genderP.setBounds( 25, 85, 100, genderP.getPreferredSize().height );
		for (String gn: genderNames)
			gender.addItem(gn);
		gender.setName("gender");
		getContentPane().add( gender );
		gender.setBounds( 150, 85, gender.getPreferredSize().width, gender.getPreferredSize().height );
		checkButton = new JButton("Check");
		checkButton.setName("checkbutton");
		checkButton.setFocusPainted(false);
		getContentPane().add( checkButton );
		checkButton.setBounds( 25, 120, checkButton.getPreferredSize().width, checkButton.getPreferredSize().height);
        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	int a=-1;
            	try { a = Integer.parseInt(age.getText().trim()); }
            	catch (Exception ex) {}
            	boolean m=married.isSelected();
            	char g;
            	if (genderNames[gender.getSelectedIndex()].equals("Male"))
            		g = 'M';
            	else
            		g = 'F';
            	int premium = Insurance.premium(a,g,m);
            	if (premium>0)
            		result.setText(Integer.toString(premium));
           		else
           			result.setText("ERROR.");
                continueButton.setVisible(true);
                result.setVisible(true);
                checkButton.setVisible(false);
                exitButton.setVisible(false);
        		window.setTitle("Premium");
            }
        }); 
		infoButton = new JButton("Info");
		infoButton.setName("infobutton");
		infoButton.setFocusPainted(false);
		getContentPane().add( infoButton );
		infoButton.setBounds( 150, 120, infoButton.getPreferredSize().width, infoButton.getPreferredSize().height);
		infoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                config.setVisible(true);
                continueButton.setVisible(true);
                checkButton.setVisible(false);
                married.setVisible(false);
                gender.setVisible(false);
                exitButton.setVisible(false);
                infoButton.setVisible(false);
                marriedP.setVisible(false);
                genderP.setVisible(false);
                ageP.setVisible(false);
                age.setVisible(false);
                result.setVisible(false);
        		window.setTitle("Premium Information");
            }
        }); 
		result.setName("result");		
		getContentPane().add( result );
		result.setBounds( 25, 130, result.getPreferredSize().width, result.getPreferredSize().height );
        result.setVisible(false);
        getContentPane().add( config );
        config.setName("configInfo");
        config.setBounds( 25, 25, config.getPreferredSize().width, config.getPreferredSize().height );
        config.setVisible(false);
		continueButton = new JButton("Continue");
		continueButton.setName("continuebutton");
		continueButton.setFocusPainted(false);
		getContentPane().add( continueButton );
		continueButton.setBounds( 25, 160, continueButton.getPreferredSize().width, continueButton.getPreferredSize().height);
        continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                continueButton.setVisible(false);
                config.setVisible(false);
                exitButton.setVisible(true);
                result.setVisible(false);  
                checkButton.setVisible(true);
                married.setVisible(true);
                marriedP.setVisible(true);
                gender.setVisible(true);
                genderP.setVisible(true);
                infoButton.setVisible(true);
                ageP.setVisible(true);
                age.setVisible(true);
        		window.setTitle("Car Insurance");
            }
        }); 
        continueButton.setVisible(false);
		exitButton = new JButton("Exit");
		exitButton.setName("exitbutton");
		exitButton.setFocusPainted(false);
		getContentPane().add( exitButton );
		exitButton.setBounds( 25, 160, exitButton.getPreferredSize().width, exitButton.getPreferredSize().height);
		exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "Do you really want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                	System.exit(0);
            }
        }); 
		setSize(350,230);
		setVisible(true);
	}
	
	public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Insurer().setVisible(true);
            }
        });		
	}
}
