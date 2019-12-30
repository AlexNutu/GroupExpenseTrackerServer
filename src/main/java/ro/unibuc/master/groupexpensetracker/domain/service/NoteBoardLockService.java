package ro.unibuc.master.groupexpensetracker.domain.service;

import org.springframework.stereotype.Service;
import ro.unibuc.master.groupexpensetracker.domain.repository.NoteBoardLockRepository;

@Service
public class NoteBoardLockService {

    private final NoteBoardLockRepository noteBoardLockRepository;

    public NoteBoardLockService(NoteBoardLockRepository noteBoardLockRepository) {
        this.noteBoardLockRepository = noteBoardLockRepository;
    }
}
