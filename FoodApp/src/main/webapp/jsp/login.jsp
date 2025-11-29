<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    request.setAttribute("pageTitle", "login");
%>

<%@ include file="shared/head.jspf" %>
<%@ include file="shared/header.jspf" %>

<div class="main-content">
    <div class="container">
		<!-- LOGIN PAGE INTRO -->
		<div class="text-center mb-5">
		    <h2 class="page-title"> Login to Your Account</h2>
		    <p style="color: var(--medium-text); max-width: 500px; margin: auto;">
		        
		    </p>
		</div>
				
        <div class="login-grid" style="max-width: 1100px; margin: auto;">

     
            <!-- LEFT : LOGIN FORM -->
            <div class="card p-3 login-card">


                <h2 class="page-title text-center mb-3">Login</h2>
                

                <!-- SUCCESS MESSAGE -->
                <c:if test="${not empty message}">
                    <div class="mb-3"
                         style="background:#e6ffee; padding:12px; border-radius:8px; color:#0a7a2f;">
                        ${message}
                    </div>
                </c:if>

                <!-- ERROR MESSAGE -->
                <c:if test="${not empty error}">
                    <div class="mb-3"
                         style="background:#ffecec; padding:12px; border-radius:8px; color:#b00020;">
                        ${error}
                    </div>
                </c:if>

                <!-- LOGIN FORM -->
                <form action="${pageContext.request.contextPath}/user" method="post">

                    <!-- Hidden action field (MANDATORY for your servlet) -->
                    <input type="hidden" name="action" value="login"/>

                    <div class="form-group">
                        <label class="form-label">Username</label>
                        <input type="text"
                               name="username"
                               class="form-control"
                               placeholder="Enter your username"
                               required />
                    </div>

                    <div class="form-group">
    <label class="form-label" for="password">Password</label>

    <div class="password-wrapper">
        <input type="password"
               id="password"
               name="password"
               class="form-control"
               placeholder="Enter your password"
               required />

        <!-- Eye Icon -->
        <span class="toggle-password" onclick="togglePassword()">👁️</span>
    </div>
</div>

                    <button type="submit" class="btn btn--primary" style="width:100%;">
                        Login
                    </button>

                </form>

				 <p class="text-center mt-4">
						Don’t have an account?
						<a href="${pageContext.request.contextPath}/user?action=register"
						style="color: var(--primary); font-weight:600; text-decoration: underline;">
				        Register
				    </a>
				</p>


            </div>

            <!-- RIGHT : IMAGE / INFO -->
            <div class="text-center">
                <img src="${pageContext.request.contextPath}/assets/images/food-banner.jpg"
                     alt="Food delivery"
                     style="max-width: 580px; border-radius: 12px; box-shadow: var(--shadow);">
            </div>

        </div>

    </div>
</div>
<script>
    function togglePassword() {
        const passwordInput = document.getElementById("password");
        const icon = document.querySelector(".toggle-password");

        if (passwordInput.type === "password") {
            passwordInput.type = "text";
            icon.textContent = "🙈"; // hide icon
        } else {
            passwordInput.type = "password";
            icon.textContent = "👁️"; // show icon
        }
    }
</script>

<%@ include file="shared/footer.jspf" %>
