package view;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class LoadingWindow extends JFrame {

	private static final long serialVersionUID = -5067214064403276335L;

	public LoadingWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(200, 75));
		setLocationByPlatform(true);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(new JLabel("Loading application context"));
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		add(progressBar);
		pack();
		setVisible(true);
	}
}
