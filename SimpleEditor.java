package zad3;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Objects;


class SimpleEditor extends JFrame implements ActionListener {


    private final Color[] colors = {Color.WHITE, Color.BLACK, Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED, Color.PINK};
    private final String[] colorNames = {"WHITE", "BLACK", "BLUE", "GREEN", "YELLOW", "RED", "PINK"};


    static File recentDir = null;
    static File fileToSave = null;

    JTextArea t;
    JFrame f;


    public SimpleEditor() {
        // Create a frame
        f = new JFrame("Untitled");


        // Text component
        t = new JTextArea();

        f.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JMenuBar mb = new JMenuBar();


        JMenu fileMenu = new JMenu("File");



        JMenuItem openItem = new JMenuItem("Open");
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));


        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));

        JMenuItem saveItemAs = new JMenuItem("Save as...");
        saveItemAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));

        JMenuItem exit = new JMenuItem("Exit");


        // Add action listener
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        saveItemAs.addActionListener(this);
        exit.addActionListener(this);


        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveItemAs);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        JMenu optionsMenu = new JMenu("Options");
        JMenu foreground = new JMenu("Foreground");
        JMenu background = new JMenu("Background");
        JMenu fontSize = new JMenu("Font size");

        int[] fontSizeList = {8, 10, 12, 14, 16, 18, 20, 22, 24};
        for (int j : fontSizeList) {
            String pos = j + " pts";
            JMenuItem Jmi = new JMenuItem(pos);
            fontSize.add(Jmi);
            Jmi.addActionListener(this);
        }

        for (int i = 0; i < colorNames.length; i++) {
            String pos = colorNames[i];
            JMenuItem color = new JMenuItem(pos);
            color.setIcon(getColorIcon(colors[i]));
            background.add(color);
            color.addActionListener(this);
        }

        String[] foregroundColorNames = {"*WHITE", "*BLACK", "*BLUE", "*GREEN", "*YELLOW", "*RED", "*PINK"};
        for (int i = 0; i < foregroundColorNames.length; i++) {
            String pos = foregroundColorNames[i];
            JMenuItem fColor = new JMenuItem(pos);
            fColor.setIcon(getColorIcon(colors[i]));
            foreground.add(fColor);
            fColor.addActionListener(this);
        }


        optionsMenu.add(foreground);
        optionsMenu.add(background);
        optionsMenu.add(fontSize);


        mb.add(fileMenu);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        mb.add(optionsMenu);
        optionsMenu.setMnemonic(KeyEvent.VK_O);


        f.setJMenuBar(mb);
        f.add(t);
        f.setSize(500, 500);
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();

        if (s.equals("Exit")) {
            System.exit(0);

        } else if (s.equals("Save")) {

            if (fileToSave == null) {
                saveFile();
            } else {
                try {
                    FileWriter file = new FileWriter(fileToSave, false);
                    BufferedWriter w = new BufferedWriter(file);
                    w.write(t.getText());
                    w.flush();
                    w.close();
                } catch (Exception evt) {
                    JOptionPane.showMessageDialog(f, evt.getMessage());
                }
            }


        } else if (s.equals("Save as...")) {
            saveFile();
        } else if (s.equals("Open")) {
            JFileChooser j = new JFileChooser();

            j.setCurrentDirectory(Objects.requireNonNullElseGet(recentDir, () -> new File(System.getProperty("user.home"))));

            int r = j.showOpenDialog(null);

            if (r == JFileChooser.APPROVE_OPTION) {
                File fi = new File(j.getSelectedFile().getAbsolutePath());
                recentDir = j.getCurrentDirectory();
                f.setTitle(fi.toString());
                fileToSave = new File(fi.toString());

                try {
                    StringBuilder sl;
                    FileReader fr = new FileReader(fi);
                    BufferedReader br = new BufferedReader(fr);
                    sl = new StringBuilder(br.readLine());

                    while (br.readLine() != null) {
                        sl.append("\n").append(br.readLine());
                    }

                    t.setText(sl.toString());
                } catch (Exception evt) {
                    JOptionPane.showMessageDialog(f, evt.getMessage());
                }
            } else
                JOptionPane.showMessageDialog(f, "The user has cancelled the operation");
        } else if (s.endsWith("pts")) {
            int fontSize = Integer.parseInt(s.split(" ")[0]);
            Font font = t.getFont();
            t.setFont(new Font(font.getName(), font.getStyle(), fontSize));
        } else if (s.startsWith("*")) {

            t.setForeground(colors[Arrays.asList(colorNames).indexOf(s.split("[*]")[1])]);
        } else if (Arrays.asList(colorNames).contains(s)) {


            t.setBackground(colors[Arrays.asList(colorNames).indexOf(s)]);
        }

    }

    private Icon getColorIcon(Color color) {
        return new Icon() {
            private final int size = 16;

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(color);
                g.fillOval(x, y, size, size);
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }

    private void saveFile() {
        JFileChooser j = new JFileChooser("f:");
        int r = j.showSaveDialog(null);

        if (r == JFileChooser.APPROVE_OPTION) {
            File fi = new File(j.getSelectedFile().getAbsolutePath());

            try {
                FileWriter wr = new FileWriter(fi, false);
                BufferedWriter w = new BufferedWriter(wr);

                w.write(t.getText());

                w.flush();
                w.close();
                fileToSave = new File(fi.toString());
                f.setTitle(fi.toString());
            } catch (Exception evt) {
                JOptionPane.showMessageDialog(f, evt.getMessage());
            }
        } else
            JOptionPane.showMessageDialog(f, "the user cancelled the operation");

    }


}
