package bitc.aws402.miniproject.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheck implements HandlerInterceptor {
//  메인 사이트용 LoginCheck Interceptor
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    HttpSession session = request.getSession();
    System.out.println("\n========== Interceptor 동작 ==========\n");
    if (session == null || session.getAttribute("memberId") == null) {
      System.out.println("로그인 되어 있지않음");

//      response.sendRedirect("/admin");
      return true;
    } else {
      System.out.println("로그인 상태");

      session.setMaxInactiveInterval(60 * 30);
      return true;
    }
  }
}
