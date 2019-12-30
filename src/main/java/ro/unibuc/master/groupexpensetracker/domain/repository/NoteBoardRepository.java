package ro.unibuc.master.groupexpensetracker.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.master.groupexpensetracker.data.noteboard.NoteBoard;

@Repository
public interface NoteBoardRepository extends JpaRepository<NoteBoard, Long> {
}
