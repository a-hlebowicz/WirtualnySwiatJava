import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

public class Okno extends JFrame implements ActionListener, KeyListener, ObserwatorSwiata {
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem loadItem;
    JMenuItem saveItem;
    JMenuItem nextTurnItem;
    JPanel boardPanel;
    JTextArea textArea;
    JScrollPane scrollPane;

    private final Swiat swiat;
    private final Czlowiek czlowiek;
    private final SerializatorSwiata serializator;
    private final String[][] imagePaths;

    private final String[] dostepneOrganizmy = {"Owca", "Wilk", "Antylopa", "Zolw","Lis" , "Trawa", "Mlecz", "Guarana", "WilczeJagody", "BarszczSosnowskiego"};

    Okno(Swiat swiat, Czlowiek czlowiek, SerializatorSwiata serializator) {
        this.swiat = swiat;
        this.czlowiek = czlowiek;
        this.serializator = serializator;
        this.setTitle("Wirtualny Świat");
        this.setSize(553, 762);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(new Color(255, 255, 255));
        this.setLayout(new BorderLayout());

        // Menu bar
        menuBar = new JMenuBar();
        fileMenu = new JMenu("Plik");
        menuBar.add(fileMenu);
        loadItem = new JMenuItem("Wczytaj");
        saveItem = new JMenuItem("Zapisz");
        loadItem.addActionListener(this);
        saveItem.addActionListener(this);
        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        nextTurnItem = new JMenuItem("Następna tura");
        nextTurnItem.addActionListener(this);
        menuBar.add(nextTurnItem);
        this.setJMenuBar(menuBar);

        // Panel planszy
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(20, 20));
        this.add(boardPanel, BorderLayout.CENTER);

        // Pole tekstowe do komunikatów
        textArea = new JTextArea(10, 30);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        this.add(scrollPane, BorderLayout.SOUTH);

        imagePaths = new String[21][21];

        updateBoard();


        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();

        this.setVisible(true);
    }

    private void updateBoard() {
        ustawSciezki();
        boardPanel.removeAll();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                ImageIcon imageIcon = new ImageIcon(imagePaths[i][j]);
                JLabel label = new JLabel(imageIcon);
                label.setName(i + "," + j);

                JPopupMenu popupMenu = getjPopupMenu(i, j);
                label.setComponentPopupMenu(popupMenu);

                boardPanel.add(label);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private JPopupMenu getjPopupMenu(int i, int j) {
        JPopupMenu popupMenu = new JPopupMenu();
        for (String organizm : dostepneOrganizmy) {
            JMenuItem menuItem = new JMenuItem("Dodaj " + organizm);
            final int x = i;
            final int y = j;
            menuItem.addActionListener(e -> {
                swiat.dodajOrganizm(Zamiana.naSymbol(organizm), x + 1, y + 1);
                appendToTextArea("Dodaj " + organizm + " do pola o koordynatach: " + x + "," + y);
            });
            popupMenu.add(menuItem);
        }
        return popupMenu;
    }

    private void ustawSciezki() {
        Organizm[][] mapaOrganizmow = swiat.getMapaOrganizmow();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (mapaOrganizmow[i + 1][j + 1] == null) {
                    imagePaths[i][j] = "src/ikony/puste.png";
                } else {
                    imagePaths[i][j] = mapaOrganizmow[i + 1][j + 1].ikona;
                }
            }
        }
    }

    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loadItem) {
            wczytaj();
        } else if (e.getSource() == saveItem) {
            zapisz();
        } else if (e.getSource() == nextTurnItem) {
            swiat.wykonajTure();
        }
    }

    private void zapisz() {
        JFileChooser wybor = new JFileChooser(".");
        if (wybor.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try (Writer plik = new FileWriter(wybor.getSelectedFile())) {
            serializator.zapisz(swiat, czlowiek, plik);
            appendToTextArea("Zapisano stan gry.");
        } catch (IOException ex) {
            appendToTextArea("Nie udało się zapisać pliku: " + ex.getMessage());
        }
    }

    private void wczytaj() {
        JFileChooser wybor = new JFileChooser(".");
        if (wybor.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try (Reader plik = new FileReader(wybor.getSelectedFile())) {
            serializator.wczytaj(swiat, czlowiek, plik);
            appendToTextArea("Wczytano stan gry.");
        } catch (IOException ex) {
            appendToTextArea("Nie udało się wczytać pliku: " + ex.getMessage());
        }
    }

    @Override
    public void poTurze(int tura, List<String> komunikaty) {
        appendToTextArea("\n" + "tura " + tura);
        for (String komunikat : komunikaty) {
            appendToTextArea(komunikat);
        }
        updateBoard();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'w' || e.getKeyChar() == 's' || e.getKeyChar() == 'a' || e.getKeyChar() == 'd') {
            czlowiek.setWejscie(e.getKeyChar());
            swiat.wykonajTure();
        }
        if (e.getKeyChar() == 'r') {
            czlowiek.aktywujUmietnosc();
            appendToTextArea("Człowiek aktywował umiejętność.");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void appendToTextArea(String message) {
        textArea.append(message + "\n");
    }


}
