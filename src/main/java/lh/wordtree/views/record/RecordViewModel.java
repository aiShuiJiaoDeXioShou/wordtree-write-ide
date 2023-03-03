package lh.wordtree.views.record;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lh.wordtree.dao.DAO;
import lh.wordtree.dao.RecordMapper;
import lh.wordtree.entity.Record;

public class RecordViewModel {
    private final SimpleListProperty<Record> listProperty = new SimpleListProperty<>();
    private final RecordMapper recordMapper = DAO.getSqlSessionFactory().getMapper(RecordMapper.class);

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
