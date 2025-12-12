<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ include file="/jsp/shared/head.jspf"%>

<body>
	<%@ include file="/jsp/shared/header.jspf"%>

	<div class="container main-content">
		<h2 class="page-title">My Orders</h2>

		<!-- EMPTY STATE -->
		<c:if test="${empty orders}">
			<div class="card text-center p-4">
				<img
					src="${pageContext.request.contextPath}/assets/images/empty-orders.svg"
					alt="No Orders" style="width: 200px; margin: auto;">
				<h3 class="mt-3">You haven’t ordered yet!</h3>
				<p class="mb-3">Explore restaurants and place your first order.</p>
				<a href="${pageContext.request.contextPath}/restaurantList"
					class="btn btn--primary"> Explore Restaurants → </a>
			</div>
		</c:if>

		<!-- ORDER LIST -->
		<c:forEach var="order" items="${orders}">
			<div class="card mb-4">
				<div class="card__body">

					<!-- HEADER -->
					<div
						style="display: flex; justify-content: space-between; align-items: center;">
						<div>
							<c:set var="restaurantName"
								value="${restaurantsMap[order.restaurantId]}" />
							<div class="card__title">${restaurantName}</div>
							<small class="text-light"> Order #${order.orderId} | <fmt:formatDate
									value="${order.createdAt}" pattern="dd MMM yyyy | hh:mm a" />
							</small>
						</div>

						<div style="text-align: right;">
							<strong>₹ ${order.totalAmount}</strong><br>

							<!-- STATUS BADGE -->
							<c:choose>
								<c:when test="${order.status eq 'placed'}">
									<span class="status-text" style="color: var(--accent-green);">Placed ✅</span>
								</c:when>
								<c:when test="${order.status eq 'preparing'}">
									<span class="status-text" style="color: var(--primary);">Preparing</span>
								</c:when>
								<c:when test="${order.status eq 'delivered'}">
									<span class="status-text">Delivered ✅</span>
								</c:when>
								<c:when test="${order.status eq 'cancelled'}">
									<span class="status-text" style="color: var(--accent-red);">Cancelled</span>
								</c:when>
							</c:choose>

						</div>
					</div>

					<hr>

					<!-- ORDER ITEMS -->
					<div>
						<c:forEach var="item" items="${orderItemsMap[order.orderId]}">
							<c:set var="menu" value="${menuItemsMap[item.menuItemId]}" />
							<div
								style="display: flex; align-items: center; margin-bottom: 8px;">
								<c:if test="${not empty menu.imageUrl}">
									<img src="${pageContext.request.contextPath}/${menu.imageUrl}"
										alt="${menu.name}"
										style="width: 50px; height: 50px; border-radius: 8px; margin-right: 12px;">
								</c:if>
								<div>
									<strong>${menu.name}</strong> × ${item.quantity}<br> <small>₹
										<fmt:formatNumber value="${item.priceAtOrder * item.quantity}" />
									</small>
								</div>
							</div>
						</c:forEach>
					</div>

					<hr>

					<!-- ACTION BUTTONS -->
					<div style="display: flex; gap: 12px; flex-wrap: wrap;">
						<a
							href="${pageContext.request.contextPath}/orderSummary?orderId=${order.orderId}"
							class="btn btn--secondary">View Details</a>

						<c:if test="${order.status eq 'delivered'}">
							<a
								href="${pageContext.request.contextPath}/reorder?orderId=${order.orderId}"
								class="btn btn--primary">Reorder</a>
							<c:if test="${not empty order.invoicePath}">
								<a
									href="${pageContext.request.contextPath}/${order.invoicePath}"
									target="_blank" class="btn btn--secondary">View Invoice</a>
							</c:if>
						</c:if>

						

						<a href="${pageContext.request.contextPath}/support"
							class="btn btn--secondary">Need Help?</a>
					</div>

				</div>
			</div>
		</c:forEach>

	</div>

	<%@ include file="/jsp/shared/footer.jspf"%>
</body>
</html>
