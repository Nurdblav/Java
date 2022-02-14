public class Product {
    private String name;
    private int quantity;

    public Product(String name, int quantity){
        this.name = name;
        this.quantity = quantity;
    }

    public void setQuantity (int quantity){
        this.quantity = quantity;
    }

    public String getName(){
        return this.name;
    }

    public int getQuantity (){
        return this.quantity;
    }
}
