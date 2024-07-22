package shared;

import shared.Anotations.FieldType;
import shared.Anotations.Id;

public class Flats {
    @Id
    @FieldType(type = "INT NOT NULL PRIMARY KEY AUTO_INCREMENT")
    private int FlatID;

    @FieldType(type = "VARCHAR(100)")
    private String street;

    @FieldType(type = "VARCHAR(100)")
    private String area;

    @FieldType(type = "INT")
    private int squares;

    @FieldType(type = "INT")
    private int numberOfRooms;

    @FieldType(type = "INT")
    private int price;

    public Flats(String street, String area, int squares, int numberOfRooms, int price) {
        this.street = street;
        this.area = area;
        this.squares = squares;
        this.numberOfRooms = numberOfRooms;
        this.price = price;
    }

    public Flats() {
    }

    public int getFlatID() {
        return FlatID;
    }


    public String getStreet() {
        return street;
    }

    public String getArea() {
        return area;
    }

    public int getSquares() {
        return squares;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Flats{" +
                "FlatID=" + FlatID +
                ", street='" + street + '\'' +
                ", area='" + area + '\'' +
                ", squares=" + squares +
                ", numberOfRooms=" + numberOfRooms +
                ", price=" + price +
                '}';
    }

    public void setFlatID(int flatID) {
        FlatID = flatID;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setSquares(int squares) {
        this.squares = squares;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
