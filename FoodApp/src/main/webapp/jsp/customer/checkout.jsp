<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="pageTitle" value="Checkout" />

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/jsp/shared/head.jspf" %>
</head>

<body>

<%@ include file="/jsp/shared/header.jspf" %>

<main class="main-content">
    <div class="container">

        <h1 class="page-title">Checkout</h1>

        <!-- ================== ERROR MESSAGE ================== -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert--error">
                ${errorMessage}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/checkout" method="post">

            <div class="checkout-layout" style="display:grid;grid-template-columns:2fr 1fr;gap:24px">

                <!-- ================= LEFT: DELIVERY + PAYMENT ================= -->
                <div class="card">

                    <!-- ========= DELIVERY ADDRESS ========= -->
                    <h3>Delivery Address</h3>

                    <p class="muted">
                        (Using saved address – can be extended later)
                    </p>

                    <!-- For now addressId can be optional -->
                    <input type="hidden" name="addressId" value="" />

                    <!-- ========= DELIVERY INSTRUCTIONS ========= -->
                    <h3 class="mt-4">Delivery Instructions</h3>
                    <textarea
                        name="instructions"
                        rows="3"
                        placeholder="Eg: Call before arriving, leave at door..."
                        style="width:100%"></textarea>

                    <!-- ========= PAYMENT METHOD ========= -->
                    <h3 class="mt-4">Payment Method</h3>

                    <label>
                        <input type="radio" name="payment_method" value="COD" checked>
                        Cash on Delivery
                    </label>

                    <br>

                    <label class="muted">
                        <input type="radio" disabled>
                        Online Payment (Coming soon)
                    </label>

                </div>

                <!-- ================= RIGHT: ORDER SUMMARY ================= -->
                <div class="card">

                    <h3>Order Summary</h3>

                    <!-- CART ITEMS -->
                    <c:forEach var="ci" items="${cartItems}">
                        <c:set var="item" value="${menuItems[ci.menuItemId]}" />

                        <div style="display:flex;justify-content:space-between;font-size:14px;margin-bottom:8px">
                            <span>
                                ${item.name} × ${ci.quantity}
                            </span>
                            <span>
                                ₹ <fmt:formatNumber
                                        value="${item.price * ci.quantity}"
                                        minFractionDigits="2"
                                        maxFractionDigits="2"/>
                            </span>
                        </div>
                    </c:forEach>

                    <hr>

                    <!-- TOTALS -->
                    <c:set var="tax" value="${totalAmount * 0.03}" />

                    <div class="bill-row">
                        <span>Item Total</span>
                        <span>₹
                            <fmt:formatNumber
                                value="${totalAmount}"
                                minFractionDigits="2"
                                maxFractionDigits="2"/>
                        </span>
                    </div>

                    <div class="bill-row">
                        <span>Taxes (3%)</span>
                        <span>₹
                            <fmt:formatNumber
                                value="${tax}"
                                minFractionDigits="2"
                                maxFractionDigits="2"/>
                        </span>
                    </div>

                    <div class="bill-row">
                        <span>Delivery + Platform</span>
                        <span>₹ 25.00</span>
                    </div>

                    <hr>

                    <div class="bill-row bill-total" style="font-weight:700">
                        <span>Total Payable</span>
                        <span>₹
                            <fmt:formatNumber
                                value="${totalAmount + tax + 25}"
                                minFractionDigits="2"
                                maxFractionDigits="2"/>
                        </span>
                    </div>

                    <!-- RESTAURANT ID (IMPORTANT) -->
                    <input type="hidden"
                           name="restaurantId"
                           value="${menuItems.values().iterator().next().restaurantId}" />

                    <button type="submit" class="btn btn--primary mt-4 w-100">
                        Place Order
                    </button>

                </div>
            </div>

        </form>

    </div>
</main>

<%@ include file="/jsp/shared/footer.jspf" %>

</body>
</html>
