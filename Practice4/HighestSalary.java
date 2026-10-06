import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Employee {

    int employeeId;
    String name;
    String department;
    double salary;

    Employee(int employeeId, String name, String department, double salary) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return employeeId + " - " + name + " - " + salary;
    }
}

public class HighestSalary {

    public static void main(String[] args) {

        ArrayList<Employee> employees = new ArrayList<>();

        employees.add(new Employee(101, "Rahul", "IT", 75000));
        employees.add(new Employee(102, "Amit", "IT", 75000));
        employees.add(new Employee(104, "Suresh", "IT", 65000));

        employees.add(new Employee(103, "Priya", "HR", 65000));
        employees.add(new Employee(105, "Neha", "HR", 60000));

        Map<String, Double> highestSalary = new HashMap<>();

        for (Employee emp : employees) {

            if (!highestSalary.containsKey(emp.department)) {

                highestSalary.put(emp.department, emp.salary);

            } else {

                if (emp.salary > highestSalary.get(emp.department)) {
                    highestSalary.put(emp.department, emp.salary);
                }
            }
        }

        for (Employee emp : employees) {

            double highest = highestSalary.get(emp.department);

            if (emp.salary == highest) {

                System.out.println(
                        emp.department + " : "
                        + emp.employeeId + " - "
                        + emp.name + " - "
                        + emp.salary
                );
            }
        }
    }
}