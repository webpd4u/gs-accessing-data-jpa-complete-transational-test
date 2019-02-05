package hello;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
//	@Transactional
	@Modifying
	@Query(value = "insert into booking(id, first_name) values (:id,:firstName)", nativeQuery = true)
	void book(@Param("id") Long id, @Param("firstName") String firstName);
}
