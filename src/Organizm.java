public abstract class Organizm {
    protected int x;
    protected int y;
    private final Gatunek gatunek;
    protected int sila;
    protected int inicjatywa;
    protected int wiek;
    protected Swiat swiat;
    protected String ikona;

    public Organizm(Swiat swiat, int x, int y, Gatunek gatunek, int sila, int inicjatywa,String ikona) {
        this.x = x;
        this.y = y;
        this.gatunek = gatunek;
        this.sila = sila;
        this.inicjatywa = inicjatywa;
        this.swiat = swiat;
        this.ikona=ikona;
        this.wiek = 0;
    }

    public abstract void akcja();
    public abstract void kolizja(Organizm inny);
    public char getSymbol() { return gatunek.symbol(); }
    public String getNazwa() { return gatunek.nazwa(); }
    public int getSila() { return sila; }
    public int getInicjatywa() { return inicjatywa; }
    public int getX() { return x; }
    public int getY() { return y; }

    public int getWiek() { return wiek; }

    public void postarzWiek() { wiek++; }
    public void setWiek(int a) { wiek = a; }
    public void setSila(int a) { sila = a; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
}