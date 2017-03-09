package ins;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JTextField;

public class GUIanalyser {

	static void showComponents(Container c, int depth) {
		Component[] l=c.getComponents();
		if (depth==0) {
			System.out.println("["+c.getClass().toString()+"] "+c.getName());
		}
		for (Component d:l) {
			for (int i=0; i<(depth+1); i++) System.out.print(" ");
			System.out.println("["+c.getClass().toString()+"] "+"\""+d.getName()+"\"");
			if (d instanceof Container) {
				showComponents( (Container)d, depth+1 );
			}			
		}
	}
	
	static void showComponent(Component c) {
		System.out.print("["+c.getClass().toString()+"] "+c.getName());
		if (c.getClass()==JTextField.class) {
			JTextField t = (JTextField)c;
			System.out.println(" = \""+t.getText()+"\""+" "+t.getText().length()+" chars");
		}
		else if (c.getClass()==JButton.class) {
			JButton t = (JButton)c;
			System.out.println(" = "+t.getText());
		} 
		else {
			System.out.println();
		}
	}

	static void showGUIinfo(Container c, int depth) {
		Component[] l=c.getComponents();
		if (depth==0)
			showComponent(c);
		for (Component d:l) {
			for (int i=0; i<(depth+1); i++) System.out.print(" ");
			showComponent(d);
			if (d instanceof Container) {
				showGUIinfo( (Container)d, depth+1 );
			}			
		}
	}

	public static void main(String[] args) {
		Container t = new Insurer();
		showComponents(t, 5);
		showGUIinfo(t,5);
		System.exit(0);
	}

}
