import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * DuunariLista v.1.00
 *
 * Ohjelma listaa yrityksen työntekijat. Kayttajan on mahdollista muokata
 * listan sisaltoa esimerkiksi lisaamalla tai poistamalla tyontekijoita.
 *
 * @version 2013-01-05
 * @author  Janne Kahkonen <janne.kahkonen@cs.tamk.fi>
 */
class Gui extends JFrame {

    /** Haetaan tiedostonimi. */
    private String filename = getFile();
    
    /** Asetetaan fontti. */
    private Font font = new Font("sans", Font.BOLD, 12);

    /** Alustetaan JFileChooser. */
    private JFileChooser fc = new JFileChooser();

    /** Asetetaan paneeli. */
    private JPanel contentPanel;

    /** Asetetaan JToolBar (napit). */
    private JToolBar mainToolBar;

    /** Taulukon kolumninimet. */
    private String colNames[] = {"ETUNIMI", "SUKUNIMI", "PUHELIN", "E-MAIL",
                                 "PALKKA", "KOULUTUS", "AMMATTI", "PVM"};

    /** Taulukon rivit (alussa tyhjia). */
    private String tableRows[][] = new String[0][0];
    
    /** Alitaulukon kolumninimet. */
    private String colNames2[] = {"",""};

    /** Alitaulukon rivit (alussa tyhjia). */
    private String tableRows2[][] = new String[0][0];

    /**
     * Asetellaan taulukon malli.
     */
    private DefaultTableModel model =
            new DefaultTableModel(tableRows, colNames);

    /**
     * Luodaan JTable.
     */
    private JTable itemTable = new JTable(model) {

        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row,
                                         int column) {

            Component c = super.prepareRenderer(renderer, row, column);

            Color color2 = new Color(25,100,25);
            Color color3 = new Color(180,250,180);

            if (!isRowSelected(row)) {
                c.setForeground(row % 2 == 0 ? getForeground() : color2);
                c.setBackground(row % 2 == 0 ? getBackground() : color3);
            }

            return c;
        }

