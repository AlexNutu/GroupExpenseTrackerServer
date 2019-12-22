package ro.unibuc.master.groupexpensetracker.domain.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.master.groupexpensetracker.common.notification.NotificationModel;
import ro.unibuc.master.groupexpensetracker.common.notification.NotificationTemplate;
import ro.unibuc.master.groupexpensetracker.common.notification.NotificationTemplateParameters;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.presentation.dto.NotificationDTO;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

@Slf4j
@Service
@NoArgsConstructor
public class NotificationService {

    public NotificationDTO sendAddMemberNotification(UserProfile newMember, String tripName, UserProfile currentMember) {
        NotificationTemplateParameters notificationTemplateParameters = new NotificationTemplateParameters.NotificationParametersBuilder()
                .username1(newMember.getName())
                .trip(tripName)
                .username2(currentMember.getName())
                .build();

        NotificationModel notificationModel = new NotificationModel(NotificationTemplate.ADD_MEMBER, notificationTemplateParameters);
        String message = getMessage(notificationModel);
        return new NotificationDTO(tripName, message, LocalDateTime.now());
    }

    public NotificationDTO sendCollectSumNotification(Expense expense) {
        NotificationTemplateParameters notificationTemplateParameters = new NotificationTemplateParameters.NotificationParametersBuilder()
                .username1(expense.getUserProfile().getName())
                .expense(String.valueOf(expense.getSum()))
                .currency(expense.getCurrency())
                .trip(expense.getTrip().getName())
                .product(expense.getProduct())
                .percent(String.valueOf(expense.getPercent()))
                .build();

        NotificationModel notificationModel = new NotificationModel(NotificationTemplate.COLLECT_SUM, notificationTemplateParameters);
        String message = getMessage(notificationModel);
        return new NotificationDTO(expense.getTrip().getName(), message, LocalDateTime.now());
    }

    // TODO: to be replaced with a more efficient way to create message notification
    private String getMessage(NotificationModel notificationModel) {
        String message = notificationModel.getTemplate();
        for (Field field : notificationModel.getParameters().getClass().getDeclaredFields()) {
            field.setAccessible(true);

            String value = null;
            try {
                value = (String) field.get(notificationModel.getParameters());
            } catch (IllegalAccessException e) {
                log.error(e.getMessage());
            }

            if (value != null) {
                String replaceParam = "{" + field.getName() + "}";
                message = message.replace(replaceParam, value);
            }
        }
        return message;
    }
}
