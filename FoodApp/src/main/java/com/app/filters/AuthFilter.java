package com.app.filters;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

import com.app.models.User;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final String SESSION_USER = "loggedUser";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        String path = uri.substring(contextPath.length());

        /* ===============================
           ✅ PUBLIC / WHITELISTED PATHS
        =============================== */
        if (
                path.equals("/") ||
                path.equals("/index.jsp") ||

                // static resources
                path.startsWith("/assets/") ||

                // login/register servlet
                path.startsWith("/user") ||

                // shared JSP fragments
                path.startsWith("/jsp/shared/") ||
                path.endsWith(".jspf")
        ) {
            chain.doFilter(request, response);
            return;
        }

        /* ===============================
           ✅ AUTH CHECK
        =============================== */
        HttpSession session = request.getSession(false);
        User user = (session == null) ? null
                : (User) session.getAttribute(SESSION_USER);

        if (user == null) {
            response.sendRedirect(contextPath + "/user?action=login");
            return;
        }

        String role = user.getRole();

        /* ===============================
           ✅ ROLE BASED ACCESS
        =============================== */
        if (path.startsWith("/jsp/admin/")
                && !"admin".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (path.startsWith("/jsp/customer/")
                && !"customer".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        /* ===============================
           ✅ ALL CHECKS PASSED
        =============================== */
        chain.doFilter(request, response);
    }
}
