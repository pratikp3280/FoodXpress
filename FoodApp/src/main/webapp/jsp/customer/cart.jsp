<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<c:set var="pageTitle" value="My Cart" />

<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="/jsp/shared/head.jspf"%>

<!-- Page-specific Cart Styles -->
<style>
.cart-layout {
	display: grid;
	grid-template-columns: 2fr 1fr;
	gap: 24px;
	align-items: flex-start;
}

@media ( max-width : 900px) {
	.cart-layout {
		grid-template-columns: 1fr;
	}
}

.cart-item {
	display: grid;
	grid-template-columns: 90px 1fr auto;
	gap: 16px;
	align-items: center;
	border-bottom: 1px solid var(--border);
	padding-bottom: 16px;
	margin-bottom: 16px;
}

.cart-item:last-child {
	border-bottom: none;
	margin-bottom: 0;
}

.cart-item img {
	width: 90px;
	height: 70px;
	object-fit: cover;
	border-radius: 8px;
}

.veg-badge {
	font-size: 12px;
	padding: 2px 8px;
	border-radius: 12px;
	color: white;
	background: var(--accent-green);
	font-weight: 600;
}

.nonveg {
	background: var(--accent-red);
}

.qty-box {
	display: flex;
	align-items: center;
	gap: 8px;
}

.qty-box button {
	width: 28px;
	height: 28px;
	border-radius: 50%;
	border: 1px solid var(--border);
	background: #fff;
	font-weight: bold;
}

.remove-btn {
	color: var(--accent-red);
	font-size: 18px;
	cursor: pointer;
}

.bill-card {
	position: sticky;
	top: 100px;
}

.bill-row {
	display: flex;
	justify-content: space-between;
	margin-bottom: 10px;
}

.bill-total {
	font-weight: 700;
	font-size: 1.2rem;
}

.empty-cart {
	text-align: center;
	padding: 60px 0;
}
</style>
</head>

<body>

	<%@ include file="/jsp/shared/header.jspf"%>

	<main class="main-content">
		<div class="container">

			<h1 class="page-title">My Cart</h1>

			<c:if test="${empty cartItems}">
				<div class="card empty-cart">
					<p>Your cart is empty 😕</p>
					<a href="${pageContext.request.contextPath}/restaurant?action=list"
						class="btn btn--primary mt-4"> Browse Restaurants </a>
				</div>
			</c:if>

			<c:if test="${not empty cartItems}">
				<div class="cart-layout">

					<!-- ================= LEFT: CART ITEMS ================= -->
					<div class="card">

						<c:forEach var="ci" items="${cartItems}">
							<c:set var="item" value="${menuItems[ci.menuItemId]}" />

							<div class="cart-item" id="item-${ci.menuItemId}">

								<!-- Image -->
								<img
									src="${pageContext.request.contextPath}/${empty item.imageUrl ? 'assets/images/default_image.png' : item.imageUrl}"
									alt="${item.name}"
									onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/default_image.png';" />


								<!-- Item Info -->
								<div>
									<strong>${item.name}</strong><br> <span
										class="veg-badge ${!item.veg ? 'nonveg' : ''}">
										${item.veg ? 'VEG' : 'NON-VEG'} </span>

									<p class="mt-1">₹ ${item.price}</p>

									<div class="qty-box mt-2">
										<button onclick="updateQty(${ci.menuItemId}, -1)">−</button>
										<span id="qty-${ci.menuItemId}">${ci.quantity}</span>
										<button onclick="updateQty(${ci.menuItemId}, 1)">+</button>
									</div>
								</div>

								<!-- Remove -->
								<span class="remove-btn" onclick="removeItem(${ci.menuItemId})">
									✖ </span>
							</div>
						</c:forEach>

						<button class="btn btn--secondary mt-4" onclick="clearCart()">
							Clear Cart</button>
					</div>

					<!-- ================= RIGHT: BILL SUMMARY ================= -->
					<div class="card bill-card">
						<h3 class="mb-3">Bill Summary</h3>

						<div class="bill-row">
							<span>Item Total</span> <span>₹ <span id="itemTotal">${cartTotal}</span></span>
						</div>

						<div class="bill-row">
							<span>Delivery Fee</span> <span>₹ 20</span>
						</div>

						<div class="bill-row">
							<span>Platform Fee</span> <span>₹ 5</span>
						</div>

						<c:set var="tax" value="${cartTotal * 0.03}" />

						<div class="bill-row">
							<span>Taxes (3%)</span> <span> ₹ <span id="tax"> <fmt:formatNumber
										value="${tax}" minFractionDigits="2" maxFractionDigits="2" />
							</span>
							</span>
						</div>


						<hr>

						<div class="bill-row bill-total">
							<span> ₹ <span id="grandTotal"> <fmt:formatNumber
										value="${cartTotal + tax + 25}" minFractionDigits="2"
										maxFractionDigits="2" />
							</span>
							</span>

						</div>

						<a href="${pageContext.request.contextPath}/checkout"
							class="btn btn--primary mt-4 w-100"> Proceed to Checkout </a> <a
							href="${pageContext.request.contextPath}/restaurant?action=list"
							class="btn btn--secondary mt-3 w-100"> Back to Restaurant </a>
					</div>
				</div>
			</c:if>

		</div>
	</main>

	<%@ include file="/jsp/shared/footer.jspf"%>

	<!-- ================= AJAX ================= -->
	<script>
    function updateQty(menuItemId, delta) {
        const qtyEl = document.getElementById("qty-" + menuItemId);
        let qty = parseInt(qtyEl.innerText) + delta;

        if (qty <= 0) {
            removeItem(menuItemId);
            return;
        }

        fetch('${pageContext.request.contextPath}/cart', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: `action=update&menuItemId=${menuItemId}&quantity=${qty}`
        })
        .then(() => location.reload());
    }

    function removeItem(menuItemId) {
        fetch('${pageContext.request.contextPath}/cart?action=remove&menuItemId=' + menuItemId)
            .then(() => location.reload());
    }

    function clearCart() {
        if (!confirm("Clear your cart?")) return;
        fetch('${pageContext.request.contextPath}/cart?action=clear')
            .then(() => location.reload());
    }
</script>

</body>
</html>
