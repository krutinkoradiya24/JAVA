 import java.util.Objects;
public class card{
    private String rank,suit;
    public card(String rank,String suit){
        this.rank=rank;
        this.suit=suit;
    }
    
    public String tostring(){
        return rank+"of"+suit;
    }
    
    public boolean equals(Object obj){
        if(this==obj) return true;
        if(!(obj instanceof card)) return false;
        card c=(card)obj;
        return this.rank==c.rank&&this.suit==c.suit;
    }
    
    public int hasecode()
    {
        return Objects.hash(rank,suit);
    }
    @Override
public String toString() {
    return rank + " of " + suit;
}
}