import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
public class Server implements Runnable{
    private BlockingQueue<Client> clienti;
    private AtomicInteger perioadaDeAsteptare=new AtomicInteger();
    private int clientiCeAsteapta=0;
    private boolean deschis=true;
    private int id;
    public Server(int id,int nrMaxClienti)
    {
        this.id=id;
        this.clienti=new ArrayBlockingQueue<Client>(nrMaxClienti);
        this.perioadaDeAsteptare.set(0);
    }
    public int getId()
    {
        return this.id;
    }
    public int getClientiCeAsteapta()
    {
        return this.clientiCeAsteapta;
    }
    public void addClient(Client newClient)
    {
        clienti.add(newClient);
        this.perioadaDeAsteptare.addAndGet(newClient.getTimpProcesare());
    }
    public int getPerioadaDeAsteptare()
    {
        return this.perioadaDeAsteptare.intValue();
    }
    private void setPerioadaDeAsteptare(int perioadaDeAsteptare)
    {
        this.perioadaDeAsteptare.set(perioadaDeAsteptare);
    }
    public boolean getDeschis()
    {
        return this.deschis;
    }
    public void setDeschis(boolean open)
    {
        this.deschis=open;
    }

    public BlockingQueue<Client> getClienti()
    {
        return clienti;
    }

    public void run()
    {
        while(deschis)
        {
            while(clienti.peek()!=null)
            {
                try
                {
                    int c=clienti.peek().getTimpProcesare();
                    Thread.sleep(1000);
                    this.perioadaDeAsteptare.getAndDecrement();
                    c--;
                    clienti.peek().setTimpProcesare(c);
                    if(c==0)
                    {
                        clienti.peek().setTimpProcesare(0);
                        clienti.poll();
                    }
                }
                catch(Exception ex)
                {
                }
            }
            setDeschis(false);
        }
    }
    public String toString()
    {
        String result;
        if(this.getPerioadaDeAsteptare()==0 || clienti.peek()==null || clienti.peek().getTimpProcesare()==0)
            result="Inchis";
        else
            result=clienti.toString();
        return result;
    }

}
