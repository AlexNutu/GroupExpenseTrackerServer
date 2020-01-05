package ro.unibuc.master.groupexpensetracker.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ro.unibuc.master.groupexpensetracker.common.notification.NotificationModel;
import ro.unibuc.master.groupexpensetracker.common.notification.NotificationTemplate;
import ro.unibuc.master.groupexpensetracker.common.notification.NotificationTemplateParameters;
import ro.unibuc.master.groupexpensetracker.common.utils.EntitySpecification;
import ro.unibuc.master.groupexpensetracker.common.utils.EntityUtils;
import ro.unibuc.master.groupexpensetracker.common.utils.SearchCriteria;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.data.notification.Notification;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.domain.repository.NotificationRepository;
import ro.unibuc.master.groupexpensetracker.presentation.dto.NotificationDTO;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationDTO sendAddMemberNotification(UserProfile newMember, String tripName, UserProfile currentMember) {
        NotificationTemplateParameters notificationTemplateParameters = new NotificationTemplateParameters.NotificationParametersBuilder()
                .username1(newMember.getFirstName() + " " + newMember.getLastName())
                .trip(tripName)
                .username2(currentMember.getFirstName() + " " + currentMember.getLastName())
                .build();

        NotificationModel notificationModel = new NotificationModel(NotificationTemplate.ADD_MEMBER, notificationTemplateParameters);
        String message = getMessage(notificationModel);
        return new NotificationDTO(tripName, message, LocalDateTime.now());
    }

    public void saveCollectExpensiveNotification(Expense expense, String percent, String notificationType, UserProfile userProfile) {
        NotificationTemplateParameters notificationTemplateParameters = new NotificationTemplateParameters.NotificationParametersBuilder()
                .username1(expense.getUser().getFirstName() + " " + expense.getUser().getLastName())
                .expense(String.valueOf(expense.getSum()))
                .currency(expense.getCurrency())
                .percent(percent)
                .product(expense.getProduct())
                .build();

        NotificationModel notificationModel = new NotificationModel(notificationType, notificationTemplateParameters);
        String message = getMessage(notificationModel);

        Notification notification = new Notification(expense.getTrip().getName(), message, false, userProfile);
        notificationRepository.save(notification);
    }

    public void saveSimpleExpenseNotification(Expense expense, List<UserProfile> userProfileList) {
        NotificationTemplateParameters notificationTemplateParameters = new NotificationTemplateParameters.NotificationParametersBuilder()
                .username1(expense.getUser().getFirstName() + " " + expense.getUser().getLastName())
                .expense(String.valueOf(expense.getSum()))
                .currency(expense.getCurrency())
                .trip(expense.getTrip().getName())
                .product(expense.getProduct())
                .build();

        NotificationModel notificationModel = new NotificationModel(NotificationTemplate.ADD_SIMPLE_EXPENSE, notificationTemplateParameters);
        String message = getMessage(notificationModel);

        List<Notification> notifications = new ArrayList<>();
        for (UserProfile userProfile : userProfileList) {
            if (!userProfile.equals(expense.getUser())) {
                Notification notification = new Notification(expense.getTrip().getName(), message, false, userProfile);
                notifications.add(notification);
            }
        }
        notificationRepository.saveAll(notifications);
    }

    public void saveInitialGroupExpenseNotification(Expense expense, List<UserProfile> userProfileList) {
        NotificationTemplateParameters notificationTemplateParameters = new NotificationTemplateParameters.NotificationParametersBuilder()
                .username1(expense.getUser().getFirstName() + " " + expense.getUser().getLastName())
                .expense(String.valueOf(expense.getSum()))
                .currency(expense.getCurrency())
                .remainingSum(String.valueOf(expense.getSum() / userProfileList.size()))
                .product(expense.getProduct())
                .build();

        NotificationModel notificationModel = new NotificationModel(NotificationTemplate.INITIAL_GROUP_EXPENSE, notificationTemplateParameters);
        String message = getMessage(notificationModel);

        List<Notification> notifications = new ArrayList<>();
        for (UserProfile userProfile : userProfileList) {
            if (!userProfile.equals(expense.getUser())) {
                Notification notification = new Notification(expense.getTrip().getName(), message, false, userProfile);
                notifications.add(notification);
            }
        }
        notificationRepository.saveAll(notifications);
    }

    public void saveInitialCollectExpenseNotification(Expense expense, List<UserProfile> userProfileList) {
        NotificationTemplateParameters notificationTemplateParameters = new NotificationTemplateParameters.NotificationParametersBuilder()
                .username1(expense.getUser().getFirstName() + " " + expense.getUser().getLastName())
                .expense(String.valueOf(expense.getSum()))
                .currency(expense.getCurrency())
                .remainingSum(String.valueOf(expense.getSum() / userProfileList.size()))
                .product(expense.getProduct())
                .trip(expense.getTrip().getName())
                .build();

        NotificationModel notificationModel = new NotificationModel(NotificationTemplate.INITIAL_COLLECT_EXPENSE, notificationTemplateParameters);
        String message = getMessage(notificationModel);

        List<Notification> notifications = new ArrayList<>();
        for (UserProfile userProfile : userProfileList) {
            if (!userProfile.equals(expense.getUser())) {
                Notification notification = new Notification(expense.getTrip().getName(), message, false, userProfile);
                notifications.add(notification);
            }
        }
        notificationRepository.saveAll(notifications);
    }

    public void saveExpenseNotification(Expense expense, UserProfile userProfile) {
        NotificationTemplateParameters notificationTemplateParameters = new NotificationTemplateParameters.NotificationParametersBuilder()
                .username1(expense.getUser().getFirstName() + " " + expense.getUser().getLastName())
                .expense(String.valueOf(expense.getSum()))
                .currency(expense.getCurrency())
                .product(expense.getProduct())
                .build();

        NotificationModel notificationModel = new NotificationModel(NotificationTemplate.EXPENSE, notificationTemplateParameters);
        String message = getMessage(notificationModel);

        Notification notification = new Notification(expense.getTrip().getName(), message, false, userProfile);
        notificationRepository.save(notification);
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

    public Page<Notification> findAll(Sort.Direction sortingDirection, String orderBy, final String search, final Integer offset, final Integer size) {
        final List<SearchCriteria> searchCriteriaList = EntityUtils.generateSearchCriteria(search);
        final Specification<Notification> spec = new EntitySpecification<>(searchCriteriaList);

        if (sortingDirection == null && orderBy == null) {
            orderBy = "createDate";
            sortingDirection = Sort.Direction.DESC;
        }

        Page<Notification> notificationPage;
        if (size != null) {
            notificationPage = notificationRepository.findAll(spec,
                    EntityUtils.getPageRequest(sortingDirection, orderBy, offset, size));
        } else {
            notificationPage = new PageImpl<>(notificationRepository.findAll(spec));
        }

        List<Notification> notifications = notificationPage.getContent();
        for (Notification notification : notifications) {
            notification.setSent(true);
            notificationRepository.save(notification);
        }

        return notificationPage;
    }
}
