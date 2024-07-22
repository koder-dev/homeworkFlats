package dao;

import shared.Flats;
import shared.SelectConditionBuilder;

import java.sql.Connection;
import java.util.List;

public class FlatsDAOImpl extends AbstractDAO<Flats> {
    public FlatsDAOImpl(Connection conn, String table) {
        super(conn, table);
    }

    public List<Flats> get(SelectConditionBuilder selectConditionBuilder) {
        return super.get(Flats.class, selectConditionBuilder);
    }

    public void createTableIfNotExist() {
        super.createTableIfNotExist(Flats.class);
    }
}
