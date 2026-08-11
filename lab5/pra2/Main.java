abstract class Employee {
    String name;
    int id;

    Employee(String n, int id) {
        name = n;
        this.id = id;
    }

    abstract double monthlySalary();
}
class FullTime extends Employee{
    double salary;

    FullTime(String name,int id,double salary)
    {
        super(name,id);
        this.salary=salary;
    }
    @Override
    double monthlySalary(){
        return salary;
    }
}
class PartTime extends Employee{
    double hours;
    double rate;

    PartTime(String name,int id,double hours,double rate)
    {
        super(name ,id);
        this.hours=hours;
        this.rate=rate;
    }
    @Override
    double monthlySalary(){
        return hours*rate;
}
}
class Contract extends Employee{
    double stipend;

        public Contract(String name,int id,double stipend) {
            super(name, id);
            this.stipend = stipend;
        }
    @Override
   double monthlySalary(){
    return stipend;
   }
    
}
public class Main{
    public static void main(String[] args) {
        Employee[] employees = {
        new FullTime("Rahul", 101, 50000),
        new PartTime("Amit", 102, 80, 200),
        new Contract("Krutin",103,15000)
        };
             double total = 0;

        for (Employee e : employees) {

            double salary = e.monthlySalary();
            System.out.println(e.name + " (ID: " + e.id + ") = " + salary);
             if (e instanceof Contract) {
                System.out.println("Note: Contract employee receives a stipend."+e.id);
            }
            

            total += salary;
        }

        System.out.println("--------------------");
        System.out.println("Total Payroll = " + total);
    }

}
