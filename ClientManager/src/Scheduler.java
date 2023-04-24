
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Scheduler {
    private List<Server> servere;
    private int nrMaxServere=0;
    private int nrMaxClientiPerServer=0;
    private Strategy strategie;
    private List<Thread> threads;

    public Scheduler(int nrServers,int nrClients)
    {
        this.nrMaxServere=nrServers;
        this.nrMaxClientiPerServer=nrClients;
        this.servere= Collections.synchronizedList(new ArrayList<Server>());
        this.threads= Collections.synchronizedList(new ArrayList<Thread>());
        for(int i=0;i<nrServers;i++)
        {
            servere.add(new Server(i,nrMaxServere));
            threads.add(new Thread(servere.get(i)));
            threads.get(i).start();
        }
    }
    public int getNrMaxServere()
    {
        return nrMaxServere;
    }
    public void setNrMaxServere(int nr)
    {
        this.nrMaxServere=nr;
    }
    public int getNrMaxClientiPerServer()
    {
        return this.nrMaxClientiPerServer;
    }
    public void setNrMaxClientiPerServer(int nr)
    {
        this.nrMaxServere=nr;
    }
    public void changeStrategie(SelectionPolicy policy)
    {
        if(policy==SelectionPolicy.SHORTEST_QUEUE)
            this.strategie=new ConcreteStrategyQueue();
        else if(policy==SelectionPolicy.SHORTEST_TIME)
            this.strategie=new ConcreteStrategyTime();
    }
    public int minTimpPerCoada()
    {
        int timpAsteptare=999;
        int minTimpAsteptare=0;
        for(int i=0;i<nrMaxServere;i++)
        {
            if(servere.get(i).getPerioadaDeAsteptare()<timpAsteptare)
            {
                timpAsteptare=servere.get(i).getPerioadaDeAsteptare();
                minTimpAsteptare=i;
            }
            if(timpAsteptare==0)
                break;
        }
        return minTimpAsteptare;
    }
    public void dispatchClient(Client c)
    {
        int minTimpAsteptare=minTimpPerCoada();
        servere.get(minTimpAsteptare).addClient(c);
        synchronized (this.threads) {
            if (servere.get(minTimpAsteptare).getDeschis() != true) {
                servere.get(minTimpAsteptare).setDeschis(true);
                threads.set(minTimpAsteptare, new Thread(servere.get(minTimpAsteptare)));
                threads.get(minTimpAsteptare).start();
            }
        }

    }
    public void killThread() {
        for(int i=0;i<this.servere.size();i++)
        {
            servere.get(i).setDeschis(false);
        }
    }
    public List<Server> getServers()
    {
        return servere;
    }
    public String toString()
    {
        String rezultat="";
        for(int i=0;i<servere.size();i++)
        {
            rezultat+="Coada"+servere.get(i).getId()+":"+servere.get(i).toString()+"\n";
        }
        return rezultat;
    }


}
