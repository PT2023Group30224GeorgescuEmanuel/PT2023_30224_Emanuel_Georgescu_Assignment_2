
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class SimulationManager implements Runnable{
    public int timeLimit = 100;
    public int timpProcesareMaxim;
    public int timpProcesareMinim;
    public int timpSosireMinim;
    public int timpSosireMaxim;
    public int numarServere;
    public int numarDeClienti;
    public int timpDeSimulareMaxim = 0;
    public double avgTime = 0.0;
    private File outFile;
    public SelectionPolicy selectionPolicy=SelectionPolicy.SHORTEST_TIME;;
    public List<Client> clientiGenerati;
    private Scheduler scheduler;

    public SimulationManager(File outFile) {
        ArrayList<String> fileResult;
            this.numarDeClienti =30;
            this.numarServere =6;
            this.timpDeSimulareMaxim =40;
            this.timpSosireMinim =3;
            this.timpSosireMaxim =30;
            this.timpProcesareMinim =3;
            this.timpProcesareMaxim =9;
            this.avgTime = 0.0;
            this.outFile = outFile;
            try {
                this.outFile.createNewFile();
            } catch (Exception ex) {
                System.out.println("Fisierul nu poate fi creat");
                return;
            }
        generareNClientiAleatori();
        this.scheduler = new Scheduler(this.numarServere, this.numarDeClienti);
    }
    private void generareNClientiAleatori() {
        this.clientiGenerati=Collections.synchronizedList(new ArrayList<Client>(this.numarDeClienti));
        for(int i=0; i < this.numarDeClienti; ++i) {
            Client c = new Client(i,this.timpSosireAleatoriu(), this.timpProcesareAleatoriu());
            this.clientiGenerati.add(c);
        }
        Collections.sort(this.clientiGenerati, new Sort());
    }
    public int timpProcesareAleatoriu() {
        return this.timpProcesareMinim + (int)(Math.random() * (this.timpProcesareMaxim - this.timpProcesareMinim));
    }
    public int timpSosireAleatoriu() {
        return this.timpSosireMinim + (int)(Math.random() * (this.timpSosireMaxim - this.timpSosireMinim));
    }
    public void run() {
        PrintWriter pWriter;
       try{
         pWriter=new PrintWriter(this.outFile.toString());}
       catch (Exception ex){System.out.println(ex.getMessage());return;}
        int currentTime = 0;
        String result;
        int ok=0;
        int time=0;
        int nrclienti=this.clientiGenerati.size();
        while((currentTime < timpDeSimulareMaxim || (!clientiGenerati.isEmpty()  )) && ok==0)
        {
            int nrmaxservers=this.scheduler.getNrMaxServere();
            List<Server> serv=this.scheduler.getServers();
            int n=0;
            for(int i=0;i<serv.size();i++)
            {
                if(serv.get(i).getDeschis()==false)
                    n++;
            }
            if(n==serv.size() && clientiGenerati.isEmpty())
                ok=1;

            while(!clientiGenerati.isEmpty() && clientiGenerati.get(0).getTimpSosire() == currentTime)
            {
                scheduler.dispatchClient(clientiGenerati.get(0));
                time+=clientiGenerati.get(0).getTimpProcesare();
                clientiGenerati.remove(0);
            }
            result = getRezultat(currentTime);
            pWriter.print(result);
            currentTime++;
            try{
            Thread.sleep(1000);
            }catch(Exception ex) {}
        }
            pWriter.print("Timp de asteptare mediu:"+ (double)((double)time/(double)nrclienti));
            pWriter.close();
        scheduler.killThread();
    }

    private String getRezultat(int currentTime) {
        String rezultat = "\nTimp: " + currentTime + "\n";
        rezultat = rezultat + "Clienti in asteptare: ";
        int j=0;
        for(j=0;j<this.clientiGenerati.size();j++)
        {
            rezultat=rezultat+this.clientiGenerati.get(j).toString();
        }
        rezultat = rezultat + "\n" + this.scheduler.toString();
        return rezultat;
    }

    public static void main(String[] args) {
            SimulationManager gen = new SimulationManager(new File("out-test-1.txt"));
            Thread simThread = new Thread(gen);
            simThread.start();
    }
}


