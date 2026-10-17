package ru.itmo.web.hw4.util;

import ru.itmo.web.hw4.model.Color;
import ru.itmo.web.hw4.model.Post;
import ru.itmo.web.hw4.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataUtil {
    private static final List<User> USERS = Arrays.asList(
            new User(1, "MikeMirzayanov", "Mike Mirzayanov", Color.RED),
            new User(6, "pashka", "Pavel Mavrin", Color.GREEN),
            new User(9, "geranazavr555", "Georgiy Nazarov", Color.BLUE),
            new User(11, "tourist", "Gennady Korotkevich", Color.RED)
    );

    private static final List<Post> POSTS = Arrays.asList(
            new Post(1, "Welcome to Codeforces",
                    "Hello, everybody! This is the first post on our small Codeforces clone. "
                            + "Here you will find announcements of rounds, editorials and other news.",
                    1),
            // текст из HW2
            new Post(2, "Codeforces Round #510 (Div. 2)",
                    "Hello, Codeforces! Codeforces Round #510 (Div. 2) will start at Monday, September 17, 2018 "
                            + "at 11:05. The round will be rated for Div. 2 contestants (participants with the rating "
                            + "below 2100). Div. 1 participants can take a part out of competition as usual. "
                            + "This round is held on the tasks of the school stage All-Russian Olympiad of Informatics "
                            + "2018/2019 year in city Saratov. The problems were prepared by PikMike, fcspartakm, "
                            + "Ne0n25, BledDest, Ajosteen and Vovuh. Great thanks to our coordinator _kun_ for the help "
                            + "with the round preparation! I also would like to thank our testers DavidDenisov, "
                            + "PrianishnikovaRina, Decibit and Vshining. "
                            + "UPD: The scoring distribution is 500-1000-1500-2000-2250-2750.",
                    1),
            new Post(3, "Editorial of Round #510",
                    "Problem A could be solved with a simple loop. In problem B it was enough to sort the array.",
                    9),
            new Post(4, "How I train",
                    "Solve a lot of problems, do it regularly and always upsolve after contests.",
                    11)
    );

    public static void addData(HttpServletRequest request, Map<String, Object> data) {
        data.put("users", USERS);
        data.put("posts", POSTS);

        for (User user : USERS) {
            if (Long.toString(user.getId()).equals(request.getParameter("logged_user_id"))) {
                data.put("user", user);
            }
        }
    }
}
