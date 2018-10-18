import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import java.awt.datatransfer.StringSelection;
import java.io.File;

public class ImgUrlAction extends com.intellij.openapi.actionSystem.AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        VirtualFile selectedFile = DataKeys.VIRTUAL_FILE.getData(event.getDataContext());
        if (project != null && selectedFile != null) {
            String path1 = "https://raw.githubusercontent.com/";
            String path2 = getPath2();
            String path3 = getPath3(project, selectedFile);
            String imgUrl = path1 + path2 + path3;
            CopyPasteManager.getInstance().setContents(new StringSelection(imgUrl));
            Notifications.Bus.notify(new Notification(
                    project.getName()
                    , project.getName()
                    , "复制路径成功!  ->  " + imgUrl
                    , NotificationType.INFORMATION));
        }
    }

    private String getPath2() {
        File userNameFile = new File("username.txt");
        if (!userNameFile.exists()) {
            userNameFile.mkdir();
        }
        VirtualFile userNameVF = LocalFileSystem.getInstance().findFileByIoFile(userNameFile);
        Document document = FileDocumentManager.getInstance().getDocument(userNameVF);
        return document.getText();
    }

    private String getPath3(Project project, VirtualFile selectedFile) {
        int startIndex = project.getBasePath().length() - project.getName().length() - 1;
        String path3 = selectedFile.getPath().substring(startIndex);
        return path3;
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
    }
}
