package com.uber.driver.reposiotry;

import com.uber.driver.model.Address;
import com.uber.driver.model.DriverDocument;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, String> {
}
