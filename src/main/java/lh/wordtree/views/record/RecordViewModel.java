package lh.wordtree.views.record;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lh.wordtree.archive.mapper.RecordMapper;
import lh.wordtree.archive.entity.Record;

public class RecordViewModel {
    private final SimpleListProperty<Record> listProperty = new SimpleListProperty<>();
    private RecordMapper recordMapper;

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