        /**
         * Tehdaan soluista muokattavia
         * @param rowIndex      Rivin indeksi.
         * @param colIndex      Sarakkeen indeksi.
         * @return true         Palauttaa true.
         */
        @Override
        public boolean isCellEditable(int rowIndex, int colIndex) {
            return true;
        }
    };

    /**
     * Ajetaan GUI (tarvittavat metodit).
     */
    public void run() {

        /** Luodaan JToolBar */
        mainToolBar = new JToolBar();

        /** Lisataan napit */
        addButtons(mainToolBar);

        /** Asetellaan paneeli ja sen ulkoasu. */
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        /** Asetetaan ikkuna koko nayton kokoiseksi. */
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        /** Asetetaan suositeltu koko ikkunalle.. */
        contentPanel.setPreferredSize(new Dimension(800, 600));

        /** Lisataan napit paneelin ylaosaan. */
        contentPanel.add(mainToolBar, BorderLayout.NORTH);

        /** Taulukon vari. */
        Color c = new Color(200,255,200);
        itemTable.setBackground(c);

        /** Asetetaan solujen jarjestaminen pois paalta. */
        itemTable.setAutoCreateRowSorter(false);

        /** Asetetaan JScrollPane */
        JScrollPane jsPane = new JScrollPane(itemTable);

        /** Asetetaan JScrollPane */
        contentPanel.add(jsPane, BorderLayout.CENTER);

        /** Asetetaan paneeli. */
        setContentPane(contentPanel);

        /** Avataan testilista testausta varten */
        readFromFile("./testilista.dat");

    }

    /**
     * Taulukodan tiedot palautetaan String -muodossa.
     * @return s    Taulukodan tiedot String -muodossa.
     */
    public String values () {

        String s = "";

        for (int i = 0; i < model.getRowCount(); i++) {

            for (int j = 0; j < 8; j++) {
                s = s + getData(i, j) + ";";
            }

            s = s + "\n";
        }

        return s;
    }

    /**
     * Haetaan taulukon tiedot
     * @param row_index     Rivin indeksi.
     * @param col_index     Sarakkeen indeksi.
     * @return val          Solun tiedot.
     */
    public Object getData(int row_index, int col_index) {
        Object val = itemTable.getModel().getValueAt(row_index, col_index);
        return val;
    }

    /**
     * Tyhjennetaan taulukko.
     */
    public void clearTable() {

        int numrows = model.getRowCount();

        for(int i = numrows - 1; i >= 0; i--) {
            model.removeRow(i);
        }
    }

    /**
     * Haetaan tiedoston nimi.
     * @return filename      Palauttaa tiedoston nimen.
     */
    public String getFile() {
        return filename;
    }

    /**
     * Luetaan tiedosto.
     * @param filename       Tiedoston nimi.
     */
    public void readFromFile(String filename) {

        this.filename = filename;

        try {

            BufferedReader in = new BufferedReader(new FileReader(filename));

            String strLine;

            /** Luetaan tiedostoa rivi kerrallaan. */
            while ((strLine = in.readLine()) != null) {

                /** Lisataan sisalto listaan. */
                String[] splitted = strLine.split(";");

                model.addRow(new Object[]{splitted[0], splitted[1], splitted[2],
                                          splitted[3], splitted[4], splitted[5],
                                          splitted[6], splitted[7]});
            }

            /** Suljetaan. */
            in.close();

        } catch (Exception ee) {
            System.out.println(ee);
        }
    }

    /**
     * Lisataan napit.
     * @param mainToolBar       JToolBar, johon painikkeet lisataan.
     */
    private void addButtons(JToolBar mainToolBar) {

        JButton toolbarButton = null;

        /**
         * Asetetaan lisaa -nappi.
         */
        ImageIcon myIcon = new ImageIcon(
                           this.getClass().getResource("/resources/add.png"));

        toolbarButton = new JButton(myIcon);
        toolbarButton.setToolTipText("LISAA");

        /**
         * Lisataan kuuntelija.
         */
        toolbarButton.addActionListener(new ActionListener() {

            /**
             * Nappia painetaan.
             * @param e      Toiminto.
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    JTextField fname = new JTextField(5);
                    JTextField sname = new JTextField(5);
                    JTextField phone = new JTextField(5);
                    JTextField mail = new JTextField(5);
                    JTextField sal = new JTextField(5);
                    JTextField edu = new JTextField(5);
                    JTextField pro = new JTextField(5);
                    JTextField date = new JTextField(5);

                    JPanel myPanel = new JPanel();
                    myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

                    myPanel.add(new JLabel("ETUNIMI"));
                    myPanel.add(fname);
                    myPanel.add(new JLabel("SUKUNIMI"));
                    myPanel.add(sname);
                    myPanel.add(new JLabel("PUHELIN"));
                    myPanel.add(phone);
                    myPanel.add(new JLabel("E-MAIL"));
                    myPanel.add(mail);
                    myPanel.add(new JLabel("PALKKA"));
                    myPanel.add(sal);
                    myPanel.add(new JLabel("KOULUTUS"));
                    myPanel.add(edu);
                    myPanel.add(new JLabel("AMMATTI"));
                    myPanel.add(pro);
                    myPanel.add(new JLabel("PVM"));
                    myPanel.add(date);

                    int result = JOptionPane.showConfirmDialog(null, myPanel,
                                 "LISAA", JOptionPane.OK_CANCEL_OPTION,
                                  JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {

                        model.addRow(new Object[]{fname.getText(),
                                     sname.getText(), phone.getText(),
                                     mail.getText(), sal.getText(),
                                     edu.getText(), pro.getText(),
                                     date.getText()});
                    }

                } catch (Exception ee) {
                    System.out.println(ee);
                }
            }
        });
        mainToolBar.add(toolbarButton);

        /**
         * Asetetaan poista nappi.
         */
        ImageIcon myIcon2 = new ImageIcon(
                        this.getClass().getResource("/resources/remove.png"));

        toolbarButton = new JButton(myIcon2);
        toolbarButton.setToolTipText("POISTA");

        /**
         * Lisataan kuuntelija.
         */
        toolbarButton.addActionListener(new ActionListener() {

            /**
             * Nappia painetaan.
             * @param e      Toiminto.
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    int s = itemTable.getSelectedRow();

                    model.removeRow(s);


                } catch (Exception ee) {
                    System.out.println(ee);
                }
            }
        });
        mainToolBar.add(toolbarButton);
        mainToolBar.addSeparator();

        /**
         * Asetetaan painiki joka listaa tyontekijat.
         */
        ImageIcon myIcon1 = new ImageIcon(
                           this.getClass().getResource("/resources/list.png"));

        toolbarButton = new JButton(myIcon1);
        toolbarButton.setToolTipText("TYONTEKIJAT");

        /**
         * Lisataan kuuntelija.
         */
        toolbarButton.addActionListener(new ActionListener() {

            /**
             * Nappia painetaan.
             * @param e      Toiminto.
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    JPanel myPanel = new JPanel();
                    myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

                    DefaultComboBoxModel items = new DefaultComboBoxModel();
                    JComboBox box = new JComboBox(items);

                    for (int emp = 0; emp < model.getRowCount(); emp++) {

                        items.addElement(getData(emp, 0).toString() +
                                               " " +
                                               getData(emp, 1).toString());
                    }

                    myPanel.add(box);

                    int result = JOptionPane.showConfirmDialog(null, myPanel,
                                 "TYONTEKIJAT", JOptionPane.OK_CANCEL_OPTION,
                                  JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {

                        int i = box.getSelectedIndex();

                        String str = (String)box.getSelectedItem();

                        /** Luodaan ikkuna. */
                        JFrame frame = new JFrame(str);
                        frame.setSize(480, 180);
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);

                        /**
                         * Asetellaan taulukon malli ja luodaan taulukko.
                         */
                        DefaultTableModel model2 = new DefaultTableModel(
                                                       tableRows2, colNames2);
                                                       
                        JTable empTable = new JTable(model2) {

                            @Override
                            public Component prepareRenderer(
                                   TableCellRenderer renderer, int row,
                                                               int column) {

                                Component c = super.prepareRenderer(renderer,
                                                                    row,
                                                                    column);

                                if (column == 0) {
                                    c.setFont(font);
                                }

                                return c;
                            }

                            /**
                             * Poistetaan solujen muokattavuus.
                             * @param rowIndex      Rivin indeksi.
                             * @param colIndex      Sarakkeen indeksi.
                             * @return true         Palauttaa false.
                             */
                            @Override
                            public boolean isCellEditable(int rowIndex,
                                                          int colIndex) {
                                return false;
                            }
                        };

                        empTable.setFocusable(false);
                        empTable.setRowSelectionAllowed(false);

                        /** Lisataan taulukko. */
                        JScrollPane jsPane2 = new JScrollPane(empTable);

                        /** Lisataan paneeli. */
                        frame.add(jsPane2, BorderLayout.CENTER);

                        /** Lisataan tiedot taulukkoon */
                        model2.addRow(new Object[]{"ETUNIMI",
                                          getData(i, 0).toString()});
                        model2.addRow(new Object[]{"SUKUNIMI",
                                          getData(i, 1).toString()});
                        model2.addRow(new Object[]{"PUHELIN",
                                          getData(i, 2).toString()});
                        model2.addRow(new Object[]{"E-MAIL",
                                          getData(i, 3).toString()});
                        model2.addRow(new Object[]{"PALKKA",
                                          getData(i, 4).toString()});
                        model2.addRow(new Object[]{"KOULUTUS",
                                          getData(i, 5).toString()});
                        model2.addRow(new Object[]{"AMMATTI",
                                          getData(i, 6).toString()});
                        model2.addRow(new Object[]{"PVM",
                                          getData(i, 7).toString()});

                    }

                } catch (Exception ee) {
                    System.out.println(ee);
                }
            }
        });
        mainToolBar.add(toolbarButton);
        mainToolBar.addSeparator();

        /**
         * Asetetaan tallennus -nappi.
         */
        ImageIcon myIcon3 = new ImageIcon(
                        this.getClass().getResource("/resources/save.png"));

        toolbarButton = new JButton(myIcon3);
        toolbarButton.setToolTipText("TALLENNA");

        /**
         * Lisataan kuuntelija.
         */
        toolbarButton.addActionListener(new ActionListener() {

            /**
             * Nappia painetaan.
             * @param e      Toiminto.
             */
            @Override
            public void actionPerformed(ActionEvent e) {

               try {

                    /** Nayta valinta. */
                    if (fc.showSaveDialog(contentPanel) ==
                        JFileChooser.APPROVE_OPTION) {

                        /** Haetaan tiedoston nimi. */
                        String filename = fc.getSelectedFile().getName();

                        /** Haetaan tiedoston polku. */
                        File path = fc.getCurrentDirectory();

                        /** Luodaan tiedosto. */
                        FileWriter fstream =
                                        new FileWriter(path + "/" + filename);

                        /** Asetetaan BufferedWriter. */
                        BufferedWriter out =
                                        new BufferedWriter(fstream);

                        /** Kirjoitetaan tiedot tiedostoon.. */
                        out.write(values());

                        /** Suljetaan. */
                        out.close();
                    }

                } catch (Exception ee) {
                    System.out.println(ee);
                }
            }
        });
        mainToolBar.add(toolbarButton);

        /**
         * Asetetaan lataus -nappi.
         */
        ImageIcon myIcon4 = new ImageIcon(
                        this.getClass().getResource("/resources/load.png"));

        toolbarButton = new JButton(myIcon4);
        toolbarButton.setToolTipText("LATAA");

        /**
         * Lisataan kuuntelija.
         */
        toolbarButton.addActionListener(new ActionListener() {

            /**
             * Nappia painetaan.
             * @param e      Toiminto.
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    /** Nayta valinta. */
                    if (fc.showOpenDialog(contentPanel) ==
                        JFileChooser.APPROVE_OPTION) {

                        /** Poistetaan vanhat tiedot. */
                        clearTable();

                        String na = fc.getSelectedFile().getName();
                        File path = fc.getCurrentDirectory();

                        readFromFile(path + "/" + na);
                    }

                } catch (Exception ee) {
                    System.out.println(ee);
                }
            }
        });
        mainToolBar.add(toolbarButton);
        mainToolBar.addSeparator();


        /**
         * Lisataan info -nappi.
         */
        ImageIcon myIcon6 = new ImageIcon(
                        this.getClass().getResource("/resources/about.png"));

        toolbarButton = new JButton(myIcon6);
        toolbarButton.setToolTipText("TIETOA SOVELLUKSESTA");

        /**
         * Lisataan kuuntelija.
         */
        toolbarButton.addActionListener(new ActionListener() {

            /**
             * Nappia painetaan.
             * @param e      Toiminto.
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    String msg = ("<html><p align=center>" +
                                  "<u>DuunariLista v.1.0</u>" +
                                  "<br>Janne K&auml;hk&ouml;nen &copy; 2013" +
                                  "<br>&lt;janne.kahkonen@cs.tamk.fi&gt;<br>" +
                                  "<br><u>Ohjelman Painikkeet</u>" +
                                  "<br>lis&auml;&auml;, poista," +
                                  "<br>listaa ty&ouml;ntekij&auml;t,<br>" +
                                  "tallenna, lataa,"+
                                  "<br>tietoa ohjelmasta</p></html>");

                    JLabel message = new JLabel(msg, JLabel.CENTER);

                    JFrame frame = new JFrame();

                    JOptionPane.showMessageDialog(frame, message, "TIETOA " +
                                                  "SOVELLUKSESTA",
                                                  JOptionPane.PLAIN_MESSAGE);


                } catch (Exception ee) {
                    System.out.println(ee);
                }
            }
        });
        mainToolBar.add(toolbarButton);
        mainToolBar.addSeparator();
    }

}
