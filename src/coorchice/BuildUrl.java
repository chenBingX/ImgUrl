package coorchice;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import java.awt.datatransfer.StringSelection;
import java.io.File;

public class BuildUrl {

    private BuildUrl(){

    }

    private static final class Holder{
        private static final BuildUrl instance = new BuildUrl();
    }

    public static BuildUrl getInstance(){
        return Holder.instance;
    }

    public String buildUrl(Project project, VirtualFile selectedFile){
        if (project != null && selectedFile != null) {
            String path1 = "https://raw.githubusercontent.com/";
            String path2 = getPath2(project);
            if (path2 != null){
                String path3 = getPath3(project, selectedFile);
                return path1 + path2 + path3;
            }
        }
        return null;
    }

    public String copy(Project project, VirtualFile selectedFile){
        String url = buildUrl(project, selectedFile);
        if (url != null){
            CopyPasteManager.getInstance().setContents(new StringSelection(url));
        }
        return url;
    }


    public String copyMd(Project project, VirtualFile selectedFile){
        String url = buildUrl(project, selectedFile);
        if (url != null){
            url = "![](" + url + ")";
            CopyPasteManager.getInstance().setContents(new StringSelection(url));
        }
        return url;
    }



    private String getPath2(Project project) {
        String projPath = project.getBasePath();
        File userNameFile = new File(projPath + "/username.txt");
        if (userNameFile.exists()) {
            VirtualFile userNameVF = LocalFileSystem.getInstance().findFileByIoFile(userNameFile);
            Document document = FileDocumentManager.getInstance().getDocument(userNameVF);
            if (document != null){
                return document.getText();
            }
        } else {
            Notifications.Bus.notify(new Notification(
                    project.getName()
                    , project.getName()
                    , "需要先在项目根目录创建 username.txt 文件，然后写入 github 的用户昵称，就是每个仓库前的那个名称。多的什么都不要多写！"
                    , NotificationType.INFORMATION));
        }
        return null;
    }

    private String getPath3(Project project, VirtualFile selectedFile) {
        int startIndex = project.getBasePath().length();
        String path3 = "/" + project.getName() + "/master" + selectedFile.getPath().substring(startIndex);
        return path3;
    }
}
