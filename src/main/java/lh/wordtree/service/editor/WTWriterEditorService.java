package lh.wordtree.service.editor;

public interface WTWriterEditorService {
    SearchInfo search(String keyWord);

    TranslationInfo translation(String keyWord);

    record SearchInfo(String keyWord, String pingYin, String info) {
    }

    record TranslationInfo() {
    }
}
