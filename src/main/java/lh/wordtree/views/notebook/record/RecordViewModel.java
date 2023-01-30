package lh.wordtree.views.notebook.record;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lh.wordtree.dao.RecordMapper;
import lh.wordtree.entity.Record;
import lh.wordtree.utils.JDBCUtils;

public class RecordViewModel {
    private final SimpleListProperty<Record> listProperty = new SimpleListProperty<>();
    private final RecordMapper recordMapper = JDBCUtils.getSqlSessionFactory().getMapper(RecordMapper.class);

    public ObservableList<Record> getListProperty() {
        listProperty.setValue(FXCollections.observableArrayList(recordMapper.selectAll()));
        return listProperty;
    }

    public SimpleListProperty<Record> listPropertyProperty() {
        return listProperty;
    }

    public RecordMapper getRecordMapper() {
        return recordMapper;
    }
}
