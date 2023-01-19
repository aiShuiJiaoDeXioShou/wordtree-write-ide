package lh.wordtree.entity;

import java.time.LocalDateTime;

public record RecentFiles(LocalDateTime time, String filePath, String userName, String workspaceName) {
}