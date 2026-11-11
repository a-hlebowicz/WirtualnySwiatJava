public class Guarana extends Roslina{
    public Guarana(Swiat swiat, int x, int y) {
        super(swiat, x, y, Gatunek.GUARANA, 0, "src/ikony/guarana.png");
    }

    @Override
    public void kolizja(Organizm inny) {
        if (this == inny)
            return;
        if (getSila() > inny.getSila())
        {
            swiat.komunikat(inny, "atakuje ");
            swiat.komunikat(this, "wygrywa ");
            swiat.usunOrganizm(inny);
        }
        else if (getSila() < inny.getSila())
        {
            swiat.komunikat(inny, "atakuje ");
            swiat.komunikat(this, "przegrywa ");
            inny.setSila(inny.getSila() + 3);
            swiat.zastapOrganizm(inny, this);
        }
    }
}

