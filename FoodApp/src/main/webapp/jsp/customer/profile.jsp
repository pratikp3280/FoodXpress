<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../shared/head.jspf" %>
<%@ include file="../shared/header.jspf" %>

<div class="container main-content">

  <div class="card">
    <h2 class="page-title">My Profile</h2>

    <!-- =========================
         PROFILE UPDATE FORM
    ========================= -->
    <form method="post" action="${pageContext.request.contextPath}/user">
      <input type="hidden" name="action" value="profileUpdate"/>

      <label>Name</label>
      <input class="form-control" name="name"
             value="${sessionScope.loggedUser.name}" required/>

      <label>Email</label>
      <input class="form-control" name="email"
             value="${sessionScope.loggedUser.email}" required/>

      <label>Phone</label>
      <input class="form-control" name="phone"
             value="${sessionScope.loggedUser.phone}" required/>

      <label>Address</label>
      <input class="form-control" name="address"
             value="${sessionScope.loggedUser.address}"/>

      <label>New Password</label>
      <input class="form-control" type="password" name="newPassword"/>

      <button class="btn btn--primary mt-3">
        Update Profile
      </button>
    </form>

    <!-- =========================
         ACCOUNT ACTIONS
    ========================= -->
    <hr class="mt-4 mb-3"/>

    <h3 class="section-title">Account Actions</h3>

    <div class="grid-3 mt-3">

      <!-- Manage Addresses -->
      <a class="card action-card"
         href="${pageContext.request.contextPath}/customer/addresses">
        📍 Manage Addresses
      </a>

      <!-- View Orders -->
      <a class="card action-card"
         href="${pageContext.request.contextPath}/orders">
        📦 View Orders
      </a>

      <!-- Go to Cart -->
      <a class="card action-card"
         href="${pageContext.request.contextPath}/cart">
        🛒 Go to Cart
      </a>

    </div>

  </div>

</div>

<%@ include file="../shared/footer.jspf" %>
