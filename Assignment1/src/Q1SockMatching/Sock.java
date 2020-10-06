package Q1SockMatching;

public class Sock {
    private Integer id;
    private Integer color;

    public Sock(Integer id, Integer color) {
        this.id = id;
        this.color = color;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Sock's " +
                "id=" + id +
                ", color=" + color;
    }
}
