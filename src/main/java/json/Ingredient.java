package json;

public class Ingredient {
    private String _id;
    private String name;
    private String type;
    private String fat;
    private String calories;
    private String price;


    public Ingredient(String _id, String name, String type, String fat, String calories, String price) {
        this._id = _id;
        this.name = name;
        this.type = type;
        this.fat = fat;
        this.calories = calories;
        this.price = price;
    }

    public Ingredient(){}

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
