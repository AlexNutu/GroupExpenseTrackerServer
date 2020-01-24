package ro.unibuc.master.groupexpensetracker.common.notification;

public interface NotificationTemplate {
    public static final String ADD_MEMBER = "{username1} was added to {trip}.";
    public static final String ADD_SIMPLE_EXPENSE = "{username1} individually bought {product} worth {expense} {currency} for {trip}.";
    public static final String INITIAL_GROUP_EXPENSE = "{username1} paid {expense} {currency} for {product}. \nYou have to pay {remainingSum} {currency}.";
    public static final String EXPENSE = "{username1} paid {expense} {currency} for {product}.";
    public static final String INITIAL_COLLECT_EXPENSE = "{username1} wants to buy {product} worth {expense} {currency} for {trip}. \nYou have to pay {remainingSum} {currency}.";
    public static final String COLLECT_SUM = "{username1} a platit {expense} {currency} pentru {product}.\n S-au strans pana acum {percent}% din suma totala.";
    public static final String COLLECT_SUM_SUCCESSFULLY = "{username1} a platit {expense} {currency} pentru {product}. S-au strans pana acum {percent}% din suma totala.\n Ai suma necesara acum pentru a achizitiona {product}";
}
