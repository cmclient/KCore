package pl.kuezese.core.comparator;

import pl.kuezese.core.object.User;

import java.util.Comparator;

public class CoinsComparator implements Comparator<User> {

    @Override
    public int compare(User o1, User o2) {
        Float c1 = o1.getCoins();
        Float c2 = o2.getCoins();
        return c1.compareTo(c2);
    }
}