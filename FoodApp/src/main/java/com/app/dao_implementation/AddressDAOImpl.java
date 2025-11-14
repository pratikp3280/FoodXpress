package com.app.dao_implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.app.dao.AddressDAO;
import com.app.models.Address;
import com.app.utility.DBConnection;

/**
 * AddressDAOImpl
 * ----------------
 * Handles all database operations for managing user addresses
 * such as adding, updating, retrieving, deleting, and fetching the default address.
 */
public class AddressDAOImpl implements AddressDAO{

	// ✅ SQL Queries
	
	private static final String INSERT_ADDRESS_QUERY = "INSERT INTO addresses (user_id, street, city, state, zip, landmark) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String GET_ADDRESS_BY_ID_QUERY = "SELECT * FROM addresses WHERE address_id = ?";
	private static final String GET_ADDRESSES_BY_USER_QUERY =  "SELECT * FROM addresses WHERE user_id = ?";
	private static final String UPDATE_ADDRESS_QUERY = "UPDATE addresses SET street = ?, city = ?, state = ?, zip = ?, landmark = ? WHERE address_id = ?";
	private static final String DELETE_ADDRESS_QUERY = "DELETE FROM addresses WHERE address_id = ?";
	
	
	
	 // ✅ Add new address for a user
	@Override
	public void addAddress(Address address) {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(INSERT_ADDRESS_QUERY)) {
			
			    pstmt.setInt(1, address.getUserId());
	            pstmt.setString(2, address.getStreet());
	            pstmt.setString(3, address.getCity());
	            pstmt.setString(4, address.getState());
	            pstmt.setString(5, address.getZip());
	            pstmt.setString(6, address.getLandmark());
	            
	            int rowsInserted = pstmt.executeUpdate();
	            if (rowsInserted > 0) {
	                System.out.println("✅ Address inserted successfully.");
	            }
			
		} catch(SQLException e) {
			 System.err.println("❌ Error while adding address: " + e.getMessage());
	            e.printStackTrace();
		}
		
	}

	
	 // -------------------------------------------------------------------------
    // ✅ Get address by address_id
	@Override
	public Address getAddressById(int addressId) {
		 Address address = null;

	        try (Connection connection = DBConnection.getConnection();
	             PreparedStatement pstmt = connection.prepareStatement(GET_ADDRESS_BY_ID_QUERY)) {

	            pstmt.setInt(1, addressId);
	            ResultSet rs = pstmt.executeQuery();

	            if (rs.next()) {
	                address = extractAddress(rs);
	            }

	        } catch (SQLException e) {
	            System.err.println("❌ Error fetching address: " + e.getMessage());
	            e.printStackTrace();
	        }

	        return address;
	}

	
	 // -------------------------------------------------------------------------
    // ✅ Get all addresses for a given user_id
	@Override
	public List<Address> getAddressesByUserId(int userId) {
		 List<Address> addressList = new ArrayList<>();

	        try (Connection connection = DBConnection.getConnection();
	             PreparedStatement pstmt = connection.prepareStatement(GET_ADDRESSES_BY_USER_QUERY)) {

	            pstmt.setInt(1, userId);
	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {
	                addressList.add(extractAddress(rs));
	            }

	        } catch (SQLException e) {
	            System.err.println("❌ Error fetching addresses: " + e.getMessage());
	            e.printStackTrace();
	        }

	        return addressList;
	}

	 // -------------------------------------------------------------------------
    // ✅ Update address
	@Override
	public void updateAddress(Address address) {
		try (Connection connection = DBConnection.getConnection();
	             PreparedStatement pstmt = connection.prepareStatement(UPDATE_ADDRESS_QUERY)) {

	            pstmt.setString(1, address.getStreet());
	            pstmt.setString(2, address.getCity());
	            pstmt.setString(3, address.getState());
	            pstmt.setString(4, address.getZip());
	            pstmt.setString(5, address.getLandmark());
	            pstmt.setInt(6, address.getAddressId());

	            int rowsUpdated = pstmt.executeUpdate();
	            if (rowsUpdated > 0) {
	                System.out.println("✅ Address updated successfully.");
	            }

	        } catch (SQLException e) {
	            System.err.println("❌ Error updating address: " + e.getMessage());
	            e.printStackTrace();
	        }
		
	}

	
	 // -------------------------------------------------------------------------
    // ✅ Delete address
	@Override
	public void deleteAddress(int addressId) {
		try (Connection connection = DBConnection.getConnection();
	             PreparedStatement pstmt = connection.prepareStatement(DELETE_ADDRESS_QUERY)) {

	            pstmt.setInt(1, addressId);
	            int rowsDeleted = pstmt.executeUpdate();

	            if (rowsDeleted > 0) {
	                System.out.println("✅ Address deleted successfully.");
	            }

	        } catch (SQLException e) {
	            System.err.println("❌ Error deleting address: " + e.getMessage());
	            e.printStackTrace();
	        }
		
	}

	 // -------------------------------------------------------------------------
    // ✅ Helper Method: Convert ResultSet → Address Object
	/*If you add a new column, you Dont need to modify the constructor
	 * You Just add one setter line — more flexible here
	 * */
	private Address extractAddress(ResultSet rs) throws SQLException {
	    Address address = new Address();

	    address.setAddressId(rs.getInt("address_id"));
	    address.setUserId(rs.getInt("user_id"));
	    address.setStreet(rs.getString("street"));
	    address.setCity(rs.getString("city"));
	    address.setState(rs.getString("state"));
	    address.setZip(rs.getString("zip"));
	    address.setLandmark(rs.getString("landmark"));
	    address.setCreatedAt(rs.getTimestamp("created_at"));

	    return address;
	}
	
	

	
	
	
	
}
