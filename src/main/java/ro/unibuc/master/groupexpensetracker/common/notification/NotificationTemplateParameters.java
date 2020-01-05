package ro.unibuc.master.groupexpensetracker.common.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class NotificationTemplateParameters {
    private String username1;
    private String username2;
    private String expense;
    private String currency;
    private String trip;
    private String product;
    private String percent;
    private String remainingSum;

    public NotificationTemplateParameters(NotificationParametersBuilder notificationParametersBuilder) {
        this.username1 = notificationParametersBuilder.username1;
        this.username2 = notificationParametersBuilder.username2;
        this.expense = notificationParametersBuilder.expense;
        this.currency = notificationParametersBuilder.currency;
        this.trip = notificationParametersBuilder.trip;
        this.product = notificationParametersBuilder.product;
        this.percent = notificationParametersBuilder.percent;
        this.remainingSum = notificationParametersBuilder.remainingSum;
    }

    @NoArgsConstructor
    public static class NotificationParametersBuilder {
        private String username1;
        private String username2;
        private String expense;
        private String currency;
        private String trip;
        private String product;
        private String percent;
        private String remainingSum;

        public NotificationParametersBuilder username1(String username1) {
            this.username1 = username1;
            return this;
        }

        public NotificationParametersBuilder username2(String username2) {
            this.username2 = username2;
            return this;
        }

        public NotificationParametersBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public NotificationParametersBuilder expense(String expense) {
            this.expense = expense;
            return this;
        }

        public NotificationParametersBuilder trip(String trip) {
            this.trip = trip;
            return this;
        }

        public NotificationParametersBuilder product(String product) {
            this.product = product;
            return this;
        }

        public NotificationParametersBuilder percent(String percent) {
            this.percent = percent;
            return this;
        }

        public NotificationParametersBuilder remainingSum(String remainingSum) {
            this.remainingSum = remainingSum;
            return this;
        }

        public NotificationTemplateParameters build() {
            return new NotificationTemplateParameters(this);
        }
    }
}
