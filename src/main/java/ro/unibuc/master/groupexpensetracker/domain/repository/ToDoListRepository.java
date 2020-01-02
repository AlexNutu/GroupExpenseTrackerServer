package ro.unibuc.master.groupexpensetracker.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.master.groupexpensetracker.data.noteboard.ToDoList;

@Repository
public interface ToDoListRepository extends JpaRepository<ToDoList, Long> {
    ToDoList findByTripId(long tripId);
}
