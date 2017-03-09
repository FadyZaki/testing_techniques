package ins;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.testng.annotations.*;

import static org.testng.Assert.*;

public class InsurerSimpleTest extends SimpleSwingTestFramework {

   // Test window
   Insurer t;

   // Use the same instance for each test method
   @BeforeClass void setupInstance() throws Exception {
      t=new Insurer();
      t.setVisible(true);
      t.toFront();
      // wait for all GUI events to be processed before continuing
      robot.waitForIdle();
      while (!t.isVisible()) Thread.sleep(100);
      // make sure the window is visible and focused before continuing
      t.requestFocus();
      robot.waitForIdle();
      while (!t.isFocusOwner()) Thread.sleep(100);      
   }
   
   @AfterClass void shutdown() {
      t.dispose();
   }

   @Test (timeOut=1000) public void test1() throws Exception {
      assertEquals( getActiveTitle(), "Car Insurance" );
      type( (JTextField)find(t,"age",JTextField.class), "40" );
      assertEquals( ((JTextField)find(t,"age",JTextField.class)).getText(), "40" );
      select( (JCheckBox)find(t,"married",JCheckBox.class), true );
      select( (JComboBox)find(t,"gender",JComboBox.class), 0 );
      click( (JButton)find(t,"checkbutton", JButton.class) );
      assertEquals( getActiveTitle(), "Premium" );
      assertEquals( ((JTextField)find(t,"result",JTextField.class)).getText(), "300" );
      click( (JButton)find(t,"continuebutton", JButton.class) );
      assertEquals( getActiveTitle(), "Car Insurance" );
   }

}

