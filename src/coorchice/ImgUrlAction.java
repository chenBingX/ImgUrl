package coorchice;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;


public class ImgUrlAction extends AnAction {

    private BuildUrl build = BuildUrl.getInstance();

    @Override
    public void actionPerformed(AnActionEvent event) {
        if (event == null){
            return;
        }
        Project project = event.getProject();
        VirtualFile selectedFile = DataKeys.VIRTUAL_FILE.getData(event.getDataContext());
        String url = build.copy(project, selectedFile);
        if (url != null){
            Notifications.Bus.notify(new Notification(
                    project.getName()
                    , project.getName()
                    , "url已复制到剪切板!  ->  " + url
                    , NotificationType.INFORMATION));
        }
    }



    @Override
    public void update(AnActionEvent e) {
        super.update(e);
    }
}
