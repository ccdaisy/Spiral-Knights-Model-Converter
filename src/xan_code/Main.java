package xan_code;

import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("serial")

public class Main extends JPanel
        implements ActionListener {
	
	static JFrame progress = new JFrame("Conversion Progress");
	static JFrame frame;
	
    static private final String newline = "\n";
    JButton openButton, saveButton;
    public static JCheckBox exportBones;
    protected static JTextArea log, stats;
    JFileChooser fc;
    
    public static JTextArea ttt;

    OpenFileFilter obj = new OpenFileFilter("obj", ".OBJ (Wavefront OBJ)");
    OpenFileFilter dat = new OpenFileFilter("dat", ".DAT (Binary Spiral Knights model)");
    OpenFileFilter fbx = new OpenFileFilter("fbx", ".FBX (AutoDesk FBX)");
    OpenFileFilter png = new OpenFileFilter("png", ".PNG (Texture file)");
    OpenFileFilter all = new OpenFileFilter("All Files");

    String objf;
    String extension;
    String Name;
    String EX_EXT;

    public boolean isXML;
    
    BufferedImage newIMG;

    File convertedFile;
    File f;
    File loadedfile;
    static File FFFF;
    
    BufferedImage BuffImg;
    
    DATRead reader;
    
    public Main(int unused) {
    	super(new BorderLayout());
    	
    	ttt = new JTextArea(20, 5);
    	ttt.setMargin(new Insets(5, 5, 5, 5));
    	ttt.setEditable(false);
    	ttt.setText("");
    	
    	add(ttt);
    }

    public Main() {
        super(new BorderLayout());

        //Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(20,100);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        log.setText("Welcome to SK Model to OBJ!\n\nSelect your models with the button at the top.\nNOTE: SOME MODELS MAY TAKE LONG AMOUNTS OF TIME TO CONVERT.\n\n\n");
        Logger logger = new Logger(log);
        JScrollPane logScrollPane = new JScrollPane(log);
        stats = new JTextArea(20, 50);
        stats.setMargin(new Insets(5, 5, 5, 5));
        stats.setEditable(false);
        JScrollPane statsScrollPane = new JScrollPane(stats);
        
        //Create a file chooser
        CreateFC();

        //Uncomment one of the following lines to try a different
        //file selection mode.  The first allows just directories
        //to be selected (and, at least in the Java look and feel,
        //shown).  The second allows both files and directories
        //to be selected.  If you leave these lines commented out,
        //then the default mode (FILES_ONLY) will be used.
        //
        //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        openButton = new JButton("Select model...",
                null);
        openButton.addActionListener(this);

        //Create the save button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        saveButton = new JButton("Export",
                null);
        saveButton.addActionListener(this);
        
        
        exportBones = new JCheckBox();
        exportBones.setEnabled(true);
        exportBones.setText("Export bones as seperate models");
        exportBones.setToolTipText("This does not make armatures, but instead splits the model up into its individual bones.");

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);
        //buttonPanel.add(exportBones);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
        add(statsScrollPane, BorderLayout.PAGE_END);

        stats.setText("Loaded Model: None\n");
    }
    
    protected void CreateFC() {
    	String cpf86 = "C:/Program Files (x86)/Steam/steamapps/common/Spiral Knights/rsrc";
        String cpf = "C:/Program Files/Steam/steamapps/common/Spiral Knights/rsrc";
        Path ProgramFilesx86path = Paths.get(cpf86);
        Path ProgramFilespath = Paths.get(cpf);
    	if (Files.exists(ProgramFilesx86path)) {
        	fc = new JFileChooser(cpf86);
        } else if (Files.exists(ProgramFilespath)) {
        	fc = new JFileChooser(cpf);
        } else {
        	fc = new JFileChooser();
        }
    	fc.setAcceptAllFileFilterUsed(false);
    }

    @SuppressWarnings("static-access")
	public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == openButton) {
        	fc.removeChoosableFileFilter(obj);
        	fc.removeChoosableFileFilter(dat);
            fc.removeChoosableFileFilter(png);
            fc.removeChoosableFileFilter(fbx);
            fc.addChoosableFileFilter(dat);
            
            //Done setting filters
            
            int returnVal = fc.showOpenDialog(Main.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                log.append("Opened file. Reading..." + newline);
                
                File file = fc.getSelectedFile();
                loadedfile = file;
                //Handle file here!
                reader = new DATRead(file);
                //reader.PrintData();
                
                ttt.setText("");
                
                //Progress update for convert method run in that code
                
               
                //Done handling file
            } else {
                //log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());

            //Handle save button action.
        } else if (e.getSource() == saveButton) {
            if (loadedfile == null) {
                log.append("Please open a model before trying to save!\n");
                log.setCaretPosition(log.getDocument().getLength());
                return;
            }
            fc.removeChoosableFileFilter(dat);
            fc.removeChoosableFileFilter(png);
            fc.addChoosableFileFilter(obj);
            //fc.addChoosableFileFilter(fbx);
            int returnVal = fc.showSaveDialog(Main.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File location = fc.getCurrentDirectory();
                
                if (fc.getFileFilter() == obj) {
                	reader.ConvertDatToOBJ(new File(location+"/Exported Model"));
                } else if (fc.getFileFilter() == fbx) {
                	reader.ConvertDatToFBX(new File(location+"/Exported Model"));
                }
                
                Logger.AppendLn("\n\n");
                
                CreateFC(); //Refresh our filechooser back to the rsrc directory
                
                //frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            } else {
                //log.append("Save command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        }
        String LF = "None";
        if (loadedfile != null) {
            LF = loadedfile.getName();
        }
        stats.setText("Loaded Model: "+LF+"\n");
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("SK Model to OBJ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        progress.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //Add content to the window.
        frame.add(new Main());
        progress.add(new Main(0));

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        progress.pack();
        progress.setVisible(false);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}