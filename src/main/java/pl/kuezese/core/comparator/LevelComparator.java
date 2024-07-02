package pl.kuezese.core.comparator;

import pl.kuezese.core.object.User;

import java.util.Comparator;

public class LevelComparator implements Comparator<User> {

    @Override
    public int compare(User o1, User o2) {
        Integer c1 = o1.getLvl();
        Integer c2 = o2.getLvl();
        return c1.compareTo(c2);
    }
}