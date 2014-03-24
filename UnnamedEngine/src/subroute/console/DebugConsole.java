package subroute.console;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import subroute.Game;

public class DebugConsole extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1095474827443662611L;
	private static DebugConsole instance;
	private StyledDocument con;
	private JTextPane ta = new JTextPane();
	private JScrollPane spane = new JScrollPane(ta);
	private JTextField tf = new JTextField();
	private ProcessReader pr;
	private static boolean stop = false;
	private int scrpos = 0;
	public static final Logger log = Logger.getLogger("Game");

	public DebugConsole() throws IOException{
		if(instance!=null){
			instance.dispose();
		}
		instance = this;
		ConsoleCommandRegistry.initCommands();
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
			if(!e.getActionCommand().isEmpty()){
				this.append(e.getActionCommand()+"\n","None");
				ConsoleCommandRegistry.executeCommand(e.getActionCommand());
				this.tf.setText("");
			}
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
			if(this.ta.getSize().height-scrpos>this.spane.getSize().height){
				JScrollBar bar = this.spane.getVerticalScrollBar();
				bar.setValue(bar.getMaximum());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class ProcessReader extends Thread{

		InputStream in, err;
		PrintStream oldout,olderr;

		public ProcessReader() throws IOException{
			this.oldout = System.out;
			this.olderr = System.err;
			PrintStream out = new PrintStream(new PipedOutputStream((PipedInputStream)(in=new PipedInputStream())));
			PrintStream er = new PrintStream(new PipedOutputStream((PipedInputStream)(err=new PipedInputStream())));
			System.setOut(out);
			System.setErr(er);
			this.start();
		}

		private String ins="",errs="";
		@Override
		public void run(){
			while(!stop){
				try{
				while(in.available()>0){
					char c = (char)in.read();
					this.oldout.write(c);
					if(c=='\n'){
						ins+=(c+"");
						Game.instance();
						append("["+Game.getTimeSinceStart()+"]:"+ins, "None");
						ins="";
						break;
					}else{
						ins+=(c+"");
					}
				}
				while(err.available()>0){
					char c = (char)err.read();
					olderr.write(c);
					if(c=='\n'){
						errs+=(c+"");
						Game.instance();
						append("["+Game.getTimeSinceStart()+"]:"+errs,"Error");
						errs="";
						break;
					}else{
						errs+=(c+"");
					}
				}
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
}
