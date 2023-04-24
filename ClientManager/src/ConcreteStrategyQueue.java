import java.util.List;
public class ConcreteStrategyQueue implements Strategy{
    public void addClient(List<Server> servers,Client c)
    {
        int minim=999;
        int id=0;
        for(int i=0;i<servers.size();i++)
        {
            if(servers.get(i).getClientiCeAsteapta()<minim)
            {
                minim=servers.get(i).getClientiCeAsteapta();
                id=servers.get(i).getId();
            }
        }
        for(int i=0;i<servers.size();i++)
        {
            if(servers.get(i).getId()==id)
            {
                servers.get(i).setDeschis(true);
                servers.get(i).addClient(c);
            }
        }
    }
}
