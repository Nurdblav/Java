public class FurnitureClass {
    private String title;
    private Integer price;
    private Integer del_price;
    private Integer del_time;

    public FurnitureClass (String title,Integer price,Integer del_price,Integer del_time){
        this.title = title;
        this.price = price;
        this.del_price = del_price;
        this.del_time = del_time;
    }

    public String getTitle (){
        return this.title;
    }
    public Integer getPrice (){
        return this.price;
    }
    public Integer getDelPrice (){
        return this.del_price;
    }
    public Integer getDelTime (){
        return this.del_time;
    }
}
