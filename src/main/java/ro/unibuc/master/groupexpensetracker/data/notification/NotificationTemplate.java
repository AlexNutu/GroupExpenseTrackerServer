package ro.unibuc.master.groupexpensetracker.data.notification;

public interface NotificationTemplate {
    public static final String ADD_MEMBER = "{username1} a fost adaugat in {trip} de {username2}.";
    public static final String ADD_SIMPLE_EXPENSE = "{username1} a cumparat individual {product} in valoare de {expense} {currency} pentru {trip}.";
    public static final String ADD_EXPENSE = "{username1} a platit {expense} {currency} pentru {product}.";
    public static final String COLLECT_SUM = "{username1} a platit {expense} {currency} pentru {product}. S-au strans pana acum {percent}% din suma totala.";
    public static final String COLLECT_SUM_SUCCESSFULLY = "{username1} a platit {expense} {currency} pentru {product}. S-au strans pana acum {percent}% din suma totala. Ai suma necesara acum pentru a achizitiona {product}";
}
