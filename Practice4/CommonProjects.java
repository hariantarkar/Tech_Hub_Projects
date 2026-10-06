import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Employee {

    String employeeName;
    ArrayList<String> projects;

    Employee(String employeeName, ArrayList<String> projects) {
        this.employeeName = employeeName;
        this.projects = projects;
    }
}

public class CommonProjects {

    public static void main(String[] args) {

        ArrayList<Employee> employees = new ArrayList<>();

        ArrayList<String> amitProjects = new ArrayList<>();
        amitProjects.add("Java");
        amitProjects.add("Banking");
        amitProjects.add("Ecommerce");

        ArrayList<String> rahulProjects = new ArrayList<>();
        rahulProjects.add("Java");
        rahulProjects.add("Ecommerce");
        rahulProjects.add("Testing");

        ArrayList<String> priyaProjects = new ArrayList<>();
        priyaProjects.add("Banking");
        priyaProjects.add("Ecommerce");

        employees.add(new Employee("Amit", amitProjects));
        employees.add(new Employee("Rahul", rahulProjects));
        employees.add(new Employee("Priya", priyaProjects));

        Map<String, ArrayList<String>> projectEmployees = new HashMap<>();

        for (Employee emp : employees) {

            for (String project : emp.projects) {

                if (!projectEmployees.containsKey(project)) {

                    projectEmployees.put(project, new ArrayList<>());
                }

                projectEmployees.get(project).add(emp.employeeName);
            }
        }

        Map<String, Set<String>> employeePairs = new HashMap<>();

        for (String project : projectEmployees.keySet()) {

            ArrayList<String> names = projectEmployees.get(project);

            for (int i = 0; i < names.size() - 1; i++) {

                for (int j = i + 1; j < names.size(); j++) {

                    String name1 = names.get(i);
                    String name2 = names.get(j);

                    String pair;

                    if (name1.compareTo(name2) < 0) {
                        pair = name1 + " & " + name2;
                    } else {
                        pair = name2 + " & " + name1;
                    }

                    if (!employeePairs.containsKey(pair)) {

                        employeePairs.put(pair, new HashSet<>());
                    }

                    employeePairs.get(pair).add(project);
                }
            }
        }

        for (String pair : employeePairs.keySet()) {

            Set<String> commonProjects = employeePairs.get(pair);

            if (commonProjects.size() > 1) {

                System.out.println(
                        pair + " -> "
                        + commonProjects.size()
                        + " Common Projects"
                );

                System.out.println(commonProjects);
            }
        }
    }
}