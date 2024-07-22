package shared;

import java.util.Objects;

public class SelectConditionBuilder {
    String table;
    String where;
    String orderBy;
    int limit;
    String[] selectOptions;

    public SelectConditionBuilder(String table, String where, String orderBy, int limit, String ...selectOptions) {
        this.table = table;
        this.where = where;
        this.orderBy = orderBy;
        this.limit = limit;
        this.selectOptions = selectOptions;
    }

    public SelectConditionBuilder() {
    }

    public String getTable() {
        return table;
    }

    public String getWhere() {
        return where;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public int getLimit() {
        return limit;
    }

    public String[] getSelectOptions() {
        return selectOptions;
    }

    public SelectConditionBuilder(String table) {
        this.table = table;
    }

    public SelectConditionBuilder setTable(String table) {
        this.table = table;
        return this;
    }

    public SelectConditionBuilder setWhere(String where) {
        this.where = where;
        return this;
    }

    public SelectConditionBuilder setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public SelectConditionBuilder setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public SelectConditionBuilder setSelectOptions(String ...selectOptions) {
        this.selectOptions = selectOptions;
        return this;
    }
    
    public String build() {
        StringBuilder conditions = new StringBuilder().append("SELECT ");
        if (Objects.nonNull(selectOptions) && selectOptions.length > 0) conditions.append(String.join(",", selectOptions));
        else conditions.append("*");
        conditions.append(" FROM ").append(table);
        if (Objects.nonNull(where)) conditions.append(" WHERE ").append(where);
        if (Objects.nonNull(orderBy)) conditions.append(" ORDER BY ").append(orderBy);
        if (limit > 0) conditions.append(" LIMIT ").append(limit);
        conditions.append(";");
        return conditions.toString();
    }
}
