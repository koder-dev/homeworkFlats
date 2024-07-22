package application;

import dao.FlatsDAOImpl;
import shared.ConnectionFactory;
import shared.Flats;
import shared.SelectConditionBuilder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final Cli cli = new Cli();
    private static final Connection connection;

    static {
        try {
            connection = ConnectionFactory.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final FlatsDAOImpl dao = new FlatsDAOImpl(connection, "Flats");

    public static void main(String[] args) {
        boolean isContinued = true;
        while(isContinued) {
            int answer = cli.showStartMenu();
            if (answer == 1) createTable();
            if (answer == 2) createFlat();
            if (answer == 3) showFlats(new SelectConditionBuilder());
            if (answer == 4) showFlatsByParameters();
            if (answer == 5) updateFlat();
            if (answer == 6) deleteFlat();
            if (answer == 7) dropTable();
            isContinued = cli.askToContinue();
        }

    }

    private static void showFlats(SelectConditionBuilder selectConditionBuilder) {
        selectConditionBuilder.setTable("Flats");
        List<Flats> flatList = dao.get(selectConditionBuilder);
        cli.showTables(flatList);
    }

    private static void showFlatsByParameters() {
        SelectConditionBuilder selectConditionBuilder = cli.getSearchParams();
        showFlats(selectConditionBuilder);
    }

    private static void updateFlat() {
        SelectConditionBuilder selectConditionBuilder = cli.getSearchParams();
        selectConditionBuilder.setTable("Flats");
        List<Flats> listFlat = dao.get(selectConditionBuilder);
        String updateInfo = cli.updateFlatMenu();
        Flats flat = listFlat.get(0);
        Arrays.stream(updateInfo.split(", ?")).forEach(updateField -> {
            String updateFieldName = updateField.split("=")[0];
            String updateFieldVal = updateField.split("=")[1];

            if (updateFieldName.equalsIgnoreCase("street")) flat.setStreet(updateFieldVal);
            if (updateFieldName.equalsIgnoreCase("area")) flat.setArea(updateFieldVal);
            if (updateFieldName.equalsIgnoreCase("squares")) flat.setSquares(Integer.parseInt(updateFieldVal));
            if (updateFieldName.equalsIgnoreCase("numberOfRooms")) flat.setNumberOfRooms(Integer.parseInt(updateFieldVal));
            if (updateFieldName.equalsIgnoreCase("price")) flat.setPrice(Integer.parseInt(updateFieldVal));
        });

        dao.update(flat);
        System.out.println("Flat with id " + flat.getFlatID() + " has been updated");
    }

    private static void deleteFlat() {
        SelectConditionBuilder selectConditionBuilder = cli.getSearchParams();
        selectConditionBuilder.setTable("Flats");
        List<Flats> flats = dao.get(selectConditionBuilder);
        flats.forEach(flat -> {
            int id = flat.getFlatID();
            dao.dropFlat(id);
            System.out.println("Flat with ID " + id + " deleted.");
        });
    }

    private static void createTable() {
        dao.createTableIfNotExist();
        System.out.println("Table created.");
    }

    private static void dropTable() {
        dao.dropTable();
        System.out.println("Flats table successfully deleted");
    }

    private static void createFlat() {
        String flatInfo = cli.createFlatMenu();
        String[] flatInfoArr = flatInfo.split(" ");
        Flats flat = new Flats();

        if (!flatInfoArr[0].equalsIgnoreCase("street")) flat.setStreet(flatInfoArr[0]);
        if (!flatInfoArr[1].equalsIgnoreCase("area")) flat.setArea(flatInfoArr[1]);
        if (!flatInfoArr[2].equalsIgnoreCase("squares")) flat.setSquares(Integer.parseInt(flatInfoArr[2]));
        if (!flatInfoArr[3].equalsIgnoreCase("numberOfRooms")) flat.setNumberOfRooms(Integer.parseInt(flatInfoArr[3]));
        if (!flatInfoArr[4].equalsIgnoreCase("price")) flat.setPrice(Integer.parseInt(flatInfoArr[4]));
        int flatId = dao.add(flat);
        flat.setFlatID(flatId);

        System.out.println("Flat with id " + flatId + " created");
    }

}
