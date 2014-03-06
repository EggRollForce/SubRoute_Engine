package subroute.debug;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class DebugConsole extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1095474827443662611L;
	private static DebugConsole instance;
	private StyledDocument con;
	private JTextPane ta = new JTextPane();
	private JScrollPane spane = new JScrollPane(ta);
	private JTextField tf = new JTextField();
	private ProcessReader pr;
	private static boolean stop = false;

	public DebugConsole() throws IOException{
		if(instance!=null){
			instance.dispose();
		}
		instance = this;
//			in = game.getInputStream();
//			out = game.getOutputStream();
//			err = game.getErrorStream();
		this.con = ta.getStyledDocument();
		this.setMinimumSize(new Dimension(800,600));
		this.setResizable(true);
		GroupLayout layt = new GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(layt);
		this.ta.setAutoscrolls(true);
		ta.setEditable(false);
		ta.setVisible(true);

		StyleConstants.setForeground(con.addStyle("Error", null), Color.red);
		StyleConstants.setForeground(con.addStyle("None", null), Color.black);

		tf.addActionListener(this);

		layt.setHorizontalGroup(
			layt.createParallelGroup()
				.addComponent(spane)
				.addComponent(tf)
		);

		layt.setVerticalGroup(
			layt.createSequentialGroup()
				.addComponent(spane)
				.addComponent(tf, 20, 20, 20)
		);
		this.setVisible(true);
		pr = new ProcessReader();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
//		System.out.println(e);
		if(e.getSource().equals(this.tf)){
			this.append(e.getActionCommand()+"\n","None");
		}
	}

	@Override
	public void dispose(){
		super.dispose();
		stop = true;
	}

	public static DebugConsole instance(){
		return instance;
	}

	private void append(String str, String style){
		try {
			this.con.insertString(con.getLength(), str, con.getStyle(style));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private class ProcessReader extends Thread{

		InputStream in, err;

		public ProcessReader() throws IOException{
			PrintStream out = new PrintStream(new PipedOutputStream((PipedInputStream)(in=new PipedInputStream())));
			PrintStream er = new PrintStream(new PipedOutputStream((PipedInputStream)(err=new PipedInputStream())));
			System.setOut(out);
			System.setErr(er);
			this.start();
		}

		@Override
		public void run(){
			while(!stop){
				try{
				while(in.available()>0){
					append((char)in.read()+"", "None");
				}
				while(err.available()>0){
					append((char)err.read()+"","Error");
				}
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
}
