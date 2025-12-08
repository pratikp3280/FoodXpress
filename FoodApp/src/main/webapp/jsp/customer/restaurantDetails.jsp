<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="pageTitle" value="${restaurant.name}" />

<!DOCTYPE html>
<html lang="en">

<head>
    <%@ include file="/jsp/shared/head.jspf" %>

    <!-- Page specific micro-styles (UI only) -->
    <style>
        .veg-badge {
            padding: 4px 10px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            color: white;
            background: var(--accent-green);
        }

        .nonveg-badge {
            background: var(--accent-red);
        }

        .qty-selector {
            display: flex;
            align-items: center;
            gap: 8px;
            margin-top: 8px;
        }

        .qty-selector button {
            width: 28px;
            height: 28px;
            border-radius: 50%;
            border: 1px solid var(--border);
            background: #fff;
            font-weight: 600;
        }

        /* Sticky Cart Summary */
        .cart-summary {
            position: fixed;
            bottom: 20px;
            right: 20px;
            background: var(--card-bg);
            box-shadow: var(--shadow-hover);
            border-radius: 14px;
            padding: 16px 22px;
            z-index: 999;
        }

        .cart-summary.hidden {
            display: none;
        }
    </style>
</head>

<body>

<%@ include file="/jsp/shared/header.jspf" %>

<main class="main-content">
    <div class="container">

        <!-- RESTAURANT HEADER -->
        <section class="card mb-5">

            <!-- Restaurant image with CDN/local detection & fallback -->
            <c:set var="rImg" value="${restaurant.imageUrl}" />
            <c:choose>
                <c:when test="${not empty rImg and (fn:startsWith(rImg,'http://') or fn:startsWith(rImg,'https://'))}">
                    <img class="banner-img"
                         src="${rImg}"
                         alt="${restaurant.name}"
                         loading="lazy"
                         onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/assets/images/restaurants/default-restaurant.jpg';" />
                </c:when>

                <c:when test="${not empty rImg}">
                    <img class="banner-img"
                         src="${pageContext.request.contextPath}/${rImg}"
                         alt="${restaurant.name}"
                         loading="lazy"
                         onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/assets/images/restaurants/default-restaurant.jpg';" />
                </c:when>

                <c:otherwise>
                    <img class="banner-img"
                         src="${pageContext.request.contextPath}/assets/images/restaurants/default-restaurant.jpg"
                         alt="${restaurant.name}" />
                </c:otherwise>
            </c:choose>

            <div class="mt-3">
                <h1 class="page-title">${restaurant.name}</h1>
                <p class="mb-2">${restaurant.cuisineType}</p>
                <p class="mb-3">${restaurant.description}</p>

                <div class="grid-3">
                    <div>⭐ ${restaurant.rating}</div>
                    <div>🕒 ${restaurant.deliveryTime} mins</div>
                    <div>📍 ${restaurant.city}</div>
                </div>
            </div>
        </section>

        <!-- MENU -->
        <h2 class="mb-4">Menu</h2>

        <div class="grid-3">
            <c:forEach var="item" items="${menuItems}">
                <c:if test="${item.available}">
                    <div class="card">

                        <!-- menu item image with CDN/local detection & fallback -->
                        <c:set var="imgUrl" value="${item.imageUrl}" />
                        <c:choose>
                            <c:when test="${not empty imgUrl and (fn:startsWith(imgUrl,'http://') or fn:startsWith(imgUrl,'https://'))}">
                                <img class="card__image"
                                     src="${imgUrl}"
                                     alt="${item.name}"
                                     loading="lazy"
                                     onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/assets/images/default-food.png';" />
                            </c:when>

                            <c:when test="${not empty imgUrl}">
                                <img class="card__image"
                                     src="${pageContext.request.contextPath}/${imgUrl}"
                                     alt="${item.name}"
                                     loading="lazy"
                                     onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/assets/images/default-food.png';" />
                            </c:when>

                            <c:otherwise>
                                <img class="card__image"
                                     src="${pageContext.request.contextPath}/assets/images/default-food.png"
                                     alt="${item.name}" />
                            </c:otherwise>
                        </c:choose>

                        <div class="card__body">
                            <h4 class="card__title">${item.name}</h4>

                            <!-- Veg / Non-Veg -->
                            <span class="veg-badge ${!item.veg ? 'nonveg-badge' : ''}">
                                ${item.veg ? 'VEG' : 'NON-VEG'}
                            </span>

                            <p class="card__text mt-2">${item.description}</p>
                            <strong>₹ ${item.price}</strong>

                            <!-- Quantity Selector -->
                            <div class="qty-selector">
                                <button type="button" onclick="changeQty(${item.menuItemId}, -1)">−</button>
                                <input type="text" id="qty-${item.menuItemId}" value="1" readonly style="width:32px;text-align:center;">
                                <button type="button" onclick="changeQty(${item.menuItemId}, 1)">+</button>
                            </div>

                            <!-- Add to Cart -->
                            <button class="btn btn--primary mt-3"
                                    onclick="addToCart(${item.menuItemId})">
                                Add to Cart
                            </button>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>

    </div>
</main>

<!-- STICKY CART SUMMARY -->
<div id="cartSummary" class="cart-summary hidden">
    <p><strong id="cartCount">1</strong> items added</p>
    <a href="${pageContext.request.contextPath}/cart" class="btn btn--success mt-2">
        View Cart
    </a>
</div>

<%@ include file="/jsp/shared/footer.jspf" %>

<!-- ✅ AJAX + INTERACTION -->
<script>
    let cartItemCount = 0;

    function addToCart(menuItemId) {
        const qty = document.getElementById('qty-' + menuItemId).value;

        const body =
            'action=add' +
            '&menuItemId=' + encodeURIComponent(menuItemId) +
            '&quantity=' + encodeURIComponent(qty);

        fetch('${pageContext.request.contextPath}/cart', {
            method: 'POST',
            credentials: 'same-origin',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: body
        })
        .then(res => {
            if (!res.ok) throw new Error(res.status);
            return res.json();
        })
        .then(data => {
            if (data.success) {
                cartItemCount = data.cartCount;
                document.getElementById('cartCount').innerText = cartItemCount;
                document.getElementById('cartSummary').classList.remove('hidden');
            } else {
                alert('Unable to add item to cart.');
            }
        })
        .catch(err => {
            console.error(err);
            alert('Unable to add item to cart. Try again.');
        });
    }
</script>



</body>
</html>