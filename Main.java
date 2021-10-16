import org.json.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Map;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yy");
    
    public static void main(String[] args) throws Exception {
        generateData(1, 1);

        ArrayList<Product> products = readFromFile("data.json");
        PrintList(products);

        Map<LocalDate, List<Product>> expiration_table = CreateTableByExpiration(products);
        PrintTable(expiration_table);

        Scanner sc = new Scanner(System.in);
        System.out.println("Choose object to delete");
        String prodName = sc.nextLine();
        sc.close();

        RemoveProduct(expiration_table, prodName);
        PrintTable(expiration_table);


        ArrayList<Product> lst1 = readFromFile("data1.json");
        ArrayList<Product> lst2 = readFromFile("data2.json");
        ArrayList<Product> unique_list = new ArrayList<Product>(GetUniqueProducts(lst1, lst2));
        PrintList(unique_list);
        
        RemoveProducedInYear(unique_list, 2021);
        PrintList(unique_list);        
    }

    public static ArrayList<Product> readFromFile(String path) throws Exception {
        String firstFile = Files.readString(Path.of(path));
        JSONArray array = new JSONArray(firstFile);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yy");
        ArrayList<Product> ret = new ArrayList<Product>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String name = obj.getString("name");
            LocalDate date_prod = LocalDate.parse(obj.getString("date_produced"), fmt);
            LocalDate date_exp = LocalDate.parse(obj.getString("date_expiration"), fmt);
            int price = obj.getInt("price");
            ret.add(new Product(name, date_prod, date_exp, price));
        }
        return ret;
    }

    public static Map<LocalDate, List<Product>> CreateTableByExpiration(List<Product> products) {
        Map<LocalDate, List<Product>> table = products.stream().collect(Collectors.groupingBy(Product::getExpirationDate));
        if (table.containsKey(LocalDate.now().plusDays(3))) {
            for(var product : table.get(LocalDate.now().plusDays(3))) {
                product.setPrice((int)(product.getPrice() * 0.9));
            }
        }
        return table;
    }

    public static void RemoveProduct(Map<LocalDate, List<Product>> table, String name) {
        for (var row : table.entrySet()) {
            List<Product> lst = row.getValue();
            for (int i = lst.size() - 1; i >= 0; i--) {
                if (lst.get(i).getName().equals(name)) {
                    lst.remove(i);
                }
            }
            if(lst.size() == 0) {
                table.remove(row.getKey());
            }
        }
    }

    public static List<Product> GetUniqueProducts(List<Product> first, List<Product> second) {
        List<Product> lst1 = new ArrayList<Product>(first);
        List<Product> lst2 = new ArrayList<Product>(second);

        lst1.removeIf(n -> (second.stream().anyMatch(o -> o.getName().equals(n.getName()))));
        lst2.removeIf(n -> (first.stream().anyMatch(o -> o.getName().equals(n.getName()))));

        return Stream.concat(lst1.stream(), lst2.stream()).collect(Collectors.toList());
    }

    public static void RemoveProducedInYear(List<Product> products, int year) {
        products.removeIf(n -> (n.getProdDate().getYear() == year));
    }

    public static void PrettyPrintProducts(List<Product> products) {
        var maxValue = products.stream().max(Comparator.comparing(v -> v.getName().length())).get();

    }

    public static void PrintList(List<?> lst) {
        int counter = 0;
        for (var obj : lst) {
            System.out.println(++counter + ".  " + obj);
        }
        System.out.println("\n");
    }

    public static void PrintTable(Map<LocalDate, List<Product>> table) {
        for (var row : table.entrySet()) {
            System.out.println(row.getKey().format(fmt));
            for (var product : row.getValue()) {
                System.out.println("\t" + product);
            }
        }
        System.out.println("\n");
    }

    public static void generateData(int num1, int num2) throws Exception {
        String cmd = "python data_generator.py";
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(cmd);
        pr.waitFor();
    }
}
