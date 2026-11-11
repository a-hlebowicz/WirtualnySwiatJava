public class Zolw extends Zwierze{
    public Zolw(Swiat swiat, int x, int y) {
        super(swiat, x, y, Gatunek.ZOLW,2,1,"src/ikony/zolw.png");
    }

    @Override
    public void akcja() {
            postarzWiek();
            if (swiat.getRand().nextInt(4) == 2) {
                super.akcja();
                setWiek(getWiek() - 1);
            }
    }

    @Override
    public void kolizja(Organizm atakujacy) {
        if (atakujacy.getSila() < 5 && atakujacy.getSymbol()!=getSymbol()) {
            swiat.komunikat(atakujacy, "bezskutecznie zaatakowal zolwia");
        }
        else super.kolizja(atakujacy);
    }
}
