package start.find.login.user;

import org.springframework.security.core.context.SecurityContextHolder;

public class FindLoginUser {
    public static String findLoginUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
