/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
//k
package xan_code;

import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.apache.commons.io.FilenameUtils;

@SuppressWarnings("serial")

public class Main extends JPanel
        implements ActionListener {
    static private final String newline = "\n";
    JButton openButton, saveButton, openTextureButton, saveTextureButton;
    protected static JTextArea log, stats;
    JFileChooser fc;

    OpenFileFilter obj = new OpenFileFilter("obj", ".OBJ (Wavefront OBJ)");
    OpenFileFilter dat = new OpenFileFilter("dat", ".DAT (Binary Spiral Knights model)");
    OpenFileFilter xml = new OpenFileFilter("xml", ".XML (SpiralSpy XML export)");
    OpenFileFilter fbx = new OpenFileFilter("fbx", ".FBX (AutoDesk FBX)");
    OpenFileFilter png = new OpenFileFilter("png", ".PNG (Texture file)");

    String objf;
    String extension;
    String Name;
    String EX_EXT;

    public boolean isXML;

    File convertedFile;
    File f;
    File loadedfile;
    File FFFF;

    public Main() {
        super(new BorderLayout());

        //Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(20,100);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
        stats = new JTextArea(20, 50);
        stats.setMargin(new Insets(5, 5, 5, 5));
        stats.setEditable(false);
        JScrollPane statsScrollPane = new JScrollPane(stats);

        //Create a file chooser
        fc = new JFileChooser();

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
        openButton = new JButton("Open model....",
                null);
        openButton.addActionListener(this);

        //Create the save button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        saveButton = new JButton("Save as....",
                null);
        saveButton.addActionListener(this);

        openTextureButton = new JButton("Convert texture to UV Map....", null);
        openTextureButton.addActionListener(this);

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(openTextureButton);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
        add(statsScrollPane, BorderLayout.PAGE_END);

        stats.setText("Loaded file: None\nLoaded texture: None\n");
    }

    public void writeFile(File converted, String text) {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(converted);
            fileWriter.write(text);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void setXML(String ext) {
        byte[] strb = ext.getBytes();
        for (int i = 0; i < strb.length; i++) {
            isXML = (strb[i] == 'x');
            if (isXML) {
                break;
            }
        }
    }

    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == openButton) {
            fc.removeChoosableFileFilter(obj);
            //fc.removeChoosableFileFilter(fbx);
            fc.removeChoosableFileFilter(png);
            fc.addChoosableFileFilter(dat);
            fc.addChoosableFileFilter(xml);
            int returnVal = fc.showOpenDialog(Main.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                log.append("Opened file. Reading..." + newline);
                File file = fc.getSelectedFile();
                loadedfile = file;
                //Handle file here!

                extension = FilenameUtils.getExtension(file.getPath());
                int i = file.getName().lastIndexOf('.');
                if (i > 0) {
                    Name = file.getName().substring(0, i);
                }
                setXML(extension);
                if (isXML) {
                    EX_EXT = ".obj";
                } else {
                    EX_EXT = ".obj";
                }
                objf = HandleFiles.convert(file, isXML);
                if (objf.length() > 25) {
                    log.append("Ready to export as OBJ!" + newline);
                } else {
                    log.append("Export file empty! Perhaps an error\noccured without notice?\n");
                    loadedfile = null;
                    FFFF = null;
                    objf = "";
                }
                //Done handling file
            } else {
                //log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());

            //Handle save button action.
        } else if (e.getSource() == saveButton) {
            if (loadedfile == null) {
                log.append("Please open a model\nbefore trying to save!\n");
                log.setCaretPosition(log.getDocument().getLength());
                return;
            }
            fc.removeChoosableFileFilter(dat);
            fc.removeChoosableFileFilter(xml);
            fc.removeChoosableFileFilter(png);
            fc.addChoosableFileFilter(obj);
            //fc.addChoosableFileFilter(fbx);
            int returnVal = fc.showSaveDialog(Main.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File location = fc.getCurrentDirectory();
                //This is where a real application would save the file.
                log.append("Saving: " + FilenameUtils.removeExtension(loadedfile.getName()) + EX_EXT + " to " + location.getName() + "." + newline);
                String path = location.getPath() + "\\" + Name;
                System.out.println(path);
                convertedFile = new File(path + EX_EXT);
                writeFile(convertedFile, objf);
                objf = "";
            } else {
                //log.append("Save command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        } else if (e.getSource() == openTextureButton) {
            fc.removeChoosableFileFilter(obj);
            //fc.removeChoosableFileFilter(fbx);
            fc.removeChoosableFileFilter(dat);
            fc.removeChoosableFileFilter(xml);
            fc.addChoosableFileFilter(png);
            int returnVal = fc.showOpenDialog(Main.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                log.append("Opened texture." + newline);
                FFFF = fc.getSelectedFile();
                File LOC = fc.getCurrentDirectory();
                String MMMM = LOC.getPath();
                try {
                    BufferedImage BI = ImageIO.read(FFFF);

                } catch (IOException ex) {

                }

                log.append("Converted texture. Modified texture should be\nin the folder you opened it from!\n");
                //Done handling file
            } else {
                //log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        }
        String LF = "None";
        String LT = "None";
        if (loadedfile != null) {
            LF = loadedfile.getName();
        }
        if (FFFF != null) {
            LT = FFFF.getName();
        }
        stats.setText("Loaded file: "+LF+"\nLoaded texture: "+LT);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("SK Model to OBJ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new Main());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
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