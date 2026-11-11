public abstract class Roslina extends Organizm {
    private static final int SZANSA_NA_ROZPRZESTRZENIENIE = 20;


    public Roslina(Swiat swiat, int x, int y, Gatunek gatunek, int sila,String ikona) {
        super(swiat, x, y, gatunek, sila, 0, ikona);
    }

    public void akcja() {
        boolean czyRozprzestrzeni = swiat.getRand().nextInt(SZANSA_NA_ROZPRZESTRZENIENIE) == 0;
        if (getWiek() <= 0) {
            postarzWiek();
            return;
        }
        postarzWiek();
        if (czyRozprzestrzeni) {
            if (swiat.czySasiadujaceWolne(getX(), getY())) {
                koordynaty pole = swiat.znajdzWolnePole(this);
                swiat.dodajOrganizm(getSymbol(), pole.x,  pole.y);
                swiat.komunikat(this, "sie rozprzestrzenil");
            }
        }
    }

    public void kolizja(Organizm inny) {
        if (this == inny)
            return;
        if (getSila() > inny.getSila()) {
            swiat.komunikat(inny, "atakuje ");
            swiat.komunikat(this, "wygrywa ");
            swiat.usunOrganizm(inny);
        } else if (getSila() < inny.getSila()) {
            swiat.komunikat(inny, "atakuje ");
            swiat.komunikat(this, "przegrywa ");
            poZjedzeniu(inny);
            swiat.zastapOrganizm(inny, this);
        }
    }

    protected void poZjedzeniu(Organizm zjadajacy) {
    }
}

