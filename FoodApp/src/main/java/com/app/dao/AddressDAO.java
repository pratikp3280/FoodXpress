package com.app.dao;

import java.util.List;
import com.app.models.Address;

public interface AddressDAO {
	
	//To Add a new address for a user
    void addAddress(Address address);
    
    // To Retrieve a single address by ID 
    Address getAddressById(int addressId, int userId);
    
    // Retrieve all addresses for a specific user
    List<Address> getAddressesByUserId(int userId);
    
    // To Update an existing address
    void updateAddress(Address address);
    
    // Delete an address by ID
    void deleteAddress(int addressId, int userId);
    
//    // To get the Default address of user
//    Address getDefaultAddress(int userId);
}