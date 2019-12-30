package ro.unibuc.master.groupexpensetracker.domain.service;

import org.springframework.stereotype.Service;
import ro.unibuc.master.groupexpensetracker.domain.repository.NoteBoardRepository;

@Service
public class NoteBoardService {

    private final NoteBoardRepository noteBoardRepository;

    public NoteBoardService(NoteBoardRepository noteBoardRepository) {
        this.noteBoardRepository = noteBoardRepository;
    }
}
