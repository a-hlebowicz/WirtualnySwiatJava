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
    private final List<ObserwatorSwiata> obserwatorzy = new ArrayList<>();



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
        napis = organizm.getNazwa();
        text = ":" + text;
        napis = napis + text;

        komunikaty.add(napis);
    }
    public void dodajObserwatora(ObserwatorSwiata obserwator) {
        obserwatorzy.add(obserwator);
    }
    private void powiadomObserwatorow() {
        List<String> komunikatyTury = List.copyOf(komunikaty);
        komunikaty.clear();
        for (ObserwatorSwiata obserwator : obserwatorzy) {
            obserwator.poTurze(tura, komunikatyTury);
        }
    }

    public Organizm[][] getMapaOrganizmow() { return mapaOrganizmow; }
    public Random getRand() { return rand; }

    public Organizm dodajOrganizm(char symbol, int x, int y) {
        Gatunek gatunek = Gatunek.zSymbolu(symbol);
        if (gatunek == null || mapaOrganizmow[x][y] != null) return null;
        Organizm organizm = gatunek.utworz(this, x, y);
        mapaOrganizmow[x][y] = organizm;
        doDodania.add(organizm);
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
            if (organizm instanceof Czlowiek) organizm.setSila(-1);
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
    public void wykonajTure() {
        tura++;
        usunOrganizmyZListy();
        listaOrganizmow.sort(new ktoPierwszy());
        for (Organizm organizm : listaOrganizmow) {
            if (mapaOrganizmow[organizm.getX()][organizm.getY()] == organizm)
                organizm.akcja();
        }
        powiadomObserwatorow();
    }
    public void ustawSwiat(){
        for (Gatunek gatunek : Gatunek.values()) {
            if (!gatunek.dodawalny()) continue;
            for (int i = 0; i < 2; i++) {
                dodajOrganizm(gatunek.symbol(), rand.nextInt(szerokosc) + 1, rand.nextInt(wysokosc) + 1);
            }
        }
    }
    static class ktoPierwszy implements Comparator<Organizm> {
        public int compare(Organizm organizm1, Organizm organizm2) {
            if (organizm1.getInicjatywa() != organizm2.getInicjatywa())
                return organizm2.getInicjatywa() - organizm1.getInicjatywa();
            else return organizm2.getWiek() - organizm1.getWiek();
        }
    }
    public int getTura() { return tura; }
    public int getSzerokosc() { return szerokosc; }
    public int getWysokosc() { return wysokosc; }

    public void wyczysc(int nowaTura) {
        wyczyscSwiat();
        usunOrganizmyZListy();
        tura = nowaTura;
    }
    private void wyczyscSwiat(){
        Organizm organizm;
        for (int i=1;20>=i;i++){
            for (int j=1;20>=j;j++){
                organizm = mapaOrganizmow[i][j];
                if (organizm!=null)usunOrganizm(organizm);
            }
        }
    }
}