import java.util.Objects;

public class point
{
    private int x,y;
    point(int x,int y)
    {
        this.x=x;
        this.y=y;
    }
    public String toString()
    {
        return "("+x+"+"+y+")";
    }
    public boolean equals(Object obj)
    {
    if (this==obj) return true;  
    if(!(obj instanceof point))return false;
    point p=(point)obj;
    return this.x==p.x&&this.y==p.y;
    
}
   public int hasecode()
    {
        return Objects.hash(x,y);
    }
}

