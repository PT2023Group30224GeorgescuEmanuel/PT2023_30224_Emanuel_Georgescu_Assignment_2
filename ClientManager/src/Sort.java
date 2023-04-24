import java.util.Comparator;

public class Sort implements Comparator<Client>{
    public int compare(Client a,Client b)
    {
        return a.getTimpSosire()-b.getTimpSosire();
    }
}
