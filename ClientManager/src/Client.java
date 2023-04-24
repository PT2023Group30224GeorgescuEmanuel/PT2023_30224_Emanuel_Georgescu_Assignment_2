public class Client {
    private int id;
    private int timpSosire;
    private int timpProcesare;

    public Client(int id,int arrival_time,int processing_time)
    {
        this.id=id;
        this.timpSosire=arrival_time;
        this.timpProcesare=processing_time;
    }
    public void setId(int id)
    {
        this.id=id;
    }
    public int getId()
    {
        return this.id;
    }
    public void setTimpSosire(int timpSosire)
    {
        this.timpSosire=timpSosire;
    }
    public int getTimpSosire()
    {
        return this.timpSosire;
    }
    public void setTimpProcesare(int timpProcesare)
    {
        this.timpProcesare=timpProcesare;
    }
    public int getTimpProcesare()
    {
        return this.timpProcesare;
    }
    public String toString()
    {
        return "("+this.getId()+","+this.getTimpSosire()+","+this.getTimpProcesare()+")";
    }
}
