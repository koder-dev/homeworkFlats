package application;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import shared.Flats;
import shared.SelectConditionBuilder;

import java.util.List;
import java.util.Scanner;

public class Cli {
    private final Scanner sc;

    public Cli(Scanner sc) {
        this.sc = sc;
    }

    public Cli() {
        this.sc = new Scanner(System.in);
    }

    public int showStartMenu() {
        System.out.println("Welcome to Flat Database");
        System.out.println("1: CREATE FLAT TABLE");
        System.out.println("2: ADD A NEW FLAT");
        System.out.println("3: SHOW ALL FLATS");
        System.out.println("4: SHOW FLATS BY PARAMETERS");
        System.out.println("5: UPDATE FLAT INFORMATION");
        System.out.println("6: DELETE FLAT BY PARAMETERS");
        System.out.println("7: DELETE TABLE");
        System.out.println("Enter the number of action that you want to perform: ");
        int answer = sc.nextInt();
        sc.nextLine();
        return answer;
    }

    public boolean askToContinue() {
        System.out.println("Would you like to continue? (y/n)");
        return sc.nextLine().equalsIgnoreCase("y");
    }

    public String createFlatMenu() {
        System.out.println("Enter the flat information in format:");
        System.out.println("STREET AREA SQUARES NumberOfRooms PRICE");
        System.out.println("If you dont know some information left this row name");
        System.out.println("Example: 'STREET DNIPRO 12 NumberOfRooms 4000'");
        return sc.nextLine();
    }

    public void showTables(List<Flats> flats) {
        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow("FlatId", "Street", "Area", "Squares", "Price");
        asciiTable.addRule();
        for (Flats flat : flats) {
            asciiTable.addRow(flat.getFlatID(), flat.getStreet(), flat.getArea(), flat.getSquares(), flat.getPrice());
            asciiTable.addRule();
        }
        asciiTable.setTextAlignment(TextAlignment.CENTER);
        String table = asciiTable.render();
        System.out.println(table);
    }

    public String updateFlatMenu() {
        System.out.println("Enter the update information in format:");
        System.out.println("Field=value, Field=value");
        System.out.println("Please dont forget use the correct datatypes");
        System.out.println("Street(String), Area(String), Squares(int),NumberOfRooms(int), Price(int));");
        System.out.println("Warning: FlatId can't be changed");
        return sc.nextLine();
    }

    public SelectConditionBuilder getSearchParams() {
        System.out.println("Enter the search parameters in format:");
        System.out.println("Param>value, Param=value");
        System.out.println("For string type values please cover the value with single quotes - 'value'");
        SelectConditionBuilder selectConditionBuilder = new SelectConditionBuilder();
        selectConditionBuilder.setWhere(sc.nextLine());
        System.out.println("Order by which column? If its unnecessary leave it empty");
        String orderBy = sc.nextLine();
        if (!orderBy.isEmpty())  selectConditionBuilder.setOrderBy(orderBy);
        System.out.println("Please specify the limit of columns or enter 0");
        selectConditionBuilder.setLimit(sc.nextInt());
        sc.nextLine();
        return selectConditionBuilder;
    }



}
