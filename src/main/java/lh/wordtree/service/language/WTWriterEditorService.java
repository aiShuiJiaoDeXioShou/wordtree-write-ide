package lh.wordtree.service.language;

public interface WTWriterEditorService {
    SearchInfo search(String keyWord);

    TranslationInfo translation(String keyWord);

    record SearchInfo(String keyWord, String pingYin, String info) {
    }

    record TranslationInfo() {
    }
}
