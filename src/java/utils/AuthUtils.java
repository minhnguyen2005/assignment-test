package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.UserDTO;

public class AuthUtils {

    public static UserDTO getCurrentUser(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            return (UserDTO) session.getAttribute("user");
        }
        return null;
    }

    public static boolean isLoggedIn(HttpServletRequest request){
        return getCurrentUser(request) != null;
    }

    public static boolean hasRole(HttpServletRequest request, String role){
        UserDTO user = getCurrentUser(request);
        if(user != null && user.getRole() != null){
            return user.getRole().equalsIgnoreCase(role);
        }
        return false;
    }

    public static boolean isAdmin(HttpServletRequest request){
        return hasRole(request, "Ad");
    }

    public static boolean isUser(HttpServletRequest request){
        return hasRole(request, "User");
    }

    public static String getLoginURL(){
        return "MainController";
    }

    public static String getAccessDeniedMessage(String action){
        return "You can not access " + action + ". Please contact the administrator.";
    }
}
