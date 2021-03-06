import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


public class MusicPlayer implements ActionListener {
    
	JFrame frame;
    
	JLabel songNameLabel = new JLabel();
    JButton selectButton = new JButton("Select Mp3");
    JButton playButton = new JButton("▶ Play");
    JButton pauseButton = new JButton("⏯ Pause");
    JButton resumeButton = new JButton("🔊 Resume");
    JButton stopButton = new JButton("⏹ Stop");
    
    JFileChooser fileChooser;
    
    FileInputStream fileInputStream;
    BufferedInputStream bufferedInputStream;
    File myFile = null;
    String filename;
    String filePath;
    long totalLength;
    long pause;
    Player player;
    Thread playThread;
    Thread resumeThread;
    
    MusicPlayer(){
        prepareGUI();
        addActionEvents();
        playThread=new Thread(runnablePlay);
        resumeThread=new Thread(runnableResume);
    }
    
    public void prepareGUI(){
        
    	frame=new JFrame();
        
    	try {
			frame.setIconImage(ImageIO.read(new File("img\\icon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
        frame.setTitle("Music Player");
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(Color.gray);
        frame.setSize(440,200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        selectButton.setBounds(160,10,100,30);
        frame.add(selectButton);

        songNameLabel.setBounds(100,50,300,30);
        frame.add(songNameLabel);

        playButton.setBounds(30,110,100,30);
        frame.add(playButton);

        pauseButton.setBounds(120,110,100,30);
        frame.add(pauseButton);

        resumeButton.setBounds(210,110,100,30);
        frame.add(resumeButton);

        stopButton.setBounds(300,110,100,30);
        frame.add(stopButton);

    }
    public void addActionEvents(){
        selectButton.addActionListener(this);
        playButton.addActionListener(this);
        pauseButton.addActionListener(this);
        resumeButton.addActionListener(this);
        stopButton.addActionListener(this);
    }

    
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==selectButton){
            
        	fileChooser = new JFileChooser();
            
            fileChooser.setCurrentDirectory(new File("*\\"));
            fileChooser.setDialogTitle("Select Mp3");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileNameExtensionFilter("Mp3 files","mp3"));
            
            if(fileChooser.showOpenDialog(selectButton)==JFileChooser.APPROVE_OPTION){
                myFile=fileChooser.getSelectedFile();
                filename=fileChooser.getSelectedFile().getName();
                filePath=fileChooser.getSelectedFile().getPath();
            }
        }
        if(e.getSource()==playButton){
          playThread.start();
          songNameLabel.setText("Playing : " + filename);
        }
        if(e.getSource()==pauseButton){
                 if(player!=null){
                     try {
                         pause=fileInputStream.available();
                         player.close();
                         songNameLabel.setText("Paused : " + filename);
                     } catch (IOException e1) {
                         e1.printStackTrace();
                     }
                 }
        }

        if(e.getSource()==resumeButton){
           resumeThread.start();
           songNameLabel.setText("Playing : " + filename);
        }
        
        if(e.getSource()==stopButton){
            if(player!=null){
                player.close();
                songNameLabel.setText("");
            }

        }

    }

  Runnable runnablePlay = new Runnable() {
	  
      public void run() {
          try {
              fileInputStream = new FileInputStream(myFile);
              bufferedInputStream = new BufferedInputStream(fileInputStream);
              player = new Player(bufferedInputStream);
              totalLength = fileInputStream.available();
              player.play();
          } catch (FileNotFoundException e) {
              e.printStackTrace();
          } catch (JavaLayerException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
  };

    Runnable runnableResume = new Runnable() {
    	
        public void run() {
            try {
                fileInputStream = new FileInputStream(myFile);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                player = new Player(bufferedInputStream);
                fileInputStream.skip(totalLength-pause);
                player.play();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
