import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Product {
    private String name;
    private LocalDate date_produced;
    private LocalDate expiration_date;
    private int price;

    public Product(String _name, LocalDate _date_produced, LocalDate _expiration_date, int _price) {
        name = _name;
        date_produced = _date_produced;
        expiration_date = _expiration_date;
        price = _price;
    }

    public String getName() { return name; }
    public void setName(String _name) { name = _name; }

    public LocalDate getProdDate() { return date_produced; }
    public void setProdDate(LocalDate _date) { date_produced = _date; }

    public LocalDate getExpirationDate() { return expiration_date; }
    public void setExpirationDate(LocalDate _date) { expiration_date = _date; }

    public int getPrice() { return price; }
    public void setPrice(int _price) { price = _price; }

    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yy");
        return String.format("%12s\t %s / %s\t %4.2f₴", name, date_produced.format(fmt), expiration_date.format(fmt), price/100.0);
    }
}