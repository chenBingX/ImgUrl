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
            String path2 = getPath2(project);
            if (path2 != null){
                String path3 = getPath3(project, selectedFile);
                String imgUrl = path1 + path2 + path3;
                CopyPasteManager.getInstance().setContents(new StringSelection(imgUrl));
                Notifications.Bus.notify(new Notification(
                        project.getName()
                        , project.getName()
                        , "url已复制到剪切板!  ->  " + imgUrl
                        , NotificationType.INFORMATION));
            }
        }
    }

    private String getPath2(Project project) {
        String projPath = project.getBasePath();
        File userNameFile = new File(projPath + "/username.txt");
        if (userNameFile.exists()) {
            VirtualFile userNameVF = LocalFileSystem.getInstance().findFileByIoFile(userNameFile);
            Document document = FileDocumentManager.getInstance().getDocument(userNameVF);
            return document.getText();
        } else {
            Notifications.Bus.notify(new Notification(
                    project.getName()
                    , project.getName()
                    , "需要先在项目根目录创建 username.txt 文件，然后写入 github 的用户昵称，就是每个仓库前的那个名称。多的什么都不要多写！"
                    , NotificationType.INFORMATION));
            return null;
        }
    }

    private String getPath3(Project project, VirtualFile selectedFile) {
        int startIndex = project.getBasePath().length();
        String path3 = "/" + project.getName() + "/master" + selectedFile.getPath().substring(startIndex);
        return path3;
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
    }
}
