package com.yangteng.library.views.notebook.entity;

import java.time.LocalDateTime;

public record RecentFiles(LocalDateTime time, String filePath, String userName, String workspaceName) {
}