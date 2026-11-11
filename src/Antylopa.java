public class Antylopa extends Zwierze{
    public Antylopa(Swiat swiat, int x, int y) {
        super(swiat, x, y, Gatunek.ANTYLOPA,4,4, "src/ikony/antylopa.png");
    }

    @Override
    public void kolizja(Organizm atakujacy) {
        if (swiat.getRand().nextInt(2) == 1 || atakujacy.getSymbol() == getSymbol()) super.kolizja(atakujacy);
        else {
            if (!swiat.czySasiadujaceWolne(x, y)) super.kolizja(atakujacy);
            else {
                swiat.komunikat(atakujacy, "przegania antylope");
                swiat.przegonOrganizm(atakujacy, this);
            }
        }
    }
}
