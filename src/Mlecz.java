
public class Mlecz extends Roslina{
    public Mlecz(Swiat swiat, int x, int y) {
        super(swiat, x, y, Gatunek.MLECZ, 0, "src/ikony/mlecz.png");
    }

    @Override
    public void akcja() {
        setWiek(getWiek() - 2);  //co ture wiek zwiekszal sie o 3
        for (int i=0;3>i;i++)
            super.akcja();
    }
}
