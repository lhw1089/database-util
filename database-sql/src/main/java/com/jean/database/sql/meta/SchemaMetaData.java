package com.jean.database.sql.meta;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Schema信息
 *
 * @author jinshubao
 */
@SuppressWarnings("FieldMayBeFinal")
public class SchemaMetaData {

    private StringProperty tableCat = new SimpleStringProperty(this, "tableCat");
    private StringProperty quoteString = new SimpleStringProperty(this, "quoteString");
    private StringProperty separator = new SimpleStringProperty(this, "separator");
    private StringProperty tableSchema = new SimpleStringProperty(this, "tableSchema");

    public String getTableCat() {
        return tableCat.get();
    }

    public StringProperty tableCatProperty() {
        return tableCat;
    }

    public void setTableCat(String tableCat) {
        this.tableCat.set(tableCat);
    }

    public String getQuoteString() {
        return quoteString.get();
    }

    public StringProperty quoteStringProperty() {
        return quoteString;
    }

    public void setQuoteString(String quoteString) {
        this.quoteString.set(quoteString);
    }

    public String getSeparator() {
        return separator.get();
    }

    public StringProperty separatorProperty() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator.set(separator);
    }

    public String getTableSchema() {
        return tableSchema.get();
    }

    public StringProperty tableSchemaProperty() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema.set(tableSchema);
    }

    @Override
    public String toString() {
        return tableSchema.get();
    }
}
