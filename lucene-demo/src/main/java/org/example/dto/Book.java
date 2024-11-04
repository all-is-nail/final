package org.example.dto;

public class Book {
    /**
     * id
     */
    private Integer id;
    /**
     * name
     */
    private String name;
    /**
     * price
     */
    private Float price;
    /**
     * desc
     */
    private String desc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
