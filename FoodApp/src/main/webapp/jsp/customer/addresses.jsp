<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="Manage Addresses"/>

<%@ include file="../shared/head.jspf" %>
<%@ include file="../shared/header.jspf" %>

<main class="main-content">
    <div class="container grid-2">

        <!-- ============================= -->
        <!-- LEFT : SAVED ADDRESSES -->
        <!-- ============================= -->
        <div class="card">
            <h2 class="page-title mb-3">📍 Saved Addresses</h2>

            <c:if test="${empty addresses}">
                <p style="color: var(--medium-text);">
                    You have not added any address yet.
                </p>
            </c:if>

            <c:forEach var="addr" items="${addresses}" varStatus="status">
                <div class="card mb-3" style="background:#fafafa">

                    <strong>
                        <c:if test="${status.first}">
                            ✅ Default Address
                        </c:if>
                        <c:if test="${!status.first}">
                            Address ${status.index + 1}
                        </c:if>
                    </strong>

                    <p class="mt-2" style="line-height:1.5">
                        ${addr.street},<br>
                        ${addr.city}, ${addr.state} - ${addr.zip}<br>
                        <c:if test="${not empty addr.landmark}">
                            Landmark: ${addr.landmark}
                        </c:if>
                    </p>

                    <div class="mt-2">
                        <!-- EDIT -->
                        <a class="btn btn--secondary btn-sm"
                           href="${pageContext.request.contextPath}/customer/addresses?action=edit&id=${addr.addressId}">
                            ✏ Edit
                        </a>

                        <!-- DELETE -->
                        <form action="${pageContext.request.contextPath}/customer/addresses"
                              method="post"
                              style="display:inline"
                              onsubmit="return confirm('Delete this address?');">

                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="${addr.addressId}">

                            <button class="btn btn--danger btn-sm">
                                🗑 Delete
                            </button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- ============================= -->
        <!-- RIGHT : ADD / EDIT ADDRESS -->
        <!-- ============================= -->
        <div class="card">
            <h2 class="page-title mb-3">
                <c:choose>
                    <c:when test="${not empty editAddress}">
                        ✏ Update Address
                    </c:when>
                    <c:otherwise>
                        ➕ Add New Address
                    </c:otherwise>
                </c:choose>
            </h2>

            <form method="post"
                  action="${pageContext.request.contextPath}/customer/addresses">

                <input type="hidden" name="action"
                       value="${not empty editAddress ? 'update' : 'add'}"/>

                <c:if test="${not empty editAddress}">
                    <input type="hidden" name="id"
                           value="${editAddress.addressId}"/>
                </c:if>

                <label class="form-label">Street</label>
                <input class="form-control" name="street" required
                       value="${editAddress.street}"/>

                <label class="form-label">City</label>
                <input class="form-control" name="city" required
                       value="${editAddress.city}"/>

                <label class="form-label">State</label>
                <input class="form-control" name="state" required
                       value="${editAddress.state}"/>

                <label class="form-label">ZIP Code</label>
                <input class="form-control" name="zip" required
                       value="${editAddress.zip}"/>

                <label class="form-label">Landmark</label>
                <input class="form-control" name="landmark"
                       value="${editAddress.landmark}"/>

                <button class="btn btn--primary mt-3">
                    <c:choose>
                        <c:when test="${not empty editAddress}">
                            Update Address
                        </c:when>
                        <c:otherwise>
                            Add Address
                        </c:otherwise>
                    </c:choose>
                </button>

                <c:if test="${not empty editAddress}">
                    <a href="${pageContext.request.contextPath}/customer/addresses"
                       class="btn btn--secondary mt-3"
                       style="margin-left:10px">
                        Cancel
                    </a>
                </c:if>
            </form>
        </div>

    </div>
</main>

<%@ include file="../shared/footer.jspf" %>
