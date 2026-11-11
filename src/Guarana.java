public class Guarana extends Roslina{
    public Guarana(Swiat swiat, int x, int y) {
        super(swiat, x, y, Gatunek.GUARANA, 0, "src/ikony/guarana.png");
    }

    @Override
    protected void poZjedzeniu(Organizm zjadajacy) {
        zjadajacy.setSila(zjadajacy.getSila() + 3);
    }
}
