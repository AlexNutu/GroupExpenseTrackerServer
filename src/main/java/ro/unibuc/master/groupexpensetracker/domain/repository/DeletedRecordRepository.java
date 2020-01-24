package ro.unibuc.master.groupexpensetracker.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ro.unibuc.master.groupexpensetracker.data.deletedrecords.DeletedRecord;

@Repository
public interface DeletedRecordRepository  extends JpaRepository<DeletedRecord, Long>, JpaSpecificationExecutor<DeletedRecord> {
}
