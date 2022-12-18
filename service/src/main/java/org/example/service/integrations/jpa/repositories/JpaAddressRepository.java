package org.example.service.integrations.jpa.repositories;

import org.example.domain.model.addresses.Address;
import org.example.domain.model.addresses.AddressRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RepositoryDefinition(domainClass = Address.class, idClass = Long.class)
public interface JpaAddressRepository extends AddressRepository {

}
