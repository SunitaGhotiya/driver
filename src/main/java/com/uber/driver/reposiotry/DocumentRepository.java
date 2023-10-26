package com.uber.driver.reposiotry;

import com.uber.driver.model.DriverDocument;
import com.uber.driver.model.UberDriver;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends CrudRepository<DriverDocument, String> {

    @Query("from DriverDocument where driverId = :driverId")
    List<DriverDocument> findAllByDriverId(@Param("driverId") String driverId);

    @Query("UPDATE DriverDocument set location = :location where documentId = :documentId")
    void updateDocumentLocation(@Param("documentId") String documentId, @Param("location") String location);

    @Query("UPDATE DriverDocument set status = :status where documentId = :documentId")
    void updateDocumentStatus(@Param("documentId") String documentId, @Param("status") String status);

}
