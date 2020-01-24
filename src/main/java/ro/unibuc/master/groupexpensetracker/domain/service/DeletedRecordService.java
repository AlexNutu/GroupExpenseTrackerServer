package ro.unibuc.master.groupexpensetracker.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.unibuc.master.groupexpensetracker.common.utils.EntitySpecification;
import ro.unibuc.master.groupexpensetracker.common.utils.EntityUtils;
import ro.unibuc.master.groupexpensetracker.common.utils.SearchCriteria;
import ro.unibuc.master.groupexpensetracker.data.deletedrecords.DeletedRecord;
import ro.unibuc.master.groupexpensetracker.domain.repository.DeletedRecordRepository;


import java.util.List;

@Service
public class DeletedRecordService {

    private final DeletedRecordRepository deletedRecordRepository;

    public DeletedRecordService(DeletedRecordRepository deletedRecordRepository) {
        this.deletedRecordRepository = deletedRecordRepository;
    }

    public Page<DeletedRecord> findAll(Sort.Direction sortingDirection, String orderBy, final String search, final Integer offset, final Integer size) {
        final List<SearchCriteria> searchCriteriaList = EntityUtils.generateSearchCriteria(search);
        final Specification<DeletedRecord> spec = new EntitySpecification<>(searchCriteriaList);

        if (sortingDirection == null && orderBy == null) {
            orderBy = "createDate";
            sortingDirection = Sort.Direction.DESC;
        }

        if (size != null) {
            return deletedRecordRepository.findAll(spec,
                    EntityUtils.getPageRequest(sortingDirection, orderBy, offset, size));
        } else {
            return new PageImpl<>(deletedRecordRepository.findAll(spec));
        }
    }

    public ResponseEntity addDeletedRecord(DeletedRecord deletedRecord) {
        deletedRecordRepository.save(deletedRecord);
        return ResponseEntity.ok().build();
    }
}
