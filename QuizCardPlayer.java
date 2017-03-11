package QuizCardsGame;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class QuizCardPlayer {
	
	private JTextArea display;
//	private JTextArea answer;
	private ArrayList<QuizCard> cardList;
	private QuizCard currentCard;
	private int currentCardIndex;
	private JFrame frame;
	private JButton nextButton;
	private boolean isShowAnswer;
	
	public static void main(String[] args) {
		QuizCardPlayer reader = new QuizCardPlayer();
		reader.go();
	}

	public void go() {
		// build gui
		
		this.frame = new JFrame("Quiz Card Player");
		JPanel mainPanel = new JPanel();
		Font bigFont = new Font("sanserif", Font.BOLD, 24);
		
		this.display = new JTextArea(6,25);
		this.display.setFont(bigFont);
		this.display.setLineWrap(true);
		this.display.setEditable(false);
		
		JScrollPane qScroller = new JScrollPane(this.display);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		this.nextButton = new JButton("Load New Card Set");
		
		mainPanel.add(qScroller);
		mainPanel.add(nextButton);
		this.nextButton.addActionListener(new NextCardListener());
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem loadMenuItem = new JMenuItem("Load Card Set");
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		loadMenuItem.addActionListener(new OpenMenuListener());
		exitMenuItem.addActionListener(new ExitMenuListener());
		fileMenu.add(loadMenuItem);
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);
		this.frame.setJMenuBar(menuBar);
		this.frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(640, 500);
		frame.setVisible(true);
	}
	
	public class NextCardListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(nextButton.getText().equals("Load New Card Set")){
				new QuizCardPlayer.OpenMenuListener().actionPerformed(null);
			}
			else if(isShowAnswer){
				// show the answer because they have seen the question
				display.setText(currentCard.getAnswer());
				nextButton.setText("Next Card");
				isShowAnswer = false;
			}
			else{
				// show the next question
				if(currentCardIndex < cardList.size()){
					showNextCard();
				}
				else{
					// there are no more cards!
					display.setText("That was Last Card");
//					nextButton.setEnabled(false);
					nextButton.setText("Load New Card Set");
				}
			}
		}
	}
	
	public class OpenMenuListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileOpen = new JFileChooser();
			fileOpen.showOpenDialog(frame);
			if(fileOpen.getSelectedFile() != null){
				loadFile(fileOpen.getSelectedFile());
			}
		}
	}
	
	public class ExitMenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	public void loadFile(File file) {
		this.cardList = new ArrayList<QuizCard>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = reader.readLine()) != null){
				makeCard(line);
			}
			reader.close();
		}
		catch (Exception ex){
			System.out.println("Couldn't read the card file");
			ex.printStackTrace();
		}
		
		// now time to start by showing the first card
		this.currentCardIndex = 0;
		showNextCard();
	}
	
	public void makeCard(String lineToParse) {
		String[] result = lineToParse.split("/");
		QuizCard card = new QuizCard(result[0], result[1]);
		this.cardList.add(card);
		System.out.println("made a card");
	}
	
	public void showNextCard() {
		this.currentCard = this.cardList.get(this.currentCardIndex);
		this.currentCardIndex++;
		this.display.setText(this.currentCard.getQuestion());
		this.nextButton.setText("Show Answer");
		this.isShowAnswer = true;
	}
	
}
