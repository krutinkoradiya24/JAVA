public class Driver
{
    public static void main(String args[])
    {
        point[] p1={
            new point(1,2),
            new point(3,4),
            new point(1,2),
            new point(5,6),
            new point(3,4),
        };
        int distinct=0;
        for(int i=0;i<p1.length;i++)
        {
            boolean seenbefore=false;
            for(int j=0;j<i;j++)
            {
                if(p1[i].equals(p1[j]))
                {
                    seenbefore=true;
                    break;
                }
            }
            if(!seenbefore)
            distinct++;
        }
        System.out.println("points:");
        for(point p:p1)
        {
            System.out.println(" "+p);
        }
        System.out.println("number of distinct points:" +distinct);
        
    }
}