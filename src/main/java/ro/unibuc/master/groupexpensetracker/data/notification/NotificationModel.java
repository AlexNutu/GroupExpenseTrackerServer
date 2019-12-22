package ro.unibuc.master.groupexpensetracker.data.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationModel {
    private String template;
    private NotificationTemplateParameters parameters;
}
