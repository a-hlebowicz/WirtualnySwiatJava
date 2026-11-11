import java.io.*;
import java.util.Scanner;
import java.util.*;
import java.util.List;



public class Swiat {
    private int tura;
    private final int szerokosc;
    private final int wysokosc;
    private final Organizm[][] mapaOrganizmow;
    private final List<Organizm> listaOrganizmow;
    private final Queue<Organizm> doUsuniecia;
    private final Queue<String> komunikaty;
    private final Queue<Organizm> doDodania;
    private final Random rand;



    public Swiat(int szerokosc, int wysokosc, Random rand) {
        this.rand = rand;
        this.szerokosc = szerokosc;
        this.wysokosc = wysokosc;
        this.mapaOrganizmow = new Organizm[szerokosc+1][wysokosc+1];
        this.listaOrganizmow = new ArrayList<>();
        this.doUsuniecia = new LinkedList<>();
        this.komunikaty = new LinkedList<>();
        this.doDodania=new LinkedList<>();
    }

    public void komunikat(Organizm organizm, String text) {
        String napis;
        napis = Zamiana.naNazwe(organizm.getSymbol());
        text = ":" + text;
        napis = napis + text;

        komunikaty.add(napis);
    }
    public void wyswietlKomunikaty(Okno okno){
        while (!komunikaty.isEmpty()){
           okno.appendToTextArea(komunikaty.poll());
        }
    }

    public Organizm[][] getMapaOrganizmow() { return mapaOrganizmow; }
    public Random getRand() { return rand; }

