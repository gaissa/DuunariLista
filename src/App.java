/**
 * DuunariLista v.1.00
 *
 * Paaluokka, josta ohjelmaa ajetaan.
 *
 * @version 2013-01-05
 * @author  Janne Kahkonen <janne.kahkonen@cs.tamk.fi>
 */
class App extends Gui {

    /**
     * Paametodi, josta ohjelmaa ajetaan.
     * @param args      Komentorivin argumentit.
     */
    public static void main(String [] args) {

        Gui gui = new Gui();
        gui.setDefaultCloseOperation(EXIT_ON_CLOSE);

        gui.setTitle("DuunariLista");

        gui.run();
        gui.pack();

        gui.setLocationRelativeTo(null);
        gui.setVisible(true);

    }

}
