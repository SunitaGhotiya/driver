package com.uber.driver.reposiotry;

import com.uber.driver.model.DriverDocument;
import com.uber.driver.model.UberDriver;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends CrudRepository<DriverDocument, Long> {

    @Query("from DriverDocument where driverId = :driverId")
    public List<DriverDocument> findAllByDriverId(@Param("driverId") long driverId);

    @Query("UPDATE DriverDocument set location = :location where driverId = :driverId and documentId = :documentId")
    public void updateDocumentLocation(@Param("driverId") long driverId, @Param("documentId") long documentId, @Param("location") String location);

    @Query("UPDATE DriverDocument set status = :status where documentId = :documentId")
    public void updateDocumentStatus(@Param("documentId") long documentId, @Param("status") String status);

}