    public Organizm dodajOrganizm(char symbol, int x, int y) {
        Organizm organizm = null;
        if (mapaOrganizmow[x][y] != null) {
            System.out.println("pole " + x + " , " + y + " jest zajete");
        } else {

            if (symbol == 'T') {
                organizm = new Trawa(this, x, y);
            }
            if (symbol == 'M') {
                organizm = new Mlecz(this, x, y);
            }
            if (symbol == 'B') {
                organizm = new BarszczSosnowskiego(this, x, y);
            }
            if (symbol == 'G') {
                organizm = new Guarana(this, x, y);
            }
            if (symbol == 'J') {
                organizm = new WilczeJagody(this, x, y);
            }
            if (symbol == 'O') {
                organizm = new Owca(this, x, y);
            }
            if (symbol == 'W') {
                organizm = new Wilk(this, x, y);
            }
            if (symbol == 'L') {
                organizm = new Lis(this, x, y);
            }
            if (symbol == 'Z') {
                organizm = new Zolw(this, x, y);
            }
            if (symbol == 'A') {
                organizm = new Antylopa(this, x, y);
            }

            if (organizm != null) {
                mapaOrganizmow[organizm.getX()][organizm.getY()] = organizm;
                doDodania.add(organizm);
            }
        }
        if (organizm==null)System.out.print( symbol+ " "+x + " " + y);
        return organizm;
    }
    public void dodajCzlowieka(Organizm organizm) {
        mapaOrganizmow[organizm.getX()][organizm.getY()] = organizm;
        listaOrganizmow.add(organizm);
    }
    public boolean czyRuchMozliwy(int x, int y) {
        return x >= 1 && x <= szerokosc && y >= 1 && y <= wysokosc;
    }
    public boolean czyPoleWolne(int x, int y) {
        return x >= 1 && x <= szerokosc && y >= 1 && y <= wysokosc && mapaOrganizmow[x][y] == null;
    }
    public boolean czySasiadujaceWolne(int x, int y) {
        return czyPoleWolne(x + 1, y) || czyPoleWolne(x, y + 1) || czyPoleWolne(x - 1, y) || czyPoleWolne(x, y - 1);
    }
    public koordynaty znajdzWolnePole(Organizm organizm) {
        koordynaty pole = new koordynaty();
        while (true) {
            if (!czySasiadujaceWolne(organizm.getX(),organizm.getY())) {
                komunikat(organizm, "nie znaleziono wolnego pola ");
                pole.x = organizm.getX();
                pole.y = organizm.getY();
                return pole;
            }
            Kierunek kierunek = Losuj.wybierz_kierunek(rand);
            pole=Losuj.ustaw_pozycje(kierunek,organizm.getX(),organizm.getY(),1);
            if (czyPoleWolne(pole.x, pole.y)) return pole;
        }
    }
    public void usunOrganizm(Organizm organizm) {
        mapaOrganizmow[organizm.getX()][organizm.getY()] = null;
        doUsuniecia.add(organizm);
    }
    public void usunOrganizmyZListy() {
        while (!doUsuniecia.isEmpty()) {
            Organizm organizm = doUsuniecia.poll();
            listaOrganizmow.remove(organizm);
            if (organizm.getSymbol() == 'X') organizm.setSila(-1);
        }
        while(!doDodania.isEmpty()){
            listaOrganizmow.add(doDodania.poll());
        }
    }
    public void zastapOrganizm(Organizm atakujacy, Organizm broniacy) {
        int nowy_x = broniacy.getX(), nowy_y = broniacy.getY();
        usunOrganizm(broniacy);
        przestawOrganizm(atakujacy, nowy_x, nowy_y);
    }
    public void przestawOrganizm(Organizm organizm, int x, int y){
        mapaOrganizmow[organizm.getX()][organizm.getY()] = null;
        organizm.setX(x);
        organizm.setY(y);
        mapaOrganizmow[organizm.getX()][organizm.getY()] = organizm;
    }
    public void tarczaPrzegon(Organizm atakujacy) {
        koordynaty pole = znajdzWolnePole(atakujacy);
        przestawOrganizm(atakujacy, pole.x, pole.y);
    }
    public void przegonOrganizm(Organizm atakujacy, Organizm broniacy) {
        koordynaty pole=znajdzWolnePole(broniacy);
        int stary_x = broniacy.getX(), stary_y = broniacy.getY();
        przestawOrganizm(broniacy, pole.x, pole.y);
        przestawOrganizm(atakujacy, stary_x, stary_y);
    }
    public void wykonajTure(Okno okno) {
        tura++;
        usunOrganizmyZListy();
        listaOrganizmow.sort(new ktoPierwszy());
        for (Organizm organizm : listaOrganizmow) {
            if (mapaOrganizmow[organizm.getX()][organizm.getY()] == organizm)
                organizm.akcja();
        }
        okno.appendToTextArea("\n"+"tura " + tura);
        wyswietlKomunikaty(okno);

    }
    public void ustawSwiat(){
        dodajOrganizm('W', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('W', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('O', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('O', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('Z', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('Z', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('A', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('A', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('L', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('L', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('T', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('T', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('M', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('M', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('J', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('J', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('B', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('B', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('G', rand.nextInt(20)+1, rand.nextInt(20)+1);
        dodajOrganizm('G', rand.nextInt(20)+1, rand.nextInt(20)+1);
    }
    public boolean jestZwierzeciem(Organizm organizm)
    {
        if (organizm == null)return false;
        if (organizm.getSymbol() == 'O')return true;
        if (organizm.getSymbol() == 'W')return true;
        if (organizm.getSymbol() == 'L')return true;
        if (organizm.getSymbol() == 'A')return true;
        if (organizm.getSymbol() == 'Z')return true;
        return organizm.getSymbol() == 'X';
    }
    static class ktoPierwszy implements Comparator<Organizm> {
        public int compare(Organizm organizm1, Organizm organizm2) {
            if (organizm1.getInicjatywa() != organizm2.getInicjatywa())
                return organizm2.getInicjatywa() - organizm1.getInicjatywa();
            else return organizm2.getWiek() - organizm1.getWiek();
        }
    }
    public void zapiszDoPliku(Czlowiek czlowiek) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Podaj nazwe pliku: ");
            String nazwaPliku = scanner.nextLine();
            FileWriter plik = new FileWriter(nazwaPliku);
            plik.write(tura + "\n");
            plik.write(czlowiek.getSymbol() + " ");
            plik.write(czlowiek.getX() + " " + czlowiek.getY() + " ");
            plik.write(czlowiek.getSila() + " ");
            plik.write(czlowiek.getWiek() + " ");
            plik.write(czlowiek.getUmiejetnosc() + "\n");
            for (int i=1;20>=i;i++) {
                for (int j=1;20>=j;j++) {
                    Organizm organizm=mapaOrganizmow[i][j];
                    if (organizm==null || organizm.getSymbol() == 'X') continue;
                    plik.write(organizm.getSymbol() + " ");
                    plik.write(organizm.getX() + " " + organizm.getY() + " ");
                    plik.write(organizm.getSila() + " ");
                    plik.write(organizm.getWiek() + "\n");
                }
            }
            plik.write('K' + "\n");
            plik.close();
            System.out.println("Gra została zapisana.");
        } catch (IOException e) {
            System.out.println("Nie udało się otworzyć pliku.");
        }
    }
    void wyczyscSwiat(){
        Organizm organizm;
        for (int i=1;20>=i;i++){
            for (int j=1;20>=j;j++){
                organizm = mapaOrganizmow[i][j];
                if (organizm!=null)usunOrganizm(organizm);
            }
        }
    }
    public void wczytajZPliku(Czlowiek czlowiek) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Podaj nazwe pliku: ");
            String nazwaPliku = scanner.nextLine();
            File plik = new File(nazwaPliku);
            Scanner plikScanner = new Scanner(plik);
            wyczyscSwiat();
            usunOrganizmyZListy();

            this.tura= plikScanner.nextInt();
            while (plikScanner.hasNextLine()) {
                char symbol = plikScanner.next().charAt(0);
                if (symbol == 'K') break;
                int x = plikScanner.nextInt();
                int y = plikScanner.nextInt();
                int sila = plikScanner.nextInt();
                int wiek = plikScanner.nextInt();
                if (symbol == 'X') {
                    czlowiek.setX(x);
                    czlowiek.setY(y);
                    czlowiek.setSila(sila);
                    czlowiek.setWiek(wiek);
                    int umiejetnosc = plikScanner.nextInt();
                    czlowiek.setUmiejetnosc(umiejetnosc);
                    dodajCzlowieka(czlowiek);
                } else {
                    Organizm organizm = dodajOrganizm(symbol, x, y);
                    organizm.setSila(sila);
                    organizm.setWiek(wiek);
                }
            }
            plikScanner.close();
            System.out.println("Gra została wczytana.");
        } catch (FileNotFoundException e) {
            System.out.println("Plik nie został znaleziony.");
        }
    }
}