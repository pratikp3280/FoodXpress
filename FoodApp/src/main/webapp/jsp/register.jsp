<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    request.setAttribute("pageTitle", "Register");
%>

<%@ include file="shared/head.jspf" %>
<%@ include file="shared/header.jspf" %>

<div class="main-content">
    <div class="container">

        <div class="grid-2" style="max-width:900px;margin:auto;align-items:center;">

            <!-- REGISTER FORM -->
            <div class="card p-3">

                <h2 class="page-title text-center mb-3">Create Account 🍔</h2>
                <p class="text-center mb-4" style="color: var(--medium-text);">
                    Sign up to start ordering your favorite food
                </p>

                <!-- ERROR MESSAGE -->
                <c:if test="${not empty error}">
                    <div class="mb-3"
                         style="background:#ffecec;padding:12px;border-radius:8px;color:#b00020;">
                        ${error}
                    </div>
                </c:if>

                <!-- REGISTER FORM -->
                <form action="${pageContext.request.contextPath}/user" method="post">

                    <!-- Required by UserServlet -->
                    <input type="hidden" name="action" value="register">

                    <!-- Default role -->
                    <input type="hidden" name="role" value="customer">

                    <!-- NAME -->
                    <div class="form-group">
                        <label class="form-label">Full Name</label>
                        <input type="text"
                               name="name"
                               class="form-control"
                               placeholder="Enter your full name"
                               required>
                    </div>

                    <!-- USERNAME -->
                    <div class="form-group">
                        <label class="form-label">Username</label>
                        <input type="text"
                               name="username"
                               class="form-control"
                               placeholder="Choose a username"
                               required>
                    </div>

                    <!-- EMAIL -->
                    <div class="form-group">
                        <label class="form-label">Email</label>
                        <input type="email"
                               name="email"
                               class="form-control"
                               placeholder="Enter your email">
                    </div>

                    <!-- PHONE -->
                    <div class="form-group">
                        <label class="form-label">Phone</label>
                        <input type="text"
                               name="phone"
                               class="form-control"
                               placeholder="Enter phone number">
                    </div>

                    <!-- ADDRESS -->
                    <div class="form-group">
                        <label class="form-label">Address</label>
                        <textarea name="address"
                                  class="form-control"
                                  rows="3"
                                  placeholder="Delivery address"></textarea>
                    </div>

                    <!-- PASSWORD -->
                    <div class="form-group">
                        <label class="form-label">New Password</label>
                        <input type="password"
                               name="password"
                               class="form-control"
                               placeholder="Create a password"
                               required>
                    </div>

                    <button type="submit" class="btn btn--primary" style="width:100%;">
                        Register
                    </button>

                </form>

                <p class="text-center mt-4">
                    Already have an account?
                    <a href="${pageContext.request.contextPath}/user?action=login"
                       style="color:var(--primary);font-weight:600;text-decoration: underline;">
                        Login here
                    </a>
                </p>

            </div>

            <!-- SIDE IMAGE -->
            <div class="text-center">
                <img src="${pageContext.request.contextPath}/assets/images/food-banner.jpg"
                     alt="Food App"
                     style="max-width:480;border-radius:12px;box-shadow:var(--shadow);">
            </div>

        </div>
    </div>
</div>

<%@ include file="shared/footer.jspf" %>
